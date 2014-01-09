package qa.qcri.aidr.output.getdata;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonOutputBuilder {
	private static Logger logger = LoggerFactory.getLogger(JsonOutputBuilder.class);
	
	public JsonOutputBuilder() {
		//BasicConfigurator.configure();
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j
	}
	
	public String buildJsonString(String rawJsonString) {
		Gson jsonObject = new GsonBuilder().serializeNulls()			//.disableHtmlEscaping()
				.serializeSpecialFloatingPointValues().setPrettyPrinting()
				.create();
		
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(rawJsonString);
		
		JsonElement tweetData = (JsonElement) obj.get("text");		// get the tweet text string
		JsonObject aidrData = (JsonObject) obj.get("aidr");			// get the aidr JSON object
			
		// extract relevant fields from the aidr JSON object
		String crisisCode = aidrData.get("crisis_code").getAsString();
		String crisisName = aidrData.get("crisis_name").getAsString();
		String nominalLabels = aidrData.get("nominal_labels") != null ?
								aidrData.get("nominal_labels").toString() : new String("[{}]");
		
		JsonReturnClass jsonObj = new JsonReturnClass();
		jsonObj.text.append(tweetData.getAsString());
		jsonObj.aidr.crisis_code.append(crisisCode);
		jsonObj.aidr.crisis_name.append(crisisName);
		jsonObj.aidr.nominal_labels.append(nominalLabels);
		
		logger.debug("[JsonOutputBuilder] text : " + jsonObj.text);
		logger.debug("[JsonOutputBuilder] crisis code: " + jsonObj.aidr.crisis_code);
		logger.debug("[JsonOutputBuilder] crisis name: " + jsonObj.aidr.crisis_name);
		logger.debug("[JsonOutputBuilder] nominal labels: " + jsonObj.aidr.nominal_labels);
		
		return jsonObject.toJson(jsonObj);
				
	}
	
	private class AIDRData {
		private StringBuilder crisis_code;
		private StringBuilder crisis_name;
		private StringBuilder nominal_labels;
	
		public AIDRData() {
			crisis_code = new StringBuilder();
			crisis_name = new StringBuilder();
			nominal_labels = new StringBuilder();
		}
	}
	
	private class JsonReturnClass {
		private StringBuilder text;
		private AIDRData aidr;
		
		public JsonReturnClass() {
			text = new StringBuilder();
			aidr = new AIDRData();
		}
	}
	
	public static void main( String[] args ) throws IOException, Exception
	{    
		String testString = "{\"filter_level\":\"medium\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":"
				+ "false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":421242081198501889,\"in_reply_to_"
				+ "user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Thu Jan 09 11:28:33 +0000 2014\",\"favorite_count\":"
				+ "0,\"place\":null,\"coordinates\":null,\"text\":\"RT @Kemety: this is what I call: "
				+ "Full Stop\\n#Chicago http://t.co/y9j0PSA1SE\",\"contributors\":null,\"retweeted_status\":"
				+ "{\"contributors\":null,\"text\":\"this is what I call: Full Stop\\n#Chicago http://t.co/y9j0PSA1SE\",\"geo\":"
				+ "null,\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,"
				+ "\"lang\":\"en\",\"entities\":{\"symbols\":[],\"urls\":[],\"hashtags\":[{\"text\":\"Chicago\",\"indices\":[31,39]}],"
				+ "\"media\":[{\"sizes\":{\"small\":{\"w\":339,\"resize\":\"fit\",\"h\":226},\"thumb\":{\"w\":150,\"resize\":"
				+ "\"crop\",\"h\":150},\"large\":{\"w\":720,\"resize\":\"fit\",\"h\":480},\"medium\":{\"w\":600,\"resize\":\"fit\","
				+ "\"h\":400}},\"id\":421230078387703808,\"media_url_https\":\"https://pbs.twimg.com/media/BdiChkcCUAAwRjQ.jpg\","
				+ "\"media_url\":\"http://pbs.twimg.com/media/BdiChkcCUAAwRjQ.jpg\",\"expanded_url\":"
				+ "\"http://twitter.com/Kemety/status/421230078517714946/photo/1\",\"indices\":[40,62],\"id_str\":\"421230078387703808\""
				+ ",\"type\":\"photo\",\"display_url\":\"pic.twitter.com/y9j0PSA1SE\",\"url\":\"http://t.co/y9j0PSA1SE\"}],"
				+ "\"user_mentions\":[]},\"in_reply_to_status_id_str\":null,\"id\":421230078517714946,\"source\":"
				+ "\"<a href=\\\"https://about.twitter.com/products/tweetdeck\\\" rel=\\\"nofollow\\\">TweetDeck<\\/a>\","
				+ "\"in_reply_to_user_id_str\":null,\"favorited\":false,\"in_reply_to_status_id\":null,\"retweet_count\":46,"
				+ "\"created_at\":\"Thu Jan 09 10:40:51 +0000 2014\",\"in_reply_to_user_id\":null,\"favorite_count\":15,\"id_str\":"
				+ "\"421230078517714946\",\"place\":null,\"user\":{\"location\":\"Cairo, Egypt\",\"default_profile\":"
				+ "true,\"profile_background_tile\":false,\"statuses_count\":90834,\"lang\":\"en\",\"profile_link_color\":"
				+ "\"0084B4\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/150989766/1356606940\",\"id\":"
				+ "150989766,\"following\":null,\"protected\":false,\"favourites_count\":385,\"profile_text_color\":\"333333\","
				+ "\"description\":\"Follow n' you'll find out..... RT ≠ endorsement.\",\"verified\":false,\"contributors_enabled\":"
				+ "false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Alfred Raouf\",\"profile_background_color\":"
				+ "\"C0DEED\",\"created_at\":\"Wed Jun 02 09:53:43 +0000 2010\",\"default_profile_image\":false,\"followers_count\":"
				+ "24775,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000792793779/8f88c7ecbba76057914a50687f973851_normal.png\","
				+ "\"geo_enabled\":true,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\","
				+ "\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,"
				+ "\"url\":\"http://thinkbank.kemety.net\",\"utc_offset\":7200,\"time_zone\":\"Cairo\",\"notifications\":null,"
				+ "\"profile_use_background_image\":true,\"friends_count\":1001,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":"
				+ "\"Kemety\",\"id_str\":\"150989766\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000792793779/8f88c7ecbba76057914a50687f973851_normal.png\","
				+ "\"listed_count\":175,\"is_translator\":false},\"coordinates\":null},\"geo\":null,\"entities\":{\"symbols\":[],\"urls\":[],\"hashtags\":"
				+ "[{\"text\":\"Chicago\",\"indices\":[43,51]}],\"media\":[{\"sizes\":{\"small\":{\"w\":339,\"resize\":\"fit\",\"h\":226},"
				+ "\"thumb\":{\"w\":150,\"resize\":\"crop\",\"h\":150},\"large\":{\"w\":720,\"resize\":\"fit\",\"h\":480},\"medium\":"
				+ "{\"w\":600,\"resize\":\"fit\",\"h\":400}},\"id\":421230078387703808,\"media_url_https\":\"https://pbs.twimg.com/media/BdiChkcCUAAwRjQ.jpg\","
				+ "\"media_url\":\"http://pbs.twimg.com/media/BdiChkcCUAAwRjQ.jpg\",\"expanded_url\":\"http://twitter.com/Kemety/status/421230078517714946/photo/1\",\"indices\":"
				+ "[52,74],\"id_str\":\"421230078387703808\",\"type\":\"photo\",\"display_url\":\"pic.twitter.com/y9j0PSA1SE\",\"url\":"
				+ "\"http://t.co/y9j0PSA1SE\"}],\"user_mentions\":[{\"id\":150989766,\"name\":\"Alfred Raouf\",\"indices\":[3,10],"
				+ "\"screen_name\":\"Kemety\",\"id_str\":\"150989766\"}]},\"source\":\"web\",\"favorited\":false,\"in_reply_to_user_id\":"
				+ "null,\"retweet_count\":0,\"id_str\":\"421242081198501889\",\"user\":{\"location\":\"Madrid\",\"default_profile\":"
				+ "false,\"profile_background_tile\":false,\"statuses_count\":14159,\"lang\":\"es\",\"profile_link_color\":"
				+ "\"0084B4\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/57415020/1363793964\",\"id\":57415020,"
				+ "\"following\":null,\"protected\":false,\"favourites_count\":239,\"profile_text_color\":\"333333\",\"description\":"
				+ "\"Ing.T. Infórmatico. Estudiando máster en Inteligencia Artificial. Cofundador de @Masjaleo_apps . "
				+ "Enganchado a internet...\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":"
				+ "\"A8C7F7\",\"name\":\"Kuu\",\"profile_background_color\":\"022330\",\"created_at\":\"Thu Jul 16 19:01:57 +0000 2009\","
				+ "\"default_profile_image\":false,\"followers_count\":133,\"profile_image_url_"
				+ "https\":\"https://pbs.twimg.com/profile_images/420599172085977088/vsbKPj8c_normal.jpeg\",\"geo_enabled\":false,"
				+ "\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme15/bg.png\","
				+ "\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme15/bg.png\","
				+ "\"follow_request_sent\":null,\"url\":\"http://elrincondekuu.blogspot.com\",\"utc_offset\":3600,"
				+ "\"time_zone\":\"Madrid\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":115,"
				+ "\"profile_sidebar_fill_color\":\"C0DFEC\",\"screen_name\":\"Kuu6\",\"id_str\":\"57415020\",\"profile_image_url\":"
				+ "\"http://pbs.twimg.com/profile_images/420599172085977088/vsbKPj8c_normal.jpeg\",\"listed_count\":14,\"is_translator\":"
				+ "false},\"aidr\":{\"crisis_code\":\"2014-01-us_freeze\",\"crisis_name\":\"US freeze\",\"doctype\":\"twitter\"}}";
		
		String testString2 = "{\"filter_level\":\"medium\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":"
				+ "false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":421242081198501889,\"in_reply_to_"
				+ "user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Thu Jan 09 11:28:33 +0000 2014\",\"favorite_count\":"
				+ "0,\"place\":null,\"coordinates\":null,\"text\":\"RT @Kemety: this is what I call: "
				+ "Full Stop\\n#Chicago http://t.co/y9j0PSA1SE\",\"contributors\":null,\"retweeted_status\":"
				+ "{\"contributors\":null,\"text\":\"this is what I call: Full Stop\\n#Chicago http://t.co/y9j0PSA1SE\",\"geo\":"
				+ "null,\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,"
				+ "\"lang\":\"en\",\"entities\":{\"symbols\":[],\"urls\":[],\"hashtags\":[{\"text\":\"Chicago\",\"indices\":[31,39]}],"
				+ "\"media\":[{\"sizes\":{\"small\":{\"w\":339,\"resize\":\"fit\",\"h\":226},\"thumb\":{\"w\":150,\"resize\":"
				+ "\"crop\",\"h\":150},\"large\":{\"w\":720,\"resize\":\"fit\",\"h\":480},\"medium\":{\"w\":600,\"resize\":\"fit\","
				+ "\"h\":400}},\"id\":421230078387703808,\"media_url_https\":\"https://pbs.twimg.com/media/BdiChkcCUAAwRjQ.jpg\","
				+ "\"media_url\":\"http://pbs.twimg.com/media/BdiChkcCUAAwRjQ.jpg\",\"expanded_url\":"
				+ "\"http://twitter.com/Kemety/status/421230078517714946/photo/1\",\"indices\":[40,62],\"id_str\":\"421230078387703808\""
				+ ",\"type\":\"photo\",\"display_url\":\"pic.twitter.com/y9j0PSA1SE\",\"url\":\"http://t.co/y9j0PSA1SE\"}],"
				+ "\"user_mentions\":[]},\"in_reply_to_status_id_str\":null,\"id\":421230078517714946,\"source\":"
				+ "\"<a href=\\\"https://about.twitter.com/products/tweetdeck\\\" rel=\\\"nofollow\\\">TweetDeck<\\/a>\","
				+ "\"in_reply_to_user_id_str\":null,\"favorited\":false,\"in_reply_to_status_id\":null,\"retweet_count\":46,"
				+ "\"created_at\":\"Thu Jan 09 10:40:51 +0000 2014\",\"in_reply_to_user_id\":null,\"favorite_count\":15,\"id_str\":"
				+ "\"421230078517714946\",\"place\":null,\"user\":{\"location\":\"Cairo, Egypt\",\"default_profile\":"
				+ "true,\"profile_background_tile\":false,\"statuses_count\":90834,\"lang\":\"en\",\"profile_link_color\":"
				+ "\"0084B4\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/150989766/1356606940\",\"id\":"
				+ "150989766,\"following\":null,\"protected\":false,\"favourites_count\":385,\"profile_text_color\":\"333333\","
				+ "\"description\":\"Follow n' you'll find out..... RT ≠ endorsement.\",\"verified\":false,\"contributors_enabled\":"
				+ "false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Alfred Raouf\",\"profile_background_color\":"
				+ "\"C0DEED\",\"created_at\":\"Wed Jun 02 09:53:43 +0000 2010\",\"default_profile_image\":false,\"followers_count\":"
				+ "24775,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000792793779/8f88c7ecbba76057914a50687f973851_normal.png\","
				+ "\"geo_enabled\":true,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\","
				+ "\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,"
				+ "\"url\":\"http://thinkbank.kemety.net\",\"utc_offset\":7200,\"time_zone\":\"Cairo\",\"notifications\":null,"
				+ "\"profile_use_background_image\":true,\"friends_count\":1001,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":"
				+ "\"Kemety\",\"id_str\":\"150989766\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000792793779/8f88c7ecbba76057914a50687f973851_normal.png\","
				+ "\"listed_count\":175,\"is_translator\":false},\"coordinates\":null},\"geo\":null,\"entities\":{\"symbols\":[],\"urls\":[],\"hashtags\":"
				+ "[{\"text\":\"Chicago\",\"indices\":[43,51]}],\"media\":[{\"sizes\":{\"small\":{\"w\":339,\"resize\":\"fit\",\"h\":226},"
				+ "\"thumb\":{\"w\":150,\"resize\":\"crop\",\"h\":150},\"large\":{\"w\":720,\"resize\":\"fit\",\"h\":480},\"medium\":"
				+ "{\"w\":600,\"resize\":\"fit\",\"h\":400}},\"id\":421230078387703808,\"media_url_https\":\"https://pbs.twimg.com/media/BdiChkcCUAAwRjQ.jpg\","
				+ "\"media_url\":\"http://pbs.twimg.com/media/BdiChkcCUAAwRjQ.jpg\",\"expanded_url\":\"http://twitter.com/Kemety/status/421230078517714946/photo/1\",\"indices\":"
				+ "[52,74],\"id_str\":\"421230078387703808\",\"type\":\"photo\",\"display_url\":\"pic.twitter.com/y9j0PSA1SE\",\"url\":"
				+ "\"http://t.co/y9j0PSA1SE\"}],\"user_mentions\":[{\"id\":150989766,\"name\":\"Alfred Raouf\",\"indices\":[3,10],"
				+ "\"screen_name\":\"Kemety\",\"id_str\":\"150989766\"}]},\"source\":\"web\",\"favorited\":false,\"in_reply_to_user_id\":"
				+ "null,\"retweet_count\":0,\"id_str\":\"421242081198501889\",\"user\":{\"location\":\"Madrid\",\"default_profile\":"
				+ "false,\"profile_background_tile\":false,\"statuses_count\":14159,\"lang\":\"es\",\"profile_link_color\":"
				+ "\"0084B4\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/57415020/1363793964\",\"id\":57415020,"
				+ "\"following\":null,\"protected\":false,\"favourites_count\":239,\"profile_text_color\":\"333333\",\"description\":"
				+ "\"Ing.T. Infórmatico. Estudiando máster en Inteligencia Artificial. Cofundador de @Masjaleo_apps . "
				+ "Enganchado a internet...\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":"
				+ "\"A8C7F7\",\"name\":\"Kuu\",\"profile_background_color\":\"022330\",\"created_at\":\"Thu Jul 16 19:01:57 +0000 2009\","
				+ "\"default_profile_image\":false,\"followers_count\":133,\"profile_image_url_"
				+ "https\":\"https://pbs.twimg.com/profile_images/420599172085977088/vsbKPj8c_normal.jpeg\",\"geo_enabled\":false,"
				+ "\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme15/bg.png\","
				+ "\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme15/bg.png\","
				+ "\"follow_request_sent\":null,\"url\":\"http://elrincondekuu.blogspot.com\",\"utc_offset\":3600,"
				+ "\"time_zone\":\"Madrid\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":115,"
				+ "\"profile_sidebar_fill_color\":\"C0DFEC\",\"screen_name\":\"Kuu6\",\"id_str\":\"57415020\",\"profile_image_url\":"
				+ "\"http://pbs.twimg.com/profile_images/420599172085977088/vsbKPj8c_normal.jpeg\",\"listed_count\":14,\"is_translator\":"
				+ "false},\"aidr\":{\"crisis_code\":\"2014-01-us_freeze\",\"nominal_labels\":"
				+ "[{\"label_name\":\"Does not apply\",\"source_id\":289,\"from_human\":false,\"attribute_description\":"
				+ "\"Informative messages enhancing situational awareness, v1.0\",\"label_code\":\"null\","
				+ "\"confidence\":0.5266666666666666,\"label_description\":\"The label does not apply\",\"attribute_code\":"
				+ "\"informative_v1\",\"attribute_name\":\"Informative v1.0\"}],\"crisis_name\":\"US freeze\",\"doctype\":\"twitter\"}}";
		
		System.out.println("sample json1:");
		System.out.println(testString);
		JsonOutputBuilder jsonOutput = new JsonOutputBuilder();
		System.out.println(jsonOutput.buildJsonString(testString));
		
		System.out.println("sample json2:");
		System.out.println(testString2);
		JsonOutputBuilder jsonOutput2 = new JsonOutputBuilder();
		System.out.println(jsonOutput2.buildJsonString(testString2));
	}	
	
}
