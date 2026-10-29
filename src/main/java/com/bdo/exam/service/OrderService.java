package com.bdo.exam.service;

import com.bdo.exam.dto.SalesReportResponse;
import com.bdo.exam.dto.XmlExportWrapper;
import com.bdo.exam.entity.Order;
import com.bdo.exam.entity.SalesReport;
import com.bdo.exam.repository.OrderRepository;
import com.bdo.exam.repository.SalesReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final JobLauncher jobLauncher;
    private final Job processOrdersJob;
    private final OrderRepository orderRepository;
    private final SalesReportRepository salesReportRepository;

    public OrderService(JobLauncher jobLauncher, Job processOrdersJob, OrderRepository orderRepository, SalesReportRepository salesReportRepository) {
        this.jobLauncher = jobLauncher;
        this.processOrdersJob = processOrdersJob;
        this.orderRepository = orderRepository;
        this.salesReportRepository = salesReportRepository;
    }

    public void runBatchJob(String filePath) throws Exception {
        log.info("Initiating batch processing for file path: {}", filePath);
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        JobParameters params = new JobParametersBuilder()
                .addString("filePath", filePath)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(processOrdersJob, params);
        log.info("Batch job execution completed successfully.");

        refreshSalesReportTable();
    }

    private void refreshSalesReportTable() {
        log.info("Refreshing tbl_sales_report in PostgreSQL...");
        List<Object[]> topCustomers = orderRepository.findTopCustomers();
        for (Object[] row : topCustomers) {
            String customerId = (String) row[0];
            BigDecimal totalSpent = (BigDecimal) row[1];
            salesReportRepository.save(new SalesReport(customerId, totalSpent));
        }
    }

    public Page<Order> getAllOrders(String customerId, Pageable pageable) {
        if (customerId != null && !customerId.trim().isEmpty()) {
            return orderRepository.findByCustomerId(customerId, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public SalesReportResponse generateSalesReport() {
        log.info("Generating aggregate sales report from PostgreSQL...");
        List<Order> processedOrders = orderRepository.findAllProcessedOrders();

        BigDecimal totalSales = processedOrders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<SalesReportResponse.CustomerSalesDto> topCustomers = orderRepository.findTopCustomers().stream()
                .map(res -> new SalesReportResponse.CustomerSalesDto((String) res[0], (BigDecimal) res[1]))
                .collect(Collectors.toList());

        List<SalesReportResponse.ProductSalesDto> topProducts = orderRepository.findTopProducts().stream()
                .map(res -> new SalesReportResponse.ProductSalesDto((String) res[0], (Long) res[1]))
                .collect(Collectors.toList());

        return new SalesReportResponse(totalSales, topCustomers, topProducts);
    }

    public XmlExportWrapper exportProcessedOrdersToXml() {
        log.info("Exporting processed orders to XML structure from PostgreSQL...");
        List<Order> processedOrders = orderRepository.findAllProcessedOrders();

        List<XmlExportWrapper.XmlItemDto> items = processedOrders.stream()
                .map(o -> new XmlExportWrapper.XmlItemDto(
                        o.getOrderId(),
                        o.getCustomerId(),
                        o.getOrderDate(),
                        o.getProductName(),
                        o.getQuantity(),
                        o.getTotalPrice()
                ))
                .collect(Collectors.toList());

        return new XmlExportWrapper(items);
    }
}
