package net.oasisgames.ban.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Conversion {

	public static Duration convertStringToTime(String input) {
	    int months = 0, weeks = 0, days = 0, hours = 0, minutes = 0, seconds = 0;
	    String currentNumber = "";
	    for (int i = 0; i < input.length(); i++) {
	        char currentChar = input.charAt(i);
	        if (Character.isDigit(currentChar)) {
	            currentNumber += currentChar;
	        } else {
	            int number = Integer.parseInt(currentNumber);
	            switch (currentChar) {
	                case 'h':
	                    hours = number;
	                    break;
	                case 'm':
	                	if (i + 1 < input.length() && input.charAt(i + 1) == 'o') {
	                        months = number;
	                    } else {
	                    	minutes = number;
	                    }
	                    break;
	                case 's':
	                    seconds = number;
	                    break;
	                case 'd':
	                    days = number;
	                    break;
	                case 'w':
	                    weeks = number;
	                    break;
	                default:
	                    break;
	            }
	            currentNumber = "";
	        }
	    }
	    return ofMonths(months).plusDays(weeks * 7 + days).plusHours(hours)
	            .plusMinutes(minutes).plusSeconds(seconds);
	}
	
	private static Duration ofMonths(long months) {
	    return Duration.ofDays(months * 30);
	}
	
	public static long convertToSeconds(Duration duration) {
	    return duration.getSeconds();
	}
	
	public static String durationToString(Duration duration) {
	    long seconds = duration.getSeconds();

	    long absSeconds = Math.abs(seconds);
	    int months = (int) (absSeconds / 2629746);
	    int days = (int) ((absSeconds % 2629746) / 86400);
	    int hours = (int) ((absSeconds % 86400) / 3600);
	    int minutes = (int) ((absSeconds % 3600) / 60);
	    int secs = (int) (absSeconds % 60);

	    StringBuilder sb = new StringBuilder();
	    if (seconds < 0) {
	        sb.append("-");
	    }
	    if (months > 0) {
	        sb.append(months);
	        sb.append(months == 1 ? " month " : " months ");
	    }
	    if (days > 0) {
	        sb.append(days);
	        sb.append(days == 1 ? " day " : " days ");
	    }
	    if (hours > 0) {
	        sb.append(hours);
	        sb.append(hours == 1 ? " hour " : " hours ");
	    }
	    if (minutes > 0) {
	        sb.append(minutes);
	        sb.append(minutes == 1 ? " minute " : " minutes ");
	    }
	    if (secs > 0) {
	        sb.append(secs);
	        sb.append(secs == 1 ? " second " : " seconds ");
	    }

	    return sb.toString().trim();
	}
	
	public static boolean isBeforeDuration(String dateStr, String durationStr) {
	    LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate targetDate = LocalDate.parse(dateStr, formatter).plus(Duration.parse(durationStr));
	    return currentDate.isBefore(targetDate);
	}
	
	public static String getCurrentDateTimeAsString() {
	    LocalDateTime currentDateTime = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    return currentDateTime.format(formatter);
	}
	
	public static Date parseDateTimeString(String dateTimeString) throws ParseException {
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return formatter.parse(dateTimeString);
	}
	
	public static boolean isBeforeDuration(Date date, Duration duration) {
	    LocalDate targetDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plus(duration);
	    LocalDate currentDate = LocalDate.now();
	    return currentDate.isBefore(targetDate);
	}
	
}
