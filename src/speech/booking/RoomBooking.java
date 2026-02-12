package speech.booking;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class RoomBooking {
	
	private static final String SERVER_URL = "http://198.74.62.248:4567";
	//grabbed it from inspect element
	private static final String AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36";
	
	public static void login(String email, String password) {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/member/login/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("OPTIONS");
			connection.setRequestProperty("User-Agent", AGENT);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.setConnectTimeout(3000);
			char c = '"';
			JsonObject json = new JsonObject();
			json.addProperty("email", email);
			json.addProperty("password", password);
			connection.getOutputStream().write(json.getBytes());
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void listAvalibleMeetingRooms() {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/available/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.setDoInput(true);
			connection.setRequestProperty("User-Agent", AGENT);
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void bookMeetingRoom() {
		
	}
	
	public static void listMyBookings() {
		try {
			URL url = URI.create(SERVER_URL + "/api/v1/meeting-rooms/my-bookings/").toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.setDoInput(true);
			connection.setRequestProperty("User-Agent", AGENT);
			connection.connect();
			String response = getResponseFromServer(connection.getInputStream());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void cancelMeetingRoom() {
		
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
