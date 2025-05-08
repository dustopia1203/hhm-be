package com.hhm.api.service.impl;

import com.hhm.api.model.elasticsearch.ProductDocument;
import com.hhm.api.repository.elasticsearch.ProductElasticsearchRepository;
import com.hhm.api.service.ProductElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductElasticsearchServiceImpl implements ProductElasticsearchService {
    private final ProductElasticsearchRepository productElasticsearchRepository;

    @Override
    public List<ProductDocument> suggest(String query) {
        return productElasticsearchRepository.suggestByNameOrDescription(query);
    }
} 