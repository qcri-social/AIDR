package qa.qcri.aidr.analysis.utils;

public class MiscUtilities {
	
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
