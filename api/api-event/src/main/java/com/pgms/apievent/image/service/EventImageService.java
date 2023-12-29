package com.pgms.apievent.image.service;

import static com.pgms.apievent.exception.EventErrorCode.*;
import static com.pgms.apievent.util.ImageUtil.*;

import java.net.URL;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pgms.apievent.exception.CustomException;
import com.pgms.apievent.image.dto.request.EventImageAddRequest;
import com.pgms.apievent.image.dto.request.EventImageCreateRequest;
import com.pgms.apievent.image.dto.request.ThumbnailUpdateRequest;
import com.pgms.apievent.util.ImageUtil;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventImage;
import com.pgms.coredomain.domain.event.repository.EventImageRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventImageService {

	private final S3Service s3Service;
	private final EventRepository eventRepository;
	private final EventImageRepository eventImageRepository;

	public URL createThumbnail(MultipartFile thumbnail) {
		String storedFileName = extractExtAndGenerateUniqueName(thumbnail.getOriginalFilename());
		return uploadThumbnailImageToServer(thumbnail, storedFileName);
	}

	public void createEventDetailImages(Long eventId, EventImageCreateRequest request) {
		Event event = getEvent(eventId);
		List<EventImage> eventImages = getEventImages(request.eventImages(), event);
		eventImageRepository.saveAll(eventImages);
	}

	public void updateThumbnail(Long eventId, ThumbnailUpdateRequest request) {
		Event event = getEvent(eventId);
		String storedFileName = extractExtAndGenerateUniqueName(request.thumbnail().getOriginalFilename());
		event.updateThumbnail(storedFileName);
		updateEventThumbnailFromS3(event.getThumbnail(), request.thumbnail(), storedFileName);
	}

	public void updateEventThumbnailFromS3(String eventThumbnail, MultipartFile file, String updateThumbnail) {
		if (!eventThumbnail.isBlank()) {
			String fileName = eventThumbnail.substring(1);
			s3Service.delete(fileName);
		}
		s3Service.upload(file, updateThumbnail);
	}

	public void addEventImages(Long id, EventImageAddRequest request) {
		Event event = getEvent(id);
		List<MultipartFile> addedImages = request.addedImages();
		List<EventImage> eventImages = convertMultiPartFileToEventImages(addedImages, event);
		uploadEventImagesToServer(eventImages, addedImages);
		eventImageRepository.saveAll(eventImages);
	}

	public void removeEventImages(Long id, List<Long> removedImageIds) {
		getEvent(id);
		List<EventImage> removedImages = eventImageRepository.findByIdIn(removedImageIds);
		deleteEventImagesFromServer(removedImages);
		eventImageRepository.deleteAllByIdIn(removedImageIds);
	}

	private Event getEvent(Long eventId) {
		return eventRepository.findById(eventId)
			.orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
	}

	private URL uploadThumbnailImageToServer(MultipartFile file, String storedFileName) {
		return s3Service.upload(file, storedFileName);
	}

	private List<EventImage> getEventImages(List<MultipartFile> multipartFiles, Event event) {
		if (multipartFiles != null) {
			List<EventImage> eventImages = convertMultiPartFileToEventImages(multipartFiles, event);
			uploadEventImagesToServer(eventImages, multipartFiles);
			return eventImages;
		}
		return List.of();
	}

	private void uploadEventImagesToServer(List<EventImage> uploadImages, List<MultipartFile> fileImages) {
		IntStream.range(0, uploadImages.size())
			.forEach(
				i -> s3Service.upload(fileImages.get(i), uploadImages.get(i).getUrl()));
	}

	private void deleteEventImagesFromServer(List<EventImage> deleteImages) {
		IntStream.range(0, deleteImages.size())
			.forEach(i -> s3Service.delete(deleteImages.get(i).getUrl()));
	}

	private List<EventImage> convertMultiPartFileToEventImages(List<MultipartFile> multipartFiles, Event event) {
		return multipartFiles.stream()
			.map(i -> new EventImage(
				ImageUtil.extractExtAndGenerateUniqueName(i.getOriginalFilename()),
				event))
			.toList();
	}
}
