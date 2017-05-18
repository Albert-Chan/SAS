package data;

public class RisingRateData {

	public String id;
	public String name;
	public double percent;
	public double dayPercent;

	public RisingRateData(String id, String name, double percent,
			double dayPercent) {
		this.id = id;
		this.name = name;
		this.percent = percent;
		this.dayPercent = dayPercent;
	}
}