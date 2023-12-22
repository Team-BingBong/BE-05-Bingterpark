package com.pgms.coredomain.domain.order;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_cancel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderCancel extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "createdBy", nullable = false)
    private String created_by;

    // TODO: 예매 매칭
}
