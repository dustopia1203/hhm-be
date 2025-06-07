package com.hhm.api.repository.elasticsearch;

import com.hhm.api.model.elasticsearch.ProductDocument;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, UUID> {
  @Query("{\"match_phrase_prefix\": {\"name.folded\": {\"query\": \"?0\"}}}")
  List<ProductDocument> suggestByNameOrDescription(String query);

}