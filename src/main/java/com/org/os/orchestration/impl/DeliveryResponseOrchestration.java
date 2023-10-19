package com.org.os.orchestration.impl;

import com.org.ma.model.Delivery;
import com.org.os.enums.Status;
import com.org.os.orchestration.api.ResponseOrchestration;
import com.org.os.persistance.entity.Order;
import com.org.os.service.OrdersService;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeliveryResponseOrchestration implements ResponseOrchestration {

    private OrdersService service;

    @Override
    public void handleResponse(Exchange e) {
        Delivery delivery = e.getIn(Delivery.class);
        Order order = service.getOrderByCorrelationId(delivery.getCorrelationId());
        order.setStatus(Status.COMPLETED);
        service.saveOrder(order);
    }
}
