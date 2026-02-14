package speech.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class JsonObject {
	
	//We need to use a linked hash map to store the key-value pairs in order.
	private final LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<>();
	private static final char c = '"';
	
	//converts a string to a jsonObject.
	//Adding support for nested objects is a nightmare... may do it later
	public static JsonObject of(String jsonObjectText) {
		JsonObject object = new JsonObject();
		int objectIndex = 0;
		String jsonText = "";
		char[] array = jsonObjectText.toCharArray();
		for(char c: array) {
			if(c == ',' || c == '}') {
				String[] tokenSplit = jsonText.split(":");
				if(tokenSplit.length != 2)
					continue;
				String key = tokenSplit[0].substring(objectIndex == 0 ? 2 : 1, tokenSplit[0].length() - 1);
				Object value = getValue(tokenSplit[1]);
				
				object.addProperty(key, value);
				
				jsonText = "";
				objectIndex++;
			} else {
				jsonText += c;
			}
		}
		return object;
	}
	
	//assume the token is a string if it starts with a "
	private static Object getValue(String token) {
		if(token.startsWith(String.valueOf('"'))) {
			return token.substring(1, token.length() - 1);
		} else {
			return Integer.parseInt(token);
		}
	}
	
	public JsonObject addProperty(String key, Object value) {
		jsonObject.put(key, value);
		return this;
	}
	
	public JsonObject addProperty(String key, JsonObject... objects) {
		jsonObject.put(key, objects);
		return this;
	}
	
	public JsonObject removeProperty(String key) {
		jsonObject.remove(key);
		return this;
	}
	
	public <T> T getProperty(String key) {
		return (T) jsonObject.get(key);
	}
	
	public boolean hasProperty(String key) {
		return jsonObject.get(key) != null;
	}
	
	public byte[] getBytes() {
		return toString().getBytes();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		int i = 0;
		int size = jsonObject.size();
		for(Entry<String, Object> entry: jsonObject.entrySet()) {
			String seperator = i == size - 1 ? "" : ",";
			Object value =  entry.getValue();
			Object displayedValue = value instanceof String ? c + "" + value + c : value;
			builder.append(c + entry.getKey() + c + ":" + displayedValue + seperator);
			i++;
		}
		builder.append("}");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof JsonObject object && this.toString().equals(object.toString());
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(jsonObject);
	}
}
