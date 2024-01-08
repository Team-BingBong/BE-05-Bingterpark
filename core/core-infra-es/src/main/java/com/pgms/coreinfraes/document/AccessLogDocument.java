package com.pgms.coreinfraes.document;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(indexName = "accessLog")
public class AccessLogDocument {

    @Id
    private String id;

    @Field(name = "@timestamp",type = FieldType.Date)
    private LocalDateTime date;

    @Field(type = FieldType.Keyword)
    private String message;

    @Field(name = "search_keyword", type = FieldType.Keyword)
    private String searchKeyword;
}
