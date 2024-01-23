package com.pgms.coredomain.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1078342923L;

    public static final QMember member = new QMember("member1");

    public final QAccountBaseEntity _super = new QAccountBaseEntity(this);

    public final StringPath birthDate = createString("birthDate");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath detailAddress = createString("detailAddress");

    public final StringPath email = createString("email");

    public final EnumPath<com.pgms.coredomain.domain.member.enums.Gender> gender = createEnum("gender", com.pgms.coredomain.domain.member.enums.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = _super.lastLoginAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastPasswordUpdatedAt = _super.lastPasswordUpdatedAt;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final EnumPath<com.pgms.coredomain.domain.member.enums.Provider> provider = createEnum("provider", com.pgms.coredomain.domain.member.enums.Provider.class);

    public final EnumPath<com.pgms.coredomain.domain.member.enums.Role> role = createEnum("role", com.pgms.coredomain.domain.member.enums.Role.class);

    public final EnumPath<com.pgms.coredomain.domain.member.enums.AccountStatus> status = createEnum("status", com.pgms.coredomain.domain.member.enums.AccountStatus.class);

    public final StringPath streetAddress = createString("streetAddress");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath zipCode = createString("zipCode");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

