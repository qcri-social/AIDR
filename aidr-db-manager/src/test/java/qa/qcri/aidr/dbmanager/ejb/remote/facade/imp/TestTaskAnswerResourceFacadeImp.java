/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 * 
 * @author Kushal
 */
public class TestTaskAnswerResourceFacadeImp {
	static EntityManager entityManager;
	static NominalLabelDTO nominalLabel;
	static NominalAttributeDTO nominalAttribute;
	static UsersDTO user;
	static CrisisDTO crisis;
	static DocumentDTO documentDTO;
	static TaskAnswerDTO taskAnswer;
	static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	static UsersResourceFacadeImp userResourceFacadeImp;
	static DocumentResourceFacadeImp documentResourceFacadeImp;
	static CrisisResourceFacadeImp crisisResourceFacadeImp;
	static TaskAnswerResourceFacadeImp taskAnswerResourceFacadeImp;
	private static Logger logger = Logger.getLogger("db-manager-log");
	
	@BeforeClass
	public static void setUpClass() {
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		
		taskAnswerResourceFacadeImp = new TaskAnswerResourceFacadeImp();
		taskAnswerResourceFacadeImp.setEntityManager(entityManager);
		documentResourceFacadeImp = new DocumentResourceFacadeImp();
		crisisResourceFacadeImp = new CrisisResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		documentResourceFacadeImp.setEntityManager(entityManager);
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		userResourceFacadeImp.setEntityManager(entityManager);
	}

	@Before
	public void setUp() {
		user = new UsersDTO("userDBTest"+new Date(), "normal"+new Date());
		entityManager.getTransaction().begin();
		user = userResourceFacadeImp.addUser(user);
		entityManager.getTransaction().commit();
		
		try {
			CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
			CrisisDTO crisisDTO = new CrisisDTO("testCrisisName"+new Date(), "testCrisisCode" + new Date(), false, crisisTypeDTO, user);
			entityManager.getTransaction().begin();
			crisis = crisisResourceFacadeImp.addCrisis(crisisDTO);
			entityManager.getTransaction().commit();
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while creating crisis "+e.getMessage());
		}
		getDoc();
		entityManager.getTransaction().begin();
		documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
		entityManager.getTransaction().commit();
		
		taskAnswer = new TaskAnswerDTO(documentDTO.getDocumentID(), user.getUserID(), "test_sample_answer");
		taskAnswer.setFromTrustedUser(true);
		Date date = new Date();
		taskAnswer.setTimestamp(date);
		System.out.println(taskAnswer.toString());
	}

	@AfterClass
	public static void shutDown() throws Exception {
		
		taskAnswerResourceFacadeImp.getEntityManager().close();
	}

	@After
	public void tearDown() {
		if(taskAnswer!=null){
			entityManager.getTransaction().begin();
			taskAnswerResourceFacadeImp.undoTaskAnswer(documentDTO.getDocumentID(), user.getUserID());
			entityManager.getTransaction().commit();
		}
		
		if (documentDTO != null) {
			entityManager.getTransaction().begin();
			documentResourceFacadeImp.deleteDocument(documentDTO);
			entityManager.getTransaction().commit();
		}
		if (crisis!= null) {
			
			try {
				entityManager.getTransaction().begin();
				crisisResourceFacadeImp.deleteCrisis(crisis);
				entityManager.getTransaction().commit();
			} catch (PropertyNotSetException e) {
				logger.error("PropertyNotSetException while deleting crisis "+e.getMessage());
			}
			
		}
		if (user != null) {
			entityManager.getTransaction().begin();
			userResourceFacadeImp.deleteUser(user.getUserID());
			entityManager.getTransaction().commit();
		}
	}
	
	private static void getDoc() {
		documentDTO = new DocumentDTO();
		String tweet = "\"filter_level\":\"medium\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,"
				+ "\"id\":445125937915387905,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:28 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,"
				+ "\"text\":\"'Those in the #cockpit' behind #missing #flight? http://t.co/OYHvM1t0CT\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"cockpit\",\"indices\":[14,22]},{\"text\":\"missing\","
				+ "\"indices\":[31,39]},{\"text\":\"flight\",\"indices\":[40,47]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://www.cnn.com/2014/03/15/world/asia/malaysia-airlines-plane/index.html\""
				+ ",\"indices\":[49,71],\"display_url\":\"cnn.com/2014/03/15/wor\u2026\",\"url\":\"http://t.co/OYHvM1t0CT\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\""
				+ ",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"\",\"favorited\":false,"
				+ "\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125937915387905\",\"user\":{\"location\":\"Mexico, Distrito Federal. \",\"default_profile\":true,\"statuses_count\":1033,"
				+ "\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/135306436/1394809176\",\"id\":135306436,\"following\":null,"
				+ "\"favourites_count\":6,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"Licenciado en derecho, he ocupado cargos dentro de la industria privada as\u00ED como dentro de la Administraci\u00F3n P\u00FAblica, tanto local (GDF), como Federal.\","
				+ "\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Leonardo Larraga\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Tue Apr 20 23:12:25 +0000 2010\","
				+ "\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":726,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/440767007290429441/GkHsYcJj_normal.jpeg\","
				+ "\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\","
				+ "\"follow_request_sent\":null,\"url\":\"http://instagram.com/larraga_ld\",\"utc_offset\":-21600,\"time_zone\":\"Mexico City\",\"notifications\":null,\"friends_count\":150,\"profile_use_background_image\":true,"
				+ "\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"larraga_ld\",\"id_str\":\"135306436\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/440767007290429441/GkHsYcJj_normal.jpeg\","
				+ "\"is_translator\":false,\"listed_count\":0}}";
		String word = "{\"words\":[\"#prayformh370\"]}";
		documentDTO.setCrisisDTO(crisis);
		documentDTO.setHasHumanLabels(false);
		documentDTO.setIsEvaluationSet(true);
		documentDTO.setReceivedAt(new Date());
		documentDTO.setLanguage("en");
		documentDTO.setDoctype("Tweet");
		documentDTO.setData(tweet);
		documentDTO.setWordFeatures(word);
		documentDTO.setValueAsTrainingSample(0.5);
	}
	

	/**
	 * Test of insertTaskAnswer method, of class TaskAnswerResourceFacadeImp.
	 */
	@Test
	public void testInsertTaskAnswer() {
		System.out.println("insertTaskAnswer");
		taskAnswer.setAnswer("test_sample_answer1");
		entityManager.getTransaction().begin();
		taskAnswer = taskAnswerResourceFacadeImp.insertTaskAnswer(taskAnswer);
		entityManager.getTransaction().commit();
		assertEquals(documentDTO.getDocumentID(),taskAnswer.getDocumentID());
	}
	
	/**
	 * Test of getTaskAnswer(documentId) method, of class TaskAnswerResourceFacadeImp.
	 */
	@Test
	public void testGetTaskAnswerByDocumentId() {
		System.out.println("getTaskAnswerByDocumentId");
		taskAnswer.setAnswer("test_sample_answer2");
		entityManager.getTransaction().begin();
		taskAnswer = taskAnswerResourceFacadeImp.insertTaskAnswer(taskAnswer);
		entityManager.getTransaction().commit();
		List<TaskAnswerDTO> result = taskAnswerResourceFacadeImp.getTaskAnswer(documentDTO.getDocumentID());
		assertTrue(result.size()>=1);
	}
	
	/**
	 * Test of getTaskAnswer(documentId, userId) method, of class TaskAnswerResourceFacadeImp.
	 */
	@Test
	public void testinsertTaskAnswerByDocumentIdAndUserId() {
		System.out.println("getTaskAnswerByDocumentIdAndUserId");
		taskAnswer.setAnswer("test_sample_answer3");
		entityManager.getTransaction().begin();
		taskAnswer = taskAnswerResourceFacadeImp.insertTaskAnswer(taskAnswer);
		entityManager.getTransaction().commit();
		TaskAnswerDTO result = taskAnswerResourceFacadeImp.getTaskAnswer(documentDTO.getDocumentID(), user.getUserID());
		assertEquals(documentDTO.getDocumentID(),result.getDocumentID());
	}

}
