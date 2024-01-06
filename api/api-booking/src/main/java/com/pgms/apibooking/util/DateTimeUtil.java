package com.pgms.apibooking.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

	private static final String FORMATTER = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";

	public static LocalDateTime parse(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
		return LocalDateTime.parse(dateString, formatter);
	}

	public static LocalDateTime parseNano(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
		return LocalDateTime.parse(dateString, formatter);
	}
}
