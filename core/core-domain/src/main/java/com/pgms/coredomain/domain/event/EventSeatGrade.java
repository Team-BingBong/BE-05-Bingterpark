package com.pgms.coredomain.domain.event;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "event_seat_grade")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventSeatGrade extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_seat_grade_id")
	private Long id;

	@Column(name = "grade")
	private String grade;

	@Column(name = "price")
	private int price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Event event;

	public EventSeatGrade(String grade, int price, Event event) {
		this.grade = grade;
		this.price = price;
		this.event = event;
	}
}
