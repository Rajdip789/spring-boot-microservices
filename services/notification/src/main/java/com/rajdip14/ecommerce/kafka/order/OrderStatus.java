package com.rajdip14.ecommerce.kafka.order;

import com.rajdip14.ecommerce.kafka.customer.Customer;
import com.rajdip14.ecommerce.kafka.payment.PaymentMethod;

import java.math.BigDecimal;

public record OrderStatus (
        Integer orderId,
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        OrderStatusType status,
        Customer customer
) {
}
