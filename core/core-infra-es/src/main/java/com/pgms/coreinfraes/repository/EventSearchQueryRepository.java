package com.pgms.coreinfraes.repository;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;
import com.pgms.coreinfraes.dto.EventSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventSearchQueryRepository {

	private static final String MINUM_SHOULD_MATCH_PERCENTAGE = "75%";

	private final ElasticsearchOperations elasticsearchOperations;

	public Page<EventDocument> findByCondition(EventSearchDto eventSearchDto) {
		Pageable pageable = eventSearchDto.pageable();
		CriteriaQuery query = createConditionCriteriaQuery(eventSearchDto, pageable);

		SearchHits<EventDocument> searchHits = elasticsearchOperations.search(query, EventDocument.class);

		return SearchHitSupport.searchPageFor(searchHits, query.getPageable()).map(SearchHit::getContent);
	}

	public Page<EventDocument> findByKeyword(EventKeywordSearchDto eventKeywordSearchDto) {
		Pageable pageable = eventKeywordSearchDto.pageable();
		NativeQuery query = getKeywordSearchNativeQuery(eventKeywordSearchDto, pageable).setPageable(pageable);

		SearchHits<EventDocument> searchHits = elasticsearchOperations.search(query, EventDocument.class);
		return SearchHitSupport.searchPageFor(searchHits, query.getPageable()).map(SearchHit::getContent);
	}

	private static NativeQuery getKeywordSearchNativeQuery(EventKeywordSearchDto eventKeywordSearchDto, Pageable pageable) {
		NativeQueryBuilder queryBuilder = new NativeQueryBuilder();

		Query multiQuery = QueryBuilders.multiMatch()
				.query(eventKeywordSearchDto.keyword())
				.fields("title^1", "title_chosung^1", "description^1", "genreType^1")
				.minimumShouldMatch(MINUM_SHOULD_MATCH_PERCENTAGE)
				.build()._toQuery();

		List<Query> filterList = new ArrayList<>();

		if(eventKeywordSearchDto.genreType() != null){
			List<FieldValue> fieldValues = eventKeywordSearchDto.genreType().stream()
					.map(FieldValue::of)
					.toList();

			TermsQueryField termsQueryField = new TermsQueryField.Builder()
					.value(fieldValues)
					.build();

			Query genreFilterQuery = QueryBuilders
					.terms()
					.field("genreType")
					.terms(termsQueryField)
					.build()._toQuery();

			filterList.add(genreFilterQuery);
		}

		if(eventKeywordSearchDto.startedAt() != null){
			Query startedAtFilterQuery = QueryBuilders
					.range()
					.field("startedAt")
					.gte(JsonData.of(eventKeywordSearchDto.startedAt()))
					.build()._toQuery();

			filterList.add(startedAtFilterQuery);
		}

		if(eventKeywordSearchDto.endedAt() != null){
			Query endedAtFilterQuery = QueryBuilders
					.range()
					.field("endedAt")
					.gte(JsonData.of(eventKeywordSearchDto.endedAt()))
					.build()._toQuery();

			filterList.add(endedAtFilterQuery);
		}

		Query boolQuery = QueryBuilders.bool()
				.filter(filterList)
				.must(multiQuery)
				.build()._toQuery();

		FunctionScore fieldValueFactorScoreFunction = new FieldValueFactorScoreFunction.Builder()
				.field("id")
				.factor(1.2)
				.modifier(FieldValueFactorModifier.None)
				.missing(1.0)
				.build()._toFunctionScore();

		Query functionScoreQuery = QueryBuilders.functionScore()
				.functions(List.of(fieldValueFactorScoreFunction))
				.query(boolQuery)
				.build()._toQuery();

		return queryBuilder.withQuery(functionScoreQuery)
				.build();
	}

	public<T> void bulkUpdate(List<T> documents) {
		try{
			List<UpdateQuery> updateQueries = new ArrayList<>();
			for (T document : documents) {
				Document esDocument = elasticsearchOperations.getElasticsearchConverter().mapObject(document);
				UpdateQuery updateQuery = UpdateQuery.builder(esDocument.getId())
						.withDocument(esDocument)
						.withDocAsUpsert(true)
						.build();
				updateQueries.add(updateQuery);
			}
			elasticsearchOperations.bulkUpdate(updateQueries, IndexCoordinates.of("event"));
		} catch (Exception e){
			throw e;
		}
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
