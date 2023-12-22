package com.pgms.coredomain.domain.common.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class EventImage {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private String url;
}
