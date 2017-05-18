package data.pojo;

import java.util.List;
import java.util.stream.Stream;

public class StockData extends Data<Datum> {
	private static final String ID = "id";
	private static final String TIME = "time";
	private static final String[] KEY_PROPS = new String[] { ID, TIME };
	
	private static final String H = "high";
	private static final String L = "low";
	private static final String O = "opening";
	private static final String C = "closing";
	private static final String MA = "MA";
	private static final String EMA = "EMA";

	public StockData(List<Datum> data) {
		super(data);
	}

	public Stream<Datum> MA(String aggProperty, int avgPeriod) {
		return movingSplit(avgPeriod, 1).map(l -> {
			double sum = 0.0;
			Datum lastDatum = null;
			for (Datum datum : l) {
				sum += (double) datum.get(aggProperty);
				lastDatum = datum;
			}

			Datum newMA = new Datum(lastDatum, KEY_PROPS);
			String MA_X = MA + avgPeriod;
			newMA.set(MA_X, sum / l.size());
			return newMA;
		});
	}

	// DI=(C*2+H+L) / 4
	public Stream<Datum> DI() {
		return getStream().map(d -> {
			d.set("DI", (double) d.get(H) + (double) d.get(L) + (double) d.get(C) * 2);
			return d;
		});
	}

	public Stream<Datum> EMA(String di, int avgPeriod, String[] keyProps) {
		double smoothRating = 2d / (avgPeriod + 1);
		String EMA_X = "EMA" + avgPeriod;
		return movingSplit(2, 1).map(l -> {
			// calculates today's AvgIndex
			Datum previous = l.get(0);
			Datum today = l.get(1);

			double previousAvgIndex = (double) previous.get(EMA_X);
			double todayAvgIndex = smoothRating * ((double) today.get(di) - previousAvgIndex) + previousAvgIndex;

			Datum newEMA = new Datum(today, keyProps);
			newEMA.set(EMA_X, todayAvgIndex);
			return newEMA;
		});
	}

	// MACD：EMA(C,12)-EMA(C,26), color blue;
	// Signal：EMA(MACD,9), color red;
	// Histogram：MACD-Signal, color tick;
	public Stream<Datum> MACD(String di, int avgPeriod) {
		Stream<Datum> EMA12 = EMA("closing", 12);
		Stream<Datum> EMA26 = EMA("closing", 26);
		Stream.of(EMA12, EMA26).flatMap(mapper)

	}

	//
	// public Stream<? extends Datum> Signal(int avgPeriod) {
	//
	// }

}
