package com.org.os.rest;

import com.org.os.exceptions.InvalidPhaseException;
import com.org.os.persistance.entity.*;
import com.org.os.service.OrdersService;
import com.org.os.service.RestaurantService;
import com.org.os.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private UsersService usersService;

    private OrdersService ordersService;

    private RestaurantService restaurantService;

    @PostMapping("/create/{id}")
    public ResponseEntity<Order> createOrder(@PathVariable Integer id, List<LineItems> itemsList) {
        return ResponseEntity.ok(ordersService.createOrder(id, itemsList));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Integer id) {
        try {
            Order order = ordersService.cancelOrder(id);
            return ResponseEntity.ok(order);
        } catch (InvalidPhaseException e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    @GetMapping("/rests")
    public ResponseEntity<List<Restaurant>> getRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/rest/{id}")
    public ResponseEntity<List<Items>> getRestaurantItems(@PathVariable Integer id) {
        return ResponseEntity.ok(restaurantService.getById(id).getRestaurantItems());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getPrincipal().toString();
        Users user = usersService.getUserByUsername(currentUser);
        return ResponseEntity.ok(user.getOrders());
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<Order> checkStatus(@PathVariable Integer id) {
        Order order = ordersService.checkOrderStatus(id);
        return order == null ? new ResponseEntity<>(HttpStatusCode.valueOf(404)) : ResponseEntity.ok(order);
    }
}
