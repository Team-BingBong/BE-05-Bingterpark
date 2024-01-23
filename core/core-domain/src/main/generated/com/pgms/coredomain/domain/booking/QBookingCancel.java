package com.pgms.coredomain.domain.booking;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookingCancel is a Querydsl query type for BookingCancel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookingCancel extends EntityPathBase<BookingCancel> {

    private static final long serialVersionUID = 2020619963L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookingCancel bookingCancel = new QBookingCancel("bookingCancel");

    public final com.pgms.coredomain.domain.common.QBaseEntity _super = new com.pgms.coredomain.domain.common.QBaseEntity(this);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final QBooking booking;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath createdBy = createString("createdBy");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reason = createString("reason");

    public final StringPath refundAccountNumber = createString("refundAccountNumber");

    public final EnumPath<BankCode> refundBankCode = createEnum("refundBankCode", BankCode.class);

    public final StringPath refundHolderName = createString("refundHolderName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBookingCancel(String variable) {
        this(BookingCancel.class, forVariable(variable), INITS);
    }

    public QBookingCancel(Path<? extends BookingCancel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookingCancel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookingCancel(PathMetadata metadata, PathInits inits) {
        this(BookingCancel.class, metadata, inits);
    }

    public QBookingCancel(Class<? extends BookingCancel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.booking = inits.isInitialized("booking") ? new QBooking(forProperty("booking"), inits.get("booking")) : null;
    }

}

