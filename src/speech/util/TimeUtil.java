package speech.util;

import java.time.LocalDateTime;

public class TimeUtil {
	
	/**
	 * Gets the current system time in a 12-hour time format.
	 * 
	 * @return The current time in a JSON Object Format.
	 */
	public static JsonObject getCurrentTime() {
		JsonObject timeObject = new JsonObject();
		LocalDateTime date = LocalDateTime.now();
		timeObject.addProperty("year", date.getYear());
		timeObject.addProperty("month", date.getMonth().getValue());
		timeObject.addProperty("day", date.getDayOfMonth());
		int hour = date.getHour();
		//convert from 24 hour to 12 hour time.
		if(hour == 0) {
			hour = 12;
		} else if(hour > 12) {
			hour -= 12;
		}
		timeObject.addProperty("hour", hour);
		timeObject.addProperty("minute", date.getMinute());
		timeObject.addProperty("second", date.getSecond());
		timeObject.addProperty("am/pm", date.getHour() < 12 ? "AM" : "PM");
	    return timeObject;
	}
}
