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
	 * The duration of the interval in minutes.
	 */
	final double intervalMinutes;

	/**
	 * The duration of the interval in milliseconds (computed from {@link #intervalMinutes}).
	 */
	final long intervalMillis;

	/**
	 * The maximum number of items that can be processed during an interval.
	 */
	final int maxLimit;

	/**
	 * Whether to emit a warning when loading or not.
	 */
	final boolean warnOnLimit;
	
	/**
	 * String identifying this load shedder for logging purposes, e.g. a channel name
	 */
	final String name;

	/**
	 * The beginning of the interval.
	 */
	long lastSetTime = 0;

	/**
	 * The number of items processed in this interval.
	 */
	int counter;

	/**
	 * Whether a warning has been issued already for this interval.
	 */
	boolean warningEmitted;

	/**
	 * Creates a load shedder that will allow the processing of up to maxLimit items in a period of
	 * intervalMinutes.
	 * 
	 * Deprecated: use {@link #LoadShedder(int, double, boolean, String)} to avoid the "(generic)" name in the log.
	 * 
	 * @param maxLimit maximum number of messages in an interval
	 * @param intervalMinutes duration of the interval in minutes (int)
	 * @param warnOnLimit if true, then log a warning message when rate limit is exceeded
	 */
	@Deprecated
	public LoadShedder(final int maxLimit, final int intervalMinutes, final boolean warnOnLimit) {
		this(maxLimit, (double)intervalMinutes, warnOnLimit, "(generic)");
	}
	
	/**
	 * Creates a load shedder that will allow the processing of up to maxLimit items in a period of
	 * intervalMinutes.
	 * 
	 * @param maxLimit maximum number of messages in an interval
	 * @param intervalMinutes duration of the interval in minutes (int)
	 * @param warnOnLimit if true, then log a warning message when rate limit is exceeded
	 * @param name the name of this load shedder, used for logging purposes.
	 */
	public LoadShedder(final int maxLimit, final int intervalMinutes, final boolean warnOnLimit, String name) {
		this(maxLimit, (double)intervalMinutes, warnOnLimit, name);
	}

	/**
	 * Creates a load shedder that will allow the processing of up to maxLimit items in a period of
	 * intervalMinutes.
	 * 
	 * @param intervalMinutes duration of the interval in minutes (double)
	 * @param maxLimit maximum number of items in an interval
	 * @param warnOnLimit if true, then log a warning message when rate limit is exceeded
	 * @param name the name of this load shedder, used for logging purposes.
	 */
	public LoadShedder(final int maxLimit, final double intervalMinutes, final boolean warnOnLimit, String name) {
		this.maxLimit = maxLimit;
		this.intervalMinutes = intervalMinutes;
		this.intervalMillis = (long)(intervalMinutes * 1000.0 * 60.0);
		this.warnOnLimit = warnOnLimit;
		this.name = name;
		
		this.counter = 0;
		this.lastSetTime = 0;
		this.warningEmitted = false;
		
		logger.info("Loadshedder[" + this.name + "] initialized with " + this.maxLimit + " per " + this.intervalMinutes + "min of items");
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
	 * Gets the flag indicating whether we should log a warning if the rate is exceeded
	 * 
	 * @return the flag 
	 */
	public boolean getWarn() {
		return warnOnLimit;
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
	 * Gets the name of this load shedder, a String used for logging purposes
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method is called before processing an item. If the method returns true, the item should
	 * be processed. If the method returns false, the item should not be processed, because we have
	 * exceeded the number of items that can be processed during a certain period.
	 * 
	 * Deprecated: use the {@link #LoadShedder(int, double, boolean, String)} constructor to set the
	 * name of this load shedder, and call {@link #canProcess()} instead.
	 * 
	 * TODO: Remove the "debug" level logging, which is unnecessary.
	 * 
	 * @param channel a collection code, used currently for logging
	 * 
	 * @return true if the current item should be processed, false if the current item should not be
	 *         processed
	 */
	@Deprecated
	public boolean canProcess(String channel) {
		logger.debug("For channel: " + channel + ", counter:maxLimit" + counter + ":" + maxLimit);
		if ((System.currentTimeMillis() - lastSetTime) <= intervalMillis) {
			if (counter < maxLimit) {
				++counter;
				logger.debug("For channel: " + channel + ", returning true");
				return true; // within bounds
			}

			// Otherwise, reset and return false
			if (warnOnLimit && !warningEmitted) {
				warningEmitted = true; // warn only once per interval
				logger.warn("Limit of " + maxLimit + " messages per " + intervalMinutes + " mins reached with current count = " + counter);
			}
			logger.debug("For channel: " + channel + ", returning false");
			return false; // wait until end of interval before resetting
		}
		// Interval over - now reset for next interval
		logger.debug("For channel: " + channel + ", returning true as interval is over");
		counter = 0;
		lastSetTime = System.currentTimeMillis();
		warningEmitted = false;
		return true;
	}
	
	/**
	 * This method is called before processing an item. If the method returns true, the item should
	 * be processed. If the method returns false, the item should not be processed, because we have
	 * exceeded the number of items that can be processed during a certain period.
	 * 
	 * @return true if the current item should be processed, false if the current item should not be
	 *         processed
	 */
	public boolean canProcess() {
		long currentTimeMillis = System.currentTimeMillis();
		double deltaMillis = currentTimeMillis - lastSetTime;

		if ( (deltaMillis <= intervalMillis) || ( lastSetTime == 0 ) ) {
			
			counter++;
		
			if (counter <= maxLimit) {

				// Within limits
				lastSetTime = currentTimeMillis;
				
				return true;
				
			} else {

				// Outside limit, check if we need to emit a warning (only once per interval)
				if (warnOnLimit && !warningEmitted) {
					warningEmitted = true;
					logger.warn("LoadShedder[" + name + "] reached limit of " + maxLimit + " messages per " + intervalMinutes + " mins reached with current count = " + counter);
				}
				
				return false;
			}
			
		} else {
			
			// Interval over - now reset for next interval
			counter = 1;
			lastSetTime = currentTimeMillis;
			warningEmitted = false;
			return true;
		}
	}
}