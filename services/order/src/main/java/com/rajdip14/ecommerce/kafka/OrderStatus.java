package com.rajdip14.ecommerce.kafka;

import com.rajdip14.ecommerce.order.OrderStatusType;
import com.rajdip14.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record OrderStatus(
        Integer orderId,
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        OrderStatusType status
) {
}