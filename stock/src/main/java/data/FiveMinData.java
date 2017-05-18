package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FiveMinData implements IDealData {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyyHHmm");

	public Date date;
	public double opening;
	public double highest;
	public double lowest;
	public double closing;
	public long volume;
	public double turnover;

	public boolean build(String raw) {
		String[] data = raw.split("\t");
		if (data.length != 8) {
			return false;
		}

		try {
			date = dateFormat.parse(data[0] + data[1]);
		} catch (ParseException e) {
			System.out.print(e.getMessage());
			return false;
		}
		opening = Double.valueOf(data[2]);
		highest = Double.valueOf(data[3]);
		lowest = Double.valueOf(data[4]);
		closing = Double.valueOf(data[5]);
		volume = Long.valueOf(data[6]);
		turnover = Double.valueOf(data[7]);
		return true;
	}

}