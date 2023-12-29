package com.pgms.apievent.image.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ThumbnailCreateRequest(MultipartFile thumbnail) {
}
