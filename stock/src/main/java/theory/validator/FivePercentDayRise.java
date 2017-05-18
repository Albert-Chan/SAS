package theory.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import data.DayData;
import data.StockInfo;


public class FivePercentDayRise extends CallBackHandler {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");
	private static Date baseline = null;
	static {
		try {
			baseline = dateFormat.parse("06/23/2012");
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
	}
	
	double saleAtNextOpen = 1;
	double saleAtNextHighest = 1;
	double saleAtNextLowest = 1;

	public void exec(StockInfo stock) {
		boolean printTitle = true;
		DayData[] dealRecords = (DayData[]) stock.getDealArray();

		for (int i = 1; i < dealRecords.length; i++) {

			if (i + 1 == dealRecords.length)
				break;
			if (dealRecords[i].date.before(baseline))
				continue;
			// close at the highest
			boolean closeAtHighest = ((dealRecords[i].high - dealRecords[i].close) / dealRecords[i].open) < 0.002;
			if (!closeAtHighest) {
				continue;
			}
			// 3% < raiseRate < 5%
			double raiseRate = (dealRecords[i].close - dealRecords[i].open)
					/ dealRecords[i].open;

			if (raiseRate > 0.03 && raiseRate < 0.05 &&
					i >= 4
							&& dealRecords[i - 4].close > dealRecords[i - 3].close
							&& dealRecords[i - 3].close > dealRecords[i - 2].close
					&& dealRecords[i - 2].close < dealRecords[i - 1].close
					&& dealRecords[i -1 ].close < dealRecords[i].close) {
		
				if (printTitle) {
					writer.println(stock.getStockId() + ":"
							+ stock.getStockName()
							+ "------------------------------------------");
					writer.println("Date\t\t\t\t\t" +
							"closeVsOpen\thighestVsOpen\t" +
							"lowestVsOpen\tnextOpenVsClose\tnextHighestVsClose\tnextLowestVsClose\tnextCloseVsClose");
					printTitle = false;
				}

				AnlaysisData3 anlaysis = new AnlaysisData3();

				anlaysis.closeVsOpen = (dealRecords[i].close - dealRecords[i].open)
						/ dealRecords[i].open;
				anlaysis.highestVsOpen= (dealRecords[i].high - dealRecords[i].open)
						/ dealRecords[i].open;
				anlaysis.lowestVsOpen= (dealRecords[i].low - dealRecords[i].open)
						/ dealRecords[i].open;
				anlaysis.nextOpenVsClose= (dealRecords[i+1].open - dealRecords[i].close)
						/ dealRecords[i].close;
				anlaysis.nextHighestVsClose= (dealRecords[i+1].high - dealRecords[i].close)
						/ dealRecords[i].close;
				anlaysis.nextLowestVsClose= (dealRecords[i+1].low - dealRecords[i].close)
						/ dealRecords[i].close;
				anlaysis.nextCloseVsClose= (dealRecords[i+1].close - dealRecords[i].close)
						/ dealRecords[i].close;
				
				writer.print(dateFormat.format(dealRecords[i].date) + "\t\t\t\t");

				writer.print(decimalFormat.format(anlaysis.closeVsOpen * 100) + "%"
						+ "\t\t\t\t");
				writer.print(decimalFormat.format(anlaysis.highestVsOpen * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.lowestVsOpen * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.nextOpenVsClose * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.nextHighestVsClose * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.nextLowestVsClose * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.nextCloseVsClose * 100) + "%"
						+ "\t\t\t");
				writer.println();

				saleAtNextOpen *= (1 + anlaysis.nextOpenVsClose);
				saleAtNextHighest *= (1 + anlaysis.nextHighestVsClose);
				saleAtNextLowest *= (1 + anlaysis.nextLowestVsClose);
			}

		}
	}
	
	public void postHandle() {
		writer.println("saleAtNextOpen:\t" + saleAtNextOpen);
		writer.println("saleAtNextHighest:\t" + saleAtNextHighest);
		writer.println("saleAtNextLowest:\t" + saleAtNextLowest);
	}

}

class AnlaysisData3 {
	double closeVsOpen;
	double highestVsOpen;
	double lowestVsOpen;
	double nextOpenVsClose;
	double nextHighestVsClose;
	double nextLowestVsClose;
	double nextCloseVsClose;
}
