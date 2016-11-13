package com.project2.roommate.repository.search;

import com.project2.roommate.domain.Easyroommate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Easyroommate entity.
 */
public interface EasyroommateSearchRepository extends ElasticsearchRepository<Easyroommate, Long> {
}
