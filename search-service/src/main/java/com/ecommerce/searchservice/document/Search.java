package com.ecommerce.searchservice.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "products")
public class Search {

    @Id
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;

}
