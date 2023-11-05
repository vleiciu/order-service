package com.org.os.persistance.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Entity
@Table(name = "RESTAURANTS")
public class Restaurant {

    @Id
    @Column(name = "RESTAURANT_ID")
    private Integer restaurantId;

    @Column(name = "RESTAURANT_NAME")
    private String restaurantName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PAYMENT_INFO")
    private String paymentInfo;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Items> restaurantItems;

    @OneToMany(mappedBy = "restaurants", fetch = FetchType.LAZY)
    private List<Order> orders;
}
