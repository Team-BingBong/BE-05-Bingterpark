package com.pgms.coredomain.domain.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "event_review")
public class EventReview extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score")
    private Integer score;

    @Lob
    @Column(name = "content")
    private String content;
}
