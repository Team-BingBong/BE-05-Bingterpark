package com.pgms.coredomain.domain.event;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventHallSeat is a Querydsl query type for EventHallSeat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventHallSeat extends EntityPathBase<EventHallSeat> {

    private static final long serialVersionUID = -676334367L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventHallSeat eventHallSeat = new QEventHallSeat("eventHallSeat");

    public final com.pgms.coredomain.domain.common.QBaseEntity _super = new com.pgms.coredomain.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QEventHall eventHall;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEventHallSeat(String variable) {
        this(EventHallSeat.class, forVariable(variable), INITS);
    }

    public QEventHallSeat(Path<? extends EventHallSeat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventHallSeat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventHallSeat(PathMetadata metadata, PathInits inits) {
        this(EventHallSeat.class, metadata, inits);
    }

    public QEventHallSeat(Class<? extends EventHallSeat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.eventHall = inits.isInitialized("eventHall") ? new QEventHall(forProperty("eventHall")) : null;
    }

}

