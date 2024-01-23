package com.pgms.coredomain.domain.event;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventSeat is a Querydsl query type for EventSeat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventSeat extends EntityPathBase<EventSeat> {

    private static final long serialVersionUID = 778862216L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventSeat eventSeat = new QEventSeat("eventSeat");

    public final com.pgms.coredomain.domain.common.QBaseEntity _super = new com.pgms.coredomain.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QEventSeatArea eventSeatArea;

    public final QEventTime eventTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final EnumPath<EventSeatStatus> status = createEnum("status", EventSeatStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEventSeat(String variable) {
        this(EventSeat.class, forVariable(variable), INITS);
    }

    public QEventSeat(Path<? extends EventSeat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventSeat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventSeat(PathMetadata metadata, PathInits inits) {
        this(EventSeat.class, metadata, inits);
    }

    public QEventSeat(Class<? extends EventSeat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.eventSeatArea = inits.isInitialized("eventSeatArea") ? new QEventSeatArea(forProperty("eventSeatArea"), inits.get("eventSeatArea")) : null;
        this.eventTime = inits.isInitialized("eventTime") ? new QEventTime(forProperty("eventTime"), inits.get("eventTime")) : null;
    }

}

