package com.rajdip14.ecommerce.email;

import lombok.Getter;

public enum EmailTemplates {

    PAYMENT_NOTIFICATION("payment-notification.html", "Update on your payment"),
    ORDER_NOTIFICATION("order-notification.html", "Update on your order")
    ;

    @Getter
    private final String template;
    @Getter
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}