package com.ecommerce.searchservice.DTO;

import com.ecommerce.searchservice.document.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private String category;

    public static SearchResponse from(Search doc) {
        return new SearchResponse(
                doc.getId(),
                doc.getName(),
                doc.getPrice(),
                doc.getCategory()
        );
    }

}
