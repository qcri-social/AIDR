package qa.qcri.aidr.getdata;

import java.util.Arrays;
import java.util.List;

public class Config {
	
	public final String host = "localhost";
	public final int port = 6379;
	
	public final String channelPrefix = "aidr_predict.";
	public final String collectionCode = "2014-03-mh370";	//2014-07-palestine_conflict";
	public final int tweets_per_sec = -1;		// -1 = no throttle control
	public final int duration = 2;				// in mins
	public final int threads = 5;
	
	public final List<String>collection_list = Arrays.asList("2014-03-mh370", "2014-04-mers", "2014-05-emsc_landslides_2014", "2014-04-terremoto_chile_2014", "2014-05-serbia_bosnia_floods_may_2014");
	public static final String DEFAULT_PERSISTER_FILE_PATH = "/var/www/aidr/data/persister/";	

}
