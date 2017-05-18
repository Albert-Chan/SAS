package data.pojo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.google.common.collect.Lists;

public class StockDataTest {

	@Test
	public void test() throws Exception {
		List<Datum> datumList = Lists.newArrayList( 
				new Datum().build("date", "0401").build("price", 1.0),
				new Datum().build("date", "0402").build("price", 2.0), 
				new Datum().build("date", "0403").build("price", 3.0),
				new Datum().build("date", "0404").build("price", 4.0), 
				new Datum().build("date", "0405").build("price", 5.0),
				new Datum().build("date", "0406").build("price", 6.0), 
				new Datum().build("date", "0407").build("price", 7.0),
				new Datum().build("date", "0408").build("price", 8.0), 
				new Datum().build("date", "0409").build("price", 9.0),
				new Datum().build("date", "0410").build("price", 10.0), 
				new Datum().build("date", "0411").build("price", 11.0),
				new Datum().build("date", "0412").build("price", 12.0), 
				new Datum().build("date", "0413").build("price", 13.0),
				new Datum().build("date", "0414").build("price", 14.0), 
				new Datum().build("date", "0415").build("price", 15.0),
				new Datum().build("date", "0416").build("price", 16.0), 
				new Datum().build("date", "0417").build("price", 17.0),
				new Datum().build("date", "0418").build("price", 18.0), 
				new Datum().build("date", "0419").build("price", 19.0),
				new Datum().build("date", "0420").build("price", 20.0));
		StockData data = new StockData(datumList);
		//data.DI();
		Stream<Datum> outStream = data.MA("price", 5);
		
		List<Datum> outList = outStream.collect(Collectors.toList());
		
		System.out.println(outList);
		
		data.EMA("DI", 5);
	}

}
