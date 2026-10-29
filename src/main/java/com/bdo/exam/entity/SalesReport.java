package com.bdo.exam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_sales_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReport {
    @Id
    @Column(name = "customer_id", length = 20)
    private String customerId;

    @Column(name = "total_spent", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalSpent;
}
