package stock.validator;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.function.Function;

import data.pojo.Data;
import data.pojo.StockDatum;

public class Estimator {

	private Data<StockDatum> data;

	private Function<Data<StockDatum>, List<ActionPoint>> buy;
	private Function<Data<StockDatum>, List<ActionPoint>> sell;

	/**
	 * Specify the buyer function. This function should guarantee each buy point
	 * have plenty of capital.
	 * 
	 * @param f
	 */
	public void buyPoint(Function<Data<StockDatum>, List<ActionPoint>> f) {
		this.buy = f;
	}

	/**
	 * Specify the seller function. This function should guarantee each sell
	 * point have plenty of stock.
	 * 
	 * @param f
	 */
	public void sellPoint(Function<Data<StockDatum>, List<ActionPoint>> f) {
		this.sell = f;
	}

	public EstimateResult estimate() {
		List<ActionPoint> buyPoints = buy.apply(data);
		List<ActionPoint> sellPoints = sell.apply(data);

		if (!buyPoints.isEmpty() && !sellPoints.isEmpty()) {
			float totalBuyCapital = 0f;
			for (ActionPoint buy : buyPoints) {
				float capital = buy.price * buy.vol;
				totalBuyCapital += capital;
			}

			float totalSellCapital = 0f;
			for (ActionPoint sell : sellPoints) {
				float capital = sell.price * sell.vol;
				totalSellCapital += capital;
			}

			float profit = totalSellCapital - totalBuyCapital;

			ActionPoint firstBuy = buyPoints.get(0);
			ActionPoint lastSell = sellPoints.get(0);
			int days = Period.between(lastSell.time, firstBuy.time).getDays();

			return new EstimateResult(days, profit);
		}
		return null;
	}
}

class ActionPoint {
	LocalDate time;
	String stockId;
	float price;
	int vol;
}

class EstimateResult {
	int days;
	float profit;
	//float profitRate;

	public EstimateResult(int days, float profit) {
		this.days = days;
		this.profit = profit;
	}
}
