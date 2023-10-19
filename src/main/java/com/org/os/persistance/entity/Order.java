package com.org.os.persistance.entity;

import com.org.os.enums.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @Column(name = "ORDER_ID")
    private Integer orderId;

    @Column(name = "PLACED_AT")
    private LocalDateTime placedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "TOTAL")
    private Double total;

    @Column(name = "CORRELATION_ID")
    private String correlationId;

    @Column(name = "DELIVERY_TIME")
    private LocalDateTime deliveryTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orders")
    private Users userOrders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orders")
    private Restaurant restaurants;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<LineItems> lineOrder;
}
