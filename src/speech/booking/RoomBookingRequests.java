package speech.booking;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import speech.gui.RoomBookingWindow;
import speech.util.JsonObject;

import static org.junit.Assume.assumeThat;
import static speech.util.PrintUtil.print;

public class RoomBookingRequests {
		
//	private static final String SERVER_URL = "http://198.74.62.248:4567";
	private static final String SERVER_URL = "http://45.55.230.108:8000";
	//grabbed it from inspect element
	private static final String AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36";
	
	private JsonObject loginToken;
	/**
	 * Basically login sessions from admin server. you need to log into the server at least once through http://45.55.230.108:8000/admin/, 
	 * and use hadam@student.bridgew.edu as the email and cs490 as the password. then grab these sessions from inspect element, the sessions 
	 * will be good for a couple of hours. Due to time constraints, a proper sql system was not able to be done.
	 */
	private String cookieSession = "csrftoken=hvCtdHn6efx84EdSBuhoDlopFTg7Fhir; sessionid=kogmjdq35g6z2r803k0vj904uoyr9xwa";
	private String csrfmiddlewaretoken = "6xiAe0wslAK5nVTGgGUZzNH6htWdDRmodSKThxJopF73hpWoH01d2YVlMc2a8YuF";
	
	public boolean login(String email, String password) {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/member/login/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.setConnectTimeout(3000);
			JsonObject json = new JsonObject();
			json.addProperty("email", email);
			json.addProperty("password", password);
			connection.getOutputStream().write(json.getBytes());
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
			loginToken = JsonObject.of(response);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	//A way to automate the cookieSession and csrfmiddlewaretoken. Was planning on that but couldn't do that.
	public void adminLogin(String email, String password) {
		
	}
	
	public List<JsonObject> listAvalibleMeetingRooms() {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/available/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.setDoOutput(true);
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Authorization", " Bearer " + loginToken.<JsonObject>getProperty("token").getProperty("access"));
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
			response = response.substring(1, response.length() - 1);
			List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
			for(String room: response.split("},")) {
				String roomJson = room.endsWith("}") ? room :room + "}";
				jsonObjects.add(JsonObject.of(roomJson));
			}
			return jsonObjects;
		} catch(IOException e) {
			e.printStackTrace();
			return List.of();
		}
	}
	
	public JsonObject bookMeetingRoom(int bookingId, String startTime, String endTime, int people) {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/" + bookingId + "/book/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", " Bearer " + loginToken.<JsonObject>getProperty("token").getProperty("access"));
			connection.setDoOutput(true);
			connection.setConnectTimeout(3000);
			JsonObject json = new JsonObject();
			json.addProperty("start_time", startTime);
			json.addProperty("end_time", endTime);
			json.addProperty("no_of_persons", people);
			connection.getOutputStream().write(json.getBytes());
			connection.connect();
			return JsonObject.of(getResponseFromServer(connection.getInputStream()));
		} catch(IOException e) {
			e.printStackTrace();
			return new JsonObject().addProperty("error", e.getMessage());
		}
	}
	
	public List<JsonObject> listMyBookings() {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/my-bookings/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.setDoInput(true);
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Authorization", " Bearer " + loginToken.<JsonObject>getProperty("token").getProperty("access"));
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
			response = response.substring(1, response.length() - 1);
			List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
			String objectText = "";
			int i = 0;
			char[] array = response.toCharArray();
			for(char character: array) {
				objectText += character;
				if(objectText.endsWith("},{") || i == array.length - 1) {
					jsonObjects.add(JsonObject.of(objectText.substring(0, objectText.length() - 2)));
					objectText = "" + character;
				}
				i++;
			}
			return jsonObjects;
		} catch(IOException e) {
			e.printStackTrace();
			return List.of();
		}
	}
	
	public String cancelMeetingRoom(int bookingId) {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/" + bookingId + "/cancel-booking/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.setConnectTimeout(3000);
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", " Bearer " + loginToken.<JsonObject>getProperty("token").getProperty("access"));
			connection.connect();
			return getResponseFromServer(connection.getInputStream());
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean addMeetingRoom(String name, int capacity) {
		try {
			URL url = URI.create(SERVER_URL + "/admin/booking/meetingroom/add/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(3000);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
			connection.setRequestProperty("Cookie", cookieSession);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setDoOutput(true);
			connection.getOutputStream().write(("csrfmiddlewaretoken=" + csrfmiddlewaretoken + "&room_name=" + name + "&capacity=" + capacity + "&is_active=on&_save=Save").getBytes());
			connection.connect();
			getResponseFromServer(connection.getInputStream());
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean removeMeetingRoom(int meetingRoomId) {
		try {
			URL url = URI.create(SERVER_URL + "/admin/booking/meetingroom/" + meetingRoomId + "/delete/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(3000);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
			connection.setRequestProperty("Cookie", cookieSession);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setDoOutput(true);
			connection.getOutputStream().write(("csrfmiddlewaretoken=" + csrfmiddlewaretoken + "&post=yes").getBytes());
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean changeRoomCapacity(int meetingRoomId, int newCapacity) {
		try {
			URL url = URI.create(SERVER_URL + "/admin/booking/meetingroom/" + meetingRoomId + "/change/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(3000);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
			connection.setRequestProperty("Cookie", cookieSession);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setDoOutput(true);
			String roomName = null;
			for(String element: RoomBookingWindow.getInstance().getRoomsList().getItems()) {
				JsonObject object = JsonObject.of(element);
				if(object.getProperty("id").equals(meetingRoomId)) {
					roomName = object.getProperty("room_name");
					break;
				}
			}
			if(roomName == null)
				return false;
			connection.getOutputStream().write(("csrfmiddlewaretoken=" + csrfmiddlewaretoken + "&room_name=" + roomName + "&capacity=" + newCapacity + "&is_active=on&_save=Save").getBytes());
			connection.connect();
			getResponseFromServer(connection.getInputStream());
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public JsonObject getLoginToken() {
		return loginToken;
	}
	
	private static String getResponseFromServer(InputStream stream) throws IOException {
		String response = "";
		while(true) {
			int character = stream.read();
			if(character == -1) {
				break;
			}
			response += (char)character;
		}
		stream.close();
		return response;
	}
}
