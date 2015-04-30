package qa.qcri.aidr.common.redis;

import org.apache.log4j.Logger;

/**
 * A utility class to implement load shedding, i.e. skipping the processing of certain items if too
 * many items have been processed recently.
 * 
 * The limitation is defined as the maximum number of items to be processed during a certain number
 * of minutes.
 *
 */
public class LoadShedder {

	/**
	 * A common logger for all the load shedders.
	 */
	private static Logger logger = Logger.getLogger(LoadShedder.class);

	/**
	 * The beginning of the interval.
	 */
	long lastSetTime = 0;

	/**
	 * The duration of the interval in minutes.
	 */
	long intervalMinutes = 0;

	/**
	 * The duration of the interval in milliseconds (computed from {@link #intervalMinutes}).
	 */
	long intervalMillis = 0;

	/**
	 * The number of items processed in this interval.
	 */
	int counter;

	/**
	 * The maximum number of items that can be processed during an interval.
	 */
	int maxLimit;

	boolean loadWarning = false;

	boolean warn = true;

	/**
	 * Creates a load shedder that will allow the processing of up to maxLimit items in a period of
	 * intervalMinutes.
	 * 
	 * @param intervalMinutes duration of the interval in minutes
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
		logger.info("Initialized Loadshedder with " + this.maxLimit + " per " + this.intervalMinutes + "min messages");
	}

	/**
	 * Gets the number of messages processed so far in this interval.
	 * 
	 * @return the number
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Sets the number of messages processed so far in this interval.
	 * 
	 * @param counter
	 */
	public void setCounter(final int counter) {
		this.counter = counter;
	}

	/**
	 * Gets a flag indicating whether we should log a warning if the rate is exceeded
	 * 
	 * @return the flag 
	 */
	public boolean getWarn() {
		return warn;
	}

	/**
	 * Sets a flag indicating whether we should log a warning if the rate is exceeded
     *
	 * @param warn
	 */
	public void setWarn(final boolean warn) {
		this.warn = warn;
	}

	/**
	 * Gets the maximum number of items that can be processed during an interval
	 * 
	 * @return the limit
	 */
	public int getMaxLimit() {
		return maxLimit;
	}

	/**
	 * Sets the maximum number of items that can be processed during an interval
	 * 
	 * @param maxLimit the limit
	 */
	public void setMaxLimit(final int maxLimit) {
		this.maxLimit = maxLimit;
	}

	/**
	 * This method is called before processing an item. If the method returns true, the item should
	 * be processed. If the method returns false, the item should not be processed, because we have
	 * exceeded the number of items that can be processed during a certain period.
	 * 
	 * TODO: Move the "channel" parameter to the constructor and make more generic (i.e. a string
	 * indicating what exactly this shedder is used for)
	 * 
	 * TODO: Remove the "debug" level logging, which is unnecessary.
	 * 
	 * @param channel a collection code, used currently for logging
	 * 
	 * @return true if the current item should be processed, false if the current item should not be
	 *         processed
	 */
	public boolean canProcess(String channel) {
		logger.debug("For channel: " + channel + ", counter:maxLimit" + counter + ":" + maxLimit);
		if ((System.currentTimeMillis() - lastSetTime) <= intervalMillis) {
			if (counter < maxLimit) {
				++counter;
				logger.debug("For channel: " + channel + ", returning true");
				return true; // within bounds
			}

			// Otherwise, reset and return false
			if (warn && !loadWarning) {
				loadWarning = true; // warn only once per interval
				logger.warn("Limit of " + maxLimit + " messages per " + intervalMinutes + " mins reached with current count = " + counter);
			}
			logger.debug("For channel: " + channel + ", returning false");
			return false; // wait until end of interval before resetting
		}
		// Interval over - now reset for next interval
		logger.debug("For channel: " + channel + ", returning true as interval is over");
		counter = 0;
		lastSetTime = System.currentTimeMillis();
		loadWarning = false;
		return true;
	}
}