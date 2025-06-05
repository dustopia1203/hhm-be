package com.hhm.api.repository.elasticsearch;

import com.hhm.api.model.elasticsearch.ProductDocument;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, UUID> {
  @Query("{\"match_phrase_prefix\": {\"name.folded\": {\"query\": \"?0\"}}}")
  List<ProductDocument> suggestByNameOrDescription(String query);

  @Query("""
      {
        "query": {
          "bool": {
            "should": [
              {
                "terms": {
                  "categoryId": ?0,
                  "boost": 2.0
                }
              },
              {
                "terms": {
                  "shopId": ?1,
                  "boost": 1.5
                }
              }
            ],
            "minimum_should_match": 1,
            "must_not": [
              {
                "terms": {
                  "id": ?2
                }
              }
            ]
          }
        },
        "sort": [
          {
            "_score": {
              "order": "desc"
            }
          }
        ],
        "size": ?3
      }
      """)
  List<ProductDocument> findSimilarProducts(List<String> categoryIds, List<String> shopIds, List<UUID> excludeIds,
      int size);

  @Query("""

        "query": {
          "bool": {
            "should": [
              {"match": {"name": {"query": "?0", "boost": 2}}},
              {"match": {"description": "?0"}}
            ],
            "must_not": [
              {"terms": {"id": ?1}}
            ]
          }
        }

      """)
  List<ProductDocument> findProductsBySearchQuery(String query, List<UUID> excludeIds, int size);
}