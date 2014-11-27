package qa.qcri.aidr.common.exception;

public abstract class AidrException extends Exception {
    
    public AidrException() {
        super();
    }
 
    public AidrException(String message) {
        super("[AIDRException] " + message);
    }
 
    public AidrException(Throwable cause) {
        super(cause);
    }

    public AidrException(String message, Throwable cause) {
    	super("[AIDRException] " + message, cause);
    }
}
