package data;

public class FiveMinStockInfo extends StockInfo {

	public static final FiveMinStockInfo ERROR_INFO = new FiveMinStockInfo("errorId",
			"errorName");

	public FiveMinStockInfo(String stockId, String stockName) {
		super(stockId, stockName);
	}

	public FiveMinData createDealData() {
		return new FiveMinData();
	}
	
	public FiveMinData[] getDealArray() {
		FiveMinData[] dealRecords = deal.toArray(new FiveMinData[0]);
		return dealRecords;
	}
}
