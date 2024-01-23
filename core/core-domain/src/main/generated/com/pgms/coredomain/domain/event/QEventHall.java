package com.pgms.coredomain.domain.event;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventHall is a Querydsl query type for EventHall
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventHall extends EntityPathBase<EventHall> {

    private static final long serialVersionUID = 778531004L;

    public static final QEventHall eventHall = new QEventHall("eventHall");

    public final com.pgms.coredomain.domain.common.QBaseEntity _super = new com.pgms.coredomain.domain.common.QBaseEntity(this);

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<EventHallSeat, QEventHallSeat> eventHallSeats = this.<EventHallSeat, QEventHallSeat>createList("eventHallSeats", EventHallSeat.class, QEventHallSeat.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEventHall(String variable) {
        super(EventHall.class, forVariable(variable));
    }

    public QEventHall(Path<? extends EventHall> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEventHall(PathMetadata metadata) {
        super(EventHall.class, metadata);
    }

}

