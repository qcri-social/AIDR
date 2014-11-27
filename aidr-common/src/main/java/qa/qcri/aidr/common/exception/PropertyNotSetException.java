package qa.qcri.aidr.common.exception;

public class PropertyNotSetException extends AidrException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4465084668381150539L;

	public PropertyNotSetException() {
		super();
	}

	public PropertyNotSetException(String message) {
		super(message);
	}

	public PropertyNotSetException(Throwable cause) {
		super(cause);
	}

	public PropertyNotSetException(String message, Throwable cause) {
		super(message, cause);
	}
}
