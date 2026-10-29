package com.bdo.exam.validator;

import com.bdo.exam.dto.OrderCsvDto;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(3)
public class OrderQuantityValidator implements OrderValidator {

    @Override
    public Optional<String> validate(OrderCsvDto dto) {
        String rawQty = dto.getQuantity() != null ? dto.getQuantity().trim() : null;
        try {
            int qty = Integer.parseInt(rawQty);
            if (qty <= 0) {
                return Optional.of("Quantity must be greater than zero");
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of("Invalid quantity value");
        }
    }
}
