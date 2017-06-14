package data.pojo;

import java.util.HashMap;

public class DatumBuilder {
	protected HashMap<String, Object> properties = new HashMap<>();

	public DatumBuilder build(String name, Object value) {
		properties.put(name, value);
		return this;
	}

	public Object get(String name) {
		return properties.get(name);
	}

	public HashMap<String, Object> getAllProperties() {
		return properties;
	}

	public String toString() {
		return properties.toString();
	}
	
	public <T extends Datum> T createDatum() {
		return (T)new Datum(properties);
	}
}
