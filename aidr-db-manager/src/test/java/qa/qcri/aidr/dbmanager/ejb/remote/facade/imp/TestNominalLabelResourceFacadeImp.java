/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertEquals;
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
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 * 
 * @author Kushal
 */
public class TestNominalLabelResourceFacadeImp {
	static EntityManager entityManager;
	static NominalLabelDTO nominalLabel;
	static NominalAttributeDTO nominalAttribute;
	static UsersDTO user;
	static NominalLabelResourceFacadeImp nominalLabelResourceFacadeImp;
	static NominalAttributeResourceFacadeImp nominalAttributeResourceFacadeImp;
	static UsersResourceFacadeImp usersResourceFacadeImp;
	private static Logger logger = Logger.getLogger("db-manager-log");
	
	@BeforeClass
	public static void setUpClass() {
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		nominalLabelResourceFacadeImp = new NominalLabelResourceFacadeImp();
		nominalLabelResourceFacadeImp.setEntityManager(entityManager);
		nominalAttributeResourceFacadeImp = new NominalAttributeResourceFacadeImp();
		nominalAttributeResourceFacadeImp.setEntityManager(entityManager);
		usersResourceFacadeImp = new UsersResourceFacadeImp();
		usersResourceFacadeImp.setEntityManager(entityManager);

		user = new UsersDTO("userDBTest"+new Date(), "normal"+new Date());
		entityManager.getTransaction().begin();
		user = usersResourceFacadeImp.addUser(user);
		entityManager.getTransaction().commit();
		try {
			nominalAttribute = new NominalAttributeDTO("test_nominal_attribute_name"+new Date(), "test_nominal_attribute_desc"+new Date(), "test_nominal_attribute_code"+new Date());
			nominalAttribute.setUsersDTO(user);
			entityManager.getTransaction().begin();
			nominalAttribute = nominalAttributeResourceFacadeImp.addAttribute(nominalAttribute);
			entityManager.getTransaction().commit();
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while adding nominal attribute "+e.getMessage());
		}
	}

	@Before
	public void setUp() {
		try {
			nominalLabel = new NominalLabelDTO(nominalAttribute, "test_nominal_label_code", "test_nominal_label_name", "test_nominal_label_desc", 500);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while creating object of nominal label "+e.getMessage());
		}
		entityManager.getTransaction().begin();
		nominalLabel = nominalLabelResourceFacadeImp.addNominalLabel(nominalLabel);
		entityManager.getTransaction().commit();
	}

	@AfterClass
	public static void shutDown() throws Exception {
		if (nominalAttribute != null) {
			entityManager.getTransaction().begin();
			nominalAttributeResourceFacadeImp.deleteAttribute(nominalAttribute.getNominalAttributeId());
			entityManager.getTransaction().commit();
			nominalAttribute=null;
		}
		if (user != null) {
			entityManager.getTransaction().begin();
			user = usersResourceFacadeImp.getUserByName(user.getName());
			usersResourceFacadeImp.deleteUser(user.getUserID());
			entityManager.getTransaction().commit();
		}
		nominalLabelResourceFacadeImp.getEntityManager().close();
	}

	@After
	public void tearDown() {
		try {
			if (nominalLabel != null) {
				entityManager.getTransaction().begin();
				nominalLabelResourceFacadeImp.deleteNominalLabel(nominalLabel);
				entityManager.getTransaction().commit();
				nominalLabel=null;
			}
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while deleting nominal label "+e.getMessage());
			fail("tearDown failed");
		}
	}

	/**
	 * Test of addNominalLabel method, of class NominalLabelResourceFacadeImp.
	 */
	@Test
	public void testAddNominalLabel() {
		System.out.println("addNominalLabel");
		assertEquals(nominalAttribute.getNominalAttributeId(), nominalLabel.getNominalAttributeDTO().getNominalAttributeId());
	}

