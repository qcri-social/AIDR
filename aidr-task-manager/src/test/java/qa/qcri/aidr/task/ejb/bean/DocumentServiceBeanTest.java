package qa.qcri.aidr.task.ejb.bean;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import qa.qcri.aidr.task.entities.Document;

public class DocumentServiceBeanTest {
	static DocumentServiceBean documentServiceBean;
	//
	@BeforeClass
	public static void setUp() throws Exception {
		documentServiceBean = new DocumentServiceBean();
		EntityManager em = Persistence.createEntityManagerFactory("ProjectTest-ejbPU").createEntityManager();
		documentServiceBean.setEntityManager(em);
	}

	@AfterClass
	public static void shutDown() throws Exception {
		documentServiceBean.getEntityManager().close();
	}
	//
	@Test
	public void updateHasHumanLabelTest() {
		Document document = new Document();
		document.setDocumentID(new Long(10));
		document.setCrisisID(new Long(117));
		document.setHasHumanLabels(false);
		document.setEvaluationSet(true);
		document.setValueAsTrainingSample(new Double(0.5));
		document.setReceivedAt(new Date());
		document.setSourceIP(2130706689L);
		document.setLanguage("en");
		document.setDoctype("Tweet");
		document.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		documentServiceBean.save(document);
		documentServiceBean.updateHasHumanLabel(document);
		Document doc = documentServiceBean.getById(new Long(10));
		//
		assertNotNull(doc);
		assertEquals(true, doc.isHasHumanLabels());
		documentServiceBean.delete(doc);
	}
	//
	@Test
	public void deleteNoLabelDocumentTest() {
		Document document = new Document();
		document.setDocumentID(new Long(11));
		document.setCrisisID(new Long(117));
		document.setHasHumanLabels(false);
		document.setEvaluationSet(true);
		document.setValueAsTrainingSample(new Double(0.5));
		document.setReceivedAt(new Date());
		document.setSourceIP(2130706689L);
		document.setLanguage("en");
		document.setDoctype("Tweet");
		document.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		documentServiceBean.save(document);
		int deleteValue = documentServiceBean.deleteNoLabelDocument(document);
		assertEquals(1, deleteValue);
	}
	//
	@Test
	public void deleteNoLabelDocumentListTest() {
		Document document1 = new Document();
		document1.setDocumentID(new Long(12));
		document1.setCrisisID(new Long(117));
		document1.setHasHumanLabels(false);
		document1.setEvaluationSet(true);
		document1.setValueAsTrainingSample(new Double(0.5));
		document1.setReceivedAt(new Date());
		document1.setSourceIP(2130706689L);
		document1.setLanguage("en");
		document1.setDoctype("Tweet");
		document1.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document1.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		Document document2 = new Document();
		document2.setDocumentID(new Long(13));
		document2.setCrisisID(new Long(117));
		document2.setHasHumanLabels(false);
		document2.setEvaluationSet(true);
		document2.setValueAsTrainingSample(new Double(0.5));
		document2.setReceivedAt(new Date());
		document2.setSourceIP(2130706689L);
		document2.setLanguage("en");
		document2.setDoctype("Tweet");
		document2.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document2.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		Document document3 = new Document();
		document3.setDocumentID(new Long(14));
		document3.setCrisisID(new Long(123));
		document3.setHasHumanLabels(false);
		document3.setEvaluationSet(true);
		document3.setValueAsTrainingSample(new Double(0.5));
		document3.setReceivedAt(new Date());
		document3.setSourceIP(2130706689L);
		document3.setLanguage("en");
		document3.setDoctype("Tweet");
		document3.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document3.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		List<Document> collectionTest = new ArrayList<Document>();
		collectionTest.add(document1);
		collectionTest.add(document2);
		collectionTest.add(document3);
		//
		documentServiceBean.save(collectionTest);
		//
		int deleteCount = documentServiceBean.deleteNoLabelDocument(collectionTest);
		assertEquals(3, deleteCount);
	}
	//
	@Test
	public void deleteUnassignedDocumentTest() {
		/*Document document = documentServiceBean.getById(new Long(4579275));
		document.setHasHumanLabels(false);*/
		Document document = new Document();
		document.setDocumentID(new Long(90));
		document.setCrisisID(new Long(123));
		document.setHasHumanLabels(false);
		document.setEvaluationSet(true);
		document.setValueAsTrainingSample(new Double(0.5));
		document.setReceivedAt(new Date());
		document.setSourceIP(2130706689L);
		document.setLanguage("en");
		document.setDoctype("Tweet");
		document.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		documentServiceBean.save(document);
		//
		int deleteResult = documentServiceBean.deleteUnassignedDocument(document);
		assertEquals(1, deleteResult);
	}
	//
	@Test
	public void deleteUnassignedDocumentCollectionTest() {
		Document document = new Document();
		document.setDocumentID(new Long(91));
		document.setCrisisID(new Long(123));
		document.setHasHumanLabels(false);
		document.setEvaluationSet(true);
		document.setValueAsTrainingSample(new Double(0.5));
		document.setReceivedAt(new Date());
		document.setSourceIP(2130706689L);
		document.setLanguage("en");
		document.setDoctype("Tweet");
		document.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		Document document2 = new Document();
		document2.setDocumentID(new Long(92));
		document2.setCrisisID(new Long(123));
		document2.setHasHumanLabels(false);
		document2.setEvaluationSet(true);
		document2.setValueAsTrainingSample(new Double(0.5));
		document2.setReceivedAt(new Date());
		document2.setSourceIP(2130706689L);
		document2.setLanguage("en");
		document2.setDoctype("Tweet");
		document2.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document2.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		Document document3 = new Document();
		document3.setDocumentID(new Long(93));
		document3.setCrisisID(new Long(123));
		document3.setHasHumanLabels(false);
		document3.setEvaluationSet(true);
		document3.setValueAsTrainingSample(new Double(0.5));
		document3.setReceivedAt(new Date());
		document3.setSourceIP(2130706689L);
		document3.setLanguage("en");
		document3.setDoctype("Tweet");
		document3.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document3.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		
		List<Document> collectionTest = new ArrayList<Document>();
		collectionTest.add(document);
		collectionTest.add(document2);
		collectionTest.add(document3);
		documentServiceBean.save(collectionTest);
		//
		int deleteResult = documentServiceBean.deleteUnassignedDocumentCollection(collectionTest);
		assertEquals(3, deleteResult);
	}
	//
	@Test
	public void deleteStaleDocumentsTest() {
		final String TASK_EXPIRY_AGE_LIMIT = "5s";
		final String TASK_BUFFER_SCAN_INTERVAL = "1s";
		// ex1
		//int result1 = documentServiceBean.deleteStaleDocuments("LEFT JOIN", "aidr_predict.task_assignment", "documentID", "ASC", null, TASK_EXPIRY_AGE_LIMIT, TASK_BUFFER_SCAN_INTERVAL);
		// ex2
		int result2 = documentServiceBean.deleteStaleDocuments("JOIN", "aidr_predict.task_assignment", "documentID", "ASC", null, TASK_EXPIRY_AGE_LIMIT, TASK_BUFFER_SCAN_INTERVAL);
		//
		//assertEquals(0, result1);
		assertEquals(0, result2);
	}
}
