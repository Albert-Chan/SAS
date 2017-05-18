package data;

public class DayStockInfo extends StockInfo {

	public static final DayStockInfo ERROR_INFO = new DayStockInfo("errorId",
			"errorName");

	private DayData[] dealRecords = null;

	public DayStockInfo(String stockId, String stockName) {
		super(stockId, stockName);
	}

	public DayData createDealData() {
		return new DayData();
	}

	public DayData[] getDealArray() {
		dealRecords = deal.toArray(new DayData[0]);
		return dealRecords;
	}

}
