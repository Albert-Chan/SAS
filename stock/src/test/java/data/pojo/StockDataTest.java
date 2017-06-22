package data.pojo;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class StockDataTest {

	@Test
	public void test() throws Exception {
		List<StockDatum> datumList = Lists.newArrayList( 
				new StockDatum().append("id","sh000001").append("time", "0401").append("high", 1.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0402").append("high", 2.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0403").append("high", 3.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0404").append("high", 4.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0405").append("high", 5.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0406").append("high", 6.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0407").append("high", 7.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0408").append("high", 8.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0409").append("high", 9.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0410").append("high", 10.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0411").append("high", 11.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0412").append("high", 12.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0413").append("high", 13.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0414").append("high", 14.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0415").append("high", 15.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0416").append("high", 16.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0417").append("high", 17.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0418").append("high", 18.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0419").append("high", 19.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0),
				new StockDatum().append("id","sh000001").append("time", "0420").append("high", 20.0).append("low", 0.5).append("opening", 0.5).append("closing", 1.0));
		CommonIndexCalculator calc = new CommonIndexCalculator();
		calc.load(datumList);
		
		Data<StockDatum> ma = calc.MA("high", 5);
		System.out.println(ma);
		
		Data<StockDatum> di =calc.DI();
		System.out.println(di);
		
		Data<StockDatum> ema5 =calc.EMA("DI", 5);
		System.out.println(ema5);
	}

}
