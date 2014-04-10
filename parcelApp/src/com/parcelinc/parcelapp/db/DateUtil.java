package com.parcelinc.parcelapp.db;

import java.util.Calendar;

public class DateUtil {

	public static final String SEPARATOR = "-";

	public static String formatNumber(int number) {
		return (number < 10) ? ("0" + number) : ("" + number);
	}

	public static String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		return getDate(c);
	}

	public static String getDate(Calendar c) {
		return formatNumber(c.get(Calendar.YEAR)) + SEPARATOR + getMonth(c)
				+ SEPARATOR + formatNumber(c.get(Calendar.DAY_OF_MONTH));
	}

	public static String getFilterMonth(String year, int month) {
		return year + SEPARATOR + getMonth(month);
	}

	private static String getMonth(Calendar c) {
		return getMonth(c.get(Calendar.MONTH));
	}

	private static String getMonth(int month) {
		return formatNumber(month + 1);
	}

}
