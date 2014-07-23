package qa.qcri.aidr.getdata;

public class Config {
	
	public final String host = "localhost";
	public final int port = 6379;
	
	public final String channelPrefix = "aidr_predict.";
	public final String collectionCode = "2014-03-mh370";	//2014-07-palestine_conflict";
	public final int tweets_per_sec = -1;		// -1 = no throttle control
	public final int duration = 10;				// in mins
	
	public static final String DEFAULT_PERSISTER_FILE_PATH = "/var/www/aidr/data/persister/";	

}
