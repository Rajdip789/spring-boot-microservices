package com.rajdip14.ecommerce.kafka;

import com.rajdip14.ecommerce.order.PaymentMethod;
import com.rajdip14.ecommerce.orderLine.OrderLine;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        List<OrderLine> products
) {
}