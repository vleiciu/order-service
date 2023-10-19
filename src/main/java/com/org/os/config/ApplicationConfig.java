package com.org.os.config;

import com.org.os.orchestration.RequestOrchestrationFactory;
import com.org.os.orchestration.ResponseOrchestrationFactory;
import com.org.os.orchestration.api.RequestOrchestration;
import com.org.os.orchestration.api.ResponseOrchestration;
import com.org.os.orchestration.impl.*;
import org.apache.camel.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.org.ma.enums.Header.REQUEST;
import static com.org.ma.enums.Header.RESPONSE;
import static com.org.os.enums.MessageStep.*;

@Configuration
public class ApplicationConfig {

    @Bean(name = "requestOrchestrations")
    public Map<String, RequestOrchestration> requestOrchestrations(DeliveryRequestOrchestration deliveryRequest,
                                                                   PaymentRequestOrchestration paymentRequest,
                                                                   RestaurantRequestOrchestration restaurantRequest) {
        return Map.of(DELIVERY_REQUEST.name(), deliveryRequest,
                RESTAURANT_REQUEST.name(), restaurantRequest,
                PAYMENT_REQUEST.name(), paymentRequest);
    }

    @Bean(name = "responseOrchestrations")
    public Map<String, ResponseOrchestration> responseOrchestrations(DeliveryResponseOrchestration deliverResponse,
                                                                     PaymentResponseOrchestration paymentResponse,
                                                                     RestaurantResponseOrchestration restaurantResponse) {
        return Map.of(DELIVERY_RESPONSE.name(), deliverResponse,
                RESTAURANT_RESPONSE.name(), restaurantResponse,
                PAYMENT_RESPONSE.name(), paymentResponse);
    }

    @Bean
    public Map<String, Processor> orchestrations(RequestOrchestrationFactory requestOrchestrationFactory,
                                                 ResponseOrchestrationFactory responseOrchestrationFactory) {
        return Map.of(REQUEST.name(), requestOrchestrationFactory,
                RESPONSE.name(), responseOrchestrationFactory);
    }
}
