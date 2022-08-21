package common.file;

public class DirectoryException extends Exception {
	private static final long serialVersionUID = 529155013820190087L;

	public enum KnownCases {
		DIRECTORY_NO_EXISTS,
		IS_NOT_A_DIRECTORY
	}
	
	private final KnownCases knownCase;
	
	public DirectoryException() {
		super();
		
		this.knownCase = null;
	}
	
	public DirectoryException(String msg, KnownCases knownCase) {
		super(msg);
		
		this.knownCase = knownCase;
	}
	
	public DirectoryException(String msg, KnownCases knownCase, Throwable trh) {
		super(msg, trh);
		
		this.knownCase = knownCase;
	}
	
	public KnownCases getKnownCase() {
		return this.knownCase;
	}
}