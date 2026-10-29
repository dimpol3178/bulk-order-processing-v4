package com.bdo.exam.processor;

import com.bdo.exam.dto.OrderCsvDto;
import com.bdo.exam.entity.Order;
import com.bdo.exam.entity.Product;
import com.bdo.exam.repository.ProductRepository;
import com.bdo.exam.validator.OrderValidationEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
public class OrderTransformationProcessor implements ItemProcessor<OrderCsvDto, Order> {

    private final OrderValidationEngine validationEngine;
    private final ProductRepository productRepository;

    public OrderTransformationProcessor(OrderValidationEngine validationEngine, ProductRepository productRepository) {
        this.validationEngine = validationEngine;
        this.productRepository = productRepository;
    }

    @Override
    public Order process(OrderCsvDto dto) {
        log.debug("Processing batch record for Order ID: {}", dto.getOrderId());
        Order order = new Order();

        String orderId = dto.getOrderId() != null ? dto.getOrderId().trim() : null;
        String customerId = dto.getCustomerId() != null ? dto.getCustomerId().trim() : null;
        String productCode = dto.getProductCode() != null ? dto.getProductCode().trim() : null;

        order.setOrderId(orderId);
        order.setCustomerId(customerId);
        order.setProductCode(productCode);

        // Fetch Product Name from PostgreSQL Reference Data
        if (productCode != null && !productCode.isEmpty()) {
            String name = productRepository.findById(productCode)
                    .map(Product::getProductName)
                    .orElse("UNKNOWN");
            order.setProductName(name);
        } else {
            order.setProductName("UNKNOWN");
        }

        // Apply Strategy-Based Validation Chain
        Optional<String> validationError = validationEngine.validateAll(dto);
        if (validationError.isPresent()) {
            order.setStatus("FAILED");
            order.setErrorMessage(validationError.get());
            log.warn("Validation failed for order {}: {}", orderId, validationError.get());
            return order;
        }


        order.setOrderDate(LocalDate.parse(dto.getOrderDate().trim()));
        int quantity = Integer.parseInt(dto.getQuantity().trim());
        BigDecimal pricePerUnit = new BigDecimal(dto.getPricePerUnit().trim());

        order.setQuantity(quantity);
        order.setPricePerUnit(pricePerUnit);
        order.setTotalPrice(pricePerUnit.multiply(BigDecimal.valueOf(quantity)));
        order.setStatus("PROCESSED");
        order.setErrorMessage(null);

        return order;
    }
}
