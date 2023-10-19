package com.org.os.orchestration;

import com.org.ma.utils.Constants;
import com.org.os.enums.MessageStep;
import com.org.os.orchestration.api.RequestOrchestration;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.org.os.enums.MessageStep.*;

@Component
@AllArgsConstructor
public class RequestOrchestrationFactory implements Processor {

    private Map<String, RequestOrchestration> orchestrationApis;

    @Override
    public void process(Exchange exchange) {
        RequestOrchestration orchestration = evaluateStep(exchange.getIn().getHeader(Constants.SUBJECT, MessageStep.class));
        orchestration.handleRequest(exchange);
    }

    private RequestOrchestration evaluateStep(MessageStep messageStep) {
        switch (messageStep) {
            case DELIVERY_REQUEST -> {
                return orchestrationApis.get(DELIVERY_REQUEST.name());
            }
            case PAYMENT_REQUEST -> {
                return orchestrationApis.get(PAYMENT_REQUEST.name());
            }
            case RESTAURANT_REQUEST -> {
                return orchestrationApis.get(RESTAURANT_REQUEST.name());
            }
            default -> throw  new IllegalStateException("State has not found");
        }
    }

}
