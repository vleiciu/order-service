package com.org.os.orchestration.impl;

import com.org.ma.enums.MessageType;
import com.org.ma.enums.Subject;
import com.org.ma.model.Payment;
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
public class PaymentResponseOrchestration implements ResponseOrchestration {

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
        Payment payment = e.getMessage(Payment.class);
        Order order = service.getOrderByCorrelationId(payment.getCorrelationId());
        ProducerRecord<String, String> record = new ProducerRecord<>(ORDER_CHANNEL, payment.getCorrelationId());

        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.RESTAURANT.name(), REQUEST.name()).getBytes());
        record.headers().add(MESSAGE_TYPE, MessageType.REGULAR.name().getBytes());
        producer.send(record);
        order.setStatus(Status.RESTAURANT_PENDING);
        service.saveOrder(order);
    }

    private void handleRejectCancelResponse(Exchange e) {
        Payment payment = e.getMessage(Payment.class);
        Order order = service.getOrderByCorrelationId(payment.getCorrelationId());
        MessageType messageType = e.getIn().getHeader(MESSAGE_TYPE, MessageType.class);
        if (messageType.equals(MessageType.REJECT)) {
            order.setStatus(Status.REJECTED);
        } else {
            order.setStatus(Status.CANCELED);
        }
        service.saveOrder(order);
    }
}
