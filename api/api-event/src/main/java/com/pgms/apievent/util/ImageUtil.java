package com.pgms.apievent.util;

import static com.pgms.apievent.exception.EventErrorCode.*;

import java.util.Arrays;
import java.util.UUID;

import com.pgms.apievent.exception.CustomException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUtil {

	private static final int EXTENSION_START_INDEX = 1;
	private static final String[] EXTENSION = {"jpg", "jpeg", "png"};

	public static String extractExtAndGenerateUniqueName(String originName) {
		return UUID.randomUUID() + "." + getExtension(originName);
	}

	private static String getExtension(String originName) {
		String extension = originName.substring(originName.lastIndexOf(".") + EXTENSION_START_INDEX);
		if (supportFormat(extension)) {
			return extension;
		}
		throw new CustomException(UNSUPPORTED_FILE_EXTENSION);
	}

	private static boolean supportFormat(String ext) {
		return Arrays.stream(EXTENSION)
			.anyMatch(e -> e.equalsIgnoreCase(ext));
	}
}
