package data.pojo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class Datum {
	protected HashMap<String, Object> properties = new HashMap<>();

	public Datum() {
	}

	public Datum(Datum datum, String[] props) {
		for (String prop : props) {
			build(prop, datum.get(prop));
		}
	}

	public static Datum merge(List<? extends Datum> datumList, String[] props) throws RuntimeException {
		if (datumList.size() == 0) {
			throw new RuntimeException("empty datum list.");
		}

		Datum newDatum = new Datum();
		for (String prop : props) {
			newDatum.build(prop, datumList.get(0).get(prop));
		}
		boolean allMatch = datumList.stream().allMatch(d -> {
			for (String prop : props) {
				if (!d.get(prop).equals(newDatum.get(prop))) {
					return false;
				}
			}
			return true;
		});
		if (!allMatch) {
			throw new RuntimeException("datumList do not all match.");
		}

		HashSet<String> ignoredProperties = new HashSet<>(Arrays.asList(props));

		for (Datum d : datumList) {
			for (Entry<String, Object> e : d.getAllProperties().entrySet()) {
				if (!ignoredProperties.contains(e.getKey())) {
					newDatum.set(e.getKey(), e.getValue());
				}
			}
		}

		return newDatum;
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

	public HashMap<String, Object> getAllProperties() {
		return properties;
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