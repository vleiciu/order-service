package com.org.os.persistance.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "Items")
public class Items {

    @Id
    @Column(name = "ITEM_ID")
    private Integer itemId;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "PRICE")
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurantItems")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "items")
    private LineItems cart;
}
