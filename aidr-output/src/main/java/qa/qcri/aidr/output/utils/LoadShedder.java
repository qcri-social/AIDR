package qa.qcri.aidr.output.utils;

import org.apache.log4j.Logger;

public class LoadShedder {

	private static Logger logger = Logger.getLogger(LoadShedder.class);
	
	long lastSetTime = 0;
	long intervalMillis = 0;
	long intervalMinutes = 0;
	int counter;
	int maxLimit;
	boolean loadWarning = false;
	boolean warn = true;

	/**
	 * 
	 * @param intervalMinutes granularity of checking, expressed as minutes
	 * @param maxLimit maximum number of messages in an interval
	 * @param warn if true, then log a warning message on rate exceed
	 */
	public LoadShedder(final int maxLimit, final int intervalMinutes, final boolean warn) {
		this.maxLimit = maxLimit;
		this.counter = 0;
		this.warn = warn;
		this.lastSetTime = System.currentTimeMillis();
		this.intervalMinutes = intervalMinutes;
		this.intervalMillis = intervalMinutes * 1000 * 60;	
		this.loadWarning = false;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(final int counter) {
		this.counter = counter;
	}

	public boolean getWarn() {
		return warn;
	}
	
	public void setWarn(final boolean warn) {
		this.warn = warn;
	}

	public int getMaxLimit() {
		return maxLimit;
	}
	
	public void setMaxLimit(final int maxLimit) {
		this.maxLimit = maxLimit;
	}

	public boolean canProcess() {	
		if ((System.currentTimeMillis() - lastSetTime) <= intervalMillis) {
			if (counter < maxLimit) {
				++counter;
				return true;		// within bounds
			}
			
			// Otherwise, reset and return false
			if (warn && !loadWarning) {
				loadWarning = true;		// warn only once per interval
				logger.warn("Limit of " + maxLimit + " messages per " + intervalMinutes
						+ " mins reached with current count = " + counter);
			}
			return false;		// wait until end of interval before resetting
		}
		// Interval over - now reset for next interval
		counter = 0;
		lastSetTime = System.currentTimeMillis();
		loadWarning = false;
		return true;	  
	}
}