package com.pgms.coreinfraes.dto.request;

import com.pgms.coredomain.domain.event.GenreType;

public record EventSearchRequest(String title, GenreType genreType) {
}
