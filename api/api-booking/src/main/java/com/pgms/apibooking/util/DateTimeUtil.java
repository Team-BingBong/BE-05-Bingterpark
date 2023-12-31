package com.pgms.apibooking.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

	public static LocalDateTime parse(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
		return LocalDateTime.parse(dateString, formatter);
	}
}
