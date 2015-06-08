/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;

/**
 * 
 * @author nalemadi
 */
public class TestCrisisTypeResourceFacadeImp {
	static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	static CrisisResourceFacadeImp crisisResourceFacadeImp;
	static UsersResourceFacadeImp userResourceFacadeImp;
	static EntityManager entityManager;
	static CrisisTypeDTO addCrisisType;
	static String crisisTypeName = "Natural Hazard: Geophysical: Earthquake and/or Tsunami";
	static Long crisisTypeId = 1100L;
	static String sampleCrisisTypeName = "Test_Crisis_Type_Name"+new Date();
	private static Logger logger = Logger.getLogger("db-manager-log");
	@BeforeClass
	public static void setUpClass() {

	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		crisisResourceFacadeImp = new CrisisResourceFacadeImp();
		crisisResourceFacadeImp.setEntityManager(entityManager);
		userResourceFacadeImp = new UsersResourceFacadeImp();
		userResourceFacadeImp.setEntityManager(entityManager);
	}

	@AfterClass
	public static void shutDown() throws Exception {
		crisisTypeResourceFacadeImp.getEntityManager().close();
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getAllCrisisTypes method, of class CrisisTypeResourceFacadeImp.
	 */
	@Test
	public void testGetAllCrisisTypes() {
		try {
			List<CrisisTypeDTO> result = crisisTypeResourceFacadeImp
					.getAllCrisisTypes();
			assertNotNull(result);
			assertEquals(crisisTypeName, result.get(1).getName());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching all crisisTypes "+ex.getMessage());
		}
	}

	/**
	 * Test of addCrisisType method, of class CrisisTypeResourceFacadeImp.
	 */
	@Test
	public void testAddCrisisType() {
		CrisisTypeDTO addCrisisType = new CrisisTypeDTO();
		addCrisisType.setName(sampleCrisisTypeName);
		entityManager.getTransaction().begin();
		CrisisTypeDTO result = crisisTypeResourceFacadeImp
				.addCrisisType(addCrisisType);
		entityManager.getTransaction().commit();
		assertEquals(sampleCrisisTypeName, result.getName());
		entityManager.getTransaction().begin();
		crisisTypeResourceFacadeImp.deleteCrisisType(result.getCrisisTypeId());
		entityManager.getTransaction().commit();
	}

	/**
	 * Test of editCrisisType method, of class CrisisTypeResourceFacadeImp.
	 */
	@Test
	public void testEditCrisisType() {
		try {
			CrisisTypeDTO addCrisisType = new CrisisTypeDTO();
			addCrisisType.setName(sampleCrisisTypeName);
			entityManager.getTransaction().begin();
			CrisisTypeDTO result = crisisTypeResourceFacadeImp
					.addCrisisType(addCrisisType);
			entityManager.getTransaction().commit();

			result.setName(sampleCrisisTypeName + "2");
			entityManager.getTransaction().begin();
			result = crisisTypeResourceFacadeImp.editCrisisType(result);
			entityManager.getTransaction().commit();
			assertEquals(sampleCrisisTypeName + "2", result.getName());
			entityManager.getTransaction().begin();
			crisisTypeResourceFacadeImp.deleteCrisisType(result
					.getCrisisTypeId());
			entityManager.getTransaction().commit();
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while editting crisis type name "+ex.getMessage());
		}
	}

	/**
	 * Test of deleteCrisisType method, of class CrisisTypeResourceFacadeImp.
	 */
	@Test
	public void testDeleteCrisisType() {
		CrisisTypeDTO addCrisisType = new CrisisTypeDTO();
		addCrisisType.setName(sampleCrisisTypeName);
		entityManager.getTransaction().begin();
		CrisisTypeDTO result = crisisTypeResourceFacadeImp
				.addCrisisType(addCrisisType);
		entityManager.getTransaction().commit();
		entityManager.getTransaction().begin();
		int delResult = crisisTypeResourceFacadeImp.deleteCrisisType(result
				.getCrisisTypeId());
		entityManager.getTransaction().commit();
		assertEquals(1, delResult);
	}

	/**
	 * Test of findCrisisTypeByID method, of class CrisisTypeResourceFacadeImp.
	 */
	@Test
	public void testFindCrisisTypeByID() {
		try {
			CrisisTypeDTO result = crisisTypeResourceFacadeImp
					.findCrisisTypeByID(crisisTypeId);
			assertEquals(crisisTypeName, result.getName());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while finding crisis type by id "+ex.getMessage());
		}
	}

	/**
	 * Test of isCrisisTypeExists method, of class CrisisTypeResourceFacadeImp.
	 */
	@Test
	public void testIsCrisisTypeExists() {
		try {
			boolean result = crisisTypeResourceFacadeImp
					.isCrisisTypeExists(crisisTypeId);
			assertEquals(true, result);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while checking crisis type exist or not "+ex.getMessage());
		}
	}

	/**
	 * Test of getAllCrisisForCrisisTypeID method, of class
	 * CrisisTypeResourceFacadeImp.
	 */
	/*
	 * @Test public void testGetAllCrisisForCrisisTypeID() { try {
	 * CrisisDTO crisisDTO =
	 * new CrisisDTO(); crisisDTO.setName("testCrisisName");
	 * crisisDTO.setCode("testCrisisCode"); crisisDTO.setIsTrashed(false);
	 * CrisisTypeDTO crisisTypeDTO =
	 * crisisTypeResourceFacadeImp.findCrisisTypeByID(crisisTypeId);
	 * crisisDTO.setCrisisTypeDTO(crisisTypeDTO); UsersDTO user =
	 * userResourceFacadeImp.getUserById(1L); crisisDTO.setUsersDTO(user);
	 * entityManager.getTransaction().begin(); crisisDTO =
	 * crisisResourceFacadeImp.addCrisis(crisisDTO);
	 * entityManager.getTransaction().commit();
	 * 
	 * List<CrisisDTO> result =
	 * crisisTypeResourceFacadeImp.getAllCrisisForCrisisTypeID(crisisTypeId);
	 * assertEquals(crisisTypeName, result.get(0).getCrisisTypeDTO().getName());
	 * 
	 * entityManager.getTransaction().begin();
	 * crisisResourceFacadeImp.deleteCrisis(crisisDTO);
	 * entityManager.getTransaction().commit(); } catch (PropertyNotSetException
	 * ex) {
	 * Logger.getLogger(CrisisTypeResourceFacadeImpTest.class.getName()).log
	 * (Level.SEVERE,
	 * "PropertyNotSetException while getting all crisises for a specific crisis type "
	 * , ex); } }
	 */
}
