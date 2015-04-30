package qa.qcri.aidr.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A deprecated utility class for error logs
 * 
 * TODO: REMOVE. Remove this class and all references to this class.
 *
 */
@Deprecated
public class ErrorLog {
	public ErrorLog() {}
	
	/**
	 * A deprecated method to create a String from an Exception.
	 * 
	 * @param e the exception
	 * @return a String representing the exception
	 */
	@Deprecated
	public String toStringException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        
        StringBuilder retVal = new StringBuilder(new String("Exception:")).append(System.getProperty("line.separator")).append(sw);
        return retVal.toString();
    }
}
