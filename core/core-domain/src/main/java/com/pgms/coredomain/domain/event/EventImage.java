package com.pgms.coredomain.domain.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "event_image")
public class EventImage extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "url")
    private String url;
}
