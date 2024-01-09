package com.pgms.apievent.eventSearch.dto.response;

import com.pgms.coreinfraes.dto.TopTenSearchResponse;

public record RecentTop10KeywordsResponse(String keyword) {
	public static RecentTop10KeywordsResponse of(TopTenSearchResponse topTenSearchResponse) {
		return new RecentTop10KeywordsResponse(topTenSearchResponse.keyword());
	}
}
