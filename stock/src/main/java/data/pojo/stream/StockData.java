package data.pojo.stream;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockData extends Data<StockDatum> {

	public StockData(List<StockDatum> data) {
		super(data);
	}

	public Stream<StockDatum> MA(String aggProperty, int avgPeriod) {
		return movingSplit(avgPeriod, 1).map(l -> {
			double sum = 0.0;
			StockDatum lastDatum = null;
			for (StockDatum datum : l) {
				sum += (double) datum.get(aggProperty);
				lastDatum = datum;
			}

			StockDatum newMA = new StockDatum(lastDatum, StockDatum.KEY_PROPS);
			String MA_X = StockDatum.MA + avgPeriod;
			newMA.set(MA_X, sum / l.size());
			return newMA;
		});
	}

	// DI=(C*2+H+L) / 4
	public Stream<StockDatum> DI() {
		return getStream().map(d -> {
			d.set("DI", (double) d.get(StockDatum.H) + (double) d.get(StockDatum.L) + (double) d.get(StockDatum.C) * 2);
			return d;
		});
	}

	public Stream<StockDatum> EMA(String di, int avgPeriod) {
		double smoothRating = 2d / (avgPeriod + 1);
		String EMA_X = "EMA" + avgPeriod;
		return movingSplit(2, 1).map(l -> {
			// calculates today's AvgIndex
			StockDatum previous = l.get(0);
			StockDatum today = l.get(1);

			double previousAvgIndex = (double) previous.get(EMA_X);
			double todayAvgIndex = smoothRating * ((double) today.get(di) - previousAvgIndex) + previousAvgIndex;

			StockDatum newEMA = new StockDatum(today, StockDatum.KEY_PROPS);
			newEMA.set(EMA_X, todayAvgIndex);
			return newEMA;
		});
	}

	// MACD：EMA(C,12)-EMA(C,26), color blue;
	public Stream<StockDatum> MACD() {
		Stream<StockDatum> EMA12 = EMA(StockDatum.C, 12);
		Stream<StockDatum> EMA26 = EMA(StockDatum.C, 26);
		return applyProperty(merge(EMA12, EMA26), StockDatum.MACD,
				d -> (double) d.get("EMA12") - (double) d.get("EMA26"));
	}

	// Signal：EMA(MACD,9), color red;
	public Stream<StockDatum> signal() {
		return new StockData(MACD().collect(Collectors.toList())).EMA(StockDatum.MACD, 9);
	}

	// Histogram：MACD-Signal, color tick;
	public Stream<StockDatum> histogram(int avgPeriod) {
		return applyProperty(merge(MACD(), signal()), StockDatum.HISTOGRAM,
				d -> (double) d.get(StockDatum.MACD) - (double) d.get(StockDatum.SIGNAL));
	}

	@SafeVarargs
	private static Stream<StockDatum> merge(Stream<StockDatum>... streams) {
		Map<String[], List<StockDatum>> map = Stream.of(streams).flatMap(s -> s)
				.collect(Collectors.groupingBy(StockDatum::getKeyProperties));
		return map.values().stream().map(list -> {
			return (StockDatum) StockDatum.merge(list, StockDatum.KEY_PROPS);
		});
	}

	private static Stream<StockDatum> applyProperty(Stream<StockDatum> inStream, String propName,
			Function<StockDatum, Object> valueGenerateF) {
		return inStream.map(d -> {
			StockDatum newEMA = new StockDatum(d, StockDatum.KEY_PROPS);
			newEMA.set(propName, valueGenerateF.apply(d));
			return newEMA;
		});
	}
}
