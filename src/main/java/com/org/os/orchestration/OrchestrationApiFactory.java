package com.org.os.orchestration;

import com.org.ma.utils.Constants;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.org.ma.enums.Header.REQUEST;
import static com.org.ma.enums.Header.RESPONSE;

@Component
@AllArgsConstructor
public class OrchestrationApiFactory implements Processor {

    private Map<String, Processor> orchestrations;

    @Override
    public void process(Exchange exchange) throws Exception {
        String header = exchange.getIn().getHeader(Constants.SUBJECT, String.class);
        Processor processor = header.endsWith(REQUEST.name()) ? orchestrations.get(REQUEST.name()) : orchestrations.get(RESPONSE.name());
        processor.process(exchange);
    }
}
