package speech.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtil {
	
	private static final String SERVER_URL = "http://198.74.62.248:4567";
	
	public static void login(String username, String password) {
		try {
			URL url = URI.create(SERVER_URL).toURL();
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			//headers below
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
