package data.pojo;

import java.util.HashMap;

public class Datum {
	private HashMap<String, Object> properties = new HashMap<>();

	public Datum() {
	}

	public Datum(Datum datum, String[] props) {
		Datum newDatum = new Datum();
		for (String prop : props) {
			newDatum.build(prop, datum.get(prop));
		}
	}

	public void set(String name, Object value) {
		properties.put(name, value);
	}

	public Datum build(String name, Object value) {
		set(name, value);
		return this;
	}

	public Object get(String name) {
		return properties.get(name);
	}

	public String toString() {
		return properties.toString();
	}
}

/*
 * private Table<String, String, String> properties = HashBasedTable.create();
 * private static final String COLUMN_VALUE_TYPE = "VALUE_TYPE"; private static
 * final String COLUMN_VALUE = "VALUE";
 * 
 * public void set(String name, String valueType, Object value) {
 * properties.put(name, COLUMN_VALUE_TYPE, valueType); properties.put(name,
 * COLUMN_VALUE, value.toString()); }
 * 
 * public void set(String name, Object value) { properties.put(name,
 * COLUMN_VALUE, value.toString()); }
 * 
 * public Datum build(String name, String valueType, Object value) { set(name,
 * valueType, value); return this; }
 * 
 * public Object get(String name) { Object value = properties.get(name,
 * COLUMN_VALUE); String valueType = (String)properties.get(name,
 * COLUMN_VALUE_TYPE); //valueTpe to handle method.
 * 
 * } }
 */