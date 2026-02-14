package com.ecommerce.productservice.service;

import com.ecommerce.productservice.DTO.ProductCreatedEvent;
import com.ecommerce.productservice.DTO.ProductIndexRequest;
import com.ecommerce.productservice.DTO.ProductRequest;
import com.ecommerce.productservice.DTO.ProductResponse;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.kafka.ProductEventProducer;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.template.SearchInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ProductEventProducer productEventProducer;
//    private final SearchInterface searchInterface;


    public Product create(ProductRequest request){

        Category category = categoryRepository.findByName(request.getName()).orElseGet(() -> {
            Category c = new Category();
            c.setName(request.getCategory());
            return categoryRepository.save(c);
        });

        Product product = modelMapper.map(request, Product.class);
        product.setCategory(category);
        Product save = productRepository.save(product);

//        searchInterface.index(new ProductIndexRequest(
//                save.getId(),
//                save.getName(),
//                save.getDescription(),
//                save.getPrice(),
//                save.getCategory().getName()
//        ));

        productEventProducer.publish(
                new ProductCreatedEvent(
                        save.getId(),
                        save.getName(),
                        save.getDescription(),
                        save.getPrice(),
                        save.getCategory().getName()
                )
        );
        log.info("event=ProductEvent_PRODUCED productId={}",save.getId());
        return save;

    }

    public List<ProductResponse> listAvailableProduct(){
        return productRepository.findByAvailableTrue().stream()
                .map(ProductResponse:: from)
                .toList();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }



}
