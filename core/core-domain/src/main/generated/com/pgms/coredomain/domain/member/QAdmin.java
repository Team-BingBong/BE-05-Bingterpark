package com.pgms.coredomain.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdmin is a Querydsl query type for Admin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdmin extends EntityPathBase<Admin> {

    private static final long serialVersionUID = -946157890L;

    public static final QAdmin admin = new QAdmin("admin");

    public final QAccountBaseEntity _super = new QAccountBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = _super.lastLoginAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastPasswordUpdatedAt = _super.lastPasswordUpdatedAt;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final EnumPath<com.pgms.coredomain.domain.member.enums.Role> role = createEnum("role", com.pgms.coredomain.domain.member.enums.Role.class);

    public final EnumPath<com.pgms.coredomain.domain.member.enums.AccountStatus> status = createEnum("status", com.pgms.coredomain.domain.member.enums.AccountStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAdmin(String variable) {
        super(Admin.class, forVariable(variable));
    }

    public QAdmin(Path<? extends Admin> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdmin(PathMetadata metadata) {
        super(Admin.class, metadata);
    }

}

