package qa.qcri.aidr.utils;



public enum DownloadJsonType {
	JSON_OBJECT("JSON", ".json"),
	TEXT_JSON("TEXT_JSON", ".txt");

	private final String value;
	private final String suffix;

	//private static Logger logger = Logger.getLogger(DownloadJsonType.class.getName());

	public String getSuffix() {
		return this.suffix;
	}

	public static String getSuffixString(DownloadJsonType jsonType) {
		for (DownloadJsonType d: DownloadJsonType.values()) {
			if (jsonType.equals(d)) {
				//logger.info("match found: " + d.suffix);
				return d.suffix;
			}
		}
		return null;
	}

	DownloadJsonType(String value, String suffix) {
		this.value = value;
		this.suffix = suffix;
	}

	public String getValue() {
		return this.value;
	}

	private static String trimString(final String str) {
		if (str.startsWith("\"")) {
			//System.out.println("split : " + str.split("\"")[1]);
			return str.split("\"")[1];
		}
		return str;
	}
	public static DownloadJsonType getDownloadJsonTypeFromString(final String str) {
		String trimmedString = trimString(str);
		for (DownloadJsonType d: DownloadJsonType.values()) {
			//System.out.println("comparing: " + d.value + " with " + trimmedString + " : " + trimmedString.equalsIgnoreCase(d.value));
			if (trimmedString.equalsIgnoreCase(d.value)) {
				//logger.info("match found: " + d);
				return d;
			}
		}
		return null;
	}

	public static DownloadJsonType getDownloadJsonTypeFromSuffix(final String str) {
		String trimmedString = trimString(str);
		for (DownloadJsonType d: DownloadJsonType.values()) {
			if (trimmedString.equalsIgnoreCase(d.suffix)) {
				//logger.info("match found: " + d);
				return d;
			}
		}
		return null;
	}

	public static String defaultSuffix() {
		return DownloadJsonType.TEXT_JSON.suffix;
	}

	/*public static void main(String args[]) throws Exception {
		DownloadJsonType d = DownloadJsonType.TEXT_JSON;
		System.out.println("suffix: " + DownloadJsonType.getSuffixString(d));
		System.out.println("testing: " + DownloadJsonType.getDownloadJsonTypeFromString("TEXT_JSON"));
	}*/
}
