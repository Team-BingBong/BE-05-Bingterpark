package com.pgms.apievent.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.GenreType;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JdbcTemplateEventRepository {

	private final JdbcTemplate jdbcTemplate;

	public void bulkSave(List<Event> events) {
		batchInsert(9000, events);
	}

	private void batchInsert(int batchSize, List<Event> events) {

		jdbcTemplate.batchUpdate(
			"INSERT INTO event " +
				"(created_at, updated_at, average_score, booking_ended_at, booking_started_at, description, " +
				"ended_at, genre, running_time, started_at, thumbnail, title, rating, event_hall_id) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Event event = events.get(i);
					ps.setObject(1, LocalDateTime.now());
					ps.setObject(2, LocalDateTime.now());
					ps.setObject(3, 0);
					ps.setObject(4, LocalDateTime.now().plusDays(10));
					ps.setObject(5, LocalDateTime.now());
					ps.setObject(6, event.getDescription());
					ps.setObject(7, LocalDateTime.now().plusDays(10));
					ps.setObject(8, GenreType.MUSICAL.name());
					ps.setObject(9, 100);
					ps.setObject(10, LocalDateTime.now());
					ps.setObject(11, LocalDateTime.now());
					ps.setObject(12, event.getTitle());
					ps.setObject(13, event.getViewRating());
					ps.setObject(14, 1L);
				}

				@Override
				public int getBatchSize() {
					return Math.min(batchSize, events.size());
				}
			});
	}
}
