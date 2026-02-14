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

import speech.util.JsonObject;

public class RoomBooking {
	
	private static final String SERVER_URL = "http://198.74.62.248:4567";
	//grabbed it from inspect element
	private static final String AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36";
	
	public static JsonObject login(String email, String password) {
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
			return new JsonObject().addProperty("token", JsonObject.of(response.substring(9)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return new JsonObject().addProperty("error", e.getMessage());
		}
	}
	
	public static List<JsonObject> listAvalibleMeetingRooms(JsonObject token) {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/available/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.setDoOutput(true);
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Authorization", " Bearer " + ((JsonObject) token.getProperty("token")).getProperty("access"));
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
			response = response.substring(1, response.length() - 1);
			List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
			for(String room: response.split("},")) {
				jsonObjects.add(JsonObject.of(room + "}"));
			}
			return jsonObjects;
		} catch(IOException e) {
			e.printStackTrace();
			return List.of();
		}
	}
	
	public static JsonObject bookMeetingRoom(JsonObject loginToken, String startTime, String endTime, int people) {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/1/book/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", " Bearer " + ((JsonObject) loginToken.getProperty("token")).getProperty("access"));
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
	
	public static String listMyBookings(JsonObject loginToken) {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/my-bookings/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.setDoInput(true);
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Authorization", " Bearer " + ((JsonObject) loginToken.getProperty("token")).getProperty("access"));
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
			return response;
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void cancelMeetingRoom(int bookingId, JsonObject loginToken) {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/" + bookingId + "/cancel-booking/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.setConnectTimeout(3000);
			connection.setDoInput(true);
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Authorization", " Bearer " + ((JsonObject) loginToken.getProperty("token")).getProperty("access"));
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
		} catch(IOException e) {
			e.printStackTrace();
		}
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
