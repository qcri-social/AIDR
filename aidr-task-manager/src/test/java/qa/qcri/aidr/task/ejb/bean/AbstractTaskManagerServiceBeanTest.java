package qa.qcri.aidr.task.ejb.bean;

import static org.junit.Assert.*;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import qa.qcri.aidr.task.ejb.AbstractTaskManagerService;
import qa.qcri.aidr.task.entities.Document;

public class AbstractTaskManagerServiceBeanTest {
	//
	static AbstractTaskManagerService<Document, Long> atmsb;

	@BeforeClass
	public static void setUp() throws Exception {
		atmsb = new AbstractTaskManagerServiceBean<Document, Long>(Document.class);
		EntityManager em = Persistence.createEntityManagerFactory("ProjectTest-ejbPU").createEntityManager();
		atmsb.setEntityManager(em);
	}

	@AfterClass
	public static void shutDown() throws Exception {
		atmsb.getEntityManager().close();
	}
	//
	@Test
	public void getByIdTest() throws NullPointerException {
		Document c1 = atmsb.getById(new Long(4579250));
		assertNotNull(c1);
		assertEquals(new Long(117), c1.getCrisisID());
	}
	//
	@Test
	public void getByCriterionIDTest() {
		Criterion newCriterion = Restrictions.conjunction()
				.add(Restrictions.eq("documentID", new Long(4582497)))
				.add(Restrictions.eq("hasHumanLabels",true));
		Document doc = atmsb.getByCriterionID(newCriterion);
		//
		assertNotNull(doc);
		assertEquals(new Long(117), doc.getCrisisID());
	}
	//
	@Test
	public void getByCriteriaTest() {
		Criterion newCriterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisID",new Long(117)))
				.add(Restrictions.eq("hasHumanLabels",true));
		Document c = atmsb.getByCriteria(newCriterion);
		//
		assertNotNull(c);
		assertEquals(new Long(117), c.getCrisisID()); // crisisID
	}
	//
	@Test
	public void getAllByCriteriaTest() {
		Criterion newCriterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisID",117L))
				.add(Restrictions.eq("hasHumanLabels",true));
		List<Document> list = atmsb.getAllByCriteria(newCriterion);
		//
		assertNotNull(list);
		assertNotNull(list.get(4));
		Long docCrisisId = list.get(4).getCrisisID();
		assertEquals(new Long(117), docCrisisId);
	}

