package com.pgms.coredomain.domain.common.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "event_image")
public class EventImage extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private String url;
}
