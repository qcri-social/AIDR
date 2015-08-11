package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TaggersForCodes;

public class TestModelFamilyResourceFacadeImp {
	
	private static Logger logger = Logger.getLogger("db-manager-log");
	private static ModelFamilyResourceFacadeImp modelFamilyResourceFacadeImp;
	private static CrisisResourceFacadeImp crisisResourceFacadeImp;
	private static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	private static NominalAttributeResourceFacadeImp nominalAttributeResourceFacadeImp;
	private static UsersResourceFacadeImp userResourceFacadeImp;
	private static EntityManager entityManager;
	
	private UsersDTO user;
	private ModelFamilyDTO modelFamilyDTO;
	private CrisisDTO crisisDTO;
	private CrisisTypeDTO crisisTypeDTO; 
	
	@BeforeClass
	public static void setUpBeforeClass() {
		modelFamilyResourceFacadeImp = new ModelFamilyResourceFacadeImp();
		crisisResourceFacadeImp = new CrisisResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		nominalAttributeResourceFacadeImp = new NominalAttributeResourceFacadeImp();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		
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
			
			user = new UsersDTO("userDBTest"+new Date(), "normal" + new Date());
			entityManager.getTransaction().begin();
			user = userResourceFacadeImp.addUser(user);
			entityManager.getTransaction().commit();
			
			// insert crisis
			crisisDTO = new CrisisDTO("tesName"+new Date(), "testCode"+new Date(), false, false, crisisTypeDTO, user);
			entityManager.getTransaction().begin();
			crisisDTO = crisisResourceFacadeImp.addCrisis(crisisDTO);
			entityManager.getTransaction().commit();
		
			// fetch nominal attribute : nominal attribute with id : 1 already exists
			NominalAttributeDTO nominalAttributeDTO = nominalAttributeResourceFacadeImp.getAttributeByID(1L);

			modelFamilyDTO = new ModelFamilyDTO();
			modelFamilyDTO.setCrisisDTO(crisisDTO);
			modelFamilyDTO.setIsActive(true);
			modelFamilyDTO.setNominalAttributeDTO(nominalAttributeDTO);

			// insert model family
			entityManager.getTransaction().begin();
			boolean success = modelFamilyResourceFacadeImp.addCrisisAttribute(modelFamilyDTO);
			entityManager.getTransaction().commit();
			assertTrue(success);
			
			List<ModelFamilyDTO> modelFamilyDTOs = modelFamilyResourceFacadeImp.getAllModelFamiliesByCrisis(crisisDTO.getCrisisID());
			assertNotNull(modelFamilyDTOs);
			
			modelFamilyDTO = modelFamilyDTOs.get(0);
			
		} catch (PropertyNotSetException ex) {
			logger.error("Property not set exception while accessing/inserting data "+ex.getMessage());
		}
	}

	@After
	public void tearDown() {
		
		// tear down data
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
				CrisisDTO result = crisisResourceFacadeImp.getCrisisByCode(crisisDTO.getCode());
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
		modelFamilyResourceFacadeImp.getEntityManager().close();
	}
	
	/**
	 *  Test to get all model families
	 */
	@Test
	public void testGetAllModelFamilies() {
		try {
			List<ModelFamilyDTO> modelFamilyDTOs = modelFamilyResourceFacadeImp.getAllModelFamilies();
			assertNotNull(modelFamilyDTOs);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testGetAllModelFamilies "+e.getMessage());
		}
		
	}
	
	/**
	 * Test to fetch model families by crisis
	 */
	@Test
	public void testGetAllModelFamiliesByCrisis() {
		
		try {
			List<ModelFamilyDTO> modelFamilyDTOs = modelFamilyResourceFacadeImp.getAllModelFamiliesByCrisis(crisisDTO.getCrisisID());
			assertNotNull(modelFamilyDTOs);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testGetAllModelFamiliesByCrisis "+e.getMessage());
		}
		
	}
	
	/**
	 * Test to fetch model family by id
	 */
	@Test
	public void testGetModelFamilyByID() {
		try {
			ModelFamilyDTO modelFamily = modelFamilyResourceFacadeImp.getModelFamilyByID(modelFamilyDTO.getModelFamilyId());
			assertNotNull(modelFamily);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testGetModelFamilyByID "+e.getMessage());
		}
	}
	
	/**
	 * Test to insert model family
	 */
	/*@Test
	public void testAddCrisisAttribute() {
		//List<ModelFamilyDTO> getAllModelFamilies() throws PropertyNotSetException;;
		ModelFamilyDTO modelFamily = new ModelFamilyDTO();
		modelFamily.setCrisisDTO(crisisDTO);
		modelFamily.setIsActive(true);
		
		try {
			NominalAttributeDTO nominalAttributeDTO = nominalAttributeResourceFacadeImp.getAttributeByID(2L);
			modelFamily.setNominalAttributeDTO(nominalAttributeDTO);
		
			// inserting model family
			entityManager.getTransaction().begin();
			boolean success = modelFamilyResourceFacadeImp.addCrisisAttribute(modelFamily);
			entityManager.getTransaction().commit();
			assertTrue(success);
		} catch (PropertyNotSetException e){
			logger.log(Level.SEVERE, "PropertyNotSetException while executing testAddCrisisAttribute", e);
		}
	}*/
	
	/**
	 * Test for deleting model family
	 */
	@Test
	public void testDeleteModelFamily() {
	
		try {
			List<ModelFamilyDTO> modelFamilyDTOs = modelFamilyResourceFacadeImp.getAllModelFamiliesByCrisis(crisisDTO.getCrisisID());
			assertNotNull(modelFamilyDTOs);
			entityManager.getTransaction().begin();
			boolean success = modelFamilyResourceFacadeImp.deleteModelFamily(modelFamilyDTOs.get(0).getModelFamilyId());
			entityManager.getTransaction().commit();
			assertTrue(success);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while executing testDeleteModelFamily "+e.getMessage());
		}
	}
	
	/**
	 * Test for fetching TaggersByCodes
	 */
	@Test
	public void testGetTaggersByCodes() {
		List<String> codeList = new ArrayList<String>();
		codeList.add(crisisDTO.getCode());
		List<TaggersForCodes> taggersForCodes = modelFamilyResourceFacadeImp.getTaggersByCodes(codeList);
		assertNotNull(taggersForCodes);
	}
	
}