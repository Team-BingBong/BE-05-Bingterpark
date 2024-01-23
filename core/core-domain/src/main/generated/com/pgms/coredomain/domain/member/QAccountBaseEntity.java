package com.pgms.coredomain.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccountBaseEntity is a Querydsl query type for AccountBaseEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAccountBaseEntity extends EntityPathBase<AccountBaseEntity> {

    private static final long serialVersionUID = -313058032L;

    public static final QAccountBaseEntity accountBaseEntity = new QAccountBaseEntity("accountBaseEntity");

    public final com.pgms.coredomain.domain.common.QBaseEntity _super = new com.pgms.coredomain.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastPasswordUpdatedAt = createDateTime("lastPasswordUpdatedAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAccountBaseEntity(String variable) {
        super(AccountBaseEntity.class, forVariable(variable));
    }

    public QAccountBaseEntity(Path<? extends AccountBaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountBaseEntity(PathMetadata metadata) {
        super(AccountBaseEntity.class, metadata);
    }

}

