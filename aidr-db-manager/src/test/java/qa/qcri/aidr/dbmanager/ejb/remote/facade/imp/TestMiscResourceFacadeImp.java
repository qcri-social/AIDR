package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelIdDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;

public class TestMiscResourceFacadeImp {
    
	private static Logger logger = Logger.getLogger("db-manager-log");
	
	private static MiscResourceFacadeImp miscResourceFacadeImp;
	
	private static ModelFamilyResourceFacadeImp modelFamilyResourceFacadeImp;
	private static CollectionResourceFacadeImp crisisResourceFacadeImp;
	private static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	private static NominalAttributeResourceFacadeImp nominalAttributeResourceFacadeImp;
	private static UsersResourceFacadeImp userResourceFacadeImp;
	private static EntityManager entityManager;
	private static DocumentNominalLabelResourceFacadeImp documentNominalLabelResourceFacadeImp;
	private static DocumentResourceFacadeImp documentResourceFacadeImp;
	private static NominalLabelResourceFacadeImp nominalLabelResourceFacadeImp;
	
	private ModelFamilyDTO modelFamilyDTO;
	private CollectionDTO crisisDTO;
	private CrisisTypeDTO crisisTypeDTO; 
	private DocumentDTO documentDTO;
	private DocumentNominalLabelDTO documentNominalLabelDTO;
	private NominalLabelDTO nominalLabelDTO;
	private UsersDTO userDTO;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		
		miscResourceFacadeImp = new MiscResourceFacadeImp();
		documentNominalLabelResourceFacadeImp = new DocumentNominalLabelResourceFacadeImp();
		documentResourceFacadeImp = new DocumentResourceFacadeImp();
		modelFamilyResourceFacadeImp = new ModelFamilyResourceFacadeImp();
		crisisResourceFacadeImp = new CollectionResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		nominalAttributeResourceFacadeImp = new NominalAttributeResourceFacadeImp();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		nominalLabelResourceFacadeImp = new NominalLabelResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		
		miscResourceFacadeImp.setEntityManager(entityManager);
		modelFamilyResourceFacadeImp.setEntityManager(entityManager);
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		userResourceFacadeImp.setEntityManager(entityManager);
		nominalAttributeResourceFacadeImp.setEntityManager(entityManager);
		documentResourceFacadeImp.setEntityManager(entityManager);
		documentNominalLabelResourceFacadeImp.setEntityManager(entityManager);
		nominalLabelResourceFacadeImp.setEntityManager(entityManager);
	}
	
	@Before
	public void setUp() {
		try {
			
			// fetch crisis type 
			crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(10L);
			
			// add test user
			userDTO = new UsersDTO("testUser"+new Date(), "testRole"+new Date());
			entityManager.getTransaction().begin();
			userDTO = userResourceFacadeImp.addUser(userDTO);
			entityManager.getTransaction().commit();
			
			// insert crisis type
			crisisDTO = new CollectionDTO("tesName"+new Date(), "testCode" +new Date(), false, false, crisisTypeDTO, userDTO, userDTO);
			entityManager.getTransaction().begin();
			crisisDTO = crisisResourceFacadeImp.addCrisis(crisisDTO);
			entityManager.getTransaction().commit();
		
			// fetch nominal attribute : nominal attribute with id : 1 already exists
			NominalAttributeDTO nominalAttributeDTO = nominalAttributeResourceFacadeImp.getAttributeByID(1L);

			modelFamilyDTO = new ModelFamilyDTO();
			modelFamilyDTO.setCrisisDTO(crisisDTO);
			modelFamilyDTO.setIsActive(true);
			modelFamilyDTO.setNominalAttributeDTO(nominalAttributeDTO);

			// insert nominal attribute
			entityManager.getTransaction().begin();
			boolean success = modelFamilyResourceFacadeImp.addCrisisAttribute(modelFamilyDTO);
			entityManager.getTransaction().commit();
			assertTrue(success);
			
			List<ModelFamilyDTO> modelFamilyDTOs = modelFamilyResourceFacadeImp.getAllModelFamiliesByCrisis(crisisDTO.getCrisisID());
			assertNotNull(modelFamilyDTOs);
			
			modelFamilyDTO = modelFamilyDTOs.get(0);
			
			nominalLabelDTO = nominalLabelResourceFacadeImp.getNominalLabelByCode("informative");
			
			// creating document
			documentDTO = createDocument();
			
			// add document nominal label
			documentNominalLabelDTO = getDocumentNominalLabel();
			documentNominalLabelDTO = documentNominalLabelResourceFacadeImp
					.addDocument(documentNominalLabelDTO);
			
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while accessing/inserting data "+ex.getMessage());
		}
	}

	@After
	public void tearDown() {
		
		// tear down data
		
		if(documentNominalLabelDTO != null) {
			entityManager.getTransaction().begin();
			documentNominalLabelResourceFacadeImp.deleteDocument(documentNominalLabelDTO);
			entityManager.getTransaction().commit();
		}
		
		if(documentDTO != null) {
			entityManager.getTransaction().begin();
			documentResourceFacadeImp.deleteDocument(documentDTO);
			entityManager.getTransaction().commit();
		}
		
		try {
			if(modelFamilyDTO != null) {
				entityManager.getTransaction().begin();
				modelFamilyResourceFacadeImp.deleteModelFamily(modelFamilyDTO.getModelFamilyId());
				entityManager.getTransaction().commit();
			}
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while deleting model family "+ex.getMessage());
		}
		
		try {
			if (crisisDTO != null) {
				entityManager.getTransaction().begin();
				crisisResourceFacadeImp.deleteCrisis(crisisDTO);
				entityManager.getTransaction().commit();
				CollectionDTO result = crisisResourceFacadeImp.getCrisisByCode(crisisDTO.getCode());
				assertNull(result);
			}
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while deleting crisis "+ex.getMessage());
		}
		try {
			if (userDTO != null) {
				entityManager.getTransaction().begin();
				userResourceFacadeImp.deleteUser(userDTO.getUserID());
				entityManager.getTransaction().commit();
				UsersDTO result = userResourceFacadeImp.getUserByName(userDTO.getName());
				assertNull(result);
			}
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while deleting user "+ex.getMessage());
		}
		
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		miscResourceFacadeImp.getEntityManager().close();
	}
	
	/**
	 *  Test to get all models 
	 */// TODO Class cast exception
	@Test
	public void testGetTraningDataByCrisisAndAttribute() {
		List<TrainingDataDTO> trainingDataDTOs = miscResourceFacadeImp.getTraningDataByCrisisAndAttribute(crisisDTO.getCrisisID(), modelFamilyDTO.getModelFamilyId(), 0, 1, null, null);
		assertNotNull(trainingDataDTOs);
		
		assertTrue("Training DTOs", trainingDataDTOs.size() == 1);
		
	}
	
	private DocumentNominalLabelDTO getDocumentNominalLabel() {
		documentNominalLabelDTO = new DocumentNominalLabelDTO();
		DocumentNominalLabelIdDTO idDTO = new DocumentNominalLabelIdDTO();
		idDTO.setUserId(userDTO.getUserID());
		idDTO.setDocumentId(documentDTO.getDocumentID());
		idDTO.setNominalLabelId(nominalLabelDTO.getNominalLabelId());
		documentNominalLabelDTO.setIdDTO(idDTO);
		documentNominalLabelDTO.setNominalLabelDTO(nominalLabelDTO);
		documentNominalLabelDTO.setDocumentDTO(documentDTO);
		documentNominalLabelDTO.setTimestamp(new Date());
		return documentNominalLabelDTO;
	}
	
	private DocumentDTO createDocument() throws PropertyNotSetException {
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
		documentDTO.setCrisisDTO(crisisDTO);
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
		
		return documentDTO;
	}
}
