package data.pojo;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class StockDataTest {

	@Test
	public void test() throws Exception {
		List<StockDatum> datumList = Lists.newArrayList( 
				new StockDatum().build("id","sh000001").build("time", "0401").build("price", 1.0),
				new StockDatum().build("id","sh000001").build("time", "0402").build("price", 2.0), 
				new StockDatum().build("id","sh000001").build("time", "0403").build("price", 3.0),
				new StockDatum().build("id","sh000001").build("time", "0404").build("price", 4.0), 
				new StockDatum().build("id","sh000001").build("time", "0405").build("price", 5.0),
				new StockDatum().build("id","sh000001").build("time", "0406").build("price", 6.0), 
				new StockDatum().build("id","sh000001").build("time", "0407").build("price", 7.0),
				new StockDatum().build("id","sh000001").build("time", "0408").build("price", 8.0), 
				new StockDatum().build("id","sh000001").build("time", "0409").build("price", 9.0),
				new StockDatum().build("id","sh000001").build("time", "0410").build("price", 10.0), 
				new StockDatum().build("id","sh000001").build("time", "0411").build("price", 11.0),
				new StockDatum().build("id","sh000001").build("time", "0412").build("price", 12.0), 
				new StockDatum().build("id","sh000001").build("time", "0413").build("price", 13.0),
				new StockDatum().build("id","sh000001").build("time", "0414").build("price", 14.0), 
				new StockDatum().build("id","sh000001").build("time", "0415").build("price", 15.0),
				new StockDatum().build("id","sh000001").build("time", "0416").build("price", 16.0), 
				new StockDatum().build("id","sh000001").build("time", "0417").build("price", 17.0),
				new StockDatum().build("id","sh000001").build("time", "0418").build("price", 18.0), 
				new StockDatum().build("id","sh000001").build("time", "0419").build("price", 19.0),
				new StockDatum().build("id","sh000001").build("time", "0420").build("price", 20.0));
		StockData data = new StockData(datumList);
		//data.DI();
		Data<StockDatum> ma = data.MA("price", 5);
		
		System.out.println(ma);
		data.DI();
		data.EMA("DI", 5);
	}

}
