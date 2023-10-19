package com.org.os.orchestration.impl;

import com.org.ma.enums.MessageType;
import com.org.ma.model.Item;
import com.org.ma.model.OrderCommand;
import com.org.ma.model.RestaurantUpdate;
import com.org.ma.utils.Constants;
import com.org.os.orchestration.api.RequestOrchestration;
import com.org.os.persistance.entity.Items;
import com.org.os.persistance.entity.Order;
import com.org.os.persistance.entity.Restaurant;
import com.org.os.service.ItemsService;
import com.org.os.service.OrdersService;
import com.org.os.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.org.ma.utils.Constants.*;

@Component
@AllArgsConstructor
public class RestaurantRequestOrchestration implements RequestOrchestration {

    private ItemsService itemsService;

    private OrdersService ordersService;

    private RestaurantService restaurantService;

    private KafkaProducer<String, OrderCommand> producer;

    @Override
    public void handleRequest(Exchange e) {
        MessageType messageType = e.getIn().getHeader(MESSAGE_TYPE, MessageType.class);
        if (messageType.equals(MessageType.INFO)) {
            handleInfoRequest(e);
        } else {
            handleOtherRequests(e);
        }
    }

    private void handleOtherRequests(Exchange e) {
        String correlationId = e.getMessage(String.class);
        Order order = ordersService.getOrderByCorrelationId(correlationId);
        ProducerRecord<String, OrderCommand> record = new ProducerRecord<>(RESTAURANT_CHANNEL, MESSAGE, OrderCommand.builder()
                .correlationId(order.getCorrelationId())
                .restaurantId(order.getLineOrder().get(0).getItems().getRestaurant().getRestaurantId())
                .itemsList(order.getLineOrder()
                        .stream()
                        .map(items -> {
                            Items currentItem = items.getItems();
                            return com.org.ma.model.LineItems.builder()
                                    .quantity(items.getQuantity())
                                    .item(Item.builder()
                                            .itemId(currentItem.getItemId())
                                            .itemName(currentItem.getItemName())
                                            .price(currentItem.getPrice())
                                            .restaurantId(currentItem.getRestaurant().getRestaurantId())
                                            .build()).build();
                        })
                        .collect(Collectors.toList()))
                .build());

        record.headers().add(Constants.MESSAGE_TYPE, e.getIn().getHeader(Constants.MESSAGE_TYPE, MessageType.class).toString().getBytes());
        record.headers().add(Constants.SUBJECT, e.getIn().getHeader(Constants.SUBJECT, String.class).getBytes());
        producer.send(record);
    }

    private void handleInfoRequest(Exchange e) {
        RestaurantUpdate restaurantUpdate = e.getIn(RestaurantUpdate.class);
        Restaurant restaurant = restaurantService.getById(restaurantUpdate.getRestaurantId());
        if (restaurant != null) {
            restaurantUpdate.getItems().forEach(item -> itemsService.saveItem(Items.builder()
                            .itemId(item.getItemId())
                            .price(item.getPrice())
                            .itemName(item.getItemName())
                            .restaurant(restaurant)
                    .build()));
        } else {
            Restaurant registerRestaurant = Restaurant.builder()
                    .restaurantId(restaurantUpdate.getRestaurantId())
                    .restaurantName(restaurantUpdate.getRestaurantName())
                    .paymentInfo(restaurantUpdate.getPaymentInfo())
                    .address(restaurantUpdate.getAddress())
                    .build();
            restaurantService.save(registerRestaurant);
            restaurantUpdate.getItems().forEach(item -> itemsService.saveItem(Items.builder()
                    .itemId(item.getItemId())
                    .price(item.getPrice())
                    .itemName(item.getItemName())
                    .restaurant(registerRestaurant)
                    .build()));
        }
    }
}
