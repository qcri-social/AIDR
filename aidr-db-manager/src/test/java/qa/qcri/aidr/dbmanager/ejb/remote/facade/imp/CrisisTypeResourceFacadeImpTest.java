/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;

/**
 *
 * @author nalemadi
 */
public class CrisisTypeResourceFacadeImpTest {
    static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
    static CrisisResourceFacadeImp crisisResourceFacadeImp;
    static EntityManager entityManager;
    static CrisisTypeDTO addCrisisType;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
        entityManager = Persistence.createEntityManagerFactory("ProjectDBManagerTest-ejbPU").createEntityManager();
	crisisTypeResourceFacadeImp.setEntityManager(entityManager);
        crisisResourceFacadeImp = new CrisisResourceFacadeImp();
        crisisResourceFacadeImp.setEntityManager(entityManager);
    }
    
    @AfterClass
    public static void shutDown() throws Exception {
        //crisisTypeResourceFacadeImp.deleteCrisisType(addCrisisType.getCrisisTypeId());
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
            System.out.println("getAllCrisisTypes");
            String expName = "Natural Hazard: Geophysical: Earthquake and/or Tsunami";
            List<CrisisTypeDTO> result = crisisTypeResourceFacadeImp.getAllCrisisTypes();
            //
            assertNotNull(result);
            assertEquals(expName, result.get(1).getName());
            // TODO review the generated test code and remove the default call to fail.
        } catch (PropertyNotSetException ex) {
            fail("GetAllCrisisTypes fails");
           // Logger.getLogger(CrisisTypeResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of addCrisisType method, of class CrisisTypeResourceFacadeImp.
     */
    @Test
    public void testAddCrisisType() {
        try {
            System.out.println("addCrisisType");
            //
            CrisisDTO crisisDTO = crisisResourceFacadeImp.findCrisisByID(51L);
            CrisisDTO crisisDTO2 = crisisResourceFacadeImp.findCrisisByID(52L);
            List<CrisisDTO> list = new ArrayList<>();
            list.add(crisisDTO);
            list.add(crisisDTO2);
            //
            addCrisisType = new CrisisTypeDTO();
            addCrisisType.setName("testDB crisis type");
            addCrisisType.setCrisisesDTO(list);
            addCrisisType.setNumberOfCrisisAssociated(2);
            //
            entityManager.getTransaction().begin();
            CrisisTypeDTO result = crisisTypeResourceFacadeImp.addCrisisType(addCrisisType);
            entityManager.getTransaction().commit();
            //
            assertEquals("testDB crisis type", result.getName());
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(CrisisTypeResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of editCrisisType method, of class CrisisTypeResourceFacadeImp.
     */
    @Test
    public void testEditCrisisType() {
        try {
            System.out.println("editCrisisType");
            CrisisDTO crisisDTO = crisisResourceFacadeImp.findCrisisByID(51L);
            CrisisDTO crisisDTO2 = crisisResourceFacadeImp.findCrisisByID(52L);
            List<CrisisDTO> list = new ArrayList<>();
            list.add(crisisDTO);
            list.add(crisisDTO2);
            //
            CrisisTypeDTO addCrisisTypeE = new CrisisTypeDTO();
            addCrisisTypeE.setName("testDB crisis typeE");
            addCrisisTypeE.setCrisisesDTO(list);
            addCrisisTypeE.setNumberOfCrisisAssociated(2);
            //
            entityManager.getTransaction().begin();
            CrisisTypeDTO addedCrisisType = crisisTypeResourceFacadeImp.addCrisisType(addCrisisTypeE);
            entityManager.getTransaction().commit();
            //
            CrisisTypeDTO editCrisisType = crisisTypeResourceFacadeImp.findCrisisTypeByID(addedCrisisType.getCrisisTypeId());
            editCrisisType.setName("testDB edit crisis type");
            
            entityManager.getTransaction().begin();
            CrisisTypeDTO result = crisisTypeResourceFacadeImp.editCrisisType(editCrisisType);
            entityManager.getTransaction().commit();
            //
            assertEquals("testDB edit crisis type", result.getName());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisTypeResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of deleteCrisisType method, of class CrisisTypeResourceFacadeImp.
     */
    @Test
    public void testDeleteCrisisType() {
        try {
            System.out.println("deleteCrisisType");
            CrisisDTO crisisDTO = crisisResourceFacadeImp.findCrisisByID(51L);
            
            CrisisDTO crisisDTO2 = crisisResourceFacadeImp.findCrisisByID(52L);
            
            List<CrisisDTO> list = new ArrayList<>();
            list.add(crisisDTO);
            list.add(crisisDTO2);
            //
            CrisisTypeDTO crisisType = new CrisisTypeDTO();
            crisisType.setName("testDB Del crisis type");
            crisisType.setCrisisesDTO(list);
            crisisType.setNumberOfCrisisAssociated(2);
            entityManager.getTransaction().begin();
            CrisisTypeDTO delCrisisType = crisisTypeResourceFacadeImp.addCrisisType(crisisType);
            entityManager.getTransaction().commit();
            //
            int result = crisisTypeResourceFacadeImp.deleteCrisisType(delCrisisType.getCrisisTypeId());
            assertEquals(1, result);
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisTypeResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findCrisisTypeByID method, of class CrisisTypeResourceFacadeImp.
     */
    @Test
    public void testFindCrisisTypeByID() {
        try {
            System.out.println("findCrisisTypeByID");
            Long id = 1210L;
            CrisisTypeDTO result = crisisTypeResourceFacadeImp.findCrisisTypeByID(id);
            assertEquals("Human Induced: War or armed conflict, incl. acts of war", result.getName());
           //
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisTypeResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of isCrisisTypeExists method, of class CrisisTypeResourceFacadeImp.
     */
    @Test
    public void testIsCrisisTypeExists() {
        try {
            System.out.println("isCrisisTypeExists");
            Long id = 1110L;
            boolean result = crisisTypeResourceFacadeImp.isCrisisTypeExists(id);
            assertEquals(true, result);
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisTypeResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getAllCrisisForCrisisTypeID method, of class CrisisTypeResourceFacadeImp.
     */
    @Test
    public void testGetAllCrisisForCrisisTypeID() {
        try {
            System.out.println("getAllCrisisForCrisisTypeID");
            Long id = 1100L;
            List<CrisisDTO> result = crisisTypeResourceFacadeImp.getAllCrisisForCrisisTypeID(id);
            assertEquals("Natural Hazard: Geophysical: Earthquake and/or Tsunami", result.get(0).getCrisisTypeDTO().getName());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisTypeResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
