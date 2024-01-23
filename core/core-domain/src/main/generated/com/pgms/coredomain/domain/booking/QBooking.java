package com.pgms.coredomain.domain.booking;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBooking is a Querydsl query type for Booking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBooking extends EntityPathBase<Booking> {

    private static final long serialVersionUID = 438110433L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBooking booking = new QBooking("booking");

    public final com.pgms.coredomain.domain.common.QBaseEntity _super = new com.pgms.coredomain.domain.common.QBaseEntity(this);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final StringPath bookingName = createString("bookingName");

    public final StringPath buyerName = createString("buyerName");

    public final StringPath buyerPhoneNumber = createString("buyerPhoneNumber");

    public final QBookingCancel cancel;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath detailAddress = createString("detailAddress");

    public final StringPath id = createString("id");

    public final com.pgms.coredomain.domain.member.QMember member;

    public final QPayment payment;

    public final EnumPath<ReceiptType> receiptType = createEnum("receiptType", ReceiptType.class);

    public final StringPath recipientName = createString("recipientName");

    public final StringPath recipientPhoneNumber = createString("recipientPhoneNumber");

    public final EnumPath<BookingStatus> status = createEnum("status", BookingStatus.class);

    public final StringPath streetAddress = createString("streetAddress");

    public final ListPath<Ticket, QTicket> tickets = this.<Ticket, QTicket>createList("tickets", Ticket.class, QTicket.class, PathInits.DIRECT2);

    public final com.pgms.coredomain.domain.event.QEventTime time;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath zipCode = createString("zipCode");

    public QBooking(String variable) {
        this(Booking.class, forVariable(variable), INITS);
    }

    public QBooking(Path<? extends Booking> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBooking(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBooking(PathMetadata metadata, PathInits inits) {
        this(Booking.class, metadata, inits);
    }

    public QBooking(Class<? extends Booking> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cancel = inits.isInitialized("cancel") ? new QBookingCancel(forProperty("cancel"), inits.get("cancel")) : null;
        this.member = inits.isInitialized("member") ? new com.pgms.coredomain.domain.member.QMember(forProperty("member")) : null;
        this.payment = inits.isInitialized("payment") ? new QPayment(forProperty("payment"), inits.get("payment")) : null;
        this.time = inits.isInitialized("time") ? new com.pgms.coredomain.domain.event.QEventTime(forProperty("time"), inits.get("time")) : null;
    }

}

