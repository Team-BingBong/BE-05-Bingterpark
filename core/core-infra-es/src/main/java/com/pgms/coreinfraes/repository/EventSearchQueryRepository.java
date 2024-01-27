package com.pgms.coreinfraes.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ScriptType;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.ScriptData;
import org.springframework.data.elasticsearch.core.query.ScriptedField;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Repository;

import com.pgms.coreinfraes.document.AccessLogDocument;
import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.dto.EventDocumentResponse;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;
import com.pgms.coreinfraes.dto.TopTenSearchResponse;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.ScriptLanguage;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EventSearchQueryRepository {

	private static final String MINIMUM_SHOULD_MATCH_PERCENTAGE = "75%";

	private final ElasticsearchOperations elasticsearchOperations;

	public Page<EventDocumentResponse> findByKeyword(EventKeywordSearchDto eventKeywordSearchDto) {
		Pageable pageable = eventKeywordSearchDto.pageable();
		NativeQuery query = getKeywordSearchNativeQuery(eventKeywordSearchDto).setPageable(pageable);

		SearchHits<EventDocument> searchHits = elasticsearchOperations.search(query, EventDocument.class);
		log.info("event-keyword-search, {}", eventKeywordSearchDto.keyword());

		return SearchHitSupport.searchPageFor(searchHits, query.getPageable()).map(s -> {
			EventDocument eventDocument = s.getContent();
			return EventDocumentResponse.of(eventDocument);
		});
	}

	public List<TopTenSearchResponse> getRecentTop10Keywords() {
		NativeQuery searchQuery = getRecentTop10KeywordsNativeQuery();

		SearchHits<AccessLogDocument> searchHits = elasticsearchOperations.search(searchQuery, AccessLogDocument.class,
			IndexCoordinates.of("logstash*"));

		ElasticsearchAggregations aggregations = (ElasticsearchAggregations)searchHits.getAggregations();
		assert aggregations != null;
		List<StringTermsBucket> topTenBuckets = aggregations.aggregationsAsMap()
			.get("top_ten")
			.aggregation()
			.getAggregate()
			.sterms()
			.buckets()
			.array();

		List<TopTenSearchResponse> result = new ArrayList<>();

		topTenBuckets.forEach(topTenBucket -> {
			TopTenSearchResponse topTenSearchResponse = new TopTenSearchResponse(topTenBucket.key().stringValue(),
				topTenBucket.docCount());
			result.add(topTenSearchResponse);
		});

		return result;
	}

	private NativeQuery getRecentTop10KeywordsNativeQuery() {
		NativeQueryBuilder queryBuilder = new NativeQueryBuilder();

		Query matchQuery = QueryBuilders.match()
			.field("message")
			.query("event-keyword-search")
			.build()
			._toQuery();

		Query loggerQuery = QueryBuilders.term()
			.field("logger_name.keyword")
			.value("com.pgms.coreinfraes.repository.EventSearchQueryRepository")
			.build()
			._toQuery();

		Query rangeQuery = QueryBuilders.range()
			.field("@timestamp")
			.gte(JsonData.of(LocalDateTime.now()
				.truncatedTo(ChronoUnit.HOURS)
				.atZone(ZoneId.systemDefault())
				.toInstant()
				.toEpochMilli()))
			.lte(JsonData.of(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
			.build()
			._toQuery();

		Script script = Script.of(scriptBuilder -> scriptBuilder.inline(inlineScriptBuilder ->
			inlineScriptBuilder.lang(ScriptLanguage.Painless)
				.source("doc['message.keyword'].value.substring(22);")
				.params(Collections.emptyMap())
		));

		Aggregation agg = AggregationBuilders.terms()
			.script(script)
			.size(10)
			.build()
			._toAggregation();

		Query boolQuery = QueryBuilders.bool()
			.must(matchQuery, loggerQuery, rangeQuery)
			.build()
			._toQuery();

		ScriptedField scriptedField = new ScriptedField("search_keyword", new ScriptData(
			ScriptType.INLINE,
			"painless",
			"return doc['message.keyword'].value.substring(22);",
			"keyword_script",
			Collections.emptyMap()
		));

		SourceFilter sourceFilter = new FetchSourceFilter(new String[] {"*"}, new String[] {});

		return queryBuilder.withQuery(boolQuery)
			.withSourceFilter(sourceFilter)
			.withScriptedField(scriptedField)
			.withAggregation("top_ten", agg)
			.build();
	}

	private NativeQuery getKeywordSearchNativeQuery(EventKeywordSearchDto eventKeywordSearchDto) {
		NativeQueryBuilder queryBuilder = new NativeQueryBuilder();

		// Query multiQuery = QueryBuilders.multiMatch()
		// 	.query(eventKeywordSearchDto.keyword())
		// 	.fields("title.ngram^1", "title_chosung^1", "description^1", "genreType^1")
		// 	.minimumShouldMatch(MINIMUM_SHOULD_MATCH_PERCENTAGE)
		// 	.build()._toQuery();

		Query query = QueryBuilders.match()
			.query(eventKeywordSearchDto.keyword())
			.field("keyword_text")
			.build()._toQuery();

		List<Query> filterList = new ArrayList<>();

		if (eventKeywordSearchDto.genreType() != null) {
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

		if (eventKeywordSearchDto.startedAt() != null) {
			Query startedAtFilterQuery = QueryBuilders
				.range()
				.field("startedAt")
				.gte(JsonData.of(eventKeywordSearchDto.startedAt()))
				.build()._toQuery();

			filterList.add(startedAtFilterQuery);
		}

		if (eventKeywordSearchDto.endedAt() != null) {
			Query endedAtFilterQuery = QueryBuilders
				.range()
				.field("endedAt")
				.gte(JsonData.of(eventKeywordSearchDto.endedAt()))
				.build()._toQuery();

			filterList.add(endedAtFilterQuery);
		}

		Query boolQuery = QueryBuilders.bool()
			.filter(filterList)
			.must(query)
			.build()._toQuery();

		// FunctionScore fieldValueFactorScoreFunction = new FieldValueFactorScoreFunction.Builder()
		// 	.field("id")
		// 	.factor(1.2)
		// 	.modifier(FieldValueFactorModifier.None)
		// 	.missing(1.0)
		// 	.build()._toFunctionScore();
		//
		// Query functionScoreQuery = QueryBuilders.functionScore()
		// 	.functions(List.of(fieldValueFactorScoreFunction))
		// 	.query(boolQuery)
		// 	.build()._toQuery();

		return queryBuilder.withQuery(boolQuery)
			.build();
	}

	public <T> void bulkUpdate(List<T> documents) {
		try {
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
		} catch (Exception e) {
			throw e;
		}
	}

	public <T> void bulkInsert(List<T> documents) {
		try {
			int chunkSize = 50;
			List<IndexQuery> insertQueries = new ArrayList<>();

			for (int i = 0; i < documents.size(); i += chunkSize) {
				List<T> chunk = documents.subList(i, Math.min(i + chunkSize, documents.size()));

				for (T document : chunk) {
					IndexQuery indexQuery = new IndexQueryBuilder()
						.withObject(document)
						.build();
					insertQueries.add(indexQuery);
				}

				elasticsearchOperations.bulkIndex(insertQueries, IndexCoordinates.of("events"));
				insertQueries.clear();
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
