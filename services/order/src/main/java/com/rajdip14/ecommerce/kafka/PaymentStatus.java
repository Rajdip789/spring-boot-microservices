package com.rajdip14.ecommerce.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rajdip14.ecommerce.customer.CustomerResponse;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentStatus(
        Integer orderId,
        String orderReference,
        BigDecimal amount,
        String status,
        CustomerResponse customer
) {
}
