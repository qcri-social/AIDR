/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.dom4j.util.UserDataAttribute;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

public class TestCrisisResourceFacadeImp {
	private static CollectionResourceFacadeImp crisisResourceFacadeImp;
	private static UsersResourceFacadeImp userResourceFacadeImp;
	private static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	private static NominalAttributeResourceFacadeImp nominalAttributeResourceFacadeImp;
	private static ModelFamilyResourceFacadeImp modelFamilyResourceFacadeImp;
	private static EntityManager entityManager;
	private static CollectionDTO editCrisis;
	private static Long crisisId;
	private static CollectionDTO crisisDTO;
	private static String crisisCode = "testCrisisCode"+new Date();
	private static String crisisName = "testCrisisName"+new Date();
	private static UsersDTO user;
	private static Logger logger = Logger.getLogger("db-manager-log");
	
	@BeforeClass
	public static void setUpClass() {
		crisisResourceFacadeImp = new CollectionResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		nominalAttributeResourceFacadeImp = new NominalAttributeResourceFacadeImp();
		modelFamilyResourceFacadeImp = new ModelFamilyResourceFacadeImp();
		modelFamilyResourceFacadeImp.setEntityManager(entityManager);
		nominalAttributeResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		userResourceFacadeImp.setEntityManager(entityManager);
	}

	@Before
	public void setUp() {
		CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
		
		user = new UsersDTO("userDBTest"+new Date(), "normal");
		entityManager.getTransaction().begin();
		user = userResourceFacadeImp.addUser(user);
		
		entityManager.getTransaction().commit();
		crisisDTO = new CollectionDTO(crisisName, crisisCode, false, false, crisisTypeDTO, user);
		entityManager.getTransaction().begin();
		crisisDTO = crisisResourceFacadeImp.addCrisis(crisisDTO);
		entityManager.getTransaction().commit();
		crisisId = crisisDTO.getCrisisID();
	}

	@After
	public void tearDown() {
		try {
			if (crisisDTO != null) {
				entityManager.getTransaction().begin();
				crisisResourceFacadeImp.deleteCrisis(crisisDTO);
				entityManager.getTransaction().commit();
				CollectionDTO result = crisisResourceFacadeImp.getCrisisByCode(crisisCode);
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
	public static void shutDown() throws Exception {
		if (crisisDTO != null)
			crisisResourceFacadeImp.deleteCrisis(crisisDTO);
		if (editCrisis != null)
			crisisResourceFacadeImp.deleteCrisis(editCrisis);

		crisisResourceFacadeImp.getEntityManager().close();
	}

	/**
	 * Test of addCrisis method, of class CrisisResourceFacadeImp.
	 */
	@Test
	public void testAddCrisis() {
		assertEquals(crisisName, crisisDTO.getName());
	}

	@Test
	public void testFindCrisisByID() {
		try {
			CollectionDTO crisisDTO = crisisResourceFacadeImp
					.findCrisisByID(crisisId);
			assertEquals(crisisName, crisisDTO.getName());
		} catch (PropertyNotSetException ex) {
			logger.error("Property not set exception while finding crisis by id "+ex.getMessage());
		}
	}

	/**
	 * Test of findByCriteria method, of class CrisisResourceFacadeImp.
	 */
	@Test
	public void testFindByCriteria() {
		try {
			String columnName = "code";
			String value = crisisCode;
			List<CollectionDTO> result = crisisResourceFacadeImp.findByCriteria(
					columnName, value);
			assertEquals(crisisCode, result.get(0).getCode());
		} catch (PropertyNotSetException ex) {
			logger.error("Property not set exception while finding crisis by criteria "+ex.getMessage());
		}
	}

	@Test
	public void testGetCrisisWithAllFieldsByID() {
		try {
			CollectionDTO crisis = crisisResourceFacadeImp
					.getCrisisWithAllFieldsByID(crisisId);
			assertEquals(crisisName, crisis.getName());
		} catch (PropertyNotSetException ex) {
			logger.error("Property not set exception while getting crisis with all fields by id "+ex.getMessage());
		}
	}

	/**
	 * Test of getCrisisByCode method, of class CrisisResourceFacadeImp.
	 */
	@Test
	public void testGetCrisisByCode() {
		try {
			CollectionDTO result = crisisResourceFacadeImp
					.getCrisisByCode(crisisCode);
			assertNotNull(result);
			assertEquals(crisisCode, result.getCode());
		} catch (PropertyNotSetException ex) {
			logger.error("Property not set exception while getting crisis by code "+ex.getMessage());
		}
	}

	/**
	 * Test of editCrisis method, of class CrisisResourceFacadeImp.
	 */
	@Test
	public void testEditCrisis() {
		try {
			editCrisis = crisisResourceFacadeImp.findCrisisByID(crisisId);
			editCrisis.setName(crisisName + "2");
			editCrisis.setCode(crisisCode + "2");
			entityManager.getTransaction().begin();
			CollectionDTO result = crisisResourceFacadeImp.editCrisis(editCrisis);
			entityManager.getTransaction().commit();

			assertEquals(crisisName + "2", result.getName());
			assertEquals(crisisCode + "2", result.getCode());

			// Modified to previous state
			editCrisis = crisisResourceFacadeImp.findCrisisByID(crisisId);
			editCrisis.setName(crisisName);
			editCrisis.setCode(crisisCode);

			entityManager.getTransaction().begin();
			result = crisisResourceFacadeImp.editCrisis(editCrisis);
			entityManager.getTransaction().commit();
			editCrisis = null;
		} catch (PropertyNotSetException ex) {
			logger.error("Property not set exception while editting crisis "+ex.getMessage());
		}
	}

	@Test
	public void testFindActiveCrisis() {
		try {
			List<CollectionDTO> list = crisisResourceFacadeImp.findActiveCrisis();
			assertEquals(false, list.get(0).isIsTrashed());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding all active crisis "+ex.getMessage());
		}
	}

	/**
	 * Test of getAllCrisis method, of class CrisisResourceFacadeImp.
	 */
	@Test
	public void testGetAllCrisis() {
		try {
			List<CollectionDTO> result = crisisResourceFacadeImp.getAllCrisis();
			assertNotNull(result);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while getting all crisis "+ex.getMessage());
		}
	}

	/**
	 * Test of getAllCrisisWithModelFamilies method, of class
	 * CrisisResourceFacadeImp.
	 */
	@Test
	public void testGetAllCrisisWithModelFamilies() {
		try {
			List<CollectionDTO> result = crisisResourceFacadeImp
					.getAllCrisisWithModelFamilies();
			assertNotNull(result.get(0).getModelFamiliesDTO());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while getting all crisis with model families "+ex.getMessage());
		}
	}

	@Test
	public void testGetAllCrisisWithModelFamilyNominalAttribute() {
		try {
			List<CollectionDTO> list = crisisResourceFacadeImp
					.getAllCrisisWithModelFamilyNominalAttribute();
			assertNotNull(list.get(0).getModelFamiliesDTO());
			assertNotNull(list.get(0).getNominalAttributesDTO());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while getting all crisis with model family nominal attribute "+ex.getMessage());
		}
	}

	/**
	 * Test of getAllCrisisByUserID method, of class CrisisResourceFacadeImp.
	 */
	/*
	 * @Test public void testGetAllCrisisByUserID() {
	 *  try { List<CrisisDTO> result
	 * = crisisResourceFacadeImp.getAllCrisisByUserID(userId);
	 * assertEquals(Long.valueOf(userId),
	 * result.get(0).getUsersDTO().getUserID()); } catch
	 * (PropertyNotSetException ex) {
	 * Logger.getLogger(CrisisResourceFacadeImpTest
	 * .class.getName()).log(Level.SEVERE,
	 * "PropertyNotSetException while getting all crisis by userID", ex); } }
	 */

	/**
	 * Test of isCrisisExists method, of class CrisisResourceFacadeImp.
	 */
	@Test
	public void testIsCrisisExists() {
		try {
			boolean result = crisisResourceFacadeImp.isCrisisExists(crisisCode);
			assertEquals(true, result);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while checking whether crisis exist or not by crisisCode "+ex.getMessage());
		}
	}

	/**
	 * Test of countClassifiersByCrisisCodes method, of class
	 * CrisisResourceFacadeImp.
	 */
	@Test
	public void testCountClassifiersByCrisisCodes() {
		try{
			List<String> codes = new ArrayList<String>();
			codes.add(crisisCode);
			codes.add("cycloneIT-nov13");
			codes.add("2014-01-australia_heat_wave");
		
			NominalAttributeDTO nominalAttributeDTO1 = nominalAttributeResourceFacadeImp.getAttributeByID(1L);
			NominalAttributeDTO nominalAttributeDTO2 = nominalAttributeResourceFacadeImp.getAttributeByID(2L);
	
			ModelFamilyDTO modelFamilyDTO1 = new ModelFamilyDTO();
			modelFamilyDTO1.setCrisisDTO(crisisDTO);
			modelFamilyDTO1.setIsActive(true);
			modelFamilyDTO1.setNominalAttributeDTO(nominalAttributeDTO1);
			
			ModelFamilyDTO modelFamilyDTO2 = new ModelFamilyDTO();
			modelFamilyDTO2.setCrisisDTO(crisisDTO);
			modelFamilyDTO2.setIsActive(false);
			modelFamilyDTO2.setNominalAttributeDTO(nominalAttributeDTO2);
	
			// inserting model families
			entityManager.getTransaction().begin();
			modelFamilyResourceFacadeImp.addCrisisAttribute(modelFamilyDTO1);
			modelFamilyResourceFacadeImp.addCrisisAttribute(modelFamilyDTO2);
			entityManager.getTransaction().commit();
			
			//count classifiers by crisis
			HashMap<String, Integer> result = crisisResourceFacadeImp
					.countClassifiersByCrisisCodes(codes);
			assertEquals(true, result.containsKey(crisisCode));
			assertTrue(result.get(crisisCode)>=Integer.valueOf(2));
			
			// deleting model families
			List<ModelFamilyDTO> modelFamilyDTOs = modelFamilyResourceFacadeImp.getAllModelFamiliesByCrisis(crisisDTO.getCrisisID());
			entityManager.getTransaction().begin();
			for (ModelFamilyDTO modelFamilyDTO : modelFamilyDTOs) {
				modelFamilyResourceFacadeImp.deleteModelFamily(modelFamilyDTO.getModelFamilyId());
			}
			entityManager.getTransaction().commit();
		} catch (PropertyNotSetException ex) {
				logger.error("Property not set exception while accessing/inserting data "+ex.getMessage());
		}
			
		
		
		
	}

	@Test
	public void testGetWithModelFamilyNominalAttributeByCrisisID() {
		try {
			CollectionDTO crisis = crisisResourceFacadeImp
					.getWithModelFamilyNominalAttributeByCrisisID(crisisId);
			assertEquals(Long.valueOf(crisisId), crisis.getCrisisID());
			assertEquals(crisisName, crisis.getName());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while getting crisis with model family nominal attribute by crisisId "+ex.getMessage());
		}
	}

	@Test
	public void testDeleteCrisis() {
		try {
			entityManager.getTransaction().begin();
			int deleteCount = crisisResourceFacadeImp.deleteCrisis(crisisDTO);
			entityManager.getTransaction().commit();
			assertEquals(1, deleteCount);
			crisisDTO = null;
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while deleting crisis "+ex.getMessage());
		}
	}

}
