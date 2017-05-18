package data.crawler;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

public class JrjCrawlerByDay {

	protected static final SimpleDateFormat DATE_FORMAT_YMD = new SimpleDateFormat(
			"yyyy-MM-dd");

	protected static final SimpleDateFormat DATE_FORMAT_YMDHMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:SS");

	private static final int MAX_CONNECTION = 16;

	public static void main(String[] args) {
		JrjCrawlerByDay crawler = new JrjCrawlerByDay();
		crawler.start();
	}

	public void start() {
		Timer timer = new Timer();
		// every 12h
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.HOUR_OF_DAY) < 8) {
			// FIXME handle the last day of a year.
			calendar.set(Calendar.DAY_OF_YEAR,
					calendar.get(Calendar.HOUR_OF_DAY) - 1);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 16);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		timer.schedule(new TaskThread(), 0,/*calendar.getTime(),*/ 12 * 3600 * 1000);
	}

	class TaskThread extends TimerTask {

		public void run() {

			if (!checkTime()) {
				return;
			}

			String today = DATE_FORMAT_YMD.format(new Date());
			File base1 = new File("E:/StockAnalysis/data/mx", today);
			doRun(base1);
			File base2 = new File("E:/StockAnalysis/data/mx", today + "x");
			doRun(base2);
		}

		private boolean checkTime() {
			// only fetch on working day and before 9:00 after 15:00
			Calendar calendar = Calendar.getInstance();
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
////			if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
////				return false;
//			int h = calendar.get(Calendar.HOUR_OF_DAY);
//			if (h < 15 && h > 9)
//				return false;
			return true;
		}

		public void doRun(File base) {

			if (base.exists()) {
				System.out.print("The base directory already exists!");
				return;
			} else {
				base.mkdir();
			}
			String baseDir = base.getAbsolutePath() + "\\";
			String path = "E:/StockAnalysis/data/day";
			File dir = new File(path);
			if (!dir.isDirectory()) {
				System.out.println(path + " is not a directory.");
			}

			ExecutorService executor = Executors
					.newFixedThreadPool(MAX_CONNECTION);

			for (File file : dir.listFiles()) {
				String stockId = file.getName();
				stockId = stockId.substring(2, 8);
				executor.execute(new GetJrJFengShiStockInfoThread(stockId,
						new PrintWriterPostHandler(baseDir)));
			}
			executor.shutdown();
		}
	}

	class PrintWriterPostHandler implements ICrawlerPostHandler {
		private String baseDir = null;

		public PrintWriterPostHandler(String baseDir) {
			this.baseDir = baseDir;
		}

		public void handle(String stockId, ArrayList<JSONObject> array)
				throws Exception {
			PrintWriter writer = new PrintWriter(baseDir + stockId + ".txt",
					"utf8");
			for (JSONObject object : array)
				writer.println(object);
			writer.close();
		}
	}

}
