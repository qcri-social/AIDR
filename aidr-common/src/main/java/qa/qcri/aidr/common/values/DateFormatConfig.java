package qa.qcri.aidr.common.values;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;

public class DateFormatConfig {
	private static Logger logger = Logger.getLogger(DateFormatConfig.class);
	private static ErrorLog elog = new ErrorLog();

	public static final String ISODateFormat = "yyyy-MM-dd'T'HH:mm'Z'";
	public static final String StandardDateFormat = "EEE MMM dd HH:mm:ss ZZZ yyyy";

	/**
	 * 
	 * @param oldFormat current date format being used
	 * @param newFormat the date format to convert to 
	 * @param timeString date as a string, conforming to oldFormat
	 * @return a Hashmap containing <key="date", value = Date() object> and, <key="dateString", value = date as a string, conforming to newFormat>
	 */
	public static Map<String, Object> convertBetweenDateFormats(final String oldFormat, final String newFormat, final String timeString) throws ParseException {
		Map<String, Object> convertedDate = new HashMap<String, Object>(2);

		DateFormat newDateFormat = new SimpleDateFormat(newFormat);
		newDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		if (timeString != null) {
			SimpleDateFormat oldDateFormat = new SimpleDateFormat(oldFormat);
			Date newDate = oldDateFormat.parse(timeString);
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

	public static Date getDateFromString(final String dateFormat, final String timeString) throws ParseException {
		if (timeString != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			return simpleDateFormat.parse(timeString);
		}
		return null;
	}
	
	public static String getStringFromDate(final String dateFormat, final Date date) throws ParseException {
		if (date != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			return simpleDateFormat.format(date);
		}
		return null;
	}
}
