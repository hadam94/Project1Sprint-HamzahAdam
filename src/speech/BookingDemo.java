package speech;

import speech.booking.RoomBooking;
import speech.util.JsonObject;

//Probably will be used for sprint 3
public class BookingDemo {
	
	public static void main(String[] args) {
		char c = '"';
		
		String e = String.valueOf("{" + c + "name" + c + ":" + c + "Hamzah" + c + "," + c + "major" + c + ":" + c + "cs" + c + "," + c + "age" + c + ":" + 33 + "}");
		JsonObject object =	JsonObject.of(e);
		System.out.println(object);
		
		JsonObject token = RoomBooking.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		RoomBooking.cancelMeetingRoom(1, token);
		
	}
}
