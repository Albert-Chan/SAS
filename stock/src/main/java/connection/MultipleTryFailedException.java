package connection;

public class MultipleTryFailedException extends Exception {

	private static final long serialVersionUID = -1343490324270435032L;

	public MultipleTryFailedException(String msg) {
		super(msg);
	}

	public MultipleTryFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}