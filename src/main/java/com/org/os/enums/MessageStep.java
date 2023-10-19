package com.org.os.enums;

import org.springframework.stereotype.Component;

@Component
public enum MessageStep {

    PAYMENT_REQUEST,
    RESTAURANT_REQUEST,
    DELIVERY_REQUEST,
    PAYMENT_RESPONSE,
    RESTAURANT_RESPONSE,
    DELIVERY_RESPONSE
}
