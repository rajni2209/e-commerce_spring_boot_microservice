package com.ecommerce.searchservice.kafka;

import com.ecommerce.searchservice.DTO.ProductCreatedEvent;
import com.ecommerce.searchservice.document.Search;
import com.ecommerce.searchservice.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final SearchRepository searchRepository;

    @KafkaListener(
            topics = "product-events",
            groupId = "search-service-groups"
    )
    public void consume(ProductCreatedEvent event){
        Search search = new Search();
        search.setId(event.getId());
        search.setName(event.getName());
        search.setDescription(event.getDescription());
        search.setPrice(event.getPrice());
        search.setCategory(event.getCategory());

        searchRepository.save(search);

        System.out.println("product received and stored");
    }

}
