package data.crawler;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import connection.HttpConnection;

public class GetJrJFengShiStockInfoThread implements Runnable {

	private static final int maxTry = 50;
	private String stockId;
	private ICrawlerPostHandler postHandler;

	public GetJrJFengShiStockInfoThread(String stockId, ICrawlerPostHandler handler) {
		this.stockId = stockId;
		this.postHandler = handler;
	}

	public void run() {
		if (!getStockByID(stockId))
			System.out.print("Downloading failed: " + stockId);
	}

	private boolean getStockByID(String stockId) {
		try {
			getStockByIdDetailed(stockId);
			return true;
		} catch (Exception e) {
			System.out.print("Fatal >>> Problem on Stock: " + stockId + "\t");
			System.out.println(e.getMessage());
			return false;
		}
	}

	private void getStockByIdDetailed(String id) throws Exception {

		ArrayList<JSONObject> array = new ArrayList<JSONObject>();

		String lastResponse = null;
		int pageIndex = 1;
		int tryCount = 0;
		while (true) {
			String result;
			String url = "http://qmx.jrjimg.cn/mx.do?code=" + id + "&page="
					+ pageIndex + "&size=120";
			try {
				result = HttpConnection.getData(url, "gbk");
			} catch (Exception e) {
				System.out.print("Problem on Stock: " + stockId
						+ "  pageIndex=" + pageIndex + "\t");
				System.out.println(e.getMessage());
				tryCount++;
				if (tryCount < maxTry)
					continue;
				else
					throw e;
			}

			String jsonText = result.substring(result.indexOf('{'));

			String compareString = jsonText.substring(jsonText
					.indexOf("DetailData"));
			if (compareString.equals(lastResponse))
				break;
			else
				lastResponse = compareString;

			JSONObject json = new JSONObject(jsonText);
			JSONArray jsonArray = json.getJSONArray("DetailData");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject arrayElement = jsonArray.getJSONObject(i);
				array.add(arrayElement);
			}
			tryCount = 0;
			pageIndex++;
		}
		postHandler.handle(id, array);
	}

}
