package net.oasisgames.ban.time;

import java.time.Duration;

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
	                    minutes = number;
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
	                case 'o':
	                    if (i + 1 < input.length() && input.charAt(i + 1) == ' ') {
	                        months = number;
	                    }
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
	
}
