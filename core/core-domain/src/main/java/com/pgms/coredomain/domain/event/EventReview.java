package com.pgms.coredomain.domain.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import com.pgms.coredomain.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event_review")
public class EventReview extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "score")
	private int score;

	@Lob
	@Column(name = "content")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	public EventReview(Integer score, String content, Event event, Member member) {
		this.score = score;
		this.content = content;
		this.event = event;
		this.member = member;
	}

	public void updateEventReview(String content) {
		this.content = content;
	}

	public boolean isSameReviewer(Member member) {
		if (member != null) {
			return this.member.equals(member);
		}
		return false;
	}
}
