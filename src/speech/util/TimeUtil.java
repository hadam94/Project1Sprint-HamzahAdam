package speech.util;

import java.time.LocalDateTime;

public class TimeUtil {
	
	/**
	 * Gets the current system time in a 12-hour time format.
	 * 
	 * @return The current time in a String Format.
	 */
	public static String getCurrentTime() {
		LocalDateTime date = LocalDateTime.now();
		StringBuilder time = new StringBuilder();
		time.append(date.getYear() + "-");
		String month = date.getMonth().getValue() < 10 ? "0" + date.getMonth().getValue() : "" + date.getMonth().getValue();
		time.append(month + "-");
		String day = date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : "" + date.getDayOfMonth();
		time.append(day + " ");
		int hour = date.getHour();
		//convert from 24 hour to 12 hour time.
		if(hour == 0) {
			hour = 12;
		} else if(hour > 12) {
			hour -= 12;
		}
		time.append(hour + ":");
		String minute = date.getMinute() < 10 ? "0" + date.getMinute() : "" + date.getMinute();
		time.append(minute + " ");
		time.append(date.getHour() < 12 ? "AM" : "PM");
	    return time.toString();
	}
}
