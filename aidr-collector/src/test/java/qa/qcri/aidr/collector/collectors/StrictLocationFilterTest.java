package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.collector.beans.TwitterCollectionTask;

/**
 * Created by noora on 5/19/15.
 */
public class StrictLocationFilterTest {

    private static StrictLocationFilter strictLocationFilter;
    private JsonReader jsonReader;

    @Before
    public void setUp() throws Exception {
        TwitterCollectionTask collectionTask = new TwitterCollectionTask();
        collectionTask.setGeoLocation("51.42,25.22,51.62,25.38,7.3182,45.818,10.4921,47.0335"); // two BB
        strictLocationFilter = new StrictLocationFilter(collectionTask);
    }

    public JsonObject getJsonObject(String text) {
    	 jsonReader = Json.createReader(new StringReader(text));
         JsonObject object = jsonReader.readObject();
         return object;
    }

    @Test
    public void testTest1() throws Exception {
    	//
        String jsonObjectString1 = "{\"contributors\":null,\"truncated\":false,\"text\":\"Looks we are gone miss the show!?\uD83D\uDE01 #dohatraffic stay at home folks \uD83D\uDE02 https://t.co/ahQhBrvF46 http://t.co/LqK4bJrWGL\",\"in_reply_to_status_id\":null,\"aidr\":{\"crisis_name\":\"Doha Traffic New\",\"doctype\":\"twitter\",\"crisis_code\":\"20150129-0234-SinhaKoushik-doha_traffic_new\"},\"id\":560824022645346300,\"favorite_count\":0,\"source\":\"<a href=\\\"http://foursquare.com\\\" rel=\\\"nofollow\\\">Foursquare</a>\",\"retweeted\":false,\"coordinates\":{\"type\":\"Point\",\"coordinates\":[51.523234,25.297208]},\"timestamp_ms\":\"1422545841262\",\"entities\":{\"user_mentions\":[],\"media\":[{\"expanded_url\":\"http://twitter.com/AhmdAbuzied/status/560824022645346304/photo/1\",\"sizes\":{\"small\":{\"h\":603,\"resize\":\"fit\",\"w\":340},\"large\":{\"h\":1818,\"resize\":\"fit\",\"w\":1024},\"medium\":{\"h\":1065,\"resize\":\"fit\",\"w\":600},\"thumb\":{\"h\":150,\"resize\":\"crop\",\"w\":150}},\"url\":\"http://t.co/LqK4bJrWGL\",\"media_url_https\":\"https://pbs.twimg.com/media/B8hyeiSIIAEnWdI.jpg\",\"display_url\":\"pic.twitter.com/LqK4bJrWGL\",\"id_str\":\"560824022532104193\",\"indices\":[93,115],\"type\":\"photo\",\"id\":560824022532104200,\"media_url\":\"http://pbs.twimg.com/media/B8hyeiSIIAEnWdI.jpg\"}],\"hashtags\":[{\"indices\":[35,47],\"text\":\"dohatraffic\"}],\"symbols\":[],\"trends\":[],\"urls\":[{\"indices\":[69,92],\"url\":\"https://t.co/ahQhBrvF46\",\"expanded_url\":\"https://www.swarmapp.com/c/39onhxZL08e\",\"display_url\":\"swarmapp.com/c/39onhxZL08e\"}]},\"in_reply_to_screen_name\":null,\"in_reply_to_user_id\":null,\"retweet_count\":0,\"id_str\":\"560824022645346304\",\"favorited\":false,\"user\":{\"follow_request_sent\":null,\"profile_use_background_image\":true,\"id\":190063189,\"verified\":false,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/557195223185362944/LYMBVlsG_normal.jpeg\",\"profile_sidebar_fill_color\":\"252429\",\"is_translator\":false,\"profile_text_color\":\"CEEBE8\",\"followers_count\":12856,\"protected\":false,\"location\":\"Cairo/Doha\",\"default_profile_image\":false,\"id_str\":\"190063189\",\"utc_offset\":10800,\"statuses_count\":9492,\"description\":\"Direct your thoughts control your emotions, and ordain your destiny.\\nTranslator cum #Marketer, #sales, #socialmedia & #photography.\",\"friends_count\":10078,\"profile_background_image_url_https\":\"https://pbs.twimg.com/profile_background_images/602977828/awlgq9ku1fyisljxslnx.jpeg\",\"profile_link_color\":\"0E89F5\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/557195223185362944/LYMBVlsG_normal.jpeg\",\"notifications\":null,\"geo_enabled\":true,\"profile_background_color\":\"080808\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/190063189/1394493587\",\"profile_background_image_url\":\"http://pbs.twimg.com/profile_background_images/602977828/awlgq9ku1fyisljxslnx.jpeg\",\"screen_name\":\"AhmdAbuzied\",\"lang\":\"en\",\"profile_background_tile\":false,\"favourites_count\":407,\"name\":\"أحمد أبوزيد\",\"url\":\"http://about.me/ahmadabuzied\",\"created_at\":\"Mon Sep 13 00:13:28 +0000 2010\",\"contributors_enabled\":false,\"time_zone\":\"Kuwait\",\"profile_sidebar_border_color\":\"000000\",\"default_profile\":false,\"following\":null,\"listed_count\":23},\"geo\":{\"type\":\"Point\",\"coordinates\":[25.297208,51.523234]},\"in_reply_to_user_id_str\":null,\"possibly_sensitive\":false,\"lang\":\"en\",\"created_at\":\"Thu Jan 29 15:37:21 +0000 2015\",\"filter_level\":\"low\",\"in_reply_to_status_id_str\":null,\"place\":{\"full_name\":\"Qatar\",\"name\":\"Qatar\",\"url\":\"https://api.twitter.com/1.1/geo/id/a54c21f6aedb2967.json\",\"country\":\"دولة قطر\",\"place_type\":\"country\",\"bounding_box\":{\"type\":\"Polygon\",\"coordinates\":[[[50.7500758,24.5563659],[50.7500758,26.1828978],[51.630581,26.1828978],[51.630581,24.5563659]]]},\"country_code\":\"QA\",\"attributes\":{},\"id\":\"a54c21f6aedb2967\"},\"extended_entities\":{\"media\":[{\"expanded_url\":\"http://twitter.com/AhmdAbuzied/status/560824022645346304/photo/1\",\"sizes\":{\"small\":{\"h\":603,\"resize\":\"fit\",\"w\":340},\"large\":{\"h\":1818,\"resize\":\"fit\",\"w\":1024},\"medium\":{\"h\":1065,\"resize\":\"fit\",\"w\":600},\"thumb\":{\"h\":150,\"resize\":\"crop\",\"w\":150}},\"url\":\"http://t.co/LqK4bJrWGL\",\"media_url_https\":\"https://pbs.twimg.com/media/B8hyeiSIIAEnWdI.jpg\",\"display_url\":\"pic.twitter.com/LqK4bJrWGL\",\"id_str\":\"560824022532104193\",\"indices\":[93,115],\"type\":\"photo\",\"id\":560824022532104200,\"media_url\":\"http://pbs.twimg.com/media/B8hyeiSIIAEnWdI.jpg\"}]}}";
        JsonObject object1 = getJsonObject(jsonObjectString1);
        jsonReader.close();
        //
        boolean test = strictLocationFilter.test(object1);
        //
        assertTrue(test);
        //
        String jsonObjectString2 = "{\"contributors\": null, \"truncated\": false, \"text\": \"This ugly scene became the daily normal on #Doha express way :(#Qatar #carcrash #highway #accednt @\u2026 http://t.co/Xr0IwZvBjr\", \"in_reply_to_status_id\": null, \"aidr\": {\"crisis_name\": \"Doha Traffic New\", \"doctype\": \"twitter\", \"crisis_code\": \"20150129-0234-SinhaKoushik-doha_traffic_new\"}, \"id\": 562495461593198592, \"favorite_count\": 0, \"source\": \"<a href=\\\"http://instagram.com\\\" rel=\\\"nofollow\\\">Instagram</a>\", \"retweeted\": false, \"coordinates\": {\"type\": \"Point\", \"coordinates\": [30.745218, 27.557897]}, \"timestamp_ms\": \"1422944343361\", \"entities\": {\"symbols\": [], \"user_mentions\": [], \"trends\": [], \"hashtags\": [{\"indices\": [43, 48], \"text\": \"Doha\"}, {\"indices\": [64, 70], \"text\": \"Qatar\"}, {\"indices\": [71, 80], \"text\": \"carcrash\"}, {\"indices\": [81, 89], \"text\": \"highway\"}, {\"indices\": [90, 98], \"text\": \"accednt\"}], \"urls\": [{\"indices\": [102, 124], \"url\": \"http://t.co/Xr0IwZvBjr\", \"expanded_url\": \"http://instagram.com/p/yoPL26p1VL/\", \"display_url\": \"instagram.com/p/yoPL26p1VL/\"}]}, \"in_reply_to_screen_name\": null, \"in_reply_to_user_id\": null, \"retweet_count\": 0, \"id_str\": \"562495461593198592\", \"favorited\": false, \"user\": {\"follow_request_sent\": null, \"profile_use_background_image\": true, \"id\": 68277622, \"verified\": false, \"profile_image_url_https\": \"https://pbs.twimg.com/profile_images/413111975162298368/s0IMLFGD_normal.jpeg\", \"profile_sidebar_fill_color\": \"DDEEF6\", \"is_translator\": false, \"profile_text_color\": \"333333\", \"followers_count\": 115, \"protected\": false, \"location\": \"Toronto, Ontario, Canada\", \"default_profile_image\": false, \"id_str\": \"68277622\", \"utc_offset\": 10800, \"statuses_count\": 1031, \"description\": \"Ph.D. in Social Media, passionate for SharePoint, big on Security Solutions and do media and music composition.\", \"friends_count\": 165, \"profile_background_image_url_https\": \"https://abs.twimg.com/images/themes/theme1/bg.png\", \"profile_link_color\": \"0084B4\", \"profile_image_url\": \"http://pbs.twimg.com/profile_images/413111975162298368/s0IMLFGD_normal.jpeg\", \"notifications\": null, \"geo_enabled\": true, \"profile_background_color\": \"C0DEED\", \"profile_banner_url\": \"https://pbs.twimg.com/profile_banners/68277622/1353256770\", \"profile_background_image_url\": \"http://abs.twimg.com/images/themes/theme1/bg.png\", \"screen_name\": \"hqaddomi\", \"lang\": \"en\", \"profile_background_tile\": false, \"favourites_count\": 20, \"name\": \"Hisham Qaddoumi\", \"url\": \"http://www.hishamqaddomi.ca\", \"created_at\": \"Mon Aug 24 00:29:01 +0000 2009\", \"contributors_enabled\": false, \"time_zone\": \"Kuwait\", \"profile_sidebar_border_color\": \"C0DEED\", \"default_profile\": true, \"following\": null, \"listed_count\": 3}, \"geo\": {\"type\": \"Point\", \"coordinates\": [30.745218, 27.557897]}, \"in_reply_to_user_id_str\": null, \"possibly_sensitive\": false, \"lang\": \"en\", \"created_at\": \"Tue Feb 03 06:19:03 +0000 2015\", \"filter_level\": \"low\", \"in_reply_to_status_id_str\": null, \"place\": {\"full_name\": \"Doha, Qatar\", \"name\": \"Doha\", \"url\": \"https://api.twitter.com/1.1/geo/id/0181f32937df0de8.json\", \"country\": \"\u062f\u0648\u0644\u0629 \u0642\u0637\u0631\", \"place_type\": \"admin\", \"bounding_box\": {\"type\": \"Polygon\", \"coordinates\": [[[30.745218, 27.557897], [30.745218, 27.557897], [30.745218, 27.557897], [30.745218, 27.557897]]]}, \"country_code\": \"QA\", \"attributes\": {}, \"id\": \"0181f32937df0de8\"}}";
        JsonObject object2 = getJsonObject(jsonObjectString2);
        jsonReader.close();
        boolean failTest = strictLocationFilter.test(object2);
        //
        assertFalse(failTest);
    }

    @Test
    public void testGetFilterName() throws Exception {
        String filterName = strictLocationFilter.getFilterName();
        //
        assertNotNull(filterName);
        assertEquals("StrictLocationFilter", filterName);
    }
}