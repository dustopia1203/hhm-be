package com.hhm.api.service;

import com.hhm.api.model.elasticsearch.ProductDocument;

import java.util.List;

public interface ProductElasticsearchService {
    List<ProductDocument> suggest(String query);
} 