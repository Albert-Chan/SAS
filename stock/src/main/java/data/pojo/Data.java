package data.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Data<T> {
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
	public Stream<List<T>> movingSplit(int window, int step) {
		if (data.size() < window) {
			return ((Data<List<T>>) EMPTY).getStream();
		}

		List<List<T>> list = new ArrayList<>();
		for (int i = window - 1; i < data.size(); i++) {
			List<T> windowData = new ArrayList<>();
			for (int j = i - (window - 1); j <= i; j++) {
				windowData.add(data.get(j));
			}
			list.add(windowData);
		}
		return new Data<>(list).getStream();
	}

	// public <U> Data<U> collapsedMap(int window, int step, Function<List<T>,
	// U> f) {
	// List<U> result = new ArrayList<U>();
	// for (List<T> l : movingSplit(window, step).getData()) {
	// result.add(f.apply(l));
	// }
	// return new Data<>(result);
	// }

}
