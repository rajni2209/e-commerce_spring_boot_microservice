package com.ecommerce.orderservice.kafka;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

    @Bean
    public NewTopic orderCreatedTopic(){
        return TopicBuilder
                .name("order-created")
                .build();
    }

}
