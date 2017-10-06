package com.enablix.commons.util.date;

import static java.time.temporal.TemporalAdjusters.previousOrSame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
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

	public static final String JSON_INPUT_DATE_FORMAT = "dd-MMM-yy";
	
	private static final String ISO8601_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	private static final ThreadLocal<SimpleDateFormat> ISO8601DateTimeFormat = 
		new ThreadLocal<SimpleDateFormat>() {
			protected SimpleDateFormat initialValue() {
				SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATETIME_FORMAT);
				df.setTimeZone(TimeZone.getTimeZone("UTC"));
				return df;
			}
		};
	
	private static final ThreadLocal<SimpleDateFormat> JsonInputDateFormat = 
		new ThreadLocal<SimpleDateFormat>() {
			protected SimpleDateFormat initialValue() {
				SimpleDateFormat df = new SimpleDateFormat(JSON_INPUT_DATE_FORMAT);
				df.setTimeZone(TimeZone.getTimeZone("UTC"));
				return df;
			}
		};
		
	
	public static Date checkAndParseJsonDate(Object date) throws ParseException {
		
		if (date != null) {
		
			if (date instanceof Date) {
				return (Date) date;
			} else if (date instanceof String) {
				return JsonInputDateFormat.get().parse((String) date);
			}
		}
		
		return null;
	}
		
	public static Date parseJsonInputDate(String date) throws ParseException {
		return date == null ? null : JsonInputDateFormat.get().parse(date);
	}
	
	/**
     * @param month Month number
     * @return The short month name
     */
    public static String getMonthNameShort(int month) {
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
    }
    
    /**
     * @param month Month number
     * @return The short month name
     */
    public static String getMonthNameShort(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
    }
	
    /**
     * @param month Month number
     * @return The short month name
     */
    public static String getMonthName(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
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
	
	public static DateDimension getDateDimension(){
		Date date = Calendar.getInstance().getTime();
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

	public static Date getNextDate(Date date){
		LocalDateTime previousDate = dateToLocalDateTime(date).plusDays(1);
		return getStartOfDay(localDateTimeToDate(previousDate));
		
	}
	
	public static int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}
	
	public static Date shiftToFirstDayOfWeek(Date date){
		LocalDateTime first = dateToLocalDateTime(date).with(previousOrSame(DayOfWeek.SUNDAY)); 
		return localDateTimeToDate(first);
	}

	public static Date shiftToFirstDayOfMonth(Date date){
		LocalDateTime monthBegin = dateToLocalDateTime(date).withDayOfMonth(1);
		return localDateTimeToDate(monthBegin);
	}
	
	public static Date getNextWeekDate(Date date){

		LocalDateTime previousDate = dateToLocalDateTime(date).plusWeeks(1);
		return getStartOfDay(localDateTimeToDate(previousDate));
		
	}
	
	public static Date getNextMonthDate(Date date){
		LocalDateTime previousDate = dateToLocalDateTime(date).plusMonths(1);
		return getStartOfDay(localDateTimeToDate(previousDate));
		
	}
	
	public static Date getNextDate(){
		LocalDateTime previousDate = LocalDateTime.now().plusDays(1);
		return getStartOfDay(localDateTimeToDate(previousDate));
		
	}
	
	public static Date getPreviousDate(){
		LocalDateTime previousDate = LocalDateTime.now().minusDays(1);
		return getEndOfDay(localDateTimeToDate(previousDate));
		
	}
	public static Date getPreviousDate(Date date){
		LocalDateTime previousDate = dateToLocalDateTime(date).minusDays(1);
		return getStartOfDay(localDateTimeToDate(previousDate));
	}

	public static Period getPreviousMonth() {
		return getPreviousMonth(Calendar.getInstance().getTime());
	}
	
	public static Period getPreviousMonth(Date date) {
		
		LocalDateTime inDate = dateToLocalDateTime(date);
		
		LocalDateTime monthStartDate = inDate.minusMonths(1).withDayOfMonth(1).with(LocalTime.MIN);
		LocalDateTime monthEndDate = inDate.withDayOfMonth(1).minusDays(1).with(LocalTime.MAX);
		
		return new Period(localDateTimeToDate(monthStartDate), localDateTimeToDate(monthEndDate));
	}
	
	private static Date localDateTimeToDate(LocalDateTime startOfDay) {
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime dateToLocalDateTime(Date date) {
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

	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	
}