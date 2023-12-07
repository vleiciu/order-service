package com.org.os.integration;

import com.org.os.orchestration.OrchestrationApiFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.org.ma.utils.Constants.ORDER_CHANNEL;

@Component
@RequiredArgsConstructor
public class AmqpAdapter extends RouteBuilder {

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String brokerAddress;

    private OrchestrationApiFactory factory;

    @Override
    public void configure() {
        from("kafka:%s?brokers=%s".formatted(ORDER_CHANNEL, brokerAddress))
                .routeId("order-service")
                .process(factory);
    }
}
