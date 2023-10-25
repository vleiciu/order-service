package com.org.os.persistance.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "items")
    private List<LineItems> cart;
}
