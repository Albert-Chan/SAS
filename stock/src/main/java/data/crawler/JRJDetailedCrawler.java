package data.crawler;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dataminer.configuration.options.OptionsParser;
import com.dataminer.configuration.options.OptionsParser.OptionsParseException;
import com.dataminer.configuration.options.OptionsParser.OptionsParserBuildException;
import com.dataminer.configuration.options.ParsedOptions;
import com.dataminer.constants.Constants;

import connection.Requests;
import connection.MultipleTryFailedException;

public class JRJDetailedCrawler {
	private static final Logger LOG = Logger.getLogger(JRJDetailedCrawler.class);
	private static final int MAX_CONNECTION = 16;
	private static final String BASE_PATH = "D:/StockAnalysis/data/";

	public static void main(String[] args) {
		try {
			LocalDate analyticDay = parseArgs(args).get("analyticDay");
			File path = new File(BASE_PATH, DateTimeFormatter.ofPattern(Constants.YMD_FORMAT).format(analyticDay));
			crawl(path);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	private static ParsedOptions parseArgs(String[] args) throws OptionsParserBuildException, OptionsParseException {
		List<String> optionDef = List
				.of("d, analyticDay, hasArg, required, , toDate, The analytic date in the format of yyyy/MM/dd");

		OptionsParser parser = new OptionsParser(optionDef);
		ParsedOptions parsedOptions = parser.parse(args);
		return parsedOptions;
	}

	public static void crawl(File base) {
		ExecutorService executor = Executors.newFixedThreadPool(MAX_CONNECTION);
		List<String> allStocks = getAllStocks();
		for (String stockId : allStocks) {
			executor.execute(new SingleStockDetailedFetcher(stockId, new FileSinker(base)));
		}
		executor.shutdown();
	}

	public static List<String> getAllStocks() {
		return List.of("sz300294");
	}

}

class SingleStockDetailedFetcher implements Runnable {
	private static final Logger LOG = Logger.getLogger(SingleStockDetailedFetcher.class);
	private static final int MAX_TRY = 5;

	private static final String URL_PATTERN = "http://qmx.jrjimg.cn/mx.do?code=%s&page=%d&size=120";

	private String stockId;
	private List<ICrawlerPostHandler> postHandlers;

	public SingleStockDetailedFetcher(String stockId, ICrawlerPostHandler... handlers) {
		this.stockId = stockId;
		postHandlers = List.of(handlers);
	}

	public void run() {
		try {
			handleStock(stockId);
		} catch (MultipleTryFailedException e) {
			LOG.warn("Failed to get stock info: " + stockId + "\nCaused by" + e.getMessage());
		} catch (JSONException e) {
			LOG.warn("Failed to parse JSON: " + stockId + "\nCaused by: " + e.getMessage());
		} catch (HandlerException e) {
			LOG.warn("Handler failed: " + stockId + "\nCaused by: " + e.getMessage());
		}
	}

	private void handleStock(String stockId) throws MultipleTryFailedException, JSONException, HandlerException {
		List<String> lastResponse = null;
		List<String> mergedContent = new ArrayList<>();
		for (int pageIndex = 1;; pageIndex++) {
			String url = String.format(URL_PATTERN, stockId, pageIndex);

			LOG.debug(url);
			
			String result = Requests.get(url, "gb2312", MAX_TRY);
			String data = result.substring(result.indexOf("{"));
			List<String> datailDataList = parseAsJSON(data);

			LOG.debug(datailDataList);

			if (datailDataList.equals(lastResponse)) {
				for (ICrawlerPostHandler handler : postHandlers) {
					handler.handle(stockId, mergedContent);
				}
				return;
			} else {
				lastResponse = datailDataList;
				mergedContent.addAll(datailDataList);
			}
		}
	}

	private List<String> parseAsJSON(String detailData) throws JSONException {
		JSONObject json = new JSONObject(detailData);
		JSONArray jsonArray = json.getJSONArray("DetailData");

		ArrayList<String> array = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject arrayElement = jsonArray.getJSONObject(i);
			array.add(arrayElement.toString());
		}
		return array;
	}
}