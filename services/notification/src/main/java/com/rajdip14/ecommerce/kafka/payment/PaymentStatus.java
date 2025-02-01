package com.rajdip14.ecommerce.kafka.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rajdip14.ecommerce.kafka.customer.Customer;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentStatus (
        Integer orderId,
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String status,
        Customer customer
) {
}
