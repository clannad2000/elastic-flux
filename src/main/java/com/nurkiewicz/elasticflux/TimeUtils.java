package com.nurkiewicz.elasticflux;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    public static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String getTimeFormat(DateTimeFormatter d) {
	return getTimeFormat(d, 0, ChronoUnit.DAYS);
    }

    public static String getTimeFormat(DateTimeFormatter d, int day, ChronoUnit chronoUnit) {
	if (d == null) {
	    // yyyyMMdd
	    d = DAY;
	}
	LocalDateTime localtime = LocalDateTime.now();
	if (day != 0) {
	    localtime.plus(day, chronoUnit);
	}
	return localtime.format(d);
    }

    public static String[] getNowAndYesterdayTimeFormat() {
	LocalDateTime localtime = LocalDateTime.now();
	String nowadays = localtime.format(DAY);
	LocalDateTime yesterdayDateTime  = localtime.plus(-1, ChronoUnit.DAYS);
	String yesterday = yesterdayDateTime.format(DAY);
	return new String[] { nowadays, yesterday };
    }

    public static Long[] getNowStartAndEndForDay() {
	LocalDate localDate = LocalDate.now();
	return getStartAndEndForDay(localDate);
    }

    public static Long[] getStartAndEndForDay(String date, DateTimeFormatter formatter) {
	LocalDate localDate = LocalDate.parse(date, formatter);
	return getStartAndEndForDay(localDate);
    }

    public static Long[] getStartAndEndForDay(LocalDate localDate) {
	LocalDateTime localDateTime = localDate.atStartOfDay();
	long statTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	long endTime = localDateTime.plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	Long[] result = new Long[] { statTime, endTime };
	return result;
    }

	public static ZonedDateTime getZeroTime(Long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
				.withHour(0)
				.withMinute(0)
				.withSecond(0)
				.withNano(0)
				.atZone(ZoneId.systemDefault());
	}

	/**
	 * @param endTime
	 * @return
	 */
	public static Long getHour(Long endTime) {
		return ((endTime - TimeUtils.getZeroTime(endTime).toInstant().toEpochMilli()) / 3600000);
	}


	public static void main(String[] args) {
	LocalDateTime localtime = LocalDateTime.now();
	@SuppressWarnings("unused")
    String timeFormat = getTimeFormat(null);
	System.out.println(getTimeFormat(DAY));
	Long[] startAndEndForDay = getStartAndEndForDay(getTimeFormat(DAY), DAY);
	System.out.println(startAndEndForDay[0] + "\t" + startAndEndForDay[1]);
	// LocalDateTime.of(2010,1,1,0,0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
	// localtime.getLong(ChronoField.DAY_OF_YEAR);
	System.out.println(localtime.getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
    }
}
