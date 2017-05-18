package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class FengShiData implements IDealData {
	protected static final SimpleDateFormat DATE_FORMAT_YMD = new SimpleDateFormat(
			"yyyy-MM-dd");
	protected static final SimpleDateFormat DATE_FORMAT_YMDHMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:SS");

	public Date date;
	public double price;
	public long volume;
	public double amount;
	public long A4;
	public boolean raise;

	private String whichDay;

	public FengShiData(String date) {
		this.whichDay = date;
	}

	public boolean build(String raw) {

		try {
			JSONObject obj = new JSONObject(raw);
			String a1 = obj.getString("A1");
			String a2 = obj.getString("A2");
			String a3 = obj.getString("A3");
			String a4 = obj.getString("A4");
			String a5 = obj.getString("A5");
			String a6 = obj.getString("A6");

			price = Double.valueOf(a1);
			volume = Long.valueOf(a2);
			amount = Double.valueOf(a3);
			A4 = Long.valueOf(a4);
			date = DATE_FORMAT_YMDHMS.parse(whichDay + " " + a5);
			raise = Long.valueOf(a6) == 1 ? true : false;

		} catch (ParseException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean equals(FengShiData d) {
		if (d == null)
			return false;
		if (d.date == date && d.price == price && d.volume == volume
				&& d.amount == amount && d.A4 == A4 && d.raise == raise)
			return true;
		return false;
	}

}
