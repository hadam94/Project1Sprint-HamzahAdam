package speech.booking;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class JsonObject {
	
	//We need to use a linked hash map to store the key-value pairs in order.
	private final Map<String, Object> jsonObject = new LinkedHashMap<>();
	private static final char c = '"';
	
	public void addProperty(String key, Object value) {
		if(value instanceof String) {
			jsonObject.put(key, c + "" + value + c);
		} else {
			jsonObject.put(key, value);
		}
	}
	
	public void removeProperty(String key) {
		jsonObject.remove(key);
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
			String seperator = i == size - 1 ? "" : ", ";
			builder.append(c + entry.getKey() + c + ": " + entry.getValue() + seperator);
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
