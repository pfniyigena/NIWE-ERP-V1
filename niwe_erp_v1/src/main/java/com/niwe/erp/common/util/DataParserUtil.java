package com.niwe.erp.common.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class DataParserUtil {
	private DataParserUtil() {
		throw new IllegalStateException("Utility class SdcUtil");
	}

	public static Instant instantFromDateString(String inputDate) {
		if (inputDate == null || inputDate.isEmpty())
			return Instant.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

		return inputDate != null ? Instant.from(formatter.parse(inputDate)) : Instant.now();
	}

	public static String dateFromInstant(Instant inputDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault());
		return inputDate != null ? formatter.format(inputDate) : "";
	}

}
