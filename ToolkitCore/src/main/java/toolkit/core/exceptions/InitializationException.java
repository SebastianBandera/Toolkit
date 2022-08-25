package toolkit.core.exceptions;

public class InitializationException extends Exception {

	private static final long serialVersionUID = -4642455576404201401L;

	public InitializationException() {
		super();
	}
	
	public InitializationException(String msg) {
		super(msg);
	}
	
	public InitializationException(String msg, Throwable trh) {
		super(msg, trh);
	}
}
