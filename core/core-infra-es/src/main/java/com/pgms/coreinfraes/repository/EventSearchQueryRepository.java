package com.pgms.coreinfraes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Repository;

import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;
import com.pgms.coreinfraes.dto.EventSearchDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventSearchQueryRepository {

	private final ElasticsearchOperations elasticsearchOperations;

	public Page<EventDocument> findByCondition(EventSearchDto eventSearchDto) {
		Pageable pageable = eventSearchDto.pageable();
		CriteriaQuery query = createConditionCriteriaQuery(eventSearchDto, pageable);

		SearchHits<EventDocument> searchHits = elasticsearchOperations.search(query, EventDocument.class);

		return SearchHitSupport.searchPageFor(searchHits, query.getPageable()).map(SearchHit::getContent);
	}

	public Page<EventDocument> findByKeyword(EventKeywordSearchDto eventKeywordSearchDto) {
		Pageable pageable = eventKeywordSearchDto.pageable();
		String elasticsearchQuery = """
			{
			    "function_score": {
			        "query": {
			            "multi_match": {
			                "query": "%s",
			                "fields": [ "title^1", "title_chosung^1", "description^1", "genreType^1" ],
			                "minimum_should_match": 1
			            }
			        },
			        "functions": [
			            {
			                "field_value_factor": {
			                    "field": "id",
			                    "factor": 1.2,
			                    "modifier": "none",
			                    "missing": 1
			                }
			            }
			        ]
			    }
			}
			""".formatted(eventKeywordSearchDto.keyword());
		Query query = new StringQuery(elasticsearchQuery).setPageable(pageable);
		SearchHits<EventDocument> searchHits = elasticsearchOperations.search(query, EventDocument.class);
		return SearchHitSupport.searchPageFor(searchHits, query.getPageable()).map(SearchHit::getContent);
	}

	private CriteriaQuery createConditionCriteriaQuery(EventSearchDto eventSearchDto, Pageable pageable) {
		CriteriaQuery query = new CriteriaQuery(new Criteria()).setPageable(pageable);

		if (eventSearchDto == null)
			return query;

		if (eventSearchDto.title() != null)
			return query.addCriteria(Criteria.where("title").is(eventSearchDto.title()));

		if (eventSearchDto.genreType() != null)
			return query.addCriteria(Criteria.where("genreType").is(eventSearchDto.genreType()));
		return query;
	}
}
