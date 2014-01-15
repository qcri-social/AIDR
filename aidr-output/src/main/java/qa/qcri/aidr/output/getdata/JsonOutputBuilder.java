package qa.qcri.aidr.output.getdata;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;




//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonOutputBuilder {
	private static Logger logger = LoggerFactory.getLogger(JsonOutputBuilder.class);
	
	public JsonOutputBuilder() {
		//BasicConfigurator.configure();		// configuration for log4j logging
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
		JsonArray nominalLabels = aidrData.get("nominal_labels").getAsJsonArray();
		
		JsonReturnClass jsonObj = new JsonReturnClass();
		jsonObj.text.append(tweetData.getAsString());
		jsonObj.aidr.crisis_code.append(crisisCode);
		jsonObj.aidr.crisis_name.append(crisisName);
		
		for (int itr = 0;itr < nominalLabels.size();itr++) {
			logger.debug("nominal label " + itr + ": " + nominalLabels.get(itr));
			jsonObj.aidr.nominal_labels.add(nominalLabels.get(itr));
		}
		
		logger.debug("[JsonOutputBuilder] text : " + jsonObj.text);
		logger.debug("[JsonOutputBuilder] crisis code: " + jsonObj.aidr.crisis_code);
		logger.debug("[JsonOutputBuilder] crisis name: " + jsonObj.aidr.crisis_name);
		logger.debug("[JsonOutputBuilder] nominal labels: " + jsonObj.aidr.nominal_labels);
		
		return jsonObject.toJson(jsonObj);			// return created jsonObject
	}
	
	@SuppressWarnings("unused")
	private boolean isemptyNominalLabels(JsonArray nominalLabels) {
		if (nominalLabels.size() == 0) 
			return true;
		else
			return false;
	}
	
	// cuurently unused - may be required in future
	@SuppressWarnings("unused")
	private class NominalLabelsData {
		private StringBuilder label_name;
		private int source_id;
		private boolean from_human;
		private StringBuilder attribute_description;
		private StringBuilder label_code;
		private float confidence;
		private StringBuilder label_description;
		private StringBuilder attribute_code;
		private StringBuilder attribute_name;
		
		public NominalLabelsData() {
			attribute_description = new StringBuilder();
			label_code = new StringBuilder();;
			label_description = new StringBuilder();
			attribute_code = new StringBuilder();
			attribute_name = new StringBuilder();
		}
	}
	
	// Json output data format
	private class AIDRData {
		private StringBuilder crisis_code;
		private StringBuilder crisis_name;
		private List<JsonElement> nominal_labels;
		
		public AIDRData() {
			crisis_code = new StringBuilder();
			crisis_name = new StringBuilder();
			nominal_labels = new ArrayList<JsonElement>();
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
	
	public static void main(String args[]) {
		String testString1 = "{\"filter_level\":\"medium\",\"text\":\"@wongt0n You in Japan now?\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"wongt0n\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"id\":123834194,\"indices\":[0,8],\"screen_name\":\"wongt0n\",\"id_str\":\"123834194\",\"name\":\"Lazy Wong~\"}]},\"lang\":\"en\",\"in_reply_to_status_id_str\":\"421190875260014592\",\"id\":421193072316149761,\"aidr\":{\"features\":[{\"words\":[\"\",\"japan_now\",\"japan\",\"now\",\"_you\",\"you\",\"in_japan\",\"in\",\"you_in\"],\"type\":\"wordvector\"}],\"crisis_code\":\"japan_chem_explosion\",\"nominal_labels\":[],\"doctype\":\"twitter\",\"crisis_name\":\"Explosion at Japan chemical factory\"},\"source\":\"web\",\"in_reply_to_user_id_str\":\"123834194\",\"favorited\":false,\"in_reply_to_status_id\":421190875260014592,\"retweet_count\":0,\"created_at\":\"Thu Jan 09 08:13:48 +0000 2014\",\"in_reply_to_user_id\":123834194,\"favorite_count\":0,\"id_str\":\"421193072316149761\",\"place\":null,\"user\":{\"location\":\"\",\"default_profile\":false,\"profile_background_tile\":true,\"statuses_count\":10045,\"lang\":\"en\",\"profile_link_color\":\"1212E3\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/457120810/1358321901\",\"id\":457120810,\"following\":null,\"protected\":false,\"favourites_count\":7,\"profile_text_color\":\"E60ED4\",\"contributors_enabled\":false,\"verified\":false,\"description\":\"An ordinary SONE who fall in love with Taeyeon+Jessica+Sunny+Tiffany+Hyoyeon+Yuri+Sooyoung+Yoona+Seohyun = SNSD. Spazzing and sharing is my vacation on twitter.\",\"name\":\"~~\uC18C\uC2DC\uB77C\uC11C \uD589\uBCF5\uD574\uC694~~\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_background_color\":\"EDFAFA\",\"created_at\":\"Sat Jan 07 01:51:54 +0000 2012\",\"default_profile_image\":false,\"followers_count\":47,\"geo_enabled\":false,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"profile_background_image_url\":\"http://a0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"profile_background_image_url_https\":\"https://si0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":28800,\"time_zone\":\"Kuala Lumpur\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":127,\"profile_sidebar_fill_color\":\"E1D2F5\",\"screen_name\":\"blueagle90\",\"id_str\":\"457120810\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"listed_count\":0,\"is_translator\":false},\"coordinates\":null}";
		String testString2 = "{\"filter_level\":\"medium\",\"text\":\"@wongt0n You in Japan now?\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"wongt0n\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"id\":123834194,\"indices\":[0,8],\"screen_name\":\"wongt0n\",\"id_str\":\"123834194\",\"name\":\"Lazy Wong~\"}]},\"lang\":\"en\",\"in_reply_to_status_id_str\":\"421190875260014592\",\"id\":421193072316149761,\"aidr\":{\"features\":[{\"words\":[\"\",\"2009\",\"edition\",\"_#mp3\",\"#mp3_#music\",\"408_the\",\"the_end\",\"peas_2009\",\"#mp3\",\"the\",\"edition_black\",\"end_japan\",\"eyed_peas\",\"#music_408\",\"408\",\"japan\",\"japan_edition\",\"black_eyed\",\"#music\",\"eyed\",\"end\",\"black\",\"peas\"],\"type\":\"wordvector\"}],\"crisis_code\":\"japan_chem_explosion\",\"nominal_labels\":[{\"label_name\":\"Not related to crisis\",\"source_id\":289,\"from_human\":false,\"attribute_description\":\"Informative messages enhancing situational awareness, v1.0\",\"label_code\":\"030_not_info\",\"confidence\":0.54,\"label_description\":\"Not related to the crisis\",\"attribute_code\":\"informative_v1\",\"attribute_name\":\"Informative v1.0\"}],\"doctype\":\"twitter\",\"crisis_name\":\"Explosion at Japan chemical factory\"},\"source\":\"web\",\"in_reply_to_user_id_str\":\"123834194\",\"favorited\":false,\"in_reply_to_status_id\":421190875260014592,\"retweet_count\":0,\"created_at\":\"Thu Jan 09 08:13:48 +0000 2014\",\"in_reply_to_user_id\":123834194,\"favorite_count\":0,\"id_str\":\"421193072316149761\",\"place\":null,\"user\":{\"location\":\"\",\"default_profile\":false,\"profile_background_tile\":true,\"statuses_count\":10045,\"lang\":\"en\",\"profile_link_color\":\"1212E3\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/457120810/1358321901\",\"id\":457120810,\"following\":null,\"protected\":false,\"favourites_count\":7,\"profile_text_color\":\"E60ED4\",\"contributors_enabled\":false,\"verified\":false,\"description\":\"An ordinary SONE who fall in love with Taeyeon+Jessica+Sunny+Tiffany+Hyoyeon+Yuri+Sooyoung+Yoona+Seohyun = SNSD. Spazzing and sharing is my vacation on twitter.\",\"name\":\"~~\uC18C\uC2DC\uB77C\uC11C \uD589\uBCF5\uD574\uC694~~\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_background_color\":\"EDFAFA\",\"created_at\":\"Sat Jan 07 01:51:54 +0000 2012\",\"default_profile_image\":false,\"followers_count\":47,\"geo_enabled\":false,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"profile_background_image_url\":\"http://a0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"profile_background_image_url_https\":\"https://si0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":28800,\"time_zone\":\"Kuala Lumpur\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":127,\"profile_sidebar_fill_color\":\"E1D2F5\",\"screen_name\":\"blueagle90\",\"id_str\":\"457120810\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"listed_count\":0,\"is_translator\":false},\"coordinates\":null}";
		String testString3 = "{\"filter_level\":\"medium\",\"text\":\"@wongt0n You in Japan now?\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"wongt0n\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"id\":123834194,\"indices\":[0,8],\"screen_name\":\"wongt0n\",\"id_str\":\"123834194\",\"name\":\"Lazy Wong~\"}]},\"lang\":\"en\",\"in_reply_to_status_id_str\":\"421190875260014592\",\"id\":421193072316149761,\"aidr\":{\"features\":[{\"words\":[\"\",\"2009\",\"edition\",\"_#mp3\",\"#mp3_#music\",\"408_the\",\"the_end\",\"peas_2009\",\"#mp3\",\"the\",\"edition_black\",\"end_japan\",\"eyed_peas\",\"#music_408\",\"408\",\"japan\",\"japan_edition\",\"black_eyed\",\"#music\",\"eyed\",\"end\",\"black\",\"peas\"],\"type\":\"wordvector\"}],\"crisis_code\":\"japan_chem_explosion\",\"nominal_labels\":[{\"label_name\":\"Not related to crisis\",\"source_id\":289,\"from_human\":false,\"attribute_description\":\"Informative messages enhancing situational awareness, v1.0\",\"label_code\":\"030_not_info\",\"confidence\":0.54,\"label_description\":\"Not related to the crisis\",\"attribute_code\":\"informative_v1\",\"attribute_name\":\"Informative v1.0\"}, {\"label_name\":\"Not related to crisis\",\"source_id\":289,\"from_human\":false,\"attribute_description\":\"Informative messages enhancing situational awareness, v1.0\",\"label_code\":\"030_not_info\",\"confidence\":0.84,\"label_description\":\"Not related to the crisis\",\"attribute_code\":\"informative_v1\",\"attribute_name\":\"Informative v1.0\"}, {\"label_name\":\"Not related to crisis\",\"source_id\":289,\"from_human\":false,\"attribute_description\":\"Informative messages enhancing situational awareness, v1.0\",\"label_code\":\"030_not_info\",\"confidence\":0.20,\"label_description\":\"related to the crisis\",\"attribute_code\":\"informative_v1\",\"attribute_name\":\"Informative v1.0\"}],\"doctype\":\"twitter\",\"crisis_name\":\"Explosion at Japan chemical factory\"},\"source\":\"web\",\"in_reply_to_user_id_str\":\"123834194\",\"favorited\":false,\"in_reply_to_status_id\":421190875260014592,\"retweet_count\":0,\"created_at\":\"Thu Jan 09 08:13:48 +0000 2014\",\"in_reply_to_user_id\":123834194,\"favorite_count\":0,\"id_str\":\"421193072316149761\",\"place\":null,\"user\":{\"location\":\"\",\"default_profile\":false,\"profile_background_tile\":true,\"statuses_count\":10045,\"lang\":\"en\",\"profile_link_color\":\"1212E3\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/457120810/1358321901\",\"id\":457120810,\"following\":null,\"protected\":false,\"favourites_count\":7,\"profile_text_color\":\"E60ED4\",\"contributors_enabled\":false,\"verified\":false,\"description\":\"An ordinary SONE who fall in love with Taeyeon+Jessica+Sunny+Tiffany+Hyoyeon+Yuri+Sooyoung+Yoona+Seohyun = SNSD. Spazzing and sharing is my vacation on twitter.\",\"name\":\"~~\uC18C\uC2DC\uB77C\uC11C \uD589\uBCF5\uD574\uC694~~\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_background_color\":\"EDFAFA\",\"created_at\":\"Sat Jan 07 01:51:54 +0000 2012\",\"default_profile_image\":false,\"followers_count\":47,\"geo_enabled\":false,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"profile_background_image_url\":\"http://a0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"profile_background_image_url_https\":\"https://si0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":28800,\"time_zone\":\"Kuala Lumpur\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":127,\"profile_sidebar_fill_color\":\"E1D2F5\",\"screen_name\":\"blueagle90\",\"id_str\":\"457120810\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"listed_count\":0,\"is_translator\":false},\"coordinates\":null}";
		
		System.out.println("JsonOutputBuilder:");
		JsonOutputBuilder obj = new JsonOutputBuilder();
		String testout1 = obj.buildJsonString(testString1);
		System.out.println("testString 1: " + testout1);
		
		String testout2 = obj.buildJsonString(testString2);
		System.out.println("testString 2: " + testout2);
		
		String testout3 = obj.buildJsonString(testString3);
		System.out.println("testString 3: " + testout3);
	}
}