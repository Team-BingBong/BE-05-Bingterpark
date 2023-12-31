package com.pgms.coredomain.domain.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.event.EventImage;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
	void deleteAllByIdIn(List<Long> imageIds);

	List<EventImage> findByIdIn(List<Long> imageIds);
}
