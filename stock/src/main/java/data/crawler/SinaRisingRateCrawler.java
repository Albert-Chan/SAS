package data.crawler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import connection.HttpConnection;
import data.RisingRateData;
import data.RisingRateDataComparator;

public class SinaRisingRateCrawler {
	// The static logger
	static protected Logger logger = Logger
			.getLogger(SinaRisingRateCrawler.class.getName());

	// protected static final Logger logger = Logger.getAnonymousLogger();

	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * ShangHai URL
	 */
	private static final String urlSH = "http://hq.sinajs.cn/rn=avmm9&format=text&list=stock_sh_up_5min_20";

	/**
	 * ShenZheng URL
	 */
	private static final String urlSZ = "http://hq.sinajs.cn/rn=avmm9&format=text&list=stock_sz_up_5min_20";

	private static final int MAX_CONNECTION = 16;

	public static void main(String[] args) {

		// FileHandler fileHandler = null;
		// try {
		// fileHandler = new FileHandler("%u.log ");
		// } catch (SecurityException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// if (null != fileHandler) {
		// fileHandler.setFormatter(new SimpleFormatter());
		// logger.addHandler(fileHandler);
		// }

		try {
			ExecutorService executor = Executors
					.newFixedThreadPool(MAX_CONNECTION);
			while (true) {
				ArrayList<RisingRateData> rank = new ArrayList<RisingRateData>();
				rank.addAll(getRealTimeData(urlSH));
				rank.addAll(getRealTimeData(urlSZ));
				RisingRateData[] rankArray = new RisingRateData[0];
				Arrays.sort(rankArray = rank.toArray(rankArray),
						new RisingRateDataComparator());

				// System.out.print( Arrays.deepToString( rankArray ) );

				for (RisingRateData data : rankArray) {
					String id = data.id;
					executor.execute(new GetJrJFengShiStockInfoThread(id,
							new SimplePostHandler()));

					// FengShiStockInfo current =
					// JrjCrawlerRealTime.getStockByID(id);
					// FengShiStockInfo history =
					// JrjFengShiDataExtractor.extract(id);
					//
					// FengShiData[] dataes = current.getDealArray();
					// FengShiData[] dataes = history.getDealArray();

				}
				TimeUnit.MILLISECONDS.sleep(1000);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	private static ArrayList<RisingRateData> getRealTimeData(String url)
			throws Exception {
		String result = HttpConnection.getData(url, "gbk");
		String jsonText = result.substring(result.indexOf('['));
		JSONArray jsonArray = new JSONArray(jsonText);

		// The jsonArray sample:
		// [{symbol:"sh600287",name:"XXX",trade:"5.840",old_trade:"5.74",percent:"1.74",day_percent:"5.04"},{symbol:"sh600396",na...}]
		ArrayList<RisingRateData> rank = new ArrayList<RisingRateData>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject arrayElement = jsonArray.getJSONObject(i);
			String id = arrayElement.getString("symbol").substring(2);
			String name = arrayElement.getString("name");
			String percent = arrayElement.getString("percent");
			String dayPercent = arrayElement.getString("day_percent");

			rank.add(new RisingRateData(id, name, Double.valueOf(percent),
					Double.valueOf(dayPercent)));
		}
		return rank;
	}

	static class SimplePostHandler implements ICrawlerPostHandler {
		public void handle(String stockId, ArrayList<JSONObject> array ){
			
		}
	}

}
