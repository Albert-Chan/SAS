package data.pojo;

import java.util.List;

public class CommonIndexCalculator {

	@SuppressWarnings("unchecked")
	private Data<StockDatum> stockData = Data.EMPTY;

	public void load(List<StockDatum> data) {
		stockData = new Data<StockDatum>(data);
	}

	public Data<StockDatum> MA(String aggProperty, int avgPeriod) {
		Data<StockDatum> data = stockData.collapsedMap(avgPeriod, 1, composited -> {
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
		stockData.getData().forEach(d -> {
			d.set("DI", (double) d.get(StockDatum.H) + (double) d.get(StockDatum.L) + (double) d.get(StockDatum.C) * 2);
		});
		return stockData;
	}

	// EMA
	public Data<StockDatum> EMA(String di, int avgPeriod, String alias) {
		double smoothRating = 2d / (avgPeriod + 1);
		return stockData.collapsedMap(avgPeriod, 1, composited -> {
			// calculates today's AvgIndex
			StockDatum lastDay = composited.get(0);
			StockDatum today = composited.get(1);

			double previousAvgIndex = (double) (lastDay.get(alias) == null ? 0d : lastDay.get(alias));
			double todayAvgIndex = smoothRating * ((double) today.get(di) - previousAvgIndex) + previousAvgIndex;
			today.set(alias, todayAvgIndex);
			return today;
		});
	}

	public Data<StockDatum> EMA(String di, int avgPeriod) {
		String EMA_X = "EMA" + avgPeriod;
		return EMA(di, avgPeriod, EMA_X);
	}

	// MACD：EMA(C,12)-EMA(C,26), color blue;
	public Data<StockDatum> MACD() {
		EMA(StockDatum.C, 12);
		EMA(StockDatum.C, 26);
		stockData.minus("EMA12", "EMA26", StockDatum.MACD);
		return stockData;
	}

	// Signal：EMA(MACD,9), color red;
	public Data<StockDatum> signal() {
		return EMA(StockDatum.MACD, 9, StockDatum.SIGNAL);
	}

	// Histogram：MACD-Signal, color tick;
	public Data<StockDatum> histogram(int avgPeriod) {
		MACD();
		signal();
		stockData.minus(StockDatum.MACD, StockDatum.SIGNAL, StockDatum.HISTOGRAM);
		return stockData;
	}

}
