package speech;

import speech.util.JsonObject;

public class BookingDemo {
	
	public static void main(String[] args) {
		char c = '"';
		String e = String.valueOf("{" + c + "name" + c + ":" + c + "Hamzah" + c + "," + c + "major" + c + ":" + c + "cs" + c + "," + c + "age" + c + ":" + 33 + "}");
		JsonObject object = new JsonObject();
		object.addProperty("hello", "world");
		
	}
}
