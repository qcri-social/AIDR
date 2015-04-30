package qa.qcri.aidr.common.values;

/**
 * Groups various types of downloads that can be generated.
 *
 */
public class DownloadType {
	/**
	 * A downloadable file containing a single JSON object.
	 */
	public static final String JSON_OBJECT = "JSON";
	
	/**
	 * A downloadable file containing one JSON object per line.
	 */
	public static final String TEXT_JSON = "TEXT_JSON";
	
	/**
	 * A downloadable file in comma-separated-value format.
	 */
	public static final String CSV = "CSV";
	
	/**
	 * A downloadable file containing full tweets.
	 */
	public static final String FULL_TWEETS = "full";
	
	/**
	 * A downloadable file containing only user-ids and tweet-ids. 
	 */
	public static final String TWEET_IDS = "id";
}
