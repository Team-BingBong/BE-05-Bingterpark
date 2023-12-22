package com.pgms.coredomain.domain.common.event;

import jakarta.persistence.*;

@Entity
@Table(name = "event_review")
public class EventReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    @Lob
    private String content;
}
