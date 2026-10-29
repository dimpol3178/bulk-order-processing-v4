package com.bdo.exam.validator;

import com.bdo.exam.dto.OrderCsvDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderValidationEngine {

    private final List<OrderValidator> validators;

    public OrderValidationEngine(List<OrderValidator> validators) {
        this.validators = validators;
    }

    public Optional<String> validateAll(OrderCsvDto dto) {
        for (OrderValidator validator : validators) {
            Optional<String> error = validator.validate(dto);
            if (error.isPresent()) {
                return error;
            }
        }
        return Optional.empty();
    }
}
