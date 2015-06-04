/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

public class TestCrisisResourceFacadeImp {
	static CrisisResourceFacadeImp crisisResourceFacadeImp;
	static UsersResourceFacadeImp userResourceFacadeImp;
	static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	static EntityManager entityManager;
	static CrisisDTO addCrisis;
	static CrisisDTO editCrisis;
	static Long crisisId;
	static CrisisDTO crisisDTO;
	static String crisisCode = "testCrisisCode"+new Date();
	static String crisisName = "testCrisisName"+new Date();
	static UsersDTO user;
	private static Logger logger = Logger.getLogger("db-manager-log");
	
	@BeforeClass
	public static void setUpClass() {
		crisisResourceFacadeImp = new CrisisResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		userResourceFacadeImp.setEntityManager(entityManager);
	}

	@Before
	public void setUp() {
		try {
			CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
			
			user = new UsersDTO("userDBTest"+new Date(), "normal");
			entityManager.getTransaction().begin();
			user = userResourceFacadeImp.addUser(user);
			
			entityManager.getTransaction().commit();
			crisisDTO = new CrisisDTO(crisisName, crisisCode, false, crisisTypeDTO, user);
			entityManager.getTransaction().begin();
			crisisDTO = crisisResourceFacadeImp.addCrisis(crisisDTO);
			entityManager.getTransaction().commit();
			crisisId = crisisDTO.getCrisisID();
		} catch (PropertyNotSetException ex) {
			logger.error("Property not set exception while adding crisis "+ex.getMessage());
		}
	}

	@After
	public void tearDown() {
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
				user = userResourceFacadeImp.getUserByName(user.getName());
				userResourceFacadeImp.deleteUser(user.getUserID());
				entityManager.getTransaction().commit();
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
		System.out.println("AddCrisis");
		assertEquals(crisisName, crisisDTO.getName());
	}

	@Test
	public void testFindCrisisByID() {
		System.out.println("FindCrisisByID " + crisisId);
		try {
			CrisisDTO crisisDTO = crisisResourceFacadeImp
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
		System.out.println("FindByCriteria");
		try {
			String columnName = "code";
			String value = crisisCode;
			List<CrisisDTO> result = crisisResourceFacadeImp.findByCriteria(
					columnName, value);
			assertEquals(crisisCode, result.get(0).getCode());
		} catch (PropertyNotSetException ex) {
			logger.error("Property not set exception while finding crisis by criteria "+ex.getMessage());
		}
	}

	@Test
	public void testGetCrisisWithAllFieldsByID() {
		System.out.println("GetCrisisWithAllFieldsByID");
		try {
			CrisisDTO crisis = crisisResourceFacadeImp
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
		System.out.println("GetCrisisByCode");
		try {
			CrisisDTO result = crisisResourceFacadeImp
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
		System.out.println("EditCrisis");
		try {
			editCrisis = crisisResourceFacadeImp.findCrisisByID(crisisId);
			editCrisis.setName(crisisName + "2");
			editCrisis.setCode(crisisCode + "2");
			entityManager.getTransaction().begin();
			CrisisDTO result = crisisResourceFacadeImp.editCrisis(editCrisis);
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
		System.out.println("FindActiveCrisis");
		try {
			List<CrisisDTO> list = crisisResourceFacadeImp.findActiveCrisis();
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
		System.out.println("GetAllCrisis");
		try {
			List<CrisisDTO> result = crisisResourceFacadeImp.getAllCrisis();
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
		System.out.println("GetAllCrisisWithModelFamilies");
		try {
			List<CrisisDTO> result = crisisResourceFacadeImp
					.getAllCrisisWithModelFamilies();
			assertNotNull(result.get(0).getModelFamiliesDTO());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while getting all crisis with model families "+ex.getMessage());
		}
	}

	@Test
	public void testGetAllCrisisWithModelFamilyNominalAttribute() {
		System.out.println("GetAllCrisisWithModelFamilyNominalAttribute");
		try {
			List<CrisisDTO> list = crisisResourceFacadeImp
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
	 * System.out.println("GetAllCrisisByUserID"); try { List<CrisisDTO> result
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
		System.out.println("IsCrisisExists");
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
		System.out.println("CountClassifiersByCrisisCodes");
		List<String> codes = new ArrayList<String>();
		codes.add(crisisCode);
		codes.add("cycloneIT-nov13");
		codes.add("2014-01-australia_heat_wave");
		HashMap<String, Integer> result = crisisResourceFacadeImp
				.countClassifiersByCrisisCodes(codes);
		assertEquals(true, result.containsKey(crisisCode));
	}

	@Test
	public void testGetWithModelFamilyNominalAttributeByCrisisID() {
		System.out.println("GetWithModelFamilyNominalAttributeByCrisisID");
		try {
			CrisisDTO crisis = crisisResourceFacadeImp
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
			System.out.println("DeleteCrisis");
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
