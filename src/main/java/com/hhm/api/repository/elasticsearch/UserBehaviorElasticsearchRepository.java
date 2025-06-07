package com.hhm.api.repository.elasticsearch;

import com.hhm.api.model.elasticsearch.UserBehaviorDocument;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface UserBehaviorElasticsearchRepository extends ElasticsearchRepository<UserBehaviorDocument, String> {
  @Query("""
    {
      "bool": {
        "must": [
          { "term": { "userId": "?0" }},
          { "term": { "behaviorType": "SEARCH" }}
        ]
      }
    }
    """)
  List<UserBehaviorDocument> findRecentSearches(UUID userId, Pageable pageable);
}