package qa.qcri.aidr.common.exception;

/**
 * An AIDR-specific exception
 *
 */
public abstract class AidrException extends Exception {
    
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -739288302986762394L;

	/**
     * Creates an exception
     */
    public AidrException() {
        super();
    }
 
    /**
     * Creates an exception with a given message.
     * 
     * @param message the message to include in the exception
     */
    public AidrException(String message) {
        super("[AIDRException] " + message);
    }
 
    /**
     * Creates an exception with a given cause
     * 
     * @param cause the cause
     */
    public AidrException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an exception with a given message and cause
     * 
     * @param message the message
     * @param cause the cause
     */
    public AidrException(String message, Throwable cause) {
    	super("[AIDRException] " + message, cause);
    }
}
