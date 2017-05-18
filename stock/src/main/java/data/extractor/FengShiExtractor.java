package data.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import data.FengShiData;
import data.FengShiStockInfo;

public class FengShiExtractor {

	protected static final SimpleDateFormat DATE_FORMAT_YMD = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static void main(String[] args) {
		Properties props = new Properties();
		try {
			FileInputStream in = new FileInputStream(
					"E:/StockAnalysis/config.ini");
			props.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}

		try {
			String baseDir = props.getProperty("baseDir");
			if (baseDir != null)
				baseDir = baseDir + "/data/mx";
			String extractToday = props.getProperty("extractToday");
			if (Boolean.parseBoolean(extractToday) == Boolean.TRUE) {
				String today = DATE_FORMAT_YMD.format(new Date());
				new FengShiExtractor().extract(baseDir, today);
			} else {
				new FengShiExtractor().extract(baseDir);
			}

		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	public boolean extract(String base, final String strDate)
			throws IOException {
		File dir = new File(base);
		if (!dir.isDirectory()) {
			System.out.println(base + " is not a directory.");
		}

		// this map stores all the stocks of one day in a run.
		HashMap<String, FengShiStockInfo> stockMap = new HashMap<String, FengShiStockInfo>();
		boolean fillMap = true;

		for (File file : dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.startsWith(strDate);
			}
		})) {
			if (file.isDirectory()) {
				for (File stockMx : file.listFiles()) {
					FengShiStockInfo stock = extractStock(stockMx, strDate);
					if (null != stock) {
						if (fillMap)
							stockMap.put(stock.getStockId(), stock);
						else
							normalize(stock, stockMap);
					}
				}
				fillMap = false;
			}
		}

		// output stockMap
		// ...

		return true;
	}

	private void normalize(FengShiStockInfo stock,
			HashMap<String, FengShiStockInfo> map) {
		FengShiStockInfo stockInfo = map.get(stock.getStockId());
		for (FengShiData data : stock.getDealArray()) {
			stockInfo.add(data);
		}
	}

	private void output() {
		
	}

	public boolean extract(String path) throws IOException {
		File dir = new File(path);
		if (!dir.isDirectory()) {
			System.out.println(path + " is not a directory.");
		}
		// every day
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {

				String date = file.getName();
				if (date.endsWith("x"))
					continue;
				extract(path, date.replace("x", ""));
			}
		}
		return true;
	}

	/**
	 * extract the data by day
	 * 
	 * @param path
	 */
	private FengShiStockInfo extractStock(File stockFile, String date)
			throws IOException {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(stockFile), "GBK"));

			FengShiStockInfo stock = new FengShiStockInfo(stockFile.getName(),
					null, date);
			String line = null;
			while (null != (line = reader.readLine())) {
				// insert the data for the stock of this date
				stock.add(line);
			}
			return stock;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			reader.close();
		}
		return FengShiStockInfo.ERROR_INFO;
	}
}
