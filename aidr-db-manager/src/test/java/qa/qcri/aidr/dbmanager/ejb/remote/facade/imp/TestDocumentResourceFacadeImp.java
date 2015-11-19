/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
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
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 * 
 * @author nalemadi
 */
public class TestDocumentResourceFacadeImp {
	//
	static DocumentResourceFacadeImp documentResourceFacadeImp;
	static CollectionResourceFacadeImp crisisResourceFacadeImp;
	static EntityManager entityManager;
	static DocumentDTO documentDTO;
	static UsersDTO user;
	static CollectionDTO crisis;
	static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	static UsersResourceFacadeImp userResourceFacadeImp;
	private static Logger logger = Logger.getLogger("db-manager-log");
	
	@BeforeClass
	public static void setUpClass() throws PropertyNotSetException {
		documentResourceFacadeImp = new DocumentResourceFacadeImp();
		crisisResourceFacadeImp = new CollectionResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		documentResourceFacadeImp.setEntityManager(entityManager);
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		userResourceFacadeImp.setEntityManager(entityManager);

		CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
		user = new UsersDTO("userDBTest"+new Date(), "normal");
		entityManager.getTransaction().begin();
		user = userResourceFacadeImp.addUser(user);
		entityManager.getTransaction().commit();
		CollectionDTO crisisDTO = new CollectionDTO("testCrisisName"+new Date(), "testCrisisCode"+new Date(), false, false, crisisTypeDTO, user, user);
		entityManager.getTransaction().begin();
		crisis = crisisResourceFacadeImp.addCrisis(crisisDTO);
		entityManager.getTransaction().commit();
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {

		documentDTO = getDoc();
	}

	@AfterClass
	public static void shutDown() throws Exception {
		if (crisis != null) {
			entityManager.getTransaction().begin();
			crisisResourceFacadeImp.deleteCrisis(crisis);
			entityManager.getTransaction().commit();
			CollectionDTO result = crisisResourceFacadeImp.getCrisisByCode(crisis.getCode());
			assertNull(result);
		}
		if (user != null) {
			entityManager.getTransaction().begin();
			user = userResourceFacadeImp.getUserByName(user.getName());
			userResourceFacadeImp.deleteUser(user.getUserID());
			entityManager.getTransaction().commit();
			UsersDTO result = userResourceFacadeImp.getUserByName(user.getName());
			assertNull(result);
		}
		documentResourceFacadeImp.getEntityManager().close();

	}

	@After
	public void tearDown() {
		if (documentDTO != null) {
			entityManager.getTransaction().begin();
			documentResourceFacadeImp.deleteDocument(documentDTO);
			entityManager.getTransaction().commit();
		}
	}

	private DocumentDTO getDoc() {
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
		return documentDTO;
	}

	/**
	 * Test of addDocument method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testAddDocument() {
		entityManager.getTransaction().begin();
		documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
		entityManager.getTransaction().commit();
		assertNotNull(documentDTO);
		assertEquals(documentDTO.getData(), documentDTO.getData());
	}

	/**
	 * Test of deleteNoLabelDocument method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testDeleteNoLabelDocument_DocumentDTO() {
		documentDTO.setHasHumanLabels(false);
		entityManager.getTransaction().begin();
		documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
		entityManager.getTransaction().commit();

		int expResult = 1;
		entityManager.getTransaction().begin();
		int delCount = documentResourceFacadeImp
				.deleteNoLabelDocument(documentDTO);
		entityManager.getTransaction().commit();
		assertEquals(expResult, delCount);
		documentDTO = null;
	}

	/**
	 * Test of deleteNoLabelDocument method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testDeleteNoLabelDocument_List() {
		List<DocumentDTO> collection = new ArrayList<DocumentDTO>();
		entityManager.getTransaction().begin();
		documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
		entityManager.getTransaction().commit();
		collection.add(documentDTO);

		int result = documentResourceFacadeImp
				.deleteNoLabelDocument(collection);
		assertEquals(1, result);
		documentDTO = null;
	}

	/**
	 * Test of deleteUnassignedDocument method, of class
	 * DocumentResourceFacadeImp.
	 */
	@Test
	public void testDeleteUnassignedDocument() {
		documentDTO.setHasHumanLabels(false);
		entityManager.getTransaction().begin();
		documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		int delCount = documentResourceFacadeImp
				.deleteUnassignedDocument(documentDTO);
		entityManager.getTransaction().commit();
		assertEquals(1, delCount);
		documentDTO = null;
	}

	/**
	 * Test of deleteUnassignedDocumentCollection method, of class
	 * DocumentResourceFacadeImp.
	 */
	@Test
	public void testDeleteUnassignedDocumentCollection() {
		List<Long> collection = new ArrayList<Long>();
		entityManager.getTransaction().begin();
		documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
		entityManager.getTransaction().commit();
		collection.add(documentDTO.getDocumentID());

		int result = documentResourceFacadeImp
				.deleteUnassignedDocumentCollection(collection);
		assertEquals(1, result);
		documentDTO = null;
	}

	/**
	 * Test of deleteStaleDocuments method, of class DocumentResourceFacadeImp.
	 */
	/*
	 * @Test public void testDeleteStaleDocuments() {
	 *  String joinType = ""; String
	 * joinTable = ""; String joinColumn = ""; String sortOrder = ""; String[]
	 * orderBy = null; String maxTaskAge = ""; String scanInterval = ""; int
	 * result = documentResourceFacadeImp.deleteStaleDocuments(joinType,
	 * joinTable, joinColumn, sortOrder, orderBy, maxTaskAge, scanInterval);
	 * assertEquals(1, result); }
	 */
	/**
	 * Test of editDocument method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testEditDocument() {
		try {
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();

			documentDTO.setHasHumanLabels(true);
			Date date = new Date();
			documentDTO.setReceivedAt(date);
			documentDTO = documentResourceFacadeImp.editDocument(documentDTO);
			assertEquals(date, documentDTO.getReceivedAt());

		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while editting the document "+ex.getMessage());
			fail("testEditDocument failed");
		}
	}

	/**
	 * Test of deleteDocument method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testDeleteDocument() {
		entityManager.getTransaction().begin();
		documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		Integer deleteDocCount = documentResourceFacadeImp
				.deleteDocument(documentDTO);
		entityManager.getTransaction().commit();
		assertEquals(Integer.valueOf(1), deleteDocCount);
		documentDTO = null;
	}

	/**
	 * Test of findByCriteria method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testFindByCriteria() {
		try {
			documentDTO.setHasHumanLabels(true);
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();

			String columnName = "hasHumanLabels";
			boolean value = true;
			List<DocumentDTO> result = documentResourceFacadeImp
					.findByCriteria(columnName, value);
			assertEquals(value, result.get(0).getHasHumanLabels());

		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding document by criteria "+ex.getMessage());
			fail("testFindByCriteria failed");
		}
	}

	/**
	 * Test of findDocumentByID method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testFindDocumentByID() {
		try {
			documentDTO.setIsEvaluationSet(true);
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();

			Long id = documentDTO.getDocumentID();
			DocumentDTO result = documentResourceFacadeImp.findDocumentByID(id);
			assertEquals(id, result.getDocumentID());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding document by id "+ex.getMessage());
			fail("testFindDocumentByID failed");
		}
	}

	/**
	 * Test of findDocumentsByCrisisID method, of class
	 * DocumentResourceFacadeImp.
	 */
	@Test
	public void testFindDocumentsByCrisisID() {
		try {
			documentDTO.setIsEvaluationSet(true);
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();

			Long crisisId = documentDTO.getCrisisDTO().getCrisisID();
			List<DocumentDTO> result = documentResourceFacadeImp
					.findDocumentsByCrisisID(crisisId);
			assertEquals(crisisId, result.get(0).getCrisisDTO().getCrisisID());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding document by crisisId "+ex.getMessage());
			fail("testFindDocumentsByCrisisID failed");
		}
	}

	/**
	 * Test of getAllDocuments method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testGetAllDocuments() {
		try {
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();

			List<DocumentDTO> result = documentResourceFacadeImp
					.getAllDocuments();
			assertNotNull(result);
			assertTrue(result.size() >= 1);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching all documents "+ex.getMessage());
			fail("testGetAllDocuments failed");
		}
	}

	/**
	 * Test of findLabeledDocumentsByCrisisID method, of class
	 * DocumentResourceFacadeImp.
	 */
	@Test
	public void testFindLabeledDocumentsByCrisisID() {
		try {
			documentDTO.setHasHumanLabels(true);
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();

			Long crisisId = documentDTO.getCrisisDTO().getCrisisID();
			List<DocumentDTO> result = documentResourceFacadeImp
					.findLabeledDocumentsByCrisisID(crisisId);
			assertNotNull(result);
			assertEquals(true, result.get(0).getHasHumanLabels());
			assertTrue(result.size() >= 1);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding Labelled documents by crisisId "+ex.getMessage());
			fail("testFindLabeledDocumentsByCrisisID failed");
		}
	}

	/**
	 * Test of findUnLabeledDocumentsByCrisisID method, of class
	 * DocumentResourceFacadeImp.
	 */
	@Test
	public void testFindUnLabeledDocumentsByCrisisID() {
		try {
			documentDTO.setHasHumanLabels(false);
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();

			Long crisisId = documentDTO.getCrisisDTO().getCrisisID();
			List<DocumentDTO> result = documentResourceFacadeImp
					.findUnLabeledDocumentsByCrisisID(crisisId);
			assertNotNull(result);
			assertEquals(false, result.get(0).getHasHumanLabels());
			assertTrue(result.size() >= 1);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding UnLabelled documents by crisisId "+ex.getMessage());
			fail("testFindUnLabeledDocumentsByCrisisID failed");
		}
	}

	/**
	 * Test of getDocumentCollectionForNominalLabel method, of class
	 * DocumentResourceFacadeImp.
	 */
	/*
	 * @Test public void testGetDocumentCollectionForNominalLabel() { try {
	 *  DocumentDTO
	 * doc = getDoc(); doc.setHasHumanLabels(true);
	 * entityManager.getTransaction().begin(); doc =
	 * documentResourceFacadeImp.addDocument(doc);
	 * entityManager.getTransaction().commit();
	 * 
	 * Criterion criterion = Restrictions.conjunction()
	 * .add(Restrictions.eq("documentId", doc.getDocumentID()))
	 * .add(Restrictions.eq("hasHumanLabels",true)); List<DocumentDTO> result =
	 * documentResourceFacadeImp
	 * .getDocumentCollectionForNominalLabel(criterion); assertNotNull(result);
	 * assertEquals(doc.getCrisisDTO().getCrisisID(),
	 * result.get(0).getCrisisDTO().getCrisisID());
	 * 
	 * entityManager.getTransaction().begin();
	 * documentResourceFacadeImp.deleteDocument(doc);
	 * entityManager.getTransaction().commit(); } catch (PropertyNotSetException
	 * ex) {
	 * //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log
	 * (Level.SEVERE, null, ex);
	 * fail("testGetDocumentCollectionForNominalLabel failed"); } }
	 */

	/**
	 * Test of getDocumentCollectionWithNominalLabelData method, of class
	 * DocumentResourceFacadeImp.
	 */
	/*
	 * @Test public void testGetDocumentCollectionWithNominalLabelData() { try {
	 *  Long
	 * nominalLabelID = 320L; List<DocumentDTO> result =
	 * documentResourceFacadeImp
	 * .getDocumentCollectionWithNominalLabelData(nominalLabelID); //
	 * assertNotNull(result); assertEquals(Long.valueOf(320),
	 * result.get(0).getNominalLabelDTO().getNominalLabelId()); } catch
	 * (Exception ex) { //
	 * Logger.getLogger(DocumentResourceFacadeImpTest.class.getName
	 * ()).log(Level.SEVERE, null, ex);
	 * fail("testGetDocumentCollectionWithNominalLabelData failed: "+ex); } }
	 */
	/**
	 * Test of getDocumentWithAllFieldsByID method, of class
	 * DocumentResourceFacadeImp.
	 */
	@Test
	public void testGetDocumentWithAllFieldsByID() {
		try {
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();

			Long id = documentDTO.getDocumentID();
			DocumentDTO result = documentResourceFacadeImp
					.getDocumentWithAllFieldsByID(id);
			assertEquals(id, result.getDocumentID());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching document with all fields by id "+ex.getMessage());
			fail("testGetDocumentWithAllFieldsByID failed");
		}
	}

	/**
	 * Test of isDocumentExists method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testIsDocumentExists() {
		try {
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();
			Long id = documentDTO.getDocumentID();
			boolean result = documentResourceFacadeImp.isDocumentExists(id);
			assertEquals(true, result);

			entityManager.getTransaction().begin();
			documentResourceFacadeImp.deleteDocument(documentDTO);
			entityManager.getTransaction().commit();
			documentDTO = null;
			boolean result2 = documentResourceFacadeImp.isDocumentExists(id);
			assertEquals(false, result2);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while checking whether document exist or not "+ex.getMessage());
			fail("testIsDocumentExists failed");
		}
	}

	/**
	 * Test of updateHasHumanLabel method, of class DocumentResourceFacadeImp.
	 */
	@Test
	public void testUpdateHasHumanLabel() {
		try {
			documentDTO.setHasHumanLabels(false);
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();

			documentResourceFacadeImp.updateHasHumanLabel(documentDTO);
			DocumentDTO result = documentResourceFacadeImp
					.findDocumentByID(documentDTO.getDocumentID());
			assertEquals(true, result.getHasHumanLabels());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while updating human label "+ex.getMessage());
			fail("testUpdateHasHumanLabel failed");
		}
	}
}
