package com.org.os.orchestration.impl;

import com.org.ma.enums.MessageType;
import com.org.ma.model.Payment;
import com.org.ma.utils.Constants;
import com.org.os.orchestration.api.RequestOrchestration;
import com.org.os.persistance.entity.Order;
import com.org.os.service.OrdersService;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.org.ma.utils.Constants.PAYMENT_CHANNEL;

@Component
@AllArgsConstructor
public class PaymentRequestOrchestration implements RequestOrchestration {

    private OrdersService service;

    private KafkaProducer<String, Payment> producer;

    @Override
    public void handleRequest(Exchange e) {
        String correlationId = e.getMessage(String.class);
        Order order = service.getOrderByCorrelationId(correlationId);
        Payment payment = Payment.builder()
                .correlationId(order.getCorrelationId()).requestedAt(LocalDateTime.now())
                .amount(order.getTotal())
                .senderId(order.getUserOrders().getPaymentInfo())
                .receiverId(order.getRestaurants().getPaymentInfo())
                .build();
        ProducerRecord<String, Payment> record = new ProducerRecord<>(PAYMENT_CHANNEL, Constants.MESSAGE, payment);
        record.headers().add(Constants.MESSAGE_TYPE, e.getIn().getHeader(Constants.MESSAGE_TYPE, MessageType.class).toString().getBytes());
        record.headers().add(Constants.SUBJECT, e.getIn().getHeader(Constants.SUBJECT, String.class).getBytes());
        producer.send(record);

    }
}
