package data.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Data<T extends Datum> {
	@SuppressWarnings("rawtypes")
	public static final Data EMPTY = new Data();

	protected List<T> data;

	private Data() {
		this(Collections.emptyList());
	}

	public Data(List<T> data) {
		this.data = data;
	}

	public List<T> getData() {
		return data;
	}

	public Stream<T> getStream() {
		return data.stream();
	}

	@SuppressWarnings("unchecked")
	private Data<CompositedDatum<T>> movingSplit(int window, int step) {
		if (data.size() < window) {
			return EMPTY;
		}

		List<CompositedDatum<T>> list = new ArrayList<>();
		for (int i = window - 1; i < data.size(); i += step) {
			// List<T> windowData = new ArrayList<>();
			CompositedDatum<T> windowDatum = new CompositedDatum<>();
			for (int j = i - (window - 1); j <= i; j++) {
				windowDatum.add(data.get(j));
			}
			list.add(windowDatum);
		}
		return new Data<>(list);
	}

	public <U extends Datum> Data<U> collapsedMap(int window, int step, Function<CompositedDatum<T>, U> f) {
		List<U> result = new ArrayList<U>();
		for (CompositedDatum<T> composited : movingSplit(window, step).getData()) {
			result.add(f.apply(composited));
		}
		return new Data<>(result);
	}
	
	public Data<T> minus(String minuend, String subtrahend, String resultName) {
		data.forEach(d -> {
			d.set(resultName, (double) d.get(minuend) - (double) d.get(subtrahend));
		});
		return this;
	}
	
	

	public String toString() {
		return this.data.toString();
	}

}
