package com.org.os.persistance.repository;

import com.org.os.persistance.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findById(String id);
    Optional<Order> findByCorrelationId(String correlationId);
}
