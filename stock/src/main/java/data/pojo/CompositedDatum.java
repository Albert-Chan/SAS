package data.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CompositedDatum<T extends Datum> extends Datum implements Iterable<T> {
	private ArrayList<T> underlay = new ArrayList<>();

	public CompositedDatum() {
	}

	public CompositedDatum(HashMap<String, Object> properties) {
		super(properties);
	}

	public CompositedDatum(CompositedDatum<T> datum, String[] props) {
		super(datum, props);
	}

	public void add(T d) {
		underlay.add(d);
	}

	public T get(int index) {
		return underlay.get(index);
	}

	public Iterator<T> iterator() {
		return underlay.iterator();
	}
}
