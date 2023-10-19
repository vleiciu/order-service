package com.org.os.integration;

import com.org.os.orchestration.OrchestrationApiFactory;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static com.org.ma.utils.Constants.ORDER_CHANNEL;

@Component
@AllArgsConstructor
public class AmqpAdapter extends RouteBuilder {

    private OrchestrationApiFactory factory;

    @Override
    public void configure() {
        from("kafka:%s".formatted(ORDER_CHANNEL))
                .routeId("order-service")
                .process(factory);
    }
}
