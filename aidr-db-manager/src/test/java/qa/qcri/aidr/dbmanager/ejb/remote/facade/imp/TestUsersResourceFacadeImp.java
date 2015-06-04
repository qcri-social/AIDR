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
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 * 
 * @author nalemadi
 */
public class TestUsersResourceFacadeImp {
	static UsersResourceFacadeImp usersResourceFacadeImp;
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
		usersResourceFacadeImp = new UsersResourceFacadeImp();
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		usersResourceFacadeImp.setEntityManager(entityManager);
		crisisResourceFacadeImp = new CrisisResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
	}

	@Before
	public void setUp() {
		user = new UsersDTO(userName, userRole);
		entityManager.getTransaction().begin();
		user = usersResourceFacadeImp.addUser(user);
		entityManager.getTransaction().commit();
	}

	@AfterClass
	public static void shutDown() throws Exception {
		usersResourceFacadeImp.getEntityManager().close();
	}

	@After
	public void tearDown() {
		try {
			if (crisis != null) {
				entityManager.getTransaction().begin();
				crisisResourceFacadeImp.deleteCrisis(crisis);
				entityManager.getTransaction().commit();
				crisis=null;
			}
			if (user != null) {
				entityManager.getTransaction().begin();
				user = usersResourceFacadeImp.getUserByName(user.getName());
				usersResourceFacadeImp.deleteUser(user.getUserID());
				entityManager.getTransaction().commit();
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
			System.out.println("getUserByName");
			UsersDTO result = usersResourceFacadeImp.getUserByName(user
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
			System.out.println("getUserById");
			UsersDTO result = usersResourceFacadeImp.getUserById(user
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
			System.out.println("getAllUsersByName");
			List<UsersDTO> result = usersResourceFacadeImp
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
			System.out.println("findAllCrisisByUserID");
			CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
			CrisisDTO crisisDTO = new CrisisDTO("testCrisisName", "testCrisisCode", false, crisisTypeDTO, user);
			entityManager.getTransaction().begin();
			crisis = crisisResourceFacadeImp.addCrisis(crisisDTO);
			entityManager.getTransaction().commit();

			List<CrisisDTO> result = usersResourceFacadeImp
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
		System.out.println("addUser");
		assertEquals(userName, user.getName());
	}

	/**
	 * Test of deleteUser method, of class UsersResourceFacadeImp.
	 */
	@Test
	public void testDeleteUser() {
		System.out.println("deleteUser");
		entityManager.getTransaction().begin();
		int result = usersResourceFacadeImp.deleteUser(user.getUserID());
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
			System.out.println("findByCriteria");
			String columnName = "role";
			String value = userRole;
			List<UsersDTO> result = usersResourceFacadeImp.findByCriteria(
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
			System.out.println("getAllUsers");
			List<UsersDTO> result = usersResourceFacadeImp.getAllUsers();
			assertNotNull(result);
			assertTrue(result.size() >= 1);
		} catch (PropertyNotSetException ex) {
			logger.error("PropertyNotSetException while fetching all users "+ex.getMessage());
			fail("testGetAllUsers failed");
		}
	}
}
