package com.org.os.service;

import com.org.os.persistance.entity.Restaurant;
import com.org.os.persistance.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {

    private RestaurantRepository restaurantRepository;

    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public Restaurant getById(Integer id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

}
