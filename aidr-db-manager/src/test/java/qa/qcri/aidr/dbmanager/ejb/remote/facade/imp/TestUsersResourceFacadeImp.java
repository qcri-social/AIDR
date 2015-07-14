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
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 * 
 * @author nalemadi
 */
public class TestUsersResourceFacadeImp {
	static UsersResourceFacadeImp userResourceFacadeImp;
	static EntityManager entityManager;
	static UsersDTO user;
	static String userName = "userDBTest"+new Date();
	static String userRole = "normal"+new Date();
	static CrisisDTO crisis;
	static CrisisResourceFacadeImp crisisResourceFacadeImp;
	static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	private static Logger logger = Logger.getLogger("db-manager-log");

	@BeforeClass
	public static void setUpClass() {
		userResourceFacadeImp = new UsersResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		userResourceFacadeImp.setEntityManager(entityManager);
		crisisResourceFacadeImp = new CrisisResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
	}

	@Before
	public void setUp() {
		user = new UsersDTO(userName, userRole);
		entityManager.getTransaction().begin();
		user = userResourceFacadeImp.addUser(user);
		entityManager.getTransaction().commit();
	}

	@AfterClass
	public static void shutDown() throws Exception {
		userResourceFacadeImp.getEntityManager().close();
	}

	@After
	public void tearDown() {
		try {
			if (crisis != null) {
				entityManager.getTransaction().begin();
				crisisResourceFacadeImp.deleteCrisis(crisis);
				entityManager.getTransaction().commit();
				CrisisDTO result = crisisResourceFacadeImp.getCrisisByCode(crisis.getCode());
				assertNull(result);
				crisis=null;
			}
			if (user != null) {
				entityManager.getTransaction().begin();
				user = userResourceFacadeImp.getUserByName(user.getName());
				userResourceFacadeImp.deleteUser(user.getUserID());
				entityManager.getTransaction().commit();
				UsersDTO result = userResourceFacadeImp.getUserByName(user.getName());
				assertNull(result);
			}
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while deleting user by id "+e.getMessage());
			fail("tearDown failed");
		}
	}

	/**
	 * Test of getUserByName method, of class UsersResourceFacadeImp.
	 */
	@Test
	public void testGetUserByName() {
		try {
			UsersDTO result = userResourceFacadeImp.getUserByName(user
					.getName());
			assertEquals(user.getUserID(), result.getUserID());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching user by name "+ex.getMessage());
			fail("testGetUserByName failed");
		}
	}

	/**
	 * Test of getUserById method, of class UsersResourceFacadeImp.
	 */
	@Test
	public void testGetUserById() {
		try {
			UsersDTO result = userResourceFacadeImp.getUserById(user
					.getUserID());
			assertEquals(user.getName(), result.getName());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching user by id "+ex.getMessage());
			fail("testGetUserById failed");
		}
	}

	/**
	 * Test of getAllUsersByName method, of class UsersResourceFacadeImp.
	 */
	@Test
	public void testGetAllUsersByName() {
		try {
			List<UsersDTO> result = userResourceFacadeImp
					.getAllUsersByName(user.getName());
			assertEquals(user.getName(), result.get(0).getName());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching all user by name "+ex.getMessage());
			fail("testGetAllUsersByName failed");
		}
	}

	/**
	 * Test of findAllCrisisByUserID method, of class UsersResourceFacadeImp.
	 */
	@Test
	public void testFindAllCrisisByUserId() {
		try {
			CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
			CrisisDTO crisisDTO = new CrisisDTO("testCrisisName", "testCrisisCode", false, false, crisisTypeDTO, user);
			entityManager.getTransaction().begin();
			crisis = crisisResourceFacadeImp.addCrisis(crisisDTO);
			entityManager.getTransaction().commit();

			List<CrisisDTO> result = userResourceFacadeImp
					.findAllCrisisByUserID(user.getUserID());
			assertEquals(user.getUserID(), result.get(0).getUsersDTO()
					.getUserID());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching all crisis by userId "+ex.getMessage());
			fail("testFindAllCrisisByUserID failed");
		}
	}

	/**
	 * Test of addUser method, of class UsersResourceFacadeImp.
	 */
	@Test
	public void testAddUser() {
		assertEquals(userName, user.getName());
	}

	/**
	 * Test of deleteUser method, of class UsersResourceFacadeImp.
	 */
	@Test
	public void testDeleteUser() {
		entityManager.getTransaction().begin();
		int result = userResourceFacadeImp.deleteUser(user.getUserID());
		entityManager.getTransaction().commit();
		assertEquals(1, result);
		user = null;
	}

	/**
	 * Test of findByCriteria method, of class UsersResourceFacadeImp.
	 */
	@Test
	public void testFindByCriteria() {
		try {
			String columnName = "role";
			String value = userRole;
			List<UsersDTO> result = userResourceFacadeImp.findByCriteria(
					columnName, value);
			assertNotNull(result);
			assertEquals(userRole, result.get(0).getRole());
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching users by criteria "+ex.getMessage());
			fail("testFindByCriteria failed");
		}
	}

	/**
	 * Test of getAllUsers method, of class UsersResourceFacadeImp.
	 */
	@Test
	public void testGetAllUsers() {
		try {
			List<UsersDTO> result = userResourceFacadeImp.getAllUsers();
			assertNotNull(result);
			assertTrue(result.size() >= 1);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching all users "+ex.getMessage());
			fail("testGetAllUsers failed");
		}
	}
}
