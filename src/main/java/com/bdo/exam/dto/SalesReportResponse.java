package com.bdo.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesReportResponse {
    private BigDecimal totalSales;
    private List<CustomerSalesDto> topCustomers;
    private List<ProductSalesDto> topProducts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomerSalesDto {
        private String customerId;
        private BigDecimal totalSpent;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductSalesDto {
        private String productCode;
        private Long unitsSold;
    }
}
