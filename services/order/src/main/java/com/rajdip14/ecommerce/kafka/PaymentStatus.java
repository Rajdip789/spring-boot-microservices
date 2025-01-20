package com.rajdip14.ecommerce.kafka;

public record PaymentStatus(
        Integer orderId,
        String status
) {
}
