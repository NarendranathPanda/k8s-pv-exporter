package com.naren.k8s.pv.monitoring.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.naren.k8s.pv.monitoring.exception.InvalidInputException;

public class Util {

	public static String getDate() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH", Locale.ENGLISH);
		String date = formmat1.format(ldt);
		return date;
	}

	public static void validateString(String field, String value) {
		if (value == null || "".equals(value)) {
			throw new InvalidInputException(field, value);
		}

	}

}
