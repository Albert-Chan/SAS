package data.pojo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class DatumBuilder {
	protected HashMap<String, Object> properties = new HashMap<>();

	public DatumBuilder() {
	}

	public static DatumBuilder from(DatumBuilder datum, String[] props) {
		DatumBuilder newDatum = new DatumBuilder();
		
		for (String prop : props) {
			newDatum.build(prop, datum.get(prop));
		}
		return newDatum;
	}

	public static DatumBuilder merge(List<? extends DatumBuilder> datumList, String[] props) throws RuntimeException {
		if (datumList.size() == 0) {
			throw new RuntimeException("empty datum list.");
		}

		DatumBuilder newDatum = new DatumBuilder();
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

		for (DatumBuilder d : datumList) {
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

	public Object get(String name) {
		return properties.get(name);
	}

	public DatumBuilder build(String name, Object value) {
		set(name, value);
		return this;
	}
	
	public HashMap<String, Object> getAllProperties() {
		return properties;
	}

	public String toString() {
		return properties.toString();
	}
}
