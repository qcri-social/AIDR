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
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.ModelNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.ModelNominalLabelIdDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.entities.model.Model;

public class TestModelNominalLabelResourceFacadeImp {
	
	private static ModelNominalLabelResourceFacadeImp modelNominalLabelResourceFacadeImp;
	private static NominalLabelResourceFacadeImp nominalLabelResourceFacadeImp;
	private static ModelResourceFacadeImp modelResourceFacadeImp;
	private static ModelFamilyResourceFacadeImp modelFamilyResourceFacadeImp;
	private static CollectionResourceFacadeImp crisisResourceFacadeImp;
	private static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	private static NominalAttributeResourceFacadeImp nominalAttributeResourceFacadeImp;
	private static UsersResourceFacadeImp userResourceFacadeImp;
	private static EntityManager entityManager;
	
	private UsersDTO user;
	private ModelFamilyDTO modelFamilyDTO;
	private CollectionDTO crisisDTO;
	private CrisisTypeDTO crisisTypeDTO; 
	private Long modelID;
	private ModelNominalLabelDTO modelNominalLabel;
	private NominalLabelDTO nominalLabelDTO;
	private static Logger logger = Logger.getLogger("db-manager-log");
	@BeforeClass
	public static void setUpBeforeClass() {
		
		modelNominalLabelResourceFacadeImp = new ModelNominalLabelResourceFacadeImp();
		nominalLabelResourceFacadeImp = new NominalLabelResourceFacadeImp();
		modelResourceFacadeImp = new ModelResourceFacadeImp();
		modelFamilyResourceFacadeImp = new ModelFamilyResourceFacadeImp();
		crisisResourceFacadeImp = new CollectionResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		nominalAttributeResourceFacadeImp = new NominalAttributeResourceFacadeImp();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		
		modelNominalLabelResourceFacadeImp.setEntityManager(entityManager);
		nominalLabelResourceFacadeImp.setEntityManager(entityManager);
		modelResourceFacadeImp.setEntityManager(entityManager);
		modelFamilyResourceFacadeImp.setEntityManager(entityManager);
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		userResourceFacadeImp.setEntityManager(entityManager);
		nominalAttributeResourceFacadeImp.setEntityManager(entityManager);
	}
	
	@Before
	public void setUp() {
		try {
			
			// fetch crisis type 
			crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(10L);
			
			user = new UsersDTO("userDBTest"+new Date(), "normal"+ new Date());
			entityManager.getTransaction().begin();
			user = userResourceFacadeImp.addUser(user);
			entityManager.getTransaction().commit();
			
			// insert crisis type
			crisisDTO = new CollectionDTO("tesName"+new Date(), "testCode"+new Date(), false, false, crisisTypeDTO, user);
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
			
			Model model = new Model();
			model.setModelFamily(modelFamilyDTO.toEntity());
			model.setAvgAuc(0);
			model.setAvgPrecision(1);
			model.setAvgRecall(1);
			model.setIsCurrentModel(true);
			model.setTrainingCount(1);
			model.setTrainingTime(new Date());
			
			// insert model
			entityManager.getTransaction().begin();
			modelID = modelResourceFacadeImp.save(model);
			entityManager.getTransaction().commit();
			
			assertNotNull(modelID);
			
			ModelDTO modelDTO = modelResourceFacadeImp.getModelByID(modelID);
			nominalLabelDTO = nominalLabelResourceFacadeImp.getNominalLabelByCode("informative");
			
			modelNominalLabel = new ModelNominalLabelDTO();
			modelNominalLabel.setNominalLabelDTO(nominalLabelDTO);
			modelNominalLabel.setNominalAttributeId(1L);
			modelNominalLabel.setModelDTO(modelDTO);
			modelNominalLabel.setIdDTO(new ModelNominalLabelIdDTO(modelID, nominalLabelDTO.getNominalLabelId()));
			
			// save nominal label
			entityManager.getTransaction().begin();
			modelNominalLabel = modelNominalLabelResourceFacadeImp.addModelNominalLabel(modelNominalLabel);
			entityManager.getTransaction().commit();
			
			assertTrue("ModelNominalLabel Created with id : " + modelNominalLabel.getNominalAttributeId(), modelNominalLabel.getIdDTO() != null);
			
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while accessing/inserting data "+ex.getMessage());
		}
	}

