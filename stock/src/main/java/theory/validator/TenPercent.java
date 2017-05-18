package theory.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import data.DayData;
import data.StockInfo;


public class TenPercent extends CallBackHandler {
	private static final SimpleDateFormat df = new SimpleDateFormat(
			"MM/dd/yyyy");
	private static Date baseline = null;
	static {
		try {
			baseline = df.parse("06/23/2012");
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
	}

	public void exec(StockInfo stock) {
		boolean printTitle = true;
		DayData[] dealRecords = (DayData[]) stock.getDealArray();

		for (int i = 1; i < dealRecords.length; i++) {

			if (i + 1 == dealRecords.length)
				break;
			if (dealRecords[i].date.before(baseline))
				continue;

			double raiseRate = (dealRecords[i].close - dealRecords[i - 1].close)
					/ dealRecords[i - 1].close;

			if (raiseRate > 0.09) {
				if (printTitle) {
					writer.println(stock.getStockId() + ":"
							+ stock.getStockName()
							+ "------------------------------------------");
					writer.println("openVsLastClose\tcloseVsLastClose\tcloseVsOpen\tcloseVsHighest\tcloseVsLowest\topenVsHighest\topenVsLowest");
					printTitle = false;
				}

				AnlaysisData anlaysis = new AnlaysisData();
				anlaysis.openVsLastClosing = (dealRecords[i + 1].open - dealRecords[i].close)
						/ dealRecords[i].close;
				anlaysis.closeVsLastClosing = (dealRecords[i + 1].close - dealRecords[i].close)
						/ dealRecords[i].close;

				anlaysis.closeVsOpening = (dealRecords[i + 1].close - dealRecords[i + 1].open)
						/ dealRecords[i + 1].close;
				anlaysis.closeVsHighest = (dealRecords[i + 1].close - dealRecords[i + 1].high)
						/ dealRecords[i + 1].close;
				anlaysis.closeVsLowest = (dealRecords[i + 1].close - dealRecords[i + 1].low)
						/ dealRecords[i + 1].close;
				anlaysis.openVsHighest = (dealRecords[i + 1].open - dealRecords[i + 1].high)
						/ dealRecords[i + 1].open;
				anlaysis.openVsLowest = (dealRecords[i + 1].open - dealRecords[i + 1].low)
						/ dealRecords[i + 1].open;

				writer.print(df.format(anlaysis.openVsLastClosing * 100) + "%"
						+ "\t\t\t\t");
				writer.print(df.format(anlaysis.closeVsLastClosing * 100) + "%"
						+ "\t\t\t\t");
				writer.print(df.format(anlaysis.closeVsOpening * 100) + "%"
						+ "\t\t\t");
				writer.print(df.format(anlaysis.closeVsHighest * 100) + "%"
						+ "\t\t\t");
				writer.print(df.format(anlaysis.closeVsLowest * 100) + "%"
						+ "\t\t\t");
				writer.print(df.format(anlaysis.openVsHighest * 100) + "%"
						+ "\t\t\t");
				writer.print(df.format(anlaysis.openVsLowest * 100) + "%"
						+ "\t\t\t");
				writer.println();

			}

		}

	}
}

class AnlaysisData {

	double openVsLastClosing;
	double closeVsLastClosing;
	double closeVsOpening;
	double closeVsHighest;
	double closeVsLowest;
	double openVsHighest;
	double openVsLowest;

}
