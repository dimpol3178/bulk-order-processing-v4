package com.bdo.exam.validator;

import com.bdo.exam.dto.OrderCsvDto;
import java.util.Optional;

public interface OrderValidator {
    Optional<String> validate(OrderCsvDto dto);
}
