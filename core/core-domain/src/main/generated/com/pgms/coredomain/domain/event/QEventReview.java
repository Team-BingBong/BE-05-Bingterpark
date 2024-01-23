package com.pgms.coredomain.domain.event;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventReview is a Querydsl query type for EventReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventReview extends EntityPathBase<EventReview> {

    private static final long serialVersionUID = 1134269211L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventReview eventReview = new QEventReview("eventReview");

    public final com.pgms.coredomain.domain.common.QBaseEntity _super = new com.pgms.coredomain.domain.common.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.pgms.coredomain.domain.member.QMember member;

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEventReview(String variable) {
        this(EventReview.class, forVariable(variable), INITS);
    }

    public QEventReview(Path<? extends EventReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventReview(PathMetadata metadata, PathInits inits) {
        this(EventReview.class, metadata, inits);
    }

    public QEventReview(Class<? extends EventReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new QEvent(forProperty("event"), inits.get("event")) : null;
        this.member = inits.isInitialized("member") ? new com.pgms.coredomain.domain.member.QMember(forProperty("member")) : null;
    }

}

