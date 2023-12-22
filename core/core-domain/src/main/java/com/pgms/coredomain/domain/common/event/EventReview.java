package com.pgms.coredomain.domain.common.event;

import jakarta.persistence.*;

@Entity
public class EventReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    @Lob
    private String content;
}
