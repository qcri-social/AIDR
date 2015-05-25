/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import qa.qcri.aidr.dbmanager.dto.UsersDTO;


public class CrisisResourceFacadeImpTest {
    static CrisisResourceFacadeImp crisisResourceFacadeImp;
    static UsersResourceFacadeImp userResourceFacadeImp;
    static CrisisTypeResourceFacadeImp crisisTypeResourceFacadeImp;
    static EntityManager entityManager;
    static CrisisDTO addCrisis;
    static CrisisDTO editCrisis;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        crisisResourceFacadeImp = new CrisisResourceFacadeImp();
        entityManager = Persistence.createEntityManagerFactory("ProjectDBManagerTest-ejbPU").createEntityManager();
	crisisResourceFacadeImp.setEntityManager(entityManager);
        //
        crisisTypeResourceFacadeImp = new CrisisTypeResourceFacadeImp();
        userResourceFacadeImp = new UsersResourceFacadeImp();
        crisisTypeResourceFacadeImp.setEntityManager(entityManager);
        userResourceFacadeImp.setEntityManager(entityManager);
    }
    
    @After
    public void tearDown() {
    }
    
    @AfterClass
    public static void shutDown() throws Exception {
        if(addCrisis != null)
            crisisResourceFacadeImp.deleteCrisis(addCrisis);
        if(editCrisis != null)
            crisisResourceFacadeImp.deleteCrisis(editCrisis);
        //
	crisisResourceFacadeImp.getEntityManager().close();
    }

     /**
     * Test of findByCriteria method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testFindByCriteria() {
        System.out.println("FindByCriteria");
        try {
            String columnName = "code";
            String value = "Qatar";
            List<CrisisDTO> result = crisisResourceFacadeImp.findByCriteria(columnName, value);
            //
            assertEquals("Qatar", result.get(0).getCode());
            assertEquals(Long.valueOf(76), result.get(0).getCrisisID());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testDeleteCrisis() {
        try {
            System.out.println("DeleteCrisis");
            CrisisDTO crisis = new CrisisDTO();
            crisis.setName("testDelDB");
            crisis.setCode("testDelDB code");
            crisis.setIsTrashed(true);
            CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
            crisis.setCrisisTypeDTO(crisisTypeDTO);
            UsersDTO user = userResourceFacadeImp.getUserById(9L);
            crisis.setUsersDTO(user);
            entityManager.getTransaction().begin();
            crisisResourceFacadeImp.addCrisis(crisis);
            entityManager.getTransaction().commit();
            //
            entityManager.getTransaction().begin();
            int deleteCount = crisisResourceFacadeImp.deleteCrisis(crisis);
            entityManager.getTransaction().commit();
            //
            assertEquals(1, deleteCount); 
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /**
     * Test of addCrisis method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testAddCrisis() {
        try {
            System.out.println("AddCrisis");
            addCrisis = new CrisisDTO();
            addCrisis.setName("testdb");
            addCrisis.setCode("testdb code");
            addCrisis.setIsTrashed(false);
            
            CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
            addCrisis.setCrisisTypeDTO(crisisTypeDTO);
            UsersDTO user = userResourceFacadeImp.getUserById(9L);
            addCrisis.setUsersDTO(user);
            entityManager.getTransaction().begin();
            CrisisDTO result = crisisResourceFacadeImp.addCrisis(addCrisis);
            entityManager.getTransaction().commit();
            //
            assertEquals("testdb", result.getName());
        } catch (PropertyNotSetException ex) {
            Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testFindCrisisByID() {
        System.out.println("FindCrisisByID");
        try {
            CrisisDTO crisisDTO = crisisResourceFacadeImp.findCrisisByID(58L);
            //
            assertEquals("Cyclone-Flood Italy", crisisDTO.getName());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testGetCrisisWithAllFieldsByID() {
        System.out.println("GetCrisisWithAllFieldsByID");
        try {
            CrisisDTO crisisDTO = crisisResourceFacadeImp.getCrisisWithAllFieldsByID(84L);
            //
            assertEquals("Hurricane Sandy", crisisDTO.getName());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.toString());
        }
    }
    
     /**
     * Test of getCrisisByCode method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetCrisisByCode() {
        System.out.println("GetCrisisByCode");
        try {
            String code = "yolan_0_nov-13";
            CrisisDTO result = crisisResourceFacadeImp.getCrisisByCode(code);
            //
            assertNotNull(result);
            assertEquals("yolan_0_nov-13", result.getCode());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /**
     * Test of editCrisis method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testEditCrisis() {
        System.out.println("EditCrisis");
        try {
            CrisisDTO crisis = new CrisisDTO();
            crisis.setName("testEditDB");
            crisis.setCode("testEditDB code");
            crisis.setIsTrashed(false);
            CrisisTypeDTO crisisTypeDTO = crisisTypeResourceFacadeImp.findCrisisTypeByID(1100L);
            crisis.setCrisisTypeDTO(crisisTypeDTO);
            UsersDTO user = userResourceFacadeImp.getUserById(9L);
            crisis.setUsersDTO(user);
            entityManager.getTransaction().begin();
            CrisisDTO addedCrisis = crisisResourceFacadeImp.addCrisis(crisis);
            entityManager.getTransaction().commit();
            //
            editCrisis = crisisResourceFacadeImp.findCrisisByID(addedCrisis.getCrisisID());
            editCrisis.setName("testEditDB1");
            editCrisis.setCode("testEditDB1 code");
            //
            entityManager.getTransaction().begin();
            CrisisDTO result = crisisResourceFacadeImp.editCrisis(editCrisis);
            entityManager.getTransaction().commit();
            //
            assertEquals("testEditDB1", result.getName());
            assertEquals("testEditDB1 code", result.getCode());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testFindActiveCrisis() {
        System.out.println("FindActiveCrisis");
        try {
            List<CrisisDTO> list = crisisResourceFacadeImp.findActiveCrisis();
            //
            assertEquals(false, list.get(0).isIsTrashed());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
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
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getAllCrisisWithModelFamilies method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetAllCrisisWithModelFamilies() {
        System.out.println("GetAllCrisisWithModelFamilies");
        try {
            List<CrisisDTO> result = crisisResourceFacadeImp.getAllCrisisWithModelFamilies();
            assertNotNull(result.get(0).getModelFamiliesDTO());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testGetAllCrisisWithModelFamilyNominalAttribute() {
        System.out.println("GetAllCrisisWithModelFamilyNominalAttribute");
        try {
            List<CrisisDTO> list = crisisResourceFacadeImp.getAllCrisisWithModelFamilyNominalAttribute();
            //
            assertNotNull(list.get(0).getModelFamiliesDTO());
            assertNotNull(list.get(0).getNominalAttributesDTO());
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getAllCrisisByUserID method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetAllCrisisByUserID() {
        System.out.println("GetAllCrisisByUserID");
        try {
            Long userID = 4L;
            List<CrisisDTO> result = crisisResourceFacadeImp.getAllCrisisByUserID(userID);
            //
            assertEquals(Long.valueOf(4L), result.get(0).getUsersDTO().getUserID());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of isCrisisExists method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testIsCrisisExists() {
        System.out.println("IsCrisisExists");
        try {
            String crisisCode = "prism_nsa";
            boolean result = crisisResourceFacadeImp.isCrisisExists(crisisCode);
            //
            assertEquals(true, result);
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of countClassifiersByCrisisCodes method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testCountClassifiersByCrisisCodes() {
        System.out.println("CountClassifiersByCrisisCodes");
        // TODO
        List<String> codes = new ArrayList<String>();
        codes.add("stjude_oct-13");
        codes.add("cycloneIT-nov13");
        codes.add("2014-01-australia_heat_wave");
        HashMap<String, Integer> result = crisisResourceFacadeImp.countClassifiersByCrisisCodes(codes);
        //
        assertEquals(true, result.containsKey("cycloneIT-nov13"));
    }
    
    @Test
    public void testGetWithModelFamilyNominalAttributeByCrisisID() {
        System.out.println("GetWithModelFamilyNominalAttributeByCrisisID");
        try {
            CrisisDTO crisisDTO = crisisResourceFacadeImp.getWithModelFamilyNominalAttributeByCrisisID(71L);
            //
            assertEquals(Long.valueOf(71), crisisDTO.getCrisisID());
            assertEquals("PRISM/NSA scandal - Alex", crisisDTO.getName());
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(CrisisResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
