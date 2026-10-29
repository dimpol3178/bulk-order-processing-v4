package com.bdo.exam.dto;

import lombok.Data;

@Data
public class OrderCsvDto {
    private String orderId;
    private String customerId;
    private String orderDate;
    private String productCode;
    private String quantity;
    private String pricePerUnit;
}
