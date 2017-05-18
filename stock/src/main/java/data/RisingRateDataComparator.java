package data;

import java.util.Comparator;

public class RisingRateDataComparator implements Comparator<RisingRateData> {

	public int compare(RisingRateData o1, RisingRateData o2) {
		if (o1.percent - o2.percent < 0) {
			return 1;
		} else if (o1.percent - o2.percent > 0) {
			return -1;
		} else {
			return 0;
		}
	}
}
