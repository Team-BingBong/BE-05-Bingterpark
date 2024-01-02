package com.pgms.coreinfraes.repository;

import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.dto.request.EventSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventSearchQueryRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<EventDocument> findByCondition(EventSearchRequest eventSearchRequest){
        CriteriaQuery query = createConditionCriteriaQuery(eventSearchRequest);

        SearchHits<EventDocument> search = elasticsearchOperations.search(query, EventDocument.class);

        return search.stream()
                .map(SearchHit::getContent)
                .toList();
    }

    private CriteriaQuery createConditionCriteriaQuery(EventSearchRequest eventSearchRequest){

        CriteriaQuery query = new CriteriaQuery(new Criteria());

        if(eventSearchRequest == null)
            return query;

        if(eventSearchRequest.title() != null)
            return query.addCriteria(Criteria.where("title").is(eventSearchRequest.title()));

        if(eventSearchRequest.genreType() != null)
            return query.addCriteria(Criteria.where("genreType").is(eventSearchRequest.genreType()));

        return query;
    }
}
