package com.bdo.exam.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JacksonXmlRootElement(localName = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlExportWrapper {
    
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    private List<XmlItemDto> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class XmlItemDto {
        private String orderId;
        private String customerId;
        private LocalDate orderDate;
        private String productName;
        private Integer quantity;
        private BigDecimal totalPrice;
    }
}
