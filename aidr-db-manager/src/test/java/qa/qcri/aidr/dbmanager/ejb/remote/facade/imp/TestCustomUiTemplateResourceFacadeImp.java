/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import static org.junit.Assert.assertEquals;
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
import qa.qcri.aidr.dbmanager.dto.CustomUiTemplateDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 * 
 * @author Kushal
 */
public class TestCustomUiTemplateResourceFacadeImp {
	static EntityManager entityManager;
	static NominalAttributeDTO nominalAttribute;
	static UsersDTO user;
	static CollectionDTO crisis;
	static CustomUiTemplateDTO customUiTemplate;
	static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
	static UsersResourceFacadeImp userResourceFacadeImp;
	static CollectionResourceFacadeImp crisisResourceFacadeImp;
	static CustomUiTemplateResourceFacadeImp customUiTemplateResourceFacadeImp;
	static NominalAttributeResourceFacadeImp nominalAttributeResourceFacadeImp;
	private static Logger logger = Logger.getLogger("db-manager-log");
	
	@BeforeClass
	public static void setUpClass() {
		entityManager = Persistence.createEntityManagerFactory(
				"ProjectDBManagerTest-ejbPU").createEntityManager();
		
		crisisResourceFacadeImp = new CollectionResourceFacadeImp();
		crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
		userResourceFacadeImp = new UsersResourceFacadeImp();
		crisisResourceFacadeImp.setEntityManager(entityManager);
		crisisTypeResourceFacadeImp.setEntityManager(entityManager);
		userResourceFacadeImp.setEntityManager(entityManager);
		customUiTemplateResourceFacadeImp = new CustomUiTemplateResourceFacadeImp();
		customUiTemplateResourceFacadeImp.setEntityManager(entityManager);
		nominalAttributeResourceFacadeImp = new NominalAttributeResourceFacadeImp();
		nominalAttributeResourceFacadeImp.setEntityManager(entityManager);
		
		user = new UsersDTO("userDBTest"+new Date(), "normal"+new Date());
		entityManager.getTransaction().begin();
		user = userResourceFacadeImp.addUser(user);
		entityManager.getTransaction().commit();
		
		try {
			CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
			CollectionDTO crisisDTO = new CollectionDTO("testCrisisName"+new Date(), "testCrisisCode"+new Date(), false, false, crisisTypeDTO, user);
			entityManager.getTransaction().begin();
			crisis = crisisResourceFacadeImp.addCrisis(crisisDTO);
			entityManager.getTransaction().commit();
			
			nominalAttribute = nominalAttributeResourceFacadeImp.getAttributeByID(1L);
		} catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while creating crisis "+e.getMessage());
		}
	}

	@Before
	public void setUp() {
		customUiTemplate = new CustomUiTemplateDTO(crisis.getCrisisID(), nominalAttribute.getNominalAttributeId(), 1, "test_template_value", 1, true, new Date());
		entityManager.getTransaction().begin();
		customUiTemplate = customUiTemplateResourceFacadeImp.addCustomUITemplate(customUiTemplate);
		entityManager.getTransaction().commit();
	}

	@AfterClass
	public static void shutDown() {
		try {
			if (crisis!= null) {
				entityManager.getTransaction().begin();
				crisisResourceFacadeImp.deleteCrisis(crisis);
				entityManager.getTransaction().commit();
				CollectionDTO result = crisisResourceFacadeImp.getCrisisByCode(crisis.getCode());
				assertNull(result);
			}
		}catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while deleting crisis "+e.getMessage());
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
		}catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while deleting user "+e.getMessage());
		}	
		customUiTemplateResourceFacadeImp.getEntityManager().close();
	}

	@After
	public void tearDown() {
		if(customUiTemplate!=null){
			entityManager.getTransaction().begin();
			customUiTemplateResourceFacadeImp.deleteCustomUiTemplateById(customUiTemplate.getCustomUitemplateId());
			entityManager.getTransaction().commit();
		}
	}
	
	/**
	 * Test of addCustomUITemplate method, of class CustomUiTemplateResourceFacadeImp.
	 */
	@Test
	public void testAddCustomUITemplate() {
		assertEquals(nominalAttribute.getNominalAttributeId(), customUiTemplate.getNominalAttributeID());
	}
	
	/**
	 * Test of getAllCustomUITemplateByCrisisID method, of class CustomUiTemplateResourceFacadeImp.
	 */
	@Test
	public void testGetAllCustomUITemplateByCrisisID() {
		entityManager.getTransaction().begin();
		List<CustomUiTemplateDTO> result = customUiTemplateResourceFacadeImp.getAllCustomUITemplateByCrisisID(crisis.getCrisisID());
		entityManager.getTransaction().commit();
		assertTrue(result.size()>=1);
	}
	
	/**
	 * Test of getCustomUITemplateBasedOnTypeByCrisisID method, of class CustomUiTemplateResourceFacadeImp.
	 */
	@Test
	public void testGetCustomUITemplateBasedOnTypeByCrisisID() {
		entityManager.getTransaction().begin();
		List<CustomUiTemplateDTO> result = customUiTemplateResourceFacadeImp.getCustomUITemplateBasedOnTypeByCrisisID(crisis.getCrisisID(), 1);
		entityManager.getTransaction().commit();
		assertTrue(result.size()>=1);
	}
	
	/**
	 * Test of getCustomUITemplateByCrisisIDAndAttributeID method, of class CustomUiTemplateResourceFacadeImp.
	 */
	@Test
	public void testGetCustomUITemplateByCrisisIDAndAttributeID() {
		entityManager.getTransaction().begin();
		List<CustomUiTemplateDTO> result = customUiTemplateResourceFacadeImp.getCustomUITemplateByCrisisIDAndAttributeID(crisis.getCrisisID(), nominalAttribute.getNominalAttributeId());
		entityManager.getTransaction().commit();
		assertTrue(result.size()>=1);
	}
	
	/**
	 * Test of updateCustomUITemplate method, of class CustomUiTemplateResourceFacadeImp.
	 */
	@Test
	public void testUpdateCustomUITemplate() {
		CustomUiTemplateDTO customUiTemplateUpdated = customUiTemplate;
		customUiTemplateUpdated.setTemplateValue("template_value_updated");
		entityManager.getTransaction().begin();
		customUiTemplate = customUiTemplateResourceFacadeImp.updateCustomUITemplate(customUiTemplate,customUiTemplateUpdated);
		entityManager.getTransaction().commit();
		assertEquals(customUiTemplateUpdated.getTemplateValue(),customUiTemplate.getTemplateValue());
	}
	
	/**
	 * Test of updateCustomUITemplateStatus method, of class CustomUiTemplateResourceFacadeImp.
	 */
	@Test
	public void testUpdateCustomUITemplateStatus() {
		CustomUiTemplateDTO customUiTemplateUpdated = customUiTemplate;
		customUiTemplateUpdated.setIsActive(false);
		entityManager.getTransaction().begin();
		customUiTemplate = customUiTemplateResourceFacadeImp.updateCustomUITemplateStatus(customUiTemplate,customUiTemplateUpdated);
		entityManager.getTransaction().commit();
		assertEquals(customUiTemplateUpdated.getStatus(),customUiTemplate.getStatus());
	}

}
