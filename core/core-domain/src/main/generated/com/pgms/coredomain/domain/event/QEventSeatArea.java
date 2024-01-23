package com.pgms.coredomain.domain.event;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventSeatArea is a Querydsl query type for EventSeatArea
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventSeatArea extends EntityPathBase<EventSeatArea> {

    private static final long serialVersionUID = 261701429L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventSeatArea eventSeatArea = new QEventSeatArea("eventSeatArea");

    public final com.pgms.coredomain.domain.common.QBaseEntity _super = new com.pgms.coredomain.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final EnumPath<SeatAreaType> seatAreaType = createEnum("seatAreaType", SeatAreaType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEventSeatArea(String variable) {
        this(EventSeatArea.class, forVariable(variable), INITS);
    }

    public QEventSeatArea(Path<? extends EventSeatArea> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventSeatArea(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventSeatArea(PathMetadata metadata, PathInits inits) {
        this(EventSeatArea.class, metadata, inits);
    }

    public QEventSeatArea(Class<? extends EventSeatArea> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new QEvent(forProperty("event"), inits.get("event")) : null;
    }

}

