package speech.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import static speech.util.PrintUtil.print;


public class JsonObject {
	
	//We need to use a linked hash map to store the key-value pairs in order, o(1) lookup time, xors the object hashcode in the hashing function when inserting a key-value pair.
	private final LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<>();
	private static final char c = '"';
	
	//converts a string to a jsonObject.
	public static JsonObject of(String jsonObjectText) {
		jsonObjectText = jsonObjectText.substring(1, jsonObjectText.length() - 1);
		JsonObject object = new JsonObject();
		char[] characters = jsonObjectText.toCharArray();
		boolean hasNestedObject = false;
		String jsonText = "";
		int index = 0;
		for(char character: characters) {
			jsonText += character;
			boolean reachedEnd = index++ == characters.length - 1;
			if(jsonText.endsWith("," + '"') || reachedEnd) {
				String jsonObject = jsonText.substring(0, jsonText.length() - (reachedEnd ? 0 : 2));
				String[] elementPair = jsonObject.split(":");
				String key = elementPair[0].substring(1, elementPair[0].length() - 1);
				Object value = getValue(elementPair[1].replaceAll("" + '"', ""));
				if(jsonObject.endsWith("}")) {
					object.addProperty(key, of(jsonObject.substring(key.length() + 3, jsonObject.length())));
					hasNestedObject = false;
					jsonText = "" + '"';
					continue;
				}
				if(value instanceof String str && str.startsWith("{")) {
					hasNestedObject = true;
				}
				if(hasNestedObject)
					continue;
				object.addProperty(key, value);
				jsonText = "" + '"';
			}
		}
		return object;
	}
	
	private static Object getValue(String value) {
		try {
			return Integer.parseInt(value);
		} catch(NumberFormatException e) {
			return value;
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
	
	//Java Generics On top
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
		//o(n)
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
