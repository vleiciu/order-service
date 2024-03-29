package com.org.os.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

import static com.org.ma.utils.Constants.*;

@Configuration
@EnableKafka
public class KafkaAdminConfig {

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic orderTopic() {return new NewTopic(ORDER_CHANNEL, 1, (short) 1);}

    @Bean
    public NewTopic paymentTopic() {
        return new NewTopic(PAYMENT_CHANNEL, 1, (short) 1);
    }

    @Bean
    public NewTopic restaurantTopic() {
        return new NewTopic(RESTAURANT_CHANNEL, 1, (short) 1);
    }

    @Bean
    public NewTopic deliveryTopic() {
        return new NewTopic(DELIVERY_CHANNEL, 1, (short) 1);
    }
}
