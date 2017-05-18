package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayData implements IDealData {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	public Date date;
	public double open;
	public double high;
	public double low;
	public double close;
	public long vol;
	public double amount;

	public boolean build(String raw) {
		String[] data = raw.split("\t");
		if (data.length != 7) {
			return false;
		}

		try {
			date = dateFormat.parse(data[0]);
		} catch (ParseException e) {
			System.out.print(e.getMessage());
			return false;
		}
		open = Double.valueOf(data[1]);
		high = Double.valueOf(data[2]);
		low = Double.valueOf(data[3]);
		close = Double.valueOf(data[4]);
		vol = Long.valueOf(data[5]);
		amount = Double.valueOf(data[6]);
		return true;
	}

}