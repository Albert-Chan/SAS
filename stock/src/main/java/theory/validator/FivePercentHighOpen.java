package theory.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import data.DayData;
import data.StockInfo;


public class FivePercentHighOpen extends CallBackHandler {
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
// high open
			double raiseRate = (dealRecords[i].open - dealRecords[i - 1].close)
					/ dealRecords[i - 1].close;

			if (raiseRate > 0.03) {
				if (printTitle) {
					writer.println(stock.getStockId() + ":"
							+ stock.getStockName()
							+ "------------------------------------------");
					writer.println("Date\t\t\t\t\topenVsLastClose\t" +
							"closeVsOpen\thighestVsOpen\t" +
							"lowestVsOpen\tnextOpenVsOpen\tnextHighestVsOpen\tnextLowestVsOpen\tnextCloseVsOpen");
					printTitle = false;
				}

				AnlaysisData2 anlaysis = new AnlaysisData2();
				anlaysis.openVsLastClose = (dealRecords[i].open - dealRecords[i-1].close)
						/ dealRecords[i-1].close;
				anlaysis.closeVsOpen = (dealRecords[i].close - dealRecords[i].open)
						/ dealRecords[i].open;
				anlaysis.highestVsOpen= (dealRecords[i].high - dealRecords[i].open)
						/ dealRecords[i].open;
				anlaysis.lowestVsOpen= (dealRecords[i].low - dealRecords[i].open)
						/ dealRecords[i].open;
				anlaysis.nextOpenVsOpen= (dealRecords[i+1].open - dealRecords[i].open)
						/ dealRecords[i].open;
				anlaysis.nextHighestVsOpen= (dealRecords[i+1].high - dealRecords[i].open)
						/ dealRecords[i].open;
				anlaysis.nextLowestVsOpen= (dealRecords[i+1].low - dealRecords[i].open)
						/ dealRecords[i].open;
				anlaysis.nextCloseVsOpen= (dealRecords[i+1].close - dealRecords[i].open)
						/ dealRecords[i].open;
				
				writer.print(dateFormat.format(dealRecords[i].date) + "\t\t\t\t");
				writer.print(decimalFormat.format(anlaysis.openVsLastClose * 100) + "%"
						+ "\t\t\t\t");
				writer.print(decimalFormat.format(anlaysis.closeVsOpen * 100) + "%"
						+ "\t\t\t\t");
				writer.print(decimalFormat.format(anlaysis.highestVsOpen * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.lowestVsOpen * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.nextOpenVsOpen * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.nextHighestVsOpen * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.nextLowestVsOpen * 100) + "%"
						+ "\t\t\t");
				writer.print(decimalFormat.format(anlaysis.nextCloseVsOpen * 100) + "%"
						+ "\t\t\t");
				writer.println();

				saleAtNextOpen *= (1 + anlaysis.nextOpenVsOpen);
				saleAtNextHighest *= (1 + anlaysis.nextHighestVsOpen);
				saleAtNextLowest *= (1 + anlaysis.nextLowestVsOpen);
			}

		}
	}
	
	public void postHandle() {
		writer.println("saleAtNextOpen:\t" + saleAtNextOpen);
		writer.println("saleAtNextHighest:\t" + saleAtNextHighest);
		writer.println("saleAtNextLowest:\t" + saleAtNextLowest);
	}

}

class AnlaysisData2 {

	double openVsLastClose;
	double closeVsOpen;
	double highestVsOpen;
	double lowestVsOpen;
	double nextOpenVsOpen;
	double nextHighestVsOpen;
	double nextLowestVsOpen;
	double nextCloseVsOpen;
}
