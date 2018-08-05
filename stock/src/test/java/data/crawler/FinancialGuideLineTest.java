package data.crawler;

import java.util.ListIterator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;




public class FinancialGuideLineTest {
	private static final String URL = "http://money.finance.sina.com.cn/corp/go.php/vFD_FinancialGuideLine/stockid/601727/ctrl/2017/displaytype/4.phtml";

	@Test
	public void test() throws Exception {
		Document doc = FinancialGuideLine.get(URL);
		Elements ele2s = doc.select("table#BalanceSheetNewTable0>tbody>tr");
		
		
		
		System.out.println(ele2s.first().children().eachText());
		for (ListIterator<Element> iter = ele2s.listIterator(1); iter.hasNext(); ) {
			System.out.println(iter.next().children().eachText());
		}
		//List<String> strs = eles.eachText();
		//Elements eles = doc.getElementsMatchingOwnText("历年数据");
		//Elements eles = doc.getElementsMatchingOwnText("2018");
		
		//System.out.println(eles);
		//System.out.println(ele2s);
	}

}
