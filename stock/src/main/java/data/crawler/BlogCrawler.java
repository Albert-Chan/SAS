package data.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BlogCrawler {
	String last = null;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");

	public static void main(String[] args) throws IOException {
		BlogCrawler c = new BlogCrawler();
		Timer timer = new Timer();
		timer.schedule(c.new Tick(), 0, 30000);

	}

	class Tick extends TimerTask {

		public void run() {
			try {
				String site = "http://blog.sina.com.cn/u/2900138744";
				HttpURLConnection connect = (HttpURLConnection) (new URL(site)
						.openConnection());
				connect.setDoInput(true);
				connect.setDoOutput(false);
				connect.setRequestProperty("Content-Type", "text/html");
				connect.setRequestMethod("GET");

				BufferedReader buf = new BufferedReader(new InputStreamReader(
						connect.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();
				while ((line = buf.readLine()) != null) {
					response.append(line);
				}
				if (last == null) {
					System.out.println(response.toString());
				}

				System.out.println("tick!");
				if (!response.toString().equals(last)) {
					last = response.toString();
					System.out.println(new Date());
					PrintWriter writer = new PrintWriter("D:\\"
							+ dateFormat.format(new Date()) + ".html");
					writer.print(last);
					writer.close();
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
