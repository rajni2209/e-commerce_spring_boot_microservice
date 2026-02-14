package com.ecommerce.searchservice.service;

import com.ecommerce.searchservice.document.Search;
import com.ecommerce.searchservice.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public List<Search> search(String keyword){
        return searchRepository.findByNameContainingIgnoreCase(keyword);
    }

}
