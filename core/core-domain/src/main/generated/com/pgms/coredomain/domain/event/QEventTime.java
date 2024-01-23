package com.pgms.coredomain.domain.event;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventTime is a Querydsl query type for EventTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventTime extends EntityPathBase<EventTime> {

    private static final long serialVersionUID = 778896208L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventTime eventTime = new QEventTime("eventTime");

    public final com.pgms.coredomain.domain.common.QBaseEntity _super = new com.pgms.coredomain.domain.common.QBaseEntity(this);

    public final ListPath<com.pgms.coredomain.domain.booking.Booking, com.pgms.coredomain.domain.booking.QBooking> bookings = this.<com.pgms.coredomain.domain.booking.Booking, com.pgms.coredomain.domain.booking.QBooking>createList("bookings", com.pgms.coredomain.domain.booking.Booking.class, com.pgms.coredomain.domain.booking.QBooking.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endedAt = createDateTime("endedAt", java.time.LocalDateTime.class);

    public final QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> round = createNumber("round", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEventTime(String variable) {
        this(EventTime.class, forVariable(variable), INITS);
    }

    public QEventTime(Path<? extends EventTime> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventTime(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventTime(PathMetadata metadata, PathInits inits) {
        this(EventTime.class, metadata, inits);
    }

    public QEventTime(Class<? extends EventTime> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new QEvent(forProperty("event"), inits.get("event")) : null;
    }

}

