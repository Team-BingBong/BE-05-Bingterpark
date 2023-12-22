package com.pgms.coredomain.domain.common.event;

import jakarta.persistence.*;

@Entity
@Table(name = "event_image")
public class EventImage {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private String url;
}
