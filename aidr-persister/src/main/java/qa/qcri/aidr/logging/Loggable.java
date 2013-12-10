package qa.qcri.aidr.logging;

import qa.qcri.aidr.logging.ErrorLog;

/**
 * Helper class for error logging.
 * 
 * @author jrogstadius
 */
public abstract class Loggable {
	public enum LogLevel { 
		INFO(1), WARNING(2), ERROR (3);
		
		private int value;
		 
		 private LogLevel(int value) {
		   this.value = value;
		 }
		 
		 public int getValue() {
		   return value;
		 }
	};
		
	public static LogLevel LOG_LEVEL = LogLevel.INFO;

	private final String name = this.getClass().getName();
	private static long logObjID = 0;
	protected long objID = logObjID++;
	
	protected void log(LogLevel messageLevel, String message) {
		if (messageLevel.getValue() >= LOG_LEVEL.getValue())
			ErrorLog.Print(name + "#" + objID, message);
	}

	protected static void log(LogLevel messageLevel, String sourceName, String message) {
		if (messageLevel.getValue() >= LOG_LEVEL.getValue())
			ErrorLog.Print(sourceName, message);
	}

	protected void log(String message, Exception error) {
		ErrorLog.Print(name + "#" + objID, message, error);
	}

	protected static void log(String sourceName, String message, Exception error) {
		ErrorLog.Print(sourceName, message, error);
	}
}