	/**
	 * Test of deleteNominalLabel method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testDeleteNominalLabel() {
		try {
			System.out.println("deleteNominalLabel");
			entityManager.getTransaction().begin();
			Integer result = nominalLabelResourceFacadeImp.deleteNominalLabel(nominalLabel);
			entityManager.getTransaction().commit();
			assertEquals(Integer.valueOf(1), result);
			nominalLabel=null;
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while deleting nominal label "+e.getMessage());
			fail("testDeleteNominalLabel failed");
		}
	}

	/**
	 * Test of deleteNominalLabelByID method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testDeleteNominalLabelByID() {
			System.out.println("deleteNominalLabelByID");
			entityManager.getTransaction().begin();
			Integer result = nominalLabelResourceFacadeImp.deleteNominalLabelByID(nominalLabel.getNominalLabelId());
			entityManager.getTransaction().commit();
			assertEquals(Integer.valueOf(1), result);
			nominalLabel=null;
	}
	
	/**
	 * Test of editNominalLabel method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testEditNominalLabel() {
		try {
			System.out.println("editNominalLabel");
			nominalLabel.setName("sample_Name_on_edit");
			entityManager.getTransaction().begin();
			nominalLabel = nominalLabelResourceFacadeImp.editNominalLabel(nominalLabel);
			entityManager.getTransaction().commit();
			assertEquals("sample_Name_on_edit", nominalLabel.getName());
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while editting nominal label "+e.getMessage());
			fail("testEditNominalLabel failed");
		}
	}
	
	/**
	 * Test of getAllNominalLabels method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testGetAllNominalLabels() {
		try {
			System.out.println("getAllNominalLabels");
			entityManager.getTransaction().begin();
			List<NominalLabelDTO> result = nominalLabelResourceFacadeImp.getAllNominalLabels();
			entityManager.getTransaction().commit();
			assertTrue(result.size()>=1);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while fetching all nominal labels "+e.getMessage());
			fail("testGetAllNominalLabels failed");
		}
	}
	
	/**
	 * Test of getNominalLabelByCode method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testGetNominalLabelByCode() {
		try {
			System.out.println("getNominalLabelByCode");
			entityManager.getTransaction().begin();
			NominalLabelDTO result = nominalLabelResourceFacadeImp.getNominalLabelByCode(nominalLabel.getNominalLabelCode());
			entityManager.getTransaction().commit();
			assertEquals(nominalLabel.getName(), result.getName());
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while fetching nominal labels by code "+e.getMessage());
			fail("testGetNominalLabelByCode failed");
		}
	}
	

	/**
	 * Test of getNominalLabelByID method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testGetNominalLabelByID() {
		try {
			System.out.println("getNominalLabelByID");
			entityManager.getTransaction().begin();
			NominalLabelDTO result = nominalLabelResourceFacadeImp.getNominalLabelByID(nominalLabel.getNominalLabelId());
			entityManager.getTransaction().commit();
			assertEquals(nominalLabel.getName(), result.getName());
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while fetching nominal labels by id "+e.getMessage());
			fail("testGetNominalLabelByID failed");
		}
	}
	
	/**
	 * Test of getNominalLabelWithAllFieldsByCode method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testGetNominalLabelWithAllFieldsByCode() {
		try {
			System.out.println("getNominalLabelWithAllFieldsByCode");
			entityManager.getTransaction().begin();
			NominalLabelDTO result = nominalLabelResourceFacadeImp.getNominalLabelWithAllFieldsByCode(nominalLabel.getNominalLabelCode());
			entityManager.getTransaction().commit();
			assertEquals(nominalLabel.getNominalAttributeDTO().getNominalAttributeId(), result.getNominalAttributeDTO().getNominalAttributeId());
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while fetching nominal labels with all fields by code "+e.getMessage());
			fail("testGetNominalLabelWithAllFieldsByCode failed");
		}
	}
	
	/**
	 * Test of getNominalLabelWithAllFieldsByCode method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testGetNominalLabelWithAllFieldsByID() {
		try {
			System.out.println("getNominalLabelWithAllFieldsByID");
			entityManager.getTransaction().begin();
			NominalLabelDTO result = nominalLabelResourceFacadeImp.getNominalLabelWithAllFieldsByID(nominalLabel.getNominalLabelId());
			entityManager.getTransaction().commit();
			assertEquals(nominalLabel.getNominalAttributeDTO().getNominalAttributeId(), result.getNominalAttributeDTO().getNominalAttributeId());
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while fetching nominal labels with all fields by id "+e.getMessage());
			fail("testGetNominalLabelWithAllFieldsByID failed");
		}
	}
	
	/**
	 * Test of isNominalLabelExists(Long id) method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testIsNominalLabelExistsById() {
		System.out.println("isNominalLabelExists");
		entityManager.getTransaction().begin();
		Boolean result = nominalLabelResourceFacadeImp.isNominalLabelExists(nominalLabel.getNominalLabelId());
		entityManager.getTransaction().commit();
		assertEquals(Boolean.valueOf(true), result);
	}
	
	/**
	 * Test of isNominalLabelExists(String code) method, of class NominalAttributeResourceFacadeImp.
	 */
	@Test
	public void testIsNominalLabelExistsByCode() {
		System.out.println("isNominalLabelExists");
		entityManager.getTransaction().begin();
		Boolean result = nominalLabelResourceFacadeImp.isNominalLabelExists(nominalLabel.getNominalLabelCode());
		entityManager.getTransaction().commit();
		assertEquals(Boolean.valueOf(true), result);
	}
}
