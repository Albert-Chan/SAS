package data.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FinancialGuideLine {
	private static final Logger LOG = Logger.getLogger(FinancialGuideLine.class);
	private static final String URL = "http://money.finance.sina.com.cn/corp/go.php/vFD_FinancialGuideLine/stockid/601727/ctrl/2017/displaytype/4.phtml";

	public static long getResultCount(String q) throws UnsupportedEncodingException, IOException {
		Document doc = get(q);
		String resultsCount = doc.select("#b_tween > .sb_count").text();

		if (StringUtils.isEmpty(resultsCount))
			return 0;
		return Long.parseLong(resultsCount.replace(" results", "").replace(",", "").trim());
	}

	public static void getQueryResults(String q) throws IOException {
		Document doc = get(q);
		Elements eles = doc.select("ol#b_results > li.b_algo");

		for (Element e : eles) {
			String header = e.select("h2").text();
			String href = e.select("h2 > a").attr("href");
			LOG.debug(header);
			LOG.debug(href);
		}
	}

	public static String sample(String q) throws IOException {
		Document doc = get(q);
		Elements eles = doc.select("ol#b_results > li.b_algo");

		Element e = eles.first();
		String header = e.select("h2").text();
		String href = e.select("h2 > a").attr("href");
		LOG.debug(header);
		LOG.debug(href);

		String content = getContent(href);
		return content;
	}

	public static String getContent(String href) throws IOException {
		Document doc = Jsoup.connect(href).get();
		return doc.toString();
	}

	public static Document get(String q) throws IOException {
		return Jsoup.connect(URL).get();
	}

}
