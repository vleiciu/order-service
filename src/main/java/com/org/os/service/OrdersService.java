package com.org.os.service;

import com.org.ma.enums.MessageType;
import com.org.ma.enums.Subject;
import com.org.ma.model.PaymentUpdate;
import com.org.os.enums.Status;
import com.org.os.exceptions.InvalidPhaseException;
import com.org.os.persistance.entity.LineItems;
import com.org.os.persistance.entity.Order;
import com.org.os.persistance.repository.OrdersRepository;
import com.org.os.persistance.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.org.ma.enums.Header.REQUEST;
import static com.org.ma.utils.Constants.*;

@Service
@AllArgsConstructor
public class OrdersService {

    private UsersService usersService;

    private OrdersRepository repository;

    private RestaurantRepository restaurantRepository;

    private KafkaProducer<String, String> producer;

    private KafkaProducer<String, PaymentUpdate> paymentProducer;

    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    public void saveOrder(Order order) {
        repository.save(order);
    }

    public Order getOrderById(Integer id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Order cancelOrder(Integer id) {
        Order order = repository.findById(id).get();
        Status status = order.getStatus();
        if (status.equals(Status.PAYMENT_PENDING)) {
            cancelPaymentPending(order);
        } else if (status.equals(Status.RESTAURANT_PENDING)) {
            cancelRestaurantPending(order);
        } else {
            throw new InvalidPhaseException();
        }
        return order;
    }

    private void cancelRestaurantPending(Order order) {
        ProducerRecord<String, String> record = new ProducerRecord<>(ORDER_CHANNEL, MESSAGE, order.getCorrelationId());

        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.RESTAURANT.name(), REQUEST.name()).getBytes());
        record.headers().add(MESSAGE_TYPE, MessageType.CANCEL.name().getBytes());
        producer.send(record);
    }

    private void cancelPaymentPending(Order order) {
        ProducerRecord<String, String> record = new ProducerRecord<>(ORDER_CHANNEL, MESSAGE, order.getCorrelationId());

        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.PAYMENT.name(), REQUEST.name()).getBytes());
        record.headers().add(MESSAGE_TYPE, MessageType.CANCEL.name().getBytes());
        producer.send(record);
    }

    public Order createOrder(Integer restaurantId, List<LineItems> lineItems) {
        Order order = Order.builder()
                .lineOrder(lineItems)
                .total(calculateSum(lineItems))
                .userOrders(usersService.getUserByUsername(authentication.getPrincipal().toString()))
                .placedAt(LocalDateTime.now())
                .status(Status.PAYMENT_PENDING)
                .restaurants(restaurantRepository.findById(restaurantId).get())
                .build();
        order.setCorrelationId(UUID.nameUUIDFromBytes(order.toString().getBytes()).toString());
        repository.save(order);
        sendOrder(order);
        return order;
    }

    public Order checkOrderStatus(Integer id) {
        Order currentOrder = getOrderById(id);
        return currentOrder.getUserOrders().getUsername().equals(authentication.getPrincipal().toString()) ? currentOrder : null;
    }

    private Double calculateSum(List<LineItems> lineItems) {
        return lineItems.stream()
                .mapToDouble((LineItems items) -> items.getItems().getPrice() * items.getQuantity())
                .sum();
    }

    private void sendOrder(Order order) {
        ProducerRecord<String, String> record = new ProducerRecord<>(ORDER_CHANNEL, MESSAGE, order.getCorrelationId());

        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.PAYMENT.name(), REQUEST.name()).getBytes());
        record.headers().add(MESSAGE_TYPE, MessageType.REGULAR.name().getBytes());
        producer.send(record);
    }

    public Order getOrderByCorrelationId(String correlationId) {
        return repository.findByCorrelationId(correlationId).orElseThrow(EntityNotFoundException::new);
    }

    public void registerPaymentInfo(PaymentUpdate paymentUpdate) {
        ProducerRecord<String, PaymentUpdate> record = new ProducerRecord<>(PAYMENT_CHANNEL, MESSAGE, paymentUpdate);

        record.headers().add(SUBJECT, "%s_%s".formatted(Subject.PAYMENT.name(), REQUEST.name()).getBytes());
        record.headers().add(MESSAGE_TYPE, MessageType.INFO.name().getBytes());
        paymentProducer.send(record);
    }
}
