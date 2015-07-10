/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelIdDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 * 
 * @author nalemadi
 */
public class TestDocumentNominalLabelResourceFacadeImp {
	static DocumentNominalLabelResourceFacadeImp documentNominalLabelResourceFacadeImp;
	static DocumentResourceFacadeImp documentResourceFacadeImp;
	static EntityManager entityManager;
	static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	static UsersResourceFacadeImp userResourceFacadeImp;
	static CrisisResourceFacadeImp crisisResourceFacadeImp;
	static DocumentDTO document;
	static CrisisDTO crisis;
	static UsersDTO user;
	static DocumentNominalLabelDTO documentNominalLabel;
	static NominalLabelResourceFacadeImp nominalLabelResourceFacadeImp;
	private static Logger logger = Logger.getLogger("db-manager-log");
	
	@BeforeClass
	public static void setUpClass() {
		documentNominalLabelResourceFacadeImp = new DocumentNominalLabelResourceFacadeImp();
		documentResourceFacadeImp = new DocumentResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		documentNominalLabelResourceFacadeImp.setEntityManager(entityManager);
		documentResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		crisisResourceFacadeImp = new CrisisResourceFacadeImp();
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		userResourceFacadeImp.setEntityManager(entityManager);
		nominalLabelResourceFacadeImp = new NominalLabelResourceFacadeImp();
		nominalLabelResourceFacadeImp.setEntityManager(entityManager);
		document = addDocument();
	}

	@AfterClass
	public static void tearDownClass() {
		if (document != null) {
			entityManager.getTransaction().begin();
			documentResourceFacadeImp.deleteDocument(document);
			entityManager.getTransaction().commit();

			if (crisis != null) {
				entityManager.getTransaction().begin();
				try {
					crisisResourceFacadeImp.deleteCrisis(crisis);
				} catch (PropertyNotSetException e) {
					e.printStackTrace();
				}
				entityManager.getTransaction().commit();
			}
		}
		try {	
			if (user != null) {
				entityManager.getTransaction().begin();
				user = userResourceFacadeImp.getUserByName(user.getName());
				userResourceFacadeImp.deleteUser(user.getUserID());
				entityManager.getTransaction().commit();
			}
		}catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while deleting user "+e.getMessage());
		}	
		documentNominalLabelResourceFacadeImp.getEntityManager().close();
	}

	@Before
	public void setUp() {
		try {
			documentNominalLabel = getDocumentNominalLabel();
			documentNominalLabel = documentNominalLabelResourceFacadeImp
					.addDocument(documentNominalLabel);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while adding document nominal label "+e.getMessage());
		}
	}

	@After
	public void tearDown() {
		if (documentNominalLabel != null) {
			documentNominalLabelResourceFacadeImp
			.deleteDocument(documentNominalLabel);
		}

	}

	private static DocumentNominalLabelDTO getDocumentNominalLabel() {
		DocumentNominalLabelDTO documentNominalLabel = new DocumentNominalLabelDTO();
		DocumentNominalLabelIdDTO idDTO = new DocumentNominalLabelIdDTO();
		idDTO.setUserId(1L);
		idDTO.setDocumentId(document.getDocumentID());
		idDTO.setNominalLabelId(1L);
		documentNominalLabel.setIdDTO(idDTO);
		return documentNominalLabel;
	}

	private static DocumentDTO addDocument() {
		DocumentDTO documentDTO = new DocumentDTO();
		try {
			CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
			user = new UsersDTO("userDBTest"+new Date(), "normal"+new Date());
			entityManager.getTransaction().begin();
			user = userResourceFacadeImp.addUser(user);
			entityManager.getTransaction().commit();
			CrisisDTO crisisDTO = new CrisisDTO("testCrisisName"+new Date(), "testCrisisCode"+new Date(), false, false, crisisTypeDTO, user);
			entityManager.getTransaction().begin();
			crisis = crisisResourceFacadeImp.addCrisis(crisisDTO);
			entityManager.getTransaction().commit();

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
			entityManager.getTransaction().begin();
			documentDTO = documentResourceFacadeImp.addDocument(documentDTO);
			entityManager.getTransaction().commit();
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while adding document "+e.getMessage());
		}
		return documentDTO;
	}

	/**
	 * Test of saveDocumentNominalLabel method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	/*
	 * @Test public void testSaveDocumentNominalLabel() throws Exception {
	 * DocumentNominalLabelDTO
	 * documentNominalLabel = new DocumentNominalLabelDTO();
	 * documentNominalLabel.setDocumentDTO(document);
	 * 
	 * DocumentNominalLabelIdDTO documentNominalLabelIdDTO = new
	 * DocumentNominalLabelIdDTO(); documentNominalLabelIdDTO.setUserId(1L);
	 * documentNominalLabelIdDTO.setDocumentId(document.getDocumentID());
	 * documentNominalLabelIdDTO.setNominalLabelId(1L);
	 * documentNominalLabel.setNominalLabelDTO
	 * (nominalLabelResourceFacadeImp.getNominalLabelByID(1L));
	 * documentNominalLabelResourceFacadeImp
	 * .saveDocumentNominalLabel(documentNominalLabel); }
	 */

	/**
	 * Test of foundDuplicate method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	/*
	 * @Test public void testFoundDuplicate() { try {
	 * DocumentNominalLabelDTO
	 * documentNominalLabel =
	 * documentNominalLabelResourceFacadeImp.getAllDocuments().get(0); boolean
	 * result =
	 * documentNominalLabelResourceFacadeImp.foundDuplicate(documentNominalLabel
	 * ); assertEquals(false, result); } catch (PropertyNotSetException ex) {
	 * fail("foundDuplicate failed");
	 * //Logger.getLogger(DocumentNominalLabelResourceFacadeImpTest
	 * .class.getName()).log(Level.SEVERE, null, ex); } }
	 */

	/**
	 * Test of addDocument method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	@Test
	public void testAddDocument() throws Exception {
		assertEquals(document.getDocumentID(), documentNominalLabel.getIdDTO()
				.getDocumentId());
	}

	/**
	 * Test of editDocument method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	/*
	 * @Test public void testEditDocument() throws Exception {
	 * documentNominalLabel =
	 * getDocumentNominalLabel(); entityManager.getTransaction().begin();
	 * documentNominalLabel =
	 * documentNominalLabelResourceFacadeImp.addDocument(documentNominalLabel);
	 * entityManager.getTransaction().commit(); Date date = new Date();
	 * documentNominalLabel.setTimestamp(date);
	 * entityManager.getTransaction().begin(); documentNominalLabel =
	 * documentNominalLabelResourceFacadeImp.editDocument(documentNominalLabel);
	 * entityManager.getTransaction().commit(); assertEquals(date,
	 * documentNominalLabel.getTimestamp()); }
	 */

	/**
	 * Test of deleteDocument method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	@Test
	public void testDeleteDocument() throws Exception {
		Integer result = documentNominalLabelResourceFacadeImp
				.deleteDocument(documentNominalLabel);
		assertEquals(Integer.valueOf(1), result);
		documentNominalLabel = null;
	}

	/**
	 * Test of findByCriteria method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	@Test
	public void testFindByCriteria() {
		try {
			String columnName = "id.documentId";
			Long value = documentNominalLabel.getIdDTO().getDocumentId();
			List<DocumentNominalLabelDTO> result = documentNominalLabelResourceFacadeImp
					.findByCriteria(columnName, value);
			assertNotNull(result);
			assertEquals(value, result.get(0).getIdDTO().getDocumentId());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding document nominal label by criteria "+ex.getMessage());
			fail("findByCriteria failed");
		}
	}

	/**
	 * Test of findDocumentByPrimaryKey method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	@Test
	public void testFindDocumentByPrimaryKey() {
		try {
			DocumentNominalLabelDTO result = documentNominalLabelResourceFacadeImp
					.findDocumentByPrimaryKey(documentNominalLabel.getIdDTO());
			assertEquals(documentNominalLabel.getIdDTO().getDocumentId(),
					result.getDocumentDTO().getDocumentID());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding document nominal label by primary key "+ex.getMessage());
			fail("findDocumentByPrimaryKey failed");
		}
	}

	/**
	 * Test of isDocumentExists method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	@Test
	public void testIsDocumentExists_DocumentNominalLabelIdDTO()
			throws Exception {
		boolean result = documentNominalLabelResourceFacadeImp
				.isDocumentExists(documentNominalLabel.getIdDTO());
		assertEquals(true, result);
	}

	/**
	 * Test of isDocumentExists method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	@Test
	public void testIsDocumentExists_Long() throws Exception {
		boolean result = documentNominalLabelResourceFacadeImp
				.isDocumentExists(documentNominalLabel.getIdDTO());
		assertEquals(true, result);

		boolean result2 = documentNominalLabelResourceFacadeImp
				.isDocumentExists(documentNominalLabel.getIdDTO()
						.getDocumentId());
		assertEquals(true, result2);
	}

	/**
	 * Test of getAllDocuments method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	@Test
	public void testGetAllDocuments() {
		try {
			List<DocumentNominalLabelDTO> result = documentNominalLabelResourceFacadeImp
					.getAllDocuments();
			assertNotNull(result);
			assertTrue(result.size() >= 1);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching all document nominal label "+ex.getMessage());
			fail("getAllDocuments failed");
		}
	}

	/**
	 * Test of findLabeledDocumentByID method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	@Test
	public void testFindLabeledDocumentByID() {
		try {
			DocumentNominalLabelDTO result = documentNominalLabelResourceFacadeImp
					.findLabeledDocumentByID(documentNominalLabel.getIdDTO()
							.getDocumentId());
			assertEquals(documentNominalLabel.getIdDTO().getDocumentId(),
					result.getDocumentDTO().getDocumentID());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding labelled documents by id "+ex.getMessage());
			fail("findLabeledDocumentByID failed");
		}
	}

	/**
	 * Test of getLabeledDocumentCollectionForNominalLabel method, of class
	 * DocumentNominalLabelResourceFacadeImp.
	 */
	@Test
	public void testGetLabeledDocumentCollectionForNominalLabel()
			throws Exception {
		List<DocumentNominalLabelDTO> result = documentNominalLabelResourceFacadeImp
				.getLabeledDocumentCollectionForNominalLabel(documentNominalLabel
						.getIdDTO().getNominalLabelId().intValue());
		assertNotNull(result);
		assertTrue(result.size() >= 1);
	}

}
