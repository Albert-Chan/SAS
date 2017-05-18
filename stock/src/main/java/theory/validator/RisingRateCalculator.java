package theory.validator;

import data.FengShiData;
import data.StockInfo;

public class RisingRateCalculator extends CallBackHandler {

	public void exec(StockInfo stock) {
		boolean printTitle = true;
		FengShiData[] dealRecords = (FengShiData[]) stock.getDealArray();

		dealRecords = flip(dealRecords);

		for (int i = 0; i < dealRecords.length; i++) {

			double raiseRate = (dealRecords[i].price - dealRecords[i - 1].price)
					/ dealRecords[i - 1].price;

			if (raiseRate > 0.09) {
				if (printTitle) {
					writer.println(stock.getStockId() + ":"
							+ stock.getStockName()
							+ "------------------------------------------");
					writer.println("openVsLastClose\tcloseVsLastClose\tcloseVsOpen\tcloseVsHighest\tcloseVsLowest\topenVsHighest\topenVsLowest");
					printTitle = false;
				}

				// AnlaysisData anlaysis = new AnlaysisData();

			}
		}
	}

	private FengShiData[] flip(FengShiData[] data) {
		int size = data.length;
		for (int i = 0; i < size / 2; i++) {
			FengShiData temp = data[0];
			data[i] = data[size - 1 - i];
			data[size - 1 - i] = temp;
		}
		return data;
	}
}
