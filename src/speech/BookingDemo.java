package speech;

import speech.booking.RoomBookingRequests;
import speech.util.JsonObject;
import static speech.util.PrintUtil.print;

//Hamzah Adam Sprint 2
public class BookingDemo {
	
	public static void main(String[] args) {
		RoomBookingRequests booking = new RoomBookingRequests();
		boolean loggedIn = booking.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		for(JsonObject object: booking.listMyBookings()) {
			print(object);
		}
		
	}
}
