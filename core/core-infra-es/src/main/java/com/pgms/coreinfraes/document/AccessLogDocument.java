package com.pgms.coreinfraes.document;

import java.time.LocalDateTime;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(indexName = "accessLog")
public class AccessLogDocument {

	@Id
	private String id;

	@Field(name = "@timestamp", type = FieldType.Date)
	private LocalDateTime date;

	@Field(type = FieldType.Keyword)
	private String message;

	@Field(name = "search_keyword", type = FieldType.Keyword)
	private String searchKeyword;
}
