package com.pgms.coreinfraes.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.pgms.coreinfraes.document.EventDocument;

public interface EventSearchRepository extends ElasticsearchRepository<EventDocument, Long> {
}
