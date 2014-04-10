package com.parcelinc.parcelapp.db;

import java.util.Calendar;

public class DateUtil {

	public static String formatNumber(int number) {
		return (number < 10) ? ("0" + number) : ("" + number);
	}

	public static String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		return getDate(c);
	}

	public static String getDate(Calendar c) {
		return formatNumber(c.get(Calendar.YEAR)) + "-"
				+ formatNumber(c.get(Calendar.MONTH) + 1) + "-"
				+ formatNumber(c.get(Calendar.DAY_OF_MONTH));
	}

}
