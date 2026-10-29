package com.bdo.exam.validator;

import com.bdo.exam.dto.OrderCsvDto;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(1)
public class OrderIdValidator implements OrderValidator {

    @Override
    public Optional<String> validate(OrderCsvDto dto) {
        String orderId = dto.getOrderId() != null ? dto.getOrderId().trim() : null;
        if (orderId == null || orderId.isEmpty()) {
            return Optional.of("Order ID is missing");
        }
        return Optional.empty();
    }
}
