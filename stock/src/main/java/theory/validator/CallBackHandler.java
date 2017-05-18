package theory.validator;

import java.io.PrintWriter;
import java.text.DecimalFormat;

import data.StockInfo;


public abstract class CallBackHandler {
	protected PrintWriter writer = new PrintWriter(System.out);

	protected static final DecimalFormat decimalFormat = new DecimalFormat(
			"#.000");

	// protected static SimpleDateFormat dateFormat = new SimpleDateFormat(
	// "yyyy-MM-dd");

	public abstract void exec(StockInfo stock);

	public void postHandle() {
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public void setDecimalFormat() {

	}

	public void printPercentage(double number) {
		writer.print(decimalFormat.format(number * 100) + "%");
	}
}
