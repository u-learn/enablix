package com.enablix.commons.util.date;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	
	private static final String ISO8601_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	private static final ThreadLocal<SimpleDateFormat> ISO8601DateTimeFormat = 
		new ThreadLocal<SimpleDateFormat>() {
			protected SimpleDateFormat initialValue() {
				SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATETIME_FORMAT);
				df.setTimeZone(TimeZone.getTimeZone("UTC"));
				return df;
			}
		};
	
	public static Date getEndOfDay(Date date) {
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		return localDateTimeToDate(endOfDay);
	}

	public static Date getStartOfDay(Date date) {
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return localDateTimeToDate(startOfDay);
	}
	
	public static DateDimension getDateDimension(Date date){
		DateDimension dateDimension = new DateDimension();
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		dateDimension.setDayOfMonth(localDateTime.getDayOfMonth());
		dateDimension.setDayOfWeek(localDateTime.getDayOfWeek().getValue());
		dateDimension.setDayOfYear(localDateTime.getDayOfYear());
		dateDimension.setMonthOfYear(localDateTime.getMonth().getValue());
		
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
		int weekNumberYear = localDateTime.get(woy);
		dateDimension.setWeekOfYear(weekNumberYear);

		
		TemporalField wom = WeekFields.of(Locale.getDefault()).weekOfMonth(); 
		int weekNumberMonth = localDateTime.get(wom);
		dateDimension.setWeekOfMonth(weekNumberMonth);
		
		dateDimension.setYear(localDateTime.getYear());
		return dateDimension;
	}

	public static Date getPreviousDate(){
		LocalDateTime previousDate = LocalDateTime.now().minusDays(1);
		return getEndOfDay(localDateTimeToDate(previousDate));
		
	}
	private static Date localDateTimeToDate(LocalDateTime startOfDay) {
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}

	private static LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
	}
	
	public static String dateToUTCiso8601String(Date date) {
		SimpleDateFormat dateFormat = ISO8601DateTimeFormat.get();
		return dateFormat.format(date);
	}
	
	public static void main(String[] args) {
		Date today = Calendar.getInstance().getTime();
		System.out.println(dateToUTCiso8601String(today));
	}
	
}