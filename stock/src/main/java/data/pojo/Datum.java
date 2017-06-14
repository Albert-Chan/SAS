package data.pojo;

import java.util.HashMap;

public class Datum {
	
	protected HashMap<String, Object> properties = new HashMap<>();
	
	public Datum() {
	}

	public Datum(HashMap<String, Object> properties) {
		this.properties = properties;
	}

	public Datum(Datum datum, String[] props) {
		for (String prop : props) {
			properties.put(prop, datum.get(prop));
		}
	}
	
	public Datum build(String name, Object value) {
		set(name, value);
		return this;
	}

	public void set(String name, Object value) {
		properties.put(name, value);
	}

	public Object get(String name) {
		return properties.get(name);
	}

	public HashMap<String, Object> getAllProperties() {
		return properties;
	}

	public String toString() {
		return getAllProperties().toString();
	}
}
