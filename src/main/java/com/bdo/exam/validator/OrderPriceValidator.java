package com.bdo.exam.validator;

import com.bdo.exam.dto.OrderCsvDto;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Order(4)
public class OrderPriceValidator implements OrderValidator {

    @Override
    public Optional<String> validate(OrderCsvDto dto) {
        String rawPrice = dto.getPricePerUnit() != null ? dto.getPricePerUnit().trim() : null;
        try {
            new BigDecimal(rawPrice);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of("Invalid price format");
        }
    }
}
