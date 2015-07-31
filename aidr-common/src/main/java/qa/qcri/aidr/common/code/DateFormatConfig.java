package qa.qcri.aidr.common.code;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * Several operations related to parsing and formatting dates.
 * 
 * TODO: REMOVE UNNECESSARY CODE. The fields {@link #logger} and {@link #elog} are not used.
 *
 */
public class DateFormatConfig {
	private static Logger logger = Logger.getLogger(DateFormatConfig.class);

	public static final String ISODateFormat = "yyyy-MM-dd'T'HH:mm'Z'";
	public static final String StandardDateFormat = "EEE MMM dd HH:mm:ss ZZZ yyyy";

	/**
	 * Converts data from one format to another format.
	 * 
	 * @param oldFormat current date format being used
	 * @param newFormat the date format to convert to
	 * @param timeString date as a string, conforming to oldFormat
	 * @return a Hashmap containing <key="date", value = Date() object> and,
	 *         <key="dateString", value = date as a string, conforming to newFormat>
	 * @throws ParseException 
	 */
	public static Map<String, Object> convertBetweenDateFormats(final String oldFormat, final String newFormat, final String timeString) throws ParseException {
		Map<String, Object> convertedDate = new HashMap<String, Object>(2);

		DateFormat newDateFormat = new SimpleDateFormat(newFormat);
		newDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		if (timeString != null) {
			SimpleDateFormat oldDateFormat = new SimpleDateFormat(oldFormat);
			Date newDate = null;
			try {
				newDate = oldDateFormat.parse(timeString);
			} catch (ParseException e) {
				logger.error("Exception while parsing the date:"+timeString);
				throw e;
			}
			if (newDate != null) {
				convertedDate.put("date", newDate);
				convertedDate.put("dateString", newDateFormat.format(newDate));
				return convertedDate;
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * Parses a string according to a date format.
	 * 
	 * TODO: RENAME. This should be named parseDate()
	 * 
	 * TODO: REMOVE UNNECESSARY CODE. The code never throws a ParseException, so the
	 * "throws ParseException" is unnecessary.
	 * 
	 * @param dateFormat the string representing the date format
	 * @param timeString the string representing the date to be parsed
	 * @return a Date object containing the date
	 * @throws ParseException
	 */
	public static Date getDateFromString(final String dateFormat, final String timeString) throws ParseException  {
		if (timeString != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			try {
				return simpleDateFormat.parse(timeString);
			} catch (ParseException e) {
				logger.error("Exception while parsing the date: "+timeString);
				throw e;
			}
		}
		return null;
	}
	
	/**
	 * Formats a date using a format expressed as a string.
	 *
	 * TODO: RENAME. This should be renamed to formatDate()
	 * 
	 * TODO: REMOVE UNNECESSARY CODE. The code never throws a ParseException, so the
	 * "throws ParseException" is unnecessary.
	 * 
	 * @param dateFormat a string representing the date format
	 * @param date the date to format
	 * @return the date formated using the specified format
	 * @throws ParseException
	 */
	public static String getStringFromDate(final String dateFormat, final Date date) throws ParseException {
		if (date != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			return simpleDateFormat.format(date);
		}
		return null;
	}
	
	/**
	 * Parses a string representing a time duration in seconds, minutes, hours, or days.
	 * 
	 * The input should be a number with a suffix from "s", "m", "d", "h", presenting respectively
	 * seconds, minutes, days, and hours. For instance, "3d" means 3 days.
	 * 
	 * TODO: RENAME. This should be renamed to parseSuffixedTimeDurationToSeconds()
	 * 
	 * TODO: ERROR HANDLING. this should throw a parseException if the suffix is not recognized, or
	 * if the substring[0,length-1] is not a number.
	 * 
	 * @param timeString the suffixed time string, with a single-letter suffix that can be "s", "m",
	 *            "h", or "d".
	 * 
	 * @return the time duration in seconds
	 */
	public static long parseTime(String timeString) {
		long duration = 0;
	
		float value = Float.parseFloat(timeString.substring(0, timeString.length()-1));
		if (value > 0) {
			String suffix = timeString.substring(timeString.length() - 1, timeString.length());
			if (suffix.equalsIgnoreCase("s"))
				duration = Math.round(value * 1000);
			if (suffix.equalsIgnoreCase("m"))
				duration = Math.round(value * 1000 * 60);
			if (suffix.equalsIgnoreCase("h"))
				duration = Math.round(value * 1000 * 60 * 60);
			if (suffix.equalsIgnoreCase("d"))
				duration = Math.round(value * 1000 * 60 * 60 * 24);
		}
		return duration;
	}
}
