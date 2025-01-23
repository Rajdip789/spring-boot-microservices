package com.rajdip14.ecommerce.payment;

import com.rajdip14.ecommerce.kafka.PaymentRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {

    public Payment toPayment(PaymentRequest request) {
        if (request == null) {
            return null;
        }
        return Payment.builder()
                .paymentMethod(request.paymentMethod())
                .amount(request.amount())
                .orderId(request.orderId())
                .build();
    }
}