
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertNotNull;
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
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelHistoryWrapper;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelWrapper;
import qa.qcri.aidr.dbmanager.entities.model.Model;

public class TestModelResourceFacadeImp {
    
	private static ModelResourceFacadeImp modelResourceFacadeImp;
	private static ModelFamilyResourceFacadeImp modelFamilyResourceFacadeImp;
	private static CrisisResourceFacadeImp crisisResourceFacadeImp;
	private static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	private static NominalAttributeResourceFacadeImp nominalAttributeResourceFacadeImp;
	private static UsersResourceFacadeImp usersResourceFacadeImp;
	private static EntityManager entityManager;
	
	private UsersDTO user;
	private ModelFamilyDTO modelFamilyDTO;
	private CrisisDTO crisisDTO;
	private CrisisTypeDTO crisisTypeDTO; 
	private Long modelID;
	private static Logger logger = Logger.getLogger("db-manager-log");
	
	@BeforeClass
	public static void setUpBeforeClass() {
		
		modelResourceFacadeImp = new ModelResourceFacadeImp();
		modelFamilyResourceFacadeImp = new ModelFamilyResourceFacadeImp();
		crisisResourceFacadeImp = new CrisisResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		nominalAttributeResourceFacadeImp = new NominalAttributeResourceFacadeImp();
		usersResourceFacadeImp = new UsersResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		
		modelResourceFacadeImp.setEntityManager(entityManager);
		modelFamilyResourceFacadeImp.setEntityManager(entityManager);
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		usersResourceFacadeImp.setEntityManager(entityManager);
		nominalAttributeResourceFacadeImp.setEntityManager(entityManager);
	}
	
	@Before
	public void setUp() {
		try {
			
			// fetch crisis type 
			crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(10L);
			
			user = new UsersDTO("userDBTest"+new Date(), "normal"+new Date());
			entityManager.getTransaction().begin();
			user = usersResourceFacadeImp.addUser(user);
			entityManager.getTransaction().commit();
			
			// insert crisis type
			crisisDTO = new CrisisDTO("tesName"+new Date(), "testCode"+new Date(), false, crisisTypeDTO, user);
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
			//entityManager.getTransaction().begin();
			modelID = modelResourceFacadeImp.save(model);
			//entityManager.getTransaction().commit();
			
			assertNotNull(modelID);
			
		} catch (PropertyNotSetException ex) {
			logger.error("Property not set exception while accessing/inserting data "+ex.getMessage());
		}
	}

	@After
	public void tearDown() {
		
		// tear down data
		
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
			}
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while deleting crisis "+ex.getMessage());
		}
		try {
			if (user != null) {
				entityManager.getTransaction().begin();
				user = usersResourceFacadeImp.getUserByName(user.getName());
				usersResourceFacadeImp.deleteUser(user.getUserID());
				entityManager.getTransaction().commit();
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
	 *  Test to get all models 
	 */
	@Test
	public void testGetAllModels() {
		try {
			List<ModelDTO> modelDTOs = modelResourceFacadeImp.getAllModels();
			assertNotNull(modelDTOs);
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testGetAllModels "+e.getMessage());
		}
		
	}
	

	/**
	 *  Test to get all models 
	 */
	@Test
	public void testGetModelByID() {
		try {
			ModelDTO modelDTO = modelResourceFacadeImp.getModelByID(modelID);
			assertNotNull(modelDTO);
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testGetModelByID "+e.getMessage());
		}
		
	}
	
	/**
	 *  Test to get all models 
	 */
	@Test
	public void testGetModelCountByModelFamilyID() {
		try {
			Integer count = modelResourceFacadeImp.getModelCountByModelFamilyID(modelFamilyDTO.getModelFamilyId());
			assertNotNull(count);
			assertTrue(count == 1);
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testGetModelCountByModelFamilyID "+e.getMessage());
		}
		
	}
	
	/**
	 *  Test to get all models 
	 */
	@Test
	public void testGetModelByModelFamilyID() {
		try {
			List<ModelHistoryWrapper> historyWrappers = modelResourceFacadeImp.getModelByModelFamilyID(modelFamilyDTO.getModelFamilyId(), 0, 10);
			assertNotNull(historyWrappers);
			assertTrue(historyWrappers.size() == 1);
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testGetModelByModelFamilyID "+e.getMessage());
		}
		
	}
	
	/**
	 *  Test to get all models 
	 */
	@Test
	public void testGetModelByCrisisID() {
		try {
			List<ModelWrapper> modelWrappers = modelResourceFacadeImp.getModelByCrisisID(crisisDTO.getCrisisID());
			assertNotNull(modelWrappers);
			assertTrue(modelWrappers.size() == 1);
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testGetModelByCrisisID "+e.getMessage());
		}
		
	}
}
