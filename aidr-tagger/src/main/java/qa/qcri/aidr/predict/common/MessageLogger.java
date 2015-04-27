/**
 * @author: koushik
 * This class is for future use: meant to replace direct logging in DataStore.class
 * so as to reduce intensity of logging.
 *  
 * TODO: may require additional methods.
 */

package qa.qcri.aidr.predict.common;

import static qa.qcri.aidr.predict.common.ConfigProperties.getProperty;

import org.apache.log4j.Logger;

public class MessageLogger {
	private long lastSaveTime = 0;
	private int saveNewDocumentsCount;
	private static final long LOG_INTERVAL = Integer.parseInt(getProperty("LOG_INTERVAL_MINUTES")) * 60 * 1000;

	private static Logger logger = Logger.getLogger(MessageLogger.class);
	
	public MessageLogger() {}

	public boolean canLog()  {
		++this.saveNewDocumentsCount;
		if (0 == lastSaveTime) {
			lastSaveTime = System.currentTimeMillis();
			return true;
		} else {
			if ((System.currentTimeMillis() - lastSaveTime) > LOG_INTERVAL) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void logMessage(String message) {
		try {
			logger.info(message);
			this.lastSaveTime = 0;
			this.saveNewDocumentsCount = 0;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
	
	public int getSaveNewDocumentsCount() {
		return this.saveNewDocumentsCount;
	}
	
	public long getLastSaveTime() {
		return this.lastSaveTime;
	}
}
