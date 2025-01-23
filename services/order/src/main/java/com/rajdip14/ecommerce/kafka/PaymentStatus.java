package com.rajdip14.ecommerce.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentStatus(
        Integer orderId,
        String status,
        String customerEmail
) {
}
