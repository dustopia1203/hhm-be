package com.hhm.api.repository.elasticsearch;

import com.hhm.api.model.elasticsearch.UserBehaviorDocument;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserBehaviorElasticsearchRepository extends ElasticsearchRepository<UserBehaviorDocument, String> {

  @Query("""
      {
        "bool": {
          "must": [
            { "term": { "userId": "?0" } },
            { "range": { "timestamp": { "gte": "?1", "format": "date_optional_time||epoch_millis" } } }
          ]
        }
      }
      """)
  List<UserBehaviorDocument> findRecentBehaviors(UUID userId, Instant fromTime);

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
  @Query("""
    {
      "size": 0,
      "query": {
        "bool": {
          "must": [
            {
              "term": {
                "behaviorType": "SEARCH"
              }
            },
            {
              "exists": {
                "field": "searchQuery"
              }
            },
            {
              "range": {
                "timestamp": {
                  "gte": "now-7d/d"
                }
              }
            }
          ]
        }
      },
      "aggs": {
        "top_keywords": {
          "terms": {
            "field": "searchQuery.keyword",
            "size": 5,
            "order": {
              "_count": "desc"
            }
          }
        }
      }
    }
    """)
  Map<String, Object> getTopSearchKeywordsLast7Days();
}