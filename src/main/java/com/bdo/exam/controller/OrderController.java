package com.bdo.exam.controller;

import com.bdo.exam.dto.ProcessOrderRequest;
import com.bdo.exam.dto.SalesReportResponse;
import com.bdo.exam.dto.XmlExportWrapper;
import com.bdo.exam.entity.Order;
import com.bdo.exam.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Bulk Order Processing API", description = "Endpoints for batch order ingestion, querying, reports, and XML export.")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/process")
    @Operation(summary = "Trigger Batch CSV Processing", description = "Reads a CSV file from the given file path, processes orders, and stores them in PostgreSQL.")
    public ResponseEntity<Map<String, String>> processOrders(@RequestBody ProcessOrderRequest request) throws Exception {
        orderService.runBatchJob(request.getFilePath());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Batch processing completed successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get All Processed Orders or Filter by Customer ID", description = "Returns a list of orders, with optional customerId filtering and pagination.")
    public ResponseEntity<?> getOrders(
            @RequestParam(required = false) String customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        if (customerId != null && !customerId.isEmpty()) {
            List<Order> customerOrders = orderService.getOrdersByCustomer(customerId);
            return ResponseEntity.ok(customerOrders);
        }
        
        Page<Order> orders = orderService.getAllOrders(null, PageRequest.of(page, size));
        return ResponseEntity.ok(orders.getContent());
    }

    @GetMapping("/sales-report")
    @Operation(summary = "Generate Aggregated Sales Report", description = "Returns total revenue, top customers by spending, and top products by units sold.")
    public ResponseEntity<SalesReportResponse> getSalesReport() {
        return ResponseEntity.ok(orderService.generateSalesReport());
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Export Processed Orders to XML", description = "Generates an XML file download containing all successfully processed orders.")
    public ResponseEntity<XmlExportWrapper> exportOrdersToXml() {
        XmlExportWrapper xmlWrapper = orderService.exportProcessedOrdersToXml();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=processed_orders.xml")
                .body(xmlWrapper);
    }
}
