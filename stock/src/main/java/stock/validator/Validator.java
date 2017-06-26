package stock.validator;

import java.util.ArrayList;
import java.util.function.Function;

import org.joda.time.DateTime;

import data.pojo.CompositedDatum;
import data.pojo.Data;
import data.pojo.StockDatum;

public class Validator {

	Data<StockDatum> data;

	private Iterable<ActionPoint> filter(Function<CompositedDatum<StockDatum>, Boolean> f) {
		ArrayList<ActionPoint> list = new ArrayList<>();
		data.movingSplit(1, 1).getData().forEach(compoisted -> {
			if (f.apply(compoisted)) {
				list.add(new ActionPoint());
			}
		});
		return list;
	}

	public Iterable<ActionPoint> buyPoint(Function<CompositedDatum<StockDatum>, Boolean> f) {
		return filter(f);
	}

	public Iterable<ActionPoint> sellPoint(Function<CompositedDatum<StockDatum>, Boolean> f) {
		return filter(f);
	}

	public EstimateResult estimate() {
		return null;
	}

}

class ActionPoint {
	DateTime t;
	String stockId;
	float price;
	int vol;
}

class EstimateResult {

}
