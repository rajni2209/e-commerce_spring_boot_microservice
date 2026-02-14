package com.ecommerce.searchservice.controller;

import com.ecommerce.searchservice.DTO.SearchResponse;
import com.ecommerce.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<SearchResponse>> search(@RequestParam String keyword) {
        List<SearchResponse> result = searchService.search(keyword)
                .stream()
                .map(SearchResponse::from)
                .toList();

        return ResponseEntity.ok(result);
    }

}
