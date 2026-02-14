package com.ecommerce.productservice.kafka;

import com.ecommerce.productservice.DTO.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private static final String TOPIC = "product-events";

    public void publish(ProductCreatedEvent event){
        kafkaTemplate.send(TOPIC,
                String.valueOf(event.getId()),
                event
        );
    }


}
