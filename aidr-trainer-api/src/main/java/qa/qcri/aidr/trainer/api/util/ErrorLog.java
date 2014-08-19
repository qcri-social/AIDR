package qa.qcri.aidr.trainer.api.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorLog {
	public ErrorLog() {}
	
	public String toStringException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        
        StringBuilder retVal = new StringBuilder(new String("Exception:")).append(System.getProperty("line.separator")).append(sw);
        return retVal.toString();
    }
}
