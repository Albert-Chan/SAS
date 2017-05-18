package data;

import java.util.Comparator;
import java.util.TreeSet;

public class FengShiStockInfo extends StockInfo {
	public static final FengShiStockInfo ERROR_INFO = new FengShiStockInfo(
			"errorId", "errorName", null);

	private String date = null;

	public FengShiStockInfo(String stockId, String stockName, String date) {
		super(stockId, stockName);
		this.date = date;
	}

	public FengShiData createDealData() {
		return new FengShiData(date);
	}

	public FengShiData[] getDealArray() {
		for (FengShiData fengShiData : set) {
			deal.add(fengShiData);
		}
		FengShiData[] dealRecords = deal.toArray(new FengShiData[0]);
		return dealRecords;
	}

	TreeSet<FengShiData> set = new TreeSet<FengShiData>(
			new DateTimeComparator());

	public boolean add(String dealData) {
		if (dealData.equals("数据来源:通达信"))
			return true;
		FengShiData data = createDealData();
		if (!data.build(dealData)) {
			System.out.println("Data error on " + stockId + ": " + stockName);
			return false;
		}
		return set.add(data);
	}

	public boolean add(FengShiData data) {
		return set.add(data);
	}

	class DateTimeComparator implements Comparator<FengShiData> {
		public int compare(FengShiData a, FengShiData b) {
			if (a.date.equals(b.date))
				return 0;
			if (a.date.before(b.date))
				return -1;
			else
				return 1;
		}
	}
}
