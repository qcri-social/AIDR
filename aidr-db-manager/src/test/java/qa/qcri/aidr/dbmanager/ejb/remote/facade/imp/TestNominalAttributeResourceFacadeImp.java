/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertEquals;
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
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 * 
 * @author Kushal
 */
public class TestNominalAttributeResourceFacadeImp {
	//
	static UsersResourceFacadeImp userResourceFacadeImp;
	static EntityManager entityManager;
	static UsersDTO user;
	static CrisisDTO crisis;
	static CrisisResourceFacadeImp crisisResourceFacadeImp;
	static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	static NominalAttributeDTO nominalAttribute;
	static NominalAttributeResourceFacadeImp nominalAttributeResourceFacadeImp;
	private static Logger logger = Logger.getLogger("db-manager-log");

	@BeforeClass
	public static void setUpClass() {
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		crisisResourceFacadeImp = new CrisisResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		userResourceFacadeImp.setEntityManager(entityManager);
		nominalAttributeResourceFacadeImp = new NominalAttributeResourceFacadeImp();
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		nominalAttributeResourceFacadeImp.setEntityManager(entityManager);

		user = new UsersDTO("userDBTest"+new Date(), "normal"+new Date());
		entityManager.getTransaction().begin();
		user = userResourceFacadeImp.addUser(user);
		entityManager.getTransaction().commit();
	}

	@Before
	public void setUp() {
		try {
			nominalAttribute = new NominalAttributeDTO("test_nominal_attribute_name"+new Date(), "test_nominal_attribute_desc"+new Date(), "test_nominal_attribute_code"+new Date());
			nominalAttribute.setUsersDTO(user);
			entityManager.getTransaction().begin();
			nominalAttribute = nominalAttributeResourceFacadeImp.addAttribute(nominalAttribute);
			entityManager.getTransaction().commit();
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while adding nominal attribute "+e.getMessage());
			fail("setUp failed");
		}
	}

	@AfterClass
	public static void shutDown() throws Exception {
		if (user != null) {
			entityManager.getTransaction().begin();
			user = userResourceFacadeImp.getUserByName(user.getName());
			userResourceFacadeImp.deleteUser(user.getUserID());
			entityManager.getTransaction().commit();
			UsersDTO result = userResourceFacadeImp.getUserByName(user.getName());
			assertNull(result);
		}
		
		nominalAttributeResourceFacadeImp.getEntityManager().close();
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
			if (nominalAttribute != null) {
				entityManager.getTransaction().begin();
				nominalAttributeResourceFacadeImp.deleteAttribute(nominalAttribute.getNominalAttributeId());
				entityManager.getTransaction().commit();
				nominalAttribute=null;
			}
			
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while deleting user by id "+e.getMessage());
			fail("tearDown failed");
		}
	}

	/**
	 * Test of addAttribute method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testAddAttribute() {
		assertEquals(user.getUserID(), nominalAttribute.getUsersDTO().getUserID());
	}

	/**
	 * Test of deleteAttribute method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testDeleteAttribute() {
		try {
			entityManager.getTransaction().begin();
			boolean result = nominalAttributeResourceFacadeImp.deleteAttribute(nominalAttribute.getNominalAttributeId());
			entityManager.getTransaction().commit();
			assertEquals(true, result);
			nominalAttribute=null;
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while deleting nominal attribute "+e.getMessage());
			fail("testdeleteAttribute failed");
		}
	}

	/**
	 * Test of editAttribute method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testEditAttribute() {
		try {
			nominalAttribute.setName("testNameNominal");
			entityManager.getTransaction().begin();
			nominalAttribute = nominalAttributeResourceFacadeImp.editAttribute(nominalAttribute);
			entityManager.getTransaction().commit();
			assertEquals("testNameNominal", nominalAttribute.getName());
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while editting nominal attribute "+e.getMessage());
			fail("testEditAttribute failed");
		}
	}

	/**
	 * Test of getAllAttributes method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testGetAllAttributes() {
		try {
			List<NominalAttributeDTO> result = nominalAttributeResourceFacadeImp.getAllAttributes();
			assertTrue(result.size()>=1);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while getting all nominal attributes "+e.getMessage());
			fail("testGetAllAttributes failed");
		}
	}

	/**
	 * Test of getAttributeByID method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testGetAttributeByID() {
		try {
			NominalAttributeDTO result = nominalAttributeResourceFacadeImp.getAttributeByID(nominalAttribute.getNominalAttributeId());
			assertEquals(nominalAttribute.getNominalAttributeId(), result.getNominalAttributeId());
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while getting nominal attribute by Id "+e.getMessage());
			fail("testGetAttributeByID failed");
		}
	}

	/**
	 * Test of getAttributeByID method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testIsAttributeExists() {
		try {
			Long result = nominalAttributeResourceFacadeImp.isAttributeExists(nominalAttribute.getCode());
			assertEquals(nominalAttribute.getNominalAttributeId(), result);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while checking whether the nominal attribute code exist or not "+e.getMessage());
			fail("isAttributeExists failed");
		}
	}

	/**
	 * Test of getAllAttributes method, of class NominalAttributeResourceFacadeImp.
	 *//*
	@Test
	public void testGetAllAttributesExceptCrisis() {
		try {
			CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
			CrisisDTO crisisDTO = new CrisisDTO("testCrisisName", "testCrisisCode", false, crisisTypeDTO, user);
			entityManager.getTransaction().begin();
			crisis = crisisResourceFacadeImp.addCrisis(crisisDTO);
			entityManager.getTransaction().commit();
			List<CrisisAttributesDTO> result = nominalAttributeResourceFacadeImp.getAllAttributesExceptCrisis(crisis.getCrisisID()+1);
			assertTrue(result.size()>=1);
		} catch (PropertyNotSetException e) {
		logger.error("PropertyNotSetException while getting all nominal attributes except crises "+e.getMessage());
			fail("testGetAllAttributesExceptCrisis failed");
		}
	}*/

}
