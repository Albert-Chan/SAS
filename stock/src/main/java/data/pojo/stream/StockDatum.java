package data.pojo.stream;

public class StockDatum extends Datum {
	public static final String ID = "id";
	public static final String TIME = "time";
	public static final String[] KEY_PROPS = new String[] { ID, TIME };

	public static final String H = "high";
	public static final String L = "low";
	public static final String O = "opening";
	public static final String C = "closing";
	public static final String MA = "MA";
	public static final String EMA = "EMA";
	public static final String MACD = "MACD";
	public static final String SIGNAL = "Signal";
	public static final String HISTOGRAM = "Histogram";

	public StockDatum() {
	}

	public StockDatum(StockDatum datum, String[] props) {
		super(datum, props);
	}

	public String[] getKeyProperties() {
		return KEY_PROPS;
	}

}