	//
	@Test
	public void getByCriteriaWithLimitTest() {
		Criterion newCriterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisID",117L))
				.add(Restrictions.eq("hasHumanLabels",true));
		List<Document> docs = atmsb.getByCriteriaWithLimit(newCriterion, 100);
		//
		assertNotNull(docs);
		assertNotNull(docs.get(4));
		Long docCrisisId = docs.get(4).getCrisisID();
		assertEquals(new Long(117), docCrisisId);
	}
	//
	@Test
	public void getByCriteriaByOrderTest() {
		Criterion newCriterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisID",117L))
				.add(Restrictions.eq("hasHumanLabels",true));
		String[] orderBy = {"valueAsTrainingSample","documentID"};
		//
		List<Document> t = atmsb.getByCriteriaByOrder(newCriterion, "", orderBy, 100);
		Long id1 = t.get(0).getDocumentID();
		Long id2 = t.get(1).getDocumentID();
		boolean actual = false;
		if(id2>id1)
			actual = true;
		//	
		assertEquals(true, actual);
	}
	//
	@Test
	public void getByCriteriaWithAliasByOrderTest() {
		String aliasTable = "taskAssignment";
		String aliasTableKey = "taskAssignment.documentID";
		String[] orderBy = {"valueAsTrainingSample","documentID"};
		Criterion aliasCriterion =  (Restrictions.isNull(aliasTableKey));
		Criterion newCriterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisID",new Long(117)))
				.add(Restrictions.eq("hasHumanLabels",true));
		//
		List<Document> t = atmsb.getByCriteriaWithAliasByOrder(newCriterion, "", orderBy, 100, aliasTable, aliasCriterion);
		Long id1 = t.get(0).getDocumentID();
		Long id2 = t.get(1).getDocumentID();
		boolean actual = false;
		if(id2>id1)
			actual = true;
		//
		assertEquals(true, actual);
	}
	//
	@Test
	public void updateTest() {
		Document document = atmsb.getById(new Long(21));
		if (document == null) {
			document = new Document();
			document.setDocumentID(new Long(21));
			document.setCrisisID(new Long(121));
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
			atmsb.save(document);
		}
		Document doc = atmsb.getById(new Long(21));
		doc.setHasHumanLabels(true);
		atmsb.update(doc);
		//
		Document updatedDoc = atmsb.getById(new Long(21));
		//
		assertNotNull(updatedDoc);
		assertEquals(true, updatedDoc.isHasHumanLabels());
		//
		atmsb.delete(document);
	}
	//
	@Test
	public void updateListTest() {
		Document doc1 = atmsb.getById(new Long(4579266));
		doc1.setLanguage("ar");
		Document doc2 = atmsb.getById(new Long(4579316));
		doc2.setHasHumanLabels(false);
		//
		List<Document> updateCollection = new ArrayList<Document>();
		updateCollection.add(doc1);
		updateCollection.add(doc2);
		atmsb.update(updateCollection);
		//
		Document updateDoc = atmsb.getById(new Long(4579316));
		assertNotNull(updateDoc);
		assertEquals(false, updateDoc.isHasHumanLabels());
	}
	//
	@Test
	public void saveTest() {
		Document document = new Document();
		document.setDocumentID(new Long(20));
		document.setCrisisID(new Long(121));
		document.setHasHumanLabels(true);
		document.setEvaluationSet(true);
		document.setValueAsTrainingSample(new Double(0.5));
		document.setReceivedAt(new Date());
		document.setSourceIP(2130706689L);
		document.setLanguage("en");
		document.setDoctype("Tweet");
		document.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");

		atmsb.save(document);
		Document result = atmsb.getById(new Long(20));
		assertNotNull(result);
		assertEquals(new Long(20), result.getDocumentID());
		atmsb.delete(document);
	}
	//
	@Test
	public void mergeTest() {
		Document document = new Document();
		document.setDocumentID(new Long(50));
		document.setCrisisID(new Long(121));
		document.setEvaluationSet(true);
		document.setValueAsTrainingSample(new Double(0.5));
		document.setReceivedAt(new Date());
		document.setSourceIP(2130706689L);
		document.setLanguage("en");
		document.setDoctype("Tweet");
		document.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		atmsb.save(document);
		Document doc = atmsb.getById(new Long(50));
		doc.setHasHumanLabels(true);
		atmsb.merge(doc);
		//
		Document actual = atmsb.getById(new Long(50));
		assertNotNull(actual);
		assertEquals(true, actual.isHasHumanLabels());
		atmsb.delete(doc);
	}
	//
	@Test
	public void mergeListTest() {
		Document document = new Document();
		document.setDocumentID(new Long(51));
		document.setCrisisID(new Long(121));
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
		document2.setDocumentID(new Long(52));
		document2.setCrisisID(new Long(121));
		document2.setEvaluationSet(true);
		document2.setValueAsTrainingSample(new Double(0.5));
		document2.setReceivedAt(new Date());
		document2.setSourceIP(2130706689L);
		document2.setLanguage("en");
		document2.setDoctype("Tweet");
		document2.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document2.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		List<Document> collection = new ArrayList<Document>();
		collection.add(document);
		collection.add(document2);
		atmsb.save(collection);
		//
		Document doc = atmsb.getById(new Long(51));
		doc.setHasHumanLabels(true);
		Document doc2 = atmsb.getById(new Long(52));
		doc2.setHasHumanLabels(true);
		List<Document> testCollection = new ArrayList<Document>();
		testCollection.add(doc);
		testCollection.add(doc2);
		//
		atmsb.merge(testCollection);
		//
		Document actual = atmsb.getById(new Long(52));
		assertNotNull(actual);
		assertEquals(true, actual.isHasHumanLabels());
		//
		atmsb.delete(doc);
		atmsb.delete(doc2);
	}
	//
	@Test
	public void saveListTest() {
		Document document = new Document();
		document.setDocumentID(new Long(28));
		document.setCrisisID(new Long(121));
		document.setHasHumanLabels(true);
		document.setEvaluationSet(true);
		document.setValueAsTrainingSample(new Double(0.5));
		document.setReceivedAt(new Date());
		document.setSourceIP(2130706689L);
		document.setLanguage("en");
		document.setDoctype("Tweet");
		document.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		Document document1 = new Document();
		document1.setDocumentID(new Long(23));
		document1.setCrisisID(new Long(122));
		document1.setHasHumanLabels(true);
		document1.setEvaluationSet(true);
		document1.setValueAsTrainingSample(new Double(0.5));
		document1.setReceivedAt(new Date());
		document1.setSourceIP(2130706689L);
		document1.setLanguage("en");
		document1.setDoctype("Tweet");
		document1.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document1.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		List<Document> list = new ArrayList<Document>();
		list.add(document);
		list.add(document1);
		//
		atmsb.save(list);
		Document savedDoc1 = atmsb.getById(new Long(23));
		//Document savedDoc2 = atmsb.getById(new Long(28));
		assertNotNull(savedDoc1);
		assertEquals(new Long(23), savedDoc1.getDocumentID());
		//
		//atmsb.delete(savedDoc1);
		//atmsb.delete(savedDoc2);
	}
	//
	@Test
	public void deleteTest() {
		Document document = new Document();
		document.setDocumentID(new Long(24));
		document.setCrisisID(new Long(121));
		document.setHasHumanLabels(true);
		document.setEvaluationSet(true);
		document.setValueAsTrainingSample(new Double(0.5));
		document.setReceivedAt(new Date());
		document.setSourceIP(2130706689L);
		document.setLanguage("en");
		document.setDoctype("Tweet");
		document.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		//
		atmsb.save(document);
		atmsb.delete(document);
		//
		Document deletedDoc = atmsb.getById(new Long(24));
		assertNull("document should be deleted by (delete) method", deletedDoc);
		assertEquals(null, deletedDoc);
	}
	//
	@Test
	public void deleteListTest() {
		/*Document document = new Document();
		document.setDocumentID(new Long(65));
		document.setCrisisID(new Long(121));
		document.setHasHumanLabels(true);
		document.setEvaluationSet(true);
		document.setValueAsTrainingSample(new Double(0.5));
		document.setReceivedAt(new Date());
		document.setSourceIP(2130706689L);
		document.setLanguage("en");
		document.setDoctype("Tweet");
		document.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		
		Document document1 = new Document();
		document1.setDocumentID(new Long(66));
		document1.setCrisisID(new Long(121));
		document1.setHasHumanLabels(true);
		document1.setEvaluationSet(true);
		document1.setValueAsTrainingSample(new Double(0.5));
		document1.setReceivedAt(new Date());
		document1.setSourceIP(2130706689);
		document1.setLanguage("en");
		document1.setDoctype("Tweet");
		document1.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document1.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
	*/
		Document document = atmsb.getById(new Long(23));
		Document document1 = atmsb.getById(new Long(28));
		List<Document> collection = new ArrayList<Document>();
		collection.add(document);
		collection.add(document1);
		atmsb.save(collection);
		//
		atmsb.delete(collection);   
		//
		Document deleteDoc = atmsb.getById(new Long(66));
		assertNull("document should be deleted by (delete(List)) method", deleteDoc);
		assertEquals(null, deleteDoc);
	}
	//
	@Test
	public void deleteByCriteria() {
		Document document1 = new Document();
		document1.setDocumentID(new Long(68));
		document1.setCrisisID(new Long(121));
		document1.setHasHumanLabels(true);
		document1.setEvaluationSet(true);
		document1.setValueAsTrainingSample(new Double(0.5));
		document1.setReceivedAt(new Date());
		document1.setSourceIP(2130706689L);
		document1.setLanguage("en");
		document1.setDoctype("Tweet");
		document1.setData("{\"filter_level\":\"none\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,\"id\":445125862137278464,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:10 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,\"text\":\"\u00A6 http://t.co/RLAE3KqGSl 618  #Andaman #and #Nicobar #Islands, #remote #Indian #archipelago, #now #part #of #MH370 #...  #On #Friday, #Reut\u2026\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"Andaman\",\"indices\":[30,38]},{\"text\":\"and\",\"indices\":[39,43]},{\"text\":\"Nicobar\",\"indices\":[44,52]},{\"text\":\"Islands\",\"indices\":[53,61]},{\"text\":\"remote\",\"indices\":[63,70]},{\"text\":\"Indian\",\"indices\":[71,78]},{\"text\":\"archipelago\",\"indices\":[79,91]},{\"text\":\"now\",\"indices\":[93,97]},{\"text\":\"part\",\"indices\":[98,103]},{\"text\":\"of\",\"indices\":[104,107]},{\"text\":\"MH370\",\"indices\":[108,114]},{\"text\":\"On\",\"indices\":[121,124]},{\"text\":\"Friday\",\"indices\":[125,132]},{\"text\":\"Reut\",\"indices\":[134,139]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://buff.ly/1fZG9en\",\"indices\":[2,24],\"display_url\":\"buff.ly/1fZG9en\",\"url\":\"http://t.co/RLAE3KqGSl\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"/\u003ca href=\"http://bufferapp.com\" rel=\"nofollow\"u003eBufferu003cau003e\",\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125862137278464\",\"user\":{\"location\":\"Ohio\",\"default_profile\":true,\"statuses_count\":39198,\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"id\":2287659854,\"following\":null,\"favourites_count\":0,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"R.I.P to the feelings that I had for you.\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Sofia\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Sun Jan 12 05:13:21 +0000 2014\",\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":1458,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"notifications\":null,\"friends_count\":1670,\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"sofiaorden\",\"id_str\":\"2287659854\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/422235563899355136/YF-XDyig_normal.jpeg\",\"is_translator\":false,\"listed_count\":3}}");
		document1.setWordFeatures("{\"words\":[\"#friday_#reut\",\"#remote_#indian\",\"#of\",\"#now\",\"#and_#nicobar\",\"#mh370\",\"618_#andaman\",\"#_#on\",\"#nicobar_#islands\",\"#of_#mh370\",\"#on_#friday\",\"#islands_#remote\",\"#andaman_#and\",\"#reut\",\"#part\",\"#andaman\",\"#indian_#archipelago\",\"#part_#of\",\"#mh370_#\",\"#now_#part\",\"#\",\"#friday\",\"\u00A6_618\",\"\u00A6\",\"#archipelago\",\"#nicobar\",\"#archipelago_#now\",\"#islands\",\"#indian\",\"#and\",\"618\",\"#remote\",\"#on\"]}");
		atmsb.save(document1);
		//
		Criterion newCriterion = Restrictions.conjunction()
				.add(Restrictions.eq("documentID",new Long(68)))
				.add(Restrictions.eq("hasHumanLabels",true));
		atmsb.deleteByCriteria(newCriterion);
		Document deletedDoc = atmsb.getById(new Long(68));
		//
		assertNull("document should be deleted by (deleteByCriteria) method", deletedDoc);
		assertEquals(null, deletedDoc);
	}
}
