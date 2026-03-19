
import org.junit.Test;

import speech.booking.RoomBookingRequests;

import static org.junit.Assert.assertEquals;
import static speech.util.PrintUtil.print;;

public class SQLServerTest {
	
	/**
	 * Logs into the server, and attempts to add a room. This test will pass if the meeting room was successfully added to the server.
	 */
	@Test
	public void addRoom() {
		RoomBookingRequests bookingSession = new RoomBookingRequests();
		boolean sucess = bookingSession.login("hadam@student.bridgew.edu", "cs490");
		if(!sucess) {
			print("Login session failed! exiting");
			return;
		}
		boolean added = bookingSession.addMeetingRoom("success!", 9000);
		assertEquals(true, added);
	}
	
	/**
	 * Logs into the server, and attempts to remove a room. if room id doesn't exist in database, this test will fail.
	 */
	@Test
	public void removeRoom() {
		RoomBookingRequests bookingSession = new RoomBookingRequests();
		boolean sucess = bookingSession.login("hadam@student.bridgew.edu", "cs490");
		if(!sucess) {
			print("Login session failed! exiting");
			return;
		}
		boolean added = bookingSession.removeMeetingRoom(22);
		assertEquals(true, added);
	}
	
	/**
	 * Logs into the server, and attempts to change a rooms capacity. if room id doesn't exist in database, this test will fail.
	 */
	@Test
	public void changeRoom() {
		RoomBookingRequests bookingSession = new RoomBookingRequests();
		boolean sucess = bookingSession.login("hadam@student.bridgew.edu", "cs490");
		if(!sucess) {
			print("Login session failed! exiting");
			return;
		}
		boolean added = bookingSession.changeRoomCapacity(24, 744);
		assertEquals(true, added);
	}
}
