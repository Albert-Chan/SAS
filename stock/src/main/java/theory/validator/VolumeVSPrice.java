package theory.validator;

import data.DayData;
import data.StockInfo;

public class VolumeVSPrice extends CallBackHandler {

	public void exec(StockInfo stock) {
		DayData[] dealRecords = (DayData[]) stock.getDealArray();
		long subTotalVolume = 0;

		int currentPositive = 0;
		int currentNegative = 0;

		int nextPositive = 0;
		int nextNegative = 0;

		for (int i = 0; i < dealRecords.length; i++) {
			if (i < 4) {
				continue;
			}

			subTotalVolume = dealRecords[i].vol + dealRecords[i - 1].vol
					+ dealRecords[i - 2].vol + dealRecords[i - 3].vol
					+ dealRecords[i - 4].vol;

			long averageTotalVolume = subTotalVolume / 5;

			if (dealRecords[i].vol > 1.5 * averageTotalVolume) {

				if (dealRecords[i].close >= dealRecords[i].open)
					currentPositive++;
				else
					currentNegative++;

				if (i + 1 < dealRecords.length) {
					if (dealRecords[i + 1].close >= dealRecords[i + 1].open)
						nextPositive++;
					else
						nextNegative++;
				}
			}
		}
		System.out.println(stock.getStockId() + ": " + stock.getStockName());
		System.out.println("+:" + currentPositive);
		System.out.println("-:" + currentNegative);
		System.out.println(">+:" + nextPositive);
		System.out.println(">-:" + nextNegative);
	}
}
