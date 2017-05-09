package com.enablix.commons.util.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	
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
	
}