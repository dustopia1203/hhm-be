package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.elasticsearch.ProductDocument;
import com.hhm.api.presentation.web.rest.ProductElasticsearchController;
import com.hhm.api.service.ProductElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductElasticsearchControllerImpl implements ProductElasticsearchController {
    private final ProductElasticsearchService productElasticsearchService;
    
    @Override
    public List<ProductDocument> suggest(String query) {
        return productElasticsearchService.suggest(query);
    }
} 