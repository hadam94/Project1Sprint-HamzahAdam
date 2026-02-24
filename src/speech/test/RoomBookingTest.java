package speech.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import speech.booking.RoomBookingRequests;
import speech.util.JsonObject;


//Sprint 2
//Hamzah Adam
public class RoomBookingTest {
	
	/**
	 * Logs in, gets jwt token
	 */
	@Test
	public void loginWithCredentials() {
		JsonObject token = RoomBookingRequests.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		boolean hasAccessToken = false;
		if(token.hasProperty("token") && ((JsonObject) token.getProperty("token")).hasProperty("access")) {
			String accessToken = ((JsonObject) token.getProperty("token")).getProperty("access");
			print(accessToken);
			hasAccessToken = true;
		}
		assertEquals("Response from the server failed.", true, hasAccessToken);
	}
	
	/**
	 * I assume the correct room is DMF 363... since that is where we meet.
	 */
	@Test
	public void retrieveRooms() {
		JsonObject token = RoomBookingRequests.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		List<JsonObject> rooms = RoomBookingRequests.listAvalibleMeetingRooms(token);
		boolean hasDmf = false;
		for(JsonObject room: rooms) {
			if(room.getProperty("room_name").equals("DMF 363")) {
				print("DMF 363");
				hasDmf = true;
				break;
			}
		}
		assertEquals("That room is not DMF 363 Or there is no rooms.", true, hasDmf);
	}
	
	/**
	 * For this function, you want to change the time after each run so it works as intended, since it will be stored in the backend of the server
	 */
	@Test
	public void makeReservation() {
		JsonObject token = RoomBookingRequests.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		//Booking room for 15 minutes
		JsonObject firstResponse = RoomBookingRequests.bookMeetingRoom(token, "2026-02-09 6:00 AM", "2026-02-09 6:15 AM", 1);
		print(firstResponse);
		assertEquals("Room Not created.", "Meeting room booked successfully.", firstResponse.getProperty("message"));
		JsonObject secondResponse = RoomBookingRequests.bookMeetingRoom(token, "2026-02-09 6:00 AM", "2026-02-09 6:15 AM", 1);
		print(secondResponse);
		assertEquals("Room creation did not fail on 2nd try.", true, secondResponse.hasProperty("error"));
	}
	
	
	private static void print(Object args) {
		System.out.println(args);
	}
}
