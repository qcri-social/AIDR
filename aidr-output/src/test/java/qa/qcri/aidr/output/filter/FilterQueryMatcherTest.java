package qa.qcri.aidr.output.filter;

import static org.junit.Assert.*;
import qa.qcri.aidr.common.filter.ClassifiedFilteredTweet;
import qa.qcri.aidr.common.filter.FilterQueryMatcher;

import org.junit.BeforeClass;
import org.junit.Test;

public class FilterQueryMatcherTest {
	//
	static FilterQueryMatcher filterQueryMatcher;
	//
	@BeforeClass
	public static void setUp() throws Exception {
		filterQueryMatcher = new FilterQueryMatcher();
	}
	//
	@Test
	public void getMatcherResultTest() {
		String testString1 = "{\"filter_level\":\"medium\",\"text\":\"@wongt0n You in Japan now?\",\"contributors\":null,\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"wongt0n\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"id\":123834194,\"indices\":[0,8],\"screen_name\":\"wongt0n\",\"id_str\":\"123834194\",\"name\":\"Lazy Wong~\"}]},\"lang\":\"en\",\"in_reply_to_status_id_str\":\"421190875260014592\",\"id\":421193072316149761,\"aidr\":{\"features\":[{\"words\":[\"\",\"japan_now\",\"japan\",\"now\",\"_you\",\"you\",\"in_japan\",\"in\",\"you_in\"],\"type\":\"wordvector\"}],\"crisis_code\":\"japan_chem_explosion\",\"nominal_labels\":[],\"doctype\":\"twitter\",\"crisis_name\":\"Explosion at Japan chemical factory\"},\"source\":\"web\",\"in_reply_to_user_id_str\":\"123834194\",\"favorited\":false,\"in_reply_to_status_id\":421190875260014592,\"retweet_count\":0,\"created_at\":\"Thu Jan 09 08:13:48 +0000 2014\",\"in_reply_to_user_id\":123834194,\"favorite_count\":0,\"id_str\":\"421193072316149761\",\"place\":null,\"user\":{\"location\":\"\",\"default_profile\":false,\"profile_background_tile\":true,\"statuses_count\":10045,\"lang\":\"en\",\"profile_link_color\":\"1212E3\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/457120810/1358321901\",\"id\":457120810,\"following\":null,\"protected\":false,\"favourites_count\":7,\"profile_text_color\":\"E60ED4\",\"contributors_enabled\":false,\"verified\":false,\"description\":\"An ordinary SONE who fall in love with Taeyeon+Jessica+Sunny+Tiffany+Hyoyeon+Yuri+Sooyoung+Yoona+Seohyun = SNSD. Spazzing and sharing is my vacation on twitter.\",\"name\":\"~~\uC18C\uC2DC\uB77C\uC11C \uD589\uBCF5\uD574\uC694~~\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_background_color\":\"EDFAFA\",\"created_at\":\"Sat Jan 07 01:51:54 +0000 2012\",\"default_profile_image\":false,\"followers_count\":47,\"geo_enabled\":false,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"profile_background_image_url\":\"http://a0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"profile_background_image_url_https\":\"https://si0.twimg.com/profile_background_images/889556219/7456374b70ecfea67145b0214f15a988.jpeg\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":28800,\"time_zone\":\"Kuala Lumpur\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":127,\"profile_sidebar_fill_color\":\"E1D2F5\",\"screen_name\":\"blueagle90\",\"id_str\":\"457120810\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000219857862/9606b10e2dd7d700111f4c5be7384f63_normal.jpeg\",\"listed_count\":0,\"is_translator\":false},\"coordinates\":null}";
		ClassifiedFilteredTweet testTweet = new ClassifiedFilteredTweet();
		testTweet.deserialize(testString1);
		
		boolean actual = filterQueryMatcher.getMatcherResult(testTweet);
		//
		assertEquals(true, actual);
	}
}
