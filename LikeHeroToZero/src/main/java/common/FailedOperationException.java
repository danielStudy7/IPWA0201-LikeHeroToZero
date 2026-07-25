package common;

public class FailedOperationException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public FailedOperationException() {
		super();
	}
	
	public FailedOperationException(String message) {
		super(message);
	}
	
	
}
