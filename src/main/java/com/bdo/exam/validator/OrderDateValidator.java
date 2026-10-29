package com.bdo.exam.validator;

import com.bdo.exam.dto.OrderCsvDto;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Component
@Order(2)
public class OrderDateValidator implements OrderValidator {

    @Override
    public Optional<String> validate(OrderCsvDto dto) {
        String rawDate = dto.getOrderDate() != null ? dto.getOrderDate().trim() : null;
        if (rawDate == null || rawDate.isEmpty()) {
            return Optional.of("Invalid order date format");
        }
        try {
            LocalDate.parse(rawDate);
            return Optional.empty();
        } catch (DateTimeParseException e) {
            return Optional.of("Invalid order date format");
        }
    }
}
