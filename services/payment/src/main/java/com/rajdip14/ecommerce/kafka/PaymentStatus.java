package com.rajdip14.ecommerce.kafka;

import com.rajdip14.ecommerce.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentStatus(
        Integer orderId,
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String status,
        String customerFirstname,
        String customerLastname,
        String customerEmail
) {
}
