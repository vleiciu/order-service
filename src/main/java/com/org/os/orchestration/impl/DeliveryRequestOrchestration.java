package com.org.os.orchestration.impl;

import com.org.ma.enums.MessageType;
import com.org.ma.enums.Subject;
import com.org.ma.model.Delivery;
import com.org.os.orchestration.api.RequestOrchestration;
import com.org.os.persistance.entity.Order;
import com.org.os.service.OrdersService;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.org.ma.enums.Header.REQUEST;
import static com.org.ma.utils.Constants.*;
import static com.org.ma.utils.Constants.MESSAGE_TYPE;

@Component
@AllArgsConstructor
public class DeliveryRequestOrchestration implements RequestOrchestration {

    private OrdersService ordersService;

    private KafkaProducer<String, Delivery> producer;

    @Override
    public void handleRequest(Exchange e) {
        String correlationId = e.getMessage(String.class);
        Order order = ordersService.getOrderByCorrelationId(correlationId);
        ProducerRecord<String, Delivery> record = new ProducerRecord<>(DELIVERY_CHANNEL, MESSAGE, Delivery.builder()
                .correlationId(correlationId)
                .address(order.getUserOrders().getAddress())
                .deliveryTime(order.getDeliveryTime())
                .cost(order.getTotal())
                .orderId(order.getOrderId())
                .restaurantId(order.getRestaurants().getRestaurantId())
                .submittedAt(LocalDateTime.now())
                .build());
        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.DELIVERY.name(), REQUEST.name()).getBytes());
        record.headers().add(MESSAGE_TYPE, MessageType.REGULAR.name().getBytes());
        producer.send(record);
    }
}
