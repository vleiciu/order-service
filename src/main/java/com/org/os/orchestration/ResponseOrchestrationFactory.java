package com.org.os.orchestration;

import com.org.ma.utils.Constants;
import com.org.os.enums.MessageStep;
import com.org.os.orchestration.api.ResponseOrchestration;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.org.os.enums.MessageStep.*;

@Component
@AllArgsConstructor
public class ResponseOrchestrationFactory implements Processor {

    private Map<String, ResponseOrchestration> orchestrationApis;

    @Override
    public void process(Exchange exchange) {
        ResponseOrchestration orchestration = evaluateStep(exchange.getIn().getHeader(Constants.SUBJECT, MessageStep.class));
        orchestration.handleResponse(exchange);
    }

    private ResponseOrchestration evaluateStep(MessageStep messageStep) {
        switch (messageStep) {
            case DELIVERY_RESPONSE -> {
                return orchestrationApis.get(DELIVERY_RESPONSE.name());
            }
            case PAYMENT_RESPONSE -> {
                return orchestrationApis.get(PAYMENT_RESPONSE.name());
            }
            case RESTAURANT_RESPONSE -> {
                return orchestrationApis.get(RESTAURANT_RESPONSE.name());
            }
            default -> throw new IllegalStateException("State has not found");
        }
    }
}
