package com.org.os.orchestration.impl;

import com.org.ma.enums.MessageType;
import com.org.ma.enums.Subject;
import com.org.ma.utils.Constants;
import com.org.os.enums.Status;
import com.org.os.orchestration.api.ResponseOrchestration;
import com.org.os.persistance.entity.Order;
import com.org.os.service.OrdersService;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.org.ma.enums.Header.REQUEST;
import static com.org.ma.utils.Constants.*;

@Component
@AllArgsConstructor
public class RestaurantResponseOrchestration implements ResponseOrchestration {

    private OrdersService service;

    private KafkaTemplate<String, String> producer;

    @Override
    public void handleResponse(Exchange e) {
        MessageType messageType = e.getIn().getHeader(MESSAGE_TYPE, MessageType.class);
        if (messageType.equals(MessageType.REGULAR)) {
            handleRegularResponse(e);
        } else {
            handleRejectCancelResponse(e);
        }
    }

    private void handleRegularResponse(Exchange e) {
        String correlationId = e.getMessage(String.class);
        Order order = service.getOrderByCorrelationId(correlationId);
        ProducerRecord<String, String> record = new ProducerRecord<>(ORDER_CHANNEL, correlationId);

        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.DELIVERY.name(), REQUEST.name()).getBytes());
        record.headers().add(MESSAGE_TYPE, MessageType.REGULAR.name().getBytes());
        producer.send(record);
        order.setStatus(Status.DELIVERY_PENDING);
        service.saveOrder(order);
    }

    private void handleRejectCancelResponse(Exchange e) {
        String correlationId = e.getMessage(String.class);
        ProducerRecord<String, String> record = new ProducerRecord<>(ORDER_CHANNEL, correlationId);
        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.PAYMENT.name(), REQUEST.name()).getBytes());
        record.headers().add(Constants.MESSAGE_TYPE, e.getIn().getHeader(Constants.MESSAGE_TYPE, MessageType.class).toString().getBytes());
        producer.send(record);
    }
}