	@After
	public void tearDown() {
		
		// tear down data
		
		try {
			if (modelNominalLabel != null) {
				entityManager.getTransaction().begin();
				modelNominalLabelResourceFacadeImp.deleteModelNominalLabel(modelNominalLabel);
				entityManager.getTransaction().commit();
			}
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while deleting model nominal label "+e.getMessage());
		}
		Model model = modelResourceFacadeImp.getById(modelID);
		assertNotNull(model);
		
		modelResourceFacadeImp.delete(model);
		
		model = modelResourceFacadeImp.getById(modelID);
		assertTrue(model == null);
		
		try {
			List<ModelFamilyDTO> modelFamilyDTOs = modelFamilyResourceFacadeImp.getAllModelFamiliesByCrisis(crisisDTO.getCrisisID());
			if (modelFamilyDTOs != null && modelFamilyDTOs.size() > 0) {
				
				for(ModelFamilyDTO modelFamilyDTO : modelFamilyDTOs) {
					entityManager.getTransaction().begin();
					modelFamilyResourceFacadeImp.deleteModelFamily(modelFamilyDTO.getModelFamilyId());
					entityManager.getTransaction().commit();
				}
				
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
			if (user != null) {
				entityManager.getTransaction().begin();
				user = userResourceFacadeImp.getUserByName(user.getName());
				userResourceFacadeImp.deleteUser(user.getUserID());
				entityManager.getTransaction().commit();
				UsersDTO result = userResourceFacadeImp.getUserByName(user.getName());
				assertNull(result);
			}
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while deleting user "+ex.getMessage());
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		modelResourceFacadeImp.getEntityManager().close();
	}
	
	/**
	 *  Test to get all model nominal labels 
	 */
	@Test
	public void testGetAllModelNominalLabels() {
		List<ModelNominalLabelDTO> modelNominalLabelDTOs = modelNominalLabelResourceFacadeImp.getAllModelNominalLabels();
		assertNotNull(modelNominalLabelDTOs);
		
		assertTrue(modelNominalLabelDTOs.size() >= 1);
	}
	
	/**
	 *  Test to get all model nominal labels by model id
	 */
	/*@Test
	public void testGetAllModelNominalLabelsByModelID() {
		List<ModelNominalLabelDTO> modelNominalLabelDTOs = modelNominalLabelResourceFacadeImp.getAllModelNominalLabelsByModelID(modelID, crisisDTO.getCode());
		assertNotNull(modelNominalLabelDTOs);
		
		assertTrue(modelNominalLabelDTOs.size() == 1);
	}*/
		
	/**
	 *  Test to get all model nominal label by nominalLabel
	 */
	@Test
	public void testGetModelNominalLabelByID() {
		try {
			ModelNominalLabelDTO modelNominalLabelDTO = modelNominalLabelResourceFacadeImp.getModelNominalLabelByID(nominalLabelDTO.getNominalLabelId());
			assertNotNull(modelNominalLabelDTO);
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testGetModelNominalLabelByID "+e.getMessage());
		}
		
	}
	
	/**
	 *  Test to edit modelNominalLabel
	 */
	@Test
	public void testEditModelNominalLabel() {
		try {
			modelNominalLabel.setClassifiedDocumentCount(5);
			ModelNominalLabelDTO modelNominalLabelDTO = modelNominalLabelResourceFacadeImp.editModelNominalLabel(modelNominalLabel);
			assertNotNull(modelNominalLabelDTO);
			
			assertTrue("Data updated", modelNominalLabelDTO.getClassifiedDocumentCount() == 5);
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testEditModelNominalLabel "+e.getMessage());
		}
		
	}
	
	/**
	 *  Test to delete modelNominalLabel
	 */
	@Test
	public void testDeleteModelNominalLabel() {
		try {
			int recordsDeleted = modelNominalLabelResourceFacadeImp.deleteModelNominalLabel(modelNominalLabel);
			assertTrue("Records deleted", recordsDeleted == 1);
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testDeleteModelNominalLabel "+e.getMessage());
		}
		
	}
	
	/**
	 *  Test to check modelNominalLable existence
	 */
	@Test
	public void testIsModelNominalLabelExists() {
		try {
			boolean exists = modelNominalLabelResourceFacadeImp.isModelNominalLabelExists(nominalLabelDTO.getNominalLabelId());
			assertTrue("testIsModelNominalLabelExists", exists);
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testIsModelNominalLabelExists "+e.getMessage());
		}
		
	}

}
