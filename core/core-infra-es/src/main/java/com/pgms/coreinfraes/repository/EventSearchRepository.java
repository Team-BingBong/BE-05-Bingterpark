package com.pgms.coreinfraes.repository;

import com.pgms.coreinfraes.document.EventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EventSearchRepository extends ElasticsearchRepository<EventDocument, Long> {
    List<EventDocument> findEventDocumentsByTitleOrGenreType(String keyword);
}
