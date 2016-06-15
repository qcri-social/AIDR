package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.collector.beans.TwitterCollectionTask;

/**
 * Created by noora on 5/19/15.
 */
public class FollowFilterTest {

    private static FollowFilter followFilter;

    @Before
    public void setUp() throws Exception {
    	TwitterCollectionTask collectionTask = new TwitterCollectionTask(); 
        collectionTask.setToFollow("1105766504");
        followFilter = new FollowFilter(collectionTask);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testTest1() throws Exception {
        //String jsonObjectString = "{\"filter_level\":\"low\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"fr\",\"in_reply_to_status_id_str\":null,\"id\":575924535930843140,\"in_reply_to_user_id_str\":null,\"timestamp_ms\":\"1426146084183\",\"in_reply_to_status_id\":null,\"created_at\":\"Thu Mar 12 07:41:24 +0000 2015\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"On Ã  sorti le leader de premiere league en jouant tous les 3 jours, une avalanche de blessÃ©s et en jouant Ã  10 #CHELPSG #LT\",\"contributors\":null,\"geo\":null,\"entities\":{\"trends\":[],\"symbols\":[],\"urls\":[],\"hashtags\":[{\"text\":\"CHELPSG\",\"indices\":[111,119]},{\"text\":\"LT\",\"indices\":[120,123]}],\"user_mentions\":[]},\"source\":\"<a href=\\\"http://tapbots.com/tweetbot\\\" rel=\\\"nofollow\\\">Tweetbot for iÎŸS</a>\",\"favorited\":false,\"in_reply_to_user_id\":null,\"retweet_count\":0,\"id_str\":\"575924535930843136\",\"user\":{\"location\":\"Paris\",\"default_profile\":false,\"profile_background_tile\":false,\"statuses_count\":783,\"lang\":\"fr\",\"profile_link_color\":\"FF3300\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/2788189586/1410101854\",\"id\":2788189586,\"following\":null,\"protected\":false,\"favourites_count\":223,\"profile_text_color\":\"333333\",\"verified\":false,\"description\":\"30 Fucking Years Old\\nCG Artist, Lead Previz, 3D Senior Animator, Rigging and Tracking Skills\\n#Cinema #3D #PSG\",\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"86A4A6\",\"name\":\"Beniga\",\"profile_background_color\":\"709397\",\"created_at\":\"Wed Sep 03 15:54:42 +0000 2014\",\"default_profile_image\":false,\"followers_count\":46,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/554740197112688640/2ofATQtR_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme6/bg.gif\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme6/bg.gif\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":0,\"time_zone\":\"London\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":115,\"profile_sidebar_fill_color\":\"A0C5C7\",\"screen_name\":\"SunBeniga\",\"id_str\":\"2788189586\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/554740197112688640/2ofATQtR_normal.jpeg\",\"listed_count\":0,\"is_translator\":false},\"aidr\":{\"doctype\":\"twitter\",\"crisis_code\":\"150312104037_emsc_landslides_by_kw_fra\",\"crisis_name\":\"EMSC Landslides by KW fra\"}}";
    	String jsonObjectString = "{\"filter_level\":\"medium\",\"contributors\":null,\"text\":\"@gvicks @OneLastStranger Dude-Thats such POOR taste, Respect a missing Plane of humans Its an #aviation tragedy #MH370 #MalaysiaAirlines\",\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"gvicks\",\"truncated\":false,\"lang\":\"en\",\"entities\":{\"hashtags\":[{\"text\":\"aviation\",\"indices\":[95,104]},{\"text\":\"MH370\",\"indices\":[113,119]},{\"text\":\"MalaysiaAirlines\",\"indices\":[120,137]}],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"id\":20862797,\"indices\":[0,7],\"screen_name\":\"gvicks\",\"id_str\":\"20862797\",\"name\":\"Vikram\"},{\"id\":31354692,\"indices\":[8,24],\"screen_name\":\"OneLastStranger\",\"id_str\":\"31354692\",\"name\":\"Busy Stranger\"}]},\"in_reply_to_status_id_str\":\"444322825088684033\",\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"id\":445127578831966208,\"in_reply_to_user_id_str\":\"20862797\",\"source\":\"\",\"favorited\":false,\"in_reply_to_status_id\":444322825088684033,\"in_reply_to_user_id\":20862797,\"created_at\":\"Sun Mar 16 09:20:59 +0000 2014\",\"retweet_count\":0,\"favorite_count\":0,\"id_str\":\"445127578831966208\",\"place\":null,\"user\":{\"location\":\"Let Me Own Your Mind & Soul xx\",\"default_profile\":false,\"statuses_count\":15830,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/1105766504/1391364260\",\"id\":1105766504,\"following\":null,\"favourites_count\":8861,\"protected\":false,\"profile_text_color\":\"666666\",\"description\":\"#Romantic #Poetry #Lover #Dom #Porn #BDSM~Lifestyle,,Not a Game, Prince of Darkness~Pain & Passion; Lust & Love~Submission Liberates~HLC~Pics with permission\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"FFFFFF\",\"name\":\"Sir-Thor\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 20 08:57:28 +0000 2013\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":2087,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000712615810/db2f0e2aae535c16eafee1f303289416_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme9/bg.gif\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme9/bg.gif\",\"follow_request_sent\":null,\"url\":\"http://followback.me/69_hornie\",\"utc_offset\":19800,\"time_zone\":\"Chennai\",\"notifications\":null,\"friends_count\":1153,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"252429\",\"screen_name\":\"69_hornie\",\"id_str\":\"1105766504\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000712615810/db2f0e2aae535c16eafee1f303289416_normal.jpeg\",\"is_translator\":false,\"listed_count\":25},\"coordinates\":null}";
    	JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectString));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        //
        boolean test = followFilter.test(object);
        //
        assertTrue(test);
        //
        String jsonObjectString1 = "{\"filter_level\":\"medium\",\"contributors\":null,\"text\":\"@gvicks @OneLastStranger Dude-Thats such POOR taste, Respect a missing Plane of humans Its an #aviation tragedy #MH370 #MalaysiaAirlines\",\"geo\":null,\"retweeted\":false,\"in_reply_to_screen_name\":\"gvicks\",\"truncated\":false,\"lang\":\"en\",\"entities\":{\"hashtags\":[{\"text\":\"aviation\",\"indices\":[95,104]},{\"text\":\"MH370\",\"indices\":[113,119]},{\"text\":\"MalaysiaAirlines\",\"indices\":[120,137]}],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"id\":20862797,\"indices\":[0,7],\"screen_name\":\"gvicks\",\"id_str\":\"20862797\",\"name\":\"Vikram\"},{\"id\":31354692,\"indices\":[8,24],\"screen_name\":\"OneLastStranger\",\"id_str\":\"31354692\",\"name\":\"Busy Stranger\"}]},\"in_reply_to_status_id_str\":\"444322825088684033\",\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"id\":445127578831966208,\"in_reply_to_user_id_str\":\"20862797\",\"source\":\"\",\"favorited\":false,\"in_reply_to_status_id\":444322825088684033,\"in_reply_to_user_id\":20862797,\"created_at\":\"Sun Mar 16 09:20:59 +0000 2014\",\"retweet_count\":0,\"favorite_count\":0,\"id_str\":\"445127578831966208\",\"place\":null,\"user\":{\"location\":\"Let Me Own Your Mind & Soul xx\",\"default_profile\":false,\"statuses_count\":15830,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/1105766504/1391364260\",\"id\":1105765504,\"following\":null,\"favourites_count\":8861,\"protected\":false,\"profile_text_color\":\"666666\",\"description\":\"#Romantic #Poetry #Lover #Dom #Porn #BDSM~Lifestyle,,Not a Game, Prince of Darkness~Pain & Passion; Lust & Love~Submission Liberates~HLC~Pics with permission\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"FFFFFF\",\"name\":\"Sir-Thor\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 20 08:57:28 +0000 2013\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":2087,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/378800000712615810/db2f0e2aae535c16eafee1f303289416_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme9/bg.gif\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme9/bg.gif\",\"follow_request_sent\":null,\"url\":\"http://followback.me/69_hornie\",\"utc_offset\":19800,\"time_zone\":\"Chennai\",\"notifications\":null,\"friends_count\":1153,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"252429\",\"screen_name\":\"69_hornie\",\"id_str\":\"1105765504\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/378800000712615810/db2f0e2aae535c16eafee1f303289416_normal.jpeg\",\"is_translator\":false,\"listed_count\":25},\"coordinates\":null}";
    	JsonReader jsonReader1 = Json.createReader(new StringReader(jsonObjectString1));
        JsonObject object1 = jsonReader1.readObject();
        jsonReader1.close();
        boolean test1 = followFilter.test(object1);
        //
        assertFalse(test1);
    }

    @Test
    public void testGetFilterName() throws Exception {
        String filterName = followFilter.getFilterName();
        //
        assertNotNull(filterName);
        assertEquals("FollowFilter", filterName);
    }
}