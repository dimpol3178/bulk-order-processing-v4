package com.bdo.exam.config;

import com.bdo.exam.dto.OrderCsvDto;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class BatchReaderFactory {

    public FlatFileItemReader<OrderCsvDto> createPipeDelimitedReader(String filePath) {
        return new FlatFileItemReaderBuilder<OrderCsvDto>()
                .name("orderCsvReader")
                .resource(new FileSystemResource(new File(filePath)))
                .delimited()
                .delimiter("|")
                .names("orderId", "customerId", "orderDate", "productCode", "quantity", "pricePerUnit")
                .linesToSkip(1)
                .targetType(OrderCsvDto.class)
                .build();
    }
}
