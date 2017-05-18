package theory.validator;

import data.FengShiData;
import data.FengShiStockInfo;
import data.StockInfo;

public class FlowDirection extends CallBackHandler {

	public void exec(StockInfo stock) {
		FengShiData[] fsData = ((FengShiStockInfo) stock).getDealArray();

		double flow = 0d;
		double totalAmount = 0;
		for (int i = 0; i < fsData.length; i++) {
			totalAmount += fsData[i].amount;
			if (fsData[i].raise) {
				flow += fsData[i].amount;
			} else {
				flow -= fsData[i].amount;
			}
		}
		
		double rate = flow/totalAmount;
		
	}
}
