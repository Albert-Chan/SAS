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
	public Data<CompositedDatum<T>> movingSplit(int window, int step) {
		if (data.size() < window) {
			return EMPTY;
		}

		List<CompositedDatum<T>> list = new ArrayList<>();
		for (int i = window - 1; i < data.size(); i++) {
			//List<T> windowData = new ArrayList<>();
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
		for (CompositedDatum<T> l : movingSplit(window, step).getData()) {
			result.add(f.apply(l));
		}
		return new Data<>(result);
	}

}
