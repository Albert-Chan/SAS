package data.pojo;

import java.util.List;

public class StockData extends Data<StockDatum> {

	public StockData(List<StockDatum> data) {
		super(data);
	}

	public Data<StockDatum> MA(String aggProperty, int avgPeriod) {
		Data<StockDatum> data = collapsedMap(avgPeriod, 1, composited -> {
			double sum = 0.0;
			StockDatum lastDatum = null;
			for (StockDatum datum : composited) {
				sum += (double) datum.get(aggProperty);
				lastDatum = datum;
			}

			StockDatum newMA = new StockDatum(lastDatum, StockDatum.KEY_PROPS);
			String MA_X = StockDatum.MA + avgPeriod;
			newMA.set(MA_X, sum / avgPeriod);
			return newMA;
		});

		return data;
	}

	// DI=(C*2+H+L) / 4
	public Data<StockDatum> DI() {
		data.forEach(d -> {
			d.set("DI", (double) d.get(StockDatum.H) + (double) d.get(StockDatum.L) + (double) d.get(StockDatum.C) * 2);
		});
		return this;
	}

	// EMA
	public Data<StockDatum> EMA(String di, int avgPeriod) {
		double smoothRating = 2d / (avgPeriod + 1);
		String EMA_X = "EMA" + avgPeriod;
		return (StockData) collapsedMap(2, 1, l -> {
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
	public Data<StockDatum> MACD() {
		EMA(StockDatum.C, 12);
		EMA(StockDatum.C, 26);
		data.forEach(d -> {
			d.set(StockDatum.MACD, (double) d.get("EMA12") - (double) d.get("EMA26"));
		});
		return this;
	}

	// Signal：EMA(MACD,9), color red;
	public Data<StockDatum> signal() {
		return EMA(StockDatum.MACD, 9);
	}

	// Histogram：MACD-Signal, color tick;
	public Data<StockDatum> histogram(int avgPeriod) {
		MACD();
		signal();
		data.forEach(d -> {
			d.set(StockDatum.HISTOGRAM, (double) d.get(StockDatum.MACD) - (double) d.get(StockDatum.SIGNAL));
		});
		return this;
	}

}
