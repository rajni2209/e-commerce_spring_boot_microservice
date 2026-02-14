package com.ecommerce.searchservice.repository;

import com.ecommerce.searchservice.document.Search;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchRepository extends ElasticsearchRepository<Search , Long> {

    List<Search> findByNameContainingIgnoreCase(String keyword);

}
