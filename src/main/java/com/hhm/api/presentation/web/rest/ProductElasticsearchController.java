package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.elasticsearch.ProductDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductElasticsearchController {
    @GetMapping("/api/v1/products/suggest")
    List<ProductDocument> suggest(@RequestParam String query);
} 