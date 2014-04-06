package qa.qcri.aidr.persister.filter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.gson.Gson;

import qa.qcri.aidr.persister.filter.QueryType;
import qa.qcri.aidr.persister.filter.ClassifierQueryJsonObject;
import qa.qcri.aidr.persister.filter.DateQueryJsonObject;
import qa.qcri.aidr.utils.ClassifiedTweet;
import qa.qcri.aidr.utils.JsonDeserializer;
import qa.qcri.aidr.persister.filter.NominalLabel;

public class FilterQueryMatcher {

	public JsonQueryList queryList;
	public int next;

	public ArrayList<QueryJsonObject> matcherArray;

	public FilterQueryMatcher() {
		queryList = new JsonQueryList();
		next = 0;
		matcherArray = new ArrayList<QueryJsonObject>();

	}

	public boolean isDateQuery(String queryString) {
		ObjectMapper mapper = new ObjectMapper();

		if (queryString.indexOf(QueryType.date_query.toString()) > -1) {
			try {
				DateQueryJsonObject dateQuery = mapper.readValue(queryString, DateQueryJsonObject.class);
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean isClassifierQuery(String queryString) {
		ObjectMapper mapper = new ObjectMapper();

		if (queryString.indexOf(QueryType.classifier_query.toString()) > -1) {
			try {
				ClassifierQueryJsonObject classiferQuery = mapper.readValue(queryString, ClassifierQueryJsonObject.class);
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean getMatcherResult(ClassifiedTweet tweet) {
		if (matcherArray != null) {
			int i = 0;
			for (QueryJsonObject q: matcherArray) {
				//System.out.println("Attempting matching with query predicate #" + i);
				++i;
				boolean matchResult = isQueryMatch(q, tweet);
				//System.out.println("Result of matching query with tweet = " + matchResult);
				if (!matchResult) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isQueryMatch(QueryJsonObject q, ClassifiedTweet tweet) {
		if (q instanceof DateQueryJsonObject) {
			// check DateQueryJsonObject fields
			if (q.getComparator().equals(ComparatorType.is_after)) {
				boolean matchResult = tweet.getDate(tweet.getCreatedAt()).after(q.getDate());
				//System.out.println("For " + tweet.getCreatedAt() + " comparing after date " + q.getDate().toString() + " : " + matchResult);
				return matchResult;
			}
			if (q.getComparator().equals(ComparatorType.is_before)) {
				boolean matchResult = tweet.getDate(tweet.getCreatedAt()).before(q.getDate());
				//System.out.println("For " + tweet.getCreatedAt().toString() + " comparing before date" + q.getDate().toString() + " : " + matchResult);
				return matchResult;
			}
		}

		if (q instanceof ClassifierQueryJsonObject) {
			// check ClassifierQueryJsonObject fields
			boolean matchResult = false;
			int i = 0;
			for (NominalLabel nLabel: tweet.getNominalLabels()) {
				//System.out.println("Going for matching nLabel#" + i); 
				++i;
				if (q.getClassifierCode() != null && 
					q.getClassifierCode().equalsIgnoreCase(nLabel.attibute_code)) {
					// classifier code matches, next match comparator
					
					// First check confidence parameter
					if (q.getComparator().equals(ComparatorType.has_confidence)) {
						matchResult = nLabel.confidence >= q.getConfidence();
						//System.out.println("comparing confidence: " + matchResult);
						if (matchResult) break;
						continue;	// else go for next nLabel
					}
					
					// Next check label comparator "is"
					if (q.getComparator().equals(ComparatorType.is) && q.getLabelCode() != null) {
						matchResult = q.getLabelCode().equalsIgnoreCase(nLabel.label_code);
						// Now check confidence value
						matchResult = matchResult && (nLabel.confidence >= q.getConfidence());
						//System.out.println("comparing tweet label '" + nLabel.label_code + "' with 'is': " + matchResult);
						if (matchResult) break;
						continue;		// else go for next nLabel
					}
					
					// Next check label comparator "is_not"
					if (q.getComparator().equals(ComparatorType.is_not) && q.getLabelCode() != null) {
						matchResult = q.getLabelCode().equalsIgnoreCase(nLabel.label_code);
						// Now check confidence value
						matchResult = (!matchResult) && (nLabel.confidence >= q.getConfidence());
						//System.out.println("comparing tweet label '" + nLabel.label_code + "' with 'is_not': " + matchResult);
						if (matchResult) break;
						continue;		// else go for next nLabel
					}
				}
			}
			return matchResult;
		}
		return false;			// should never come here
	}

	public QueryJsonObject serializeQuery(String queryString) {
		QueryJsonObject queryObject = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		mapper.setDateFormat(df);

		if (isDateQuery(queryString)) {
			queryObject = new DateQueryJsonObject();
			queryObject.setQueryType(QueryType.date_query);

			try {
				queryObject = mapper.readValue(queryString, DateQueryJsonObject.class);
				System.out.println("[serializeQuery] DateQueryObject: " + queryObject.toString());
			} catch (JsonParseException e) {
				System.err.println("[serializeQuery] for DateQueryJsonObject attempt");
			} catch (JsonMappingException e) {
				System.err.println("[serializeQuery] for DateQueryJsonObject attempt");
			} catch (IOException e) {
				System.err.println("[serializeQuery] for DateQueryJsonObject attempt");
				e.printStackTrace();
			}
		} else if (isClassifierQuery(queryString)) {
			queryObject = new ClassifierQueryJsonObject();
			queryObject.setQueryType(QueryType.classifier_query);

			try {
				queryObject = mapper.readValue(queryString, ClassifierQueryJsonObject.class);
				System.out.println("[serializeQuery] ClassifierQueryObject: " + queryObject.toString());
			} catch (JsonParseException e) {
				System.err.println("[serializeQuery] for ClassifierQueryJsonObject attempt");
			} catch (JsonMappingException e) {
				System.err.println("[serializeQuery] for ClassifierQueryJsonObject attempt");
			} catch (IOException e) {
				System.err.println("[serializeQuery] for ClassifierQueryJsonObject attempt");
				e.printStackTrace();
			}
		}
		return queryObject;
	}

	public String getNextQueryObject() {
		Gson gson = new Gson();
		if (queryList.getConstraints() != null && next < queryList.getConstraints().size()) {
			String retValue = gson.toJson(queryList.getConstraints().get(next), QueryJsonObject.class);
			++next;
			return retValue;
		}
		return null;
	}
	
	public void buildMatcherArray() {
		//System.out.println("[buildMatcherArray] Attempting to build the Matcher Array from input query");
		String query = null;
		while ((query = getNextQueryObject()) != null) {
			System.out.println("[buildMatcherArray] Added query to matcher array: " + query);
			matcherArray.add(serializeQuery(query));
		}
	}

	public void buildMatcherArray(JsonQueryList qList) {
		Gson gson = new Gson();
		for (QueryJsonObject query: qList.getConstraints()) {
			String queryString = gson.toJson(query, QueryJsonObject.class);
			System.out.println("[buildMatcherArray] Added query to matcher array: " + queryString);
			matcherArray.add(serializeQuery(queryString));
		}
	}
	
	public static void main(String args[]) {
		System.out.println("In main - testing code");

		ArrayList<String> temp = new ArrayList<String>();
		temp.add("{\"queryType\":\"date_query\",\"comparator\":\"is_before\",\"time\":\"2014-03-04\"}");
		temp.add("{\"queryType\":\"date_query\",\"comparator\":\"is_after\",\"time\":\"2013-05-01\"}");
		temp.add("{\"queryType\":\"classifier_query\",\"classifier_code\":\"informative_v1\","
				+ "\"label_code\":\"030_not_info\","  
				+ "\"comparator\":\"is\","
				+ "\"min_confidence\":0.5}");
		temp.add("{\"queryType\":\"classifier_query\",\"classifier_code\":\"informative_v1\","
				+ "\"label_code\":\"030_info\","  
				+ "\"comparator\":\"is_not\","
				+ "\"min_confidence\":0.64}");
		temp.add("{\"queryType\":\"classifier_query\",\"classifier_code\":\"informative_v1\","
				+ "\"label_code\":\"null\","  
				+ "\"comparator\":\"has_confidence\","
				+ "\"min_confidence\":0.75}");
		
		FilterQueryMatcher test = new FilterQueryMatcher();
		Gson gson = new Gson();
		for (int i = 0; i < temp.size();i++) {
			test.queryList.createConstraint(gson.fromJson(temp.get(i), GenericInputQuery.class));
		}
		test.buildMatcherArray();
		
		System.out.println("matcherArray size = " + test.matcherArray.size());
		
		// Now to test matcher functionality with test data
		String testString1 = "{\"filter_level\":\"medium\",\"text\":\"@wongt0n You in Japan now?\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"wongt0n\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"id\":123834194,\"indices\":[0,8],\"screen_name\":\"wongt0n\",\"id_str\":\"123834194\",\"name\":\"Lazy Wong~\"}]},\"lang\":\"en\",\"in_reply_to_status_id_str\":\"421190875260014592\",\"id\":421193072316149761,\"aidr\":{\"features\":[{\"words\":[\"\",\"japan_now\",\"japan\",\"now\",\"_you\",\"you\",\"in_japan\",\"in\",\"you_in\"],\"type\":\"wordvector\"}],\"crisis_code\":\"japan_chem_explosion\",\"nominal_labels\":[],\"doctype\":\"twitter\",\"crisis_name\":\"Explosion at Japan chemical factory\"},\"source\":\"web\",\"in_reply_to_user_id_str\":\"123834194\",\"favorited\":false,\"in_reply_to_status_id\":421190875260014592,\"retweet_count\":0,\"created_at\":\"Thu Jan 09 08:13:48 +0000 2014\",\"in_reply_to_user_id\":123834194,\"favorite_count\":0,\"id_str\":\"421193072316149761\",\"place\":null,\"user\":{\"location\":\"\",\"default_profile\":false,\"profile_background_tile\":true,\"statuses_count\":10045,\"lang\":\"en\",\"profile_link_color\":\"1212E3\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/457120810/1358321901\",\"id\":457120810,\"following\":null,\"protected\":false,\"favourites_count\":7,\"profile_text_color\":\"E60ED4\",\"contributors_enabled\":false,\"verified\":false,\"description\":\"An ordinary SONE who fall in love with Taeyeon+Jessica+Sunny+Tiffany+Hyoyeon+Yuri+Sooyoung+Yoona+Seohyun = SNSD. Spazzing and sharing is my vacation on twitter.\",\"name\":\"~~\uC18C\uC2DC\uB77C\uC11C \uD589\uBCF5\uD574\uC694~~\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_background_color\":\"EDFAFA\",\"created_at\":\"Sat Jan 07 01:51:54 +0000 2012\",\"default_profile_image\":false,\"followers_count\":47,\"geo_enabled\":false,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"profile_background_image_url\":\"http://a0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"profile_background_image_url_https\":\"https://si0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":28800,\"time_zone\":\"Kuala Lumpur\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":127,\"profile_sidebar_fill_color\":\"E1D2F5\",\"screen_name\":\"blueagle90\",\"id_str\":\"457120810\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"listed_count\":0,\"is_translator\":false},\"coordinates\":null}";
		String testString2 = "{\"filter_level\":\"medium\",\"text\":\"@wongt0n You in Japan now?\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"wongt0n\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"id\":123834194,\"indices\":[0,8],\"screen_name\":\"wongt0n\",\"id_str\":\"123834194\",\"name\":\"Lazy Wong~\"}]},\"lang\":\"en\",\"in_reply_to_status_id_str\":\"421190875260014592\",\"id\":421193072316149761,\"aidr\":{\"features\":[{\"words\":[\"\",\"2009\",\"edition\",\"_#mp3\",\"#mp3_#music\",\"408_the\",\"the_end\",\"peas_2009\",\"#mp3\",\"the\",\"edition_black\",\"end_japan\",\"eyed_peas\",\"#music_408\",\"408\",\"japan\",\"japan_edition\",\"black_eyed\",\"#music\",\"eyed\",\"end\",\"black\",\"peas\"],\"type\":\"wordvector\"}],\"crisis_code\":\"japan_chem_explosion\",\"nominal_labels\":[{\"label_name\":\"Not related to crisis\",\"source_id\":289,\"from_human\":false,\"attribute_description\":\"Informative messages enhancing situational awareness, v1.0\",\"label_code\":\"030_not_info\",\"confidence\":0.54,\"label_description\":\"Not related to the crisis\",\"attribute_code\":\"informative_v1\",\"attribute_name\":\"Informative v1.0\"}],\"doctype\":\"twitter\",\"crisis_name\":\"Explosion at Japan chemical factory\"},\"source\":\"web\",\"in_reply_to_user_id_str\":\"123834194\",\"favorited\":false,\"in_reply_to_status_id\":421190875260014592,\"retweet_count\":0,\"created_at\":\"Thu Jan 09 08:13:48 +0000 2014\",\"in_reply_to_user_id\":123834194,\"favorite_count\":0,\"id_str\":\"421193072316149761\",\"place\":null,\"user\":{\"location\":\"\",\"default_profile\":false,\"profile_background_tile\":true,\"statuses_count\":10045,\"lang\":\"en\",\"profile_link_color\":\"1212E3\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/457120810/1358321901\",\"id\":457120810,\"following\":null,\"protected\":false,\"favourites_count\":7,\"profile_text_color\":\"E60ED4\",\"contributors_enabled\":false,\"verified\":false,\"description\":\"An ordinary SONE who fall in love with Taeyeon+Jessica+Sunny+Tiffany+Hyoyeon+Yuri+Sooyoung+Yoona+Seohyun = SNSD. Spazzing and sharing is my vacation on twitter.\",\"name\":\"~~\uC18C\uC2DC\uB77C\uC11C \uD589\uBCF5\uD574\uC694~~\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_background_color\":\"EDFAFA\",\"created_at\":\"Sat Jan 07 01:51:54 +0000 2012\",\"default_profile_image\":false,\"followers_count\":47,\"geo_enabled\":false,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"profile_background_image_url\":\"http://a0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"profile_background_image_url_https\":\"https://si0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":28800,\"time_zone\":\"Kuala Lumpur\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":127,\"profile_sidebar_fill_color\":\"E1D2F5\",\"screen_name\":\"blueagle90\",\"id_str\":\"457120810\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"listed_count\":0,\"is_translator\":false},\"coordinates\":null}";
		String testString3 = "{\"filter_level\":\"medium\",\"text\":\"@wongt0n You in Japan now?\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"wongt0n\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"id\":123834194,\"indices\":[0,8],\"screen_name\":\"wongt0n\",\"id_str\":\"123834194\",\"name\":\"Lazy Wong~\"}]},\"lang\":\"en\",\"in_reply_to_status_id_str\":\"421190875260014592\",\"id\":421193072316149761,\"aidr\":{\"features\":[{\"words\":[\"\",\"2009\",\"edition\",\"_#mp3\",\"#mp3_#music\",\"408_the\",\"the_end\",\"peas_2009\",\"#mp3\",\"the\",\"edition_black\",\"end_japan\",\"eyed_peas\",\"#music_408\",\"408\",\"japan\",\"japan_edition\",\"black_eyed\",\"#music\",\"eyed\",\"end\",\"black\",\"peas\"],\"type\":\"wordvector\"}],\"crisis_code\":\"japan_chem_explosion\",\"nominal_labels\":[{\"label_name\":\"Not related to crisis\",\"source_id\":289,\"from_human\":false,\"attribute_description\":\"Informative messages enhancing situational awareness, v1.0\",\"label_code\":\"030_info\",\"confidence\":0.54,\"label_description\":\"Not related to the crisis\",\"attribute_code\":\"informative_v2\",\"attribute_name\":\"Informative v1.0\"}, {\"label_name\":\"Not related to crisis\",\"source_id\":289,\"from_human\":false,\"attribute_description\":\"Informative messages enhancing situational awareness, v1.0\",\"label_code\":\"030_info\",\"confidence\":0.8433459674,\"label_description\":\"Not related to the crisis\",\"attribute_code\":\"informative_v1\",\"attribute_name\":\"Informative v1.0\"}, {\"label_name\":\"Not related to crisis\",\"source_id\":289,\"from_human\":false,\"attribute_description\":\"Informative messages enhancing situational awareness, v1.0\",\"label_code\":\"030_not_info\",\"confidence\":0.20,\"label_description\":\"related to the crisis\",\"attribute_code\":\"informative_v1\",\"attribute_name\":\"Informative v1.0\"}],\"doctype\":\"twitter\",\"crisis_name\":\"Explosion at Japan chemical factory\"},\"source\":\"web\",\"in_reply_to_user_id_str\":\"123834194\",\"favorited\":false,\"in_reply_to_status_id\":421190875260014592,\"retweet_count\":0,\"created_at\":\"Thu Jan 09 08:13:48 +0000 2014\",\"in_reply_to_user_id\":123834194,\"favorite_count\":0,\"id_str\":\"421193072316149761\",\"place\":null,\"user\":{\"location\":\"\",\"default_profile\":false,\"profile_background_tile\":true,\"statuses_count\":10045,\"lang\":\"en\",\"profile_link_color\":\"1212E3\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/457120810/1358321901\",\"id\":457120810,\"following\":null,\"protected\":false,\"favourites_count\":7,\"profile_text_color\":\"E60ED4\",\"contributors_enabled\":false,\"verified\":false,\"description\":\"An ordinary SONE who fall in love with Taeyeon+Jessica+Sunny+Tiffany+Hyoyeon+Yuri+Sooyoung+Yoona+Seohyun = SNSD. Spazzing and sharing is my vacation on twitter.\",\"name\":\"~~\uC18C\uC2DC\uB77C\uC11C \uD589\uBCF5\uD574\uC694~~\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_background_color\":\"EDFAFA\",\"created_at\":\"Sat Jan 07 01:51:54 +0000 2012\",\"default_profile_image\":false,\"followers_count\":47,\"geo_enabled\":false,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"profile_background_image_url\":\"http://a0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"profile_background_image_url_https\":\"https://si0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":28800,\"time_zone\":\"Kuala Lumpur\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":127,\"profile_sidebar_fill_color\":\"E1D2F5\",\"screen_name\":\"blueagle90\",\"id_str\":\"457120810\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"listed_count\":0,\"is_translator\":false},\"coordinates\":null}";
		
		JsonDeserializer jc = new JsonDeserializer();
		
		ClassifiedTweet testTweet = jc.getClassifiedTweet(testString2);
		
		int i = 0;
		for (NominalLabel t: testTweet.getNominalLabels()) {
			System.out.println("i = " + i + ": createAt = " + testTweet.getCreatedAt()  
					+ ", attribute code = " + t.attibute_code 
					+ ", label_code = " + t.label_code + ", confidence = " + t.confidence);
			++i;
		}
		System.out.println("[main] Result of matching: " + test.getMatcherResult(testTweet));
	}
}
