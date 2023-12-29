package com.pgms.apievent.image.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record EventImageAddRequest(List<MultipartFile> addedImages) {
}
