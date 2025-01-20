package com.rajdip14.ecommerce.kafka;

import com.rajdip14.ecommerce.customer.CustomerResponse;
import com.rajdip14.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}