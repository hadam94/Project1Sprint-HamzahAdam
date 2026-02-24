package speech;

import speech.booking.RoomBookingRequests;
import speech.util.JsonObject;

//Hamzah Adam Sprint 2
public class BookingDemo {
	
	public static void main(String[] args) {
		char c = '"';
		
		String e = String.valueOf("{" + c + "name" + c + ":" + c + "Hamzah" + c + "," + c + "major" + c + ":" + c + "cs" + c + "," + c + "age" + c + ":" + 33 + "}");
		JsonObject object =	JsonObject.of(e);
		System.out.println(object);
		
		JsonObject token = RoomBookingRequests.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		print(token);
		print(RoomBookingRequests.listAvalibleMeetingRooms(token));
		print(RoomBookingRequests.bookMeetingRoom(token, "2026-02-03 5:00 AM", "2026-02-03 9:38 AM", 1));
		print(RoomBookingRequests.listMyBookings(token));
		print(RoomBookingRequests.cancelMeetingRoom(token, 167));
		
	}
	
	private static void print(Object args) {
		System.out.println(args);
	}
}
