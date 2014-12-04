/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.HashMap;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;

/**
 *
 * @author Imran
 */
public class CrisisResourceFacadeImpTest {
    
    public CrisisResourceFacadeImpTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getEntityManager method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetEntityManager() throws Exception {
        System.out.println("getEntityManager");
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEntityManager method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testSetEntityManager() throws Exception {
        System.out.println("setEntityManager");
        EntityManager em = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        int expResult = 0;
        int result = instance.setEntityManager(em);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCurrentSession method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetCurrentSession() throws Exception {
        System.out.println("getCurrentSession");
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        Session expResult = null;
        Session result = instance.getCurrentSession();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getById method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetById() throws Exception {
        System.out.println("getById");
        Long id = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        Crisis expResult = null;
        Crisis result = instance.getById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getByCriterionID method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetByCriterionID() throws Exception {
        System.out.println("getByCriterionID");
        Criterion criterion = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        Crisis expResult = null;
        Crisis result = instance.getByCriterionID(criterion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getByCriteria method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetByCriteria() throws Exception {
        System.out.println("getByCriteria");
        Criterion criterion = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        Crisis expResult = null;
        Crisis result = instance.getByCriteria(criterion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAll method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetAll() throws Exception {
        System.out.println("getAll");
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        List<Crisis> expResult = null;
        List<Crisis> result = instance.getAll();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllByCriteria method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetAllByCriteria() throws Exception {
        System.out.println("getAllByCriteria");
        Criterion criterion = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        List<Crisis> expResult = null;
        List<Crisis> result = instance.getAllByCriteria(criterion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getByCriteriaWithLimit method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetByCriteriaWithLimit() throws Exception {
        System.out.println("getByCriteriaWithLimit");
        Criterion criterion = null;
        Integer count = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        List<Crisis> expResult = null;
        List<Crisis> result = instance.getByCriteriaWithLimit(criterion, count);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getByCriteriaByOrder method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetByCriteriaByOrder() throws Exception {
        System.out.println("getByCriteriaByOrder");
        Criterion criterion = null;
        String order = "";
        String[] orderBy = null;
        Integer count = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        List<Crisis> expResult = null;
        List<Crisis> result = instance.getByCriteriaByOrder(criterion, order, orderBy, count);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getByCriteriaWithAliasByOrder method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetByCriteriaWithAliasByOrder() throws Exception {
        System.out.println("getByCriteriaWithAliasByOrder");
        Criterion criterion = null;
        String order = "";
        String[] orderBy = null;
        Integer count = null;
        String aliasTable = "";
        Criterion aliasCriterion = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        List<Crisis> expResult = null;
        List<Crisis> result = instance.getByCriteriaWithAliasByOrder(criterion, order, orderBy, count, aliasTable, aliasCriterion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getByCriteriaWithInnerJoinByOrder method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetByCriteriaWithInnerJoinByOrder() throws Exception {
        System.out.println("getByCriteriaWithInnerJoinByOrder");
        Criterion criterion = null;
        String order = "";
        String[] orderBy = null;
        Integer count = null;
        String aliasTable = "";
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        List<Crisis> expResult = null;
        List<Crisis> result = instance.getByCriteriaWithInnerJoinByOrder(criterion, order, orderBy, count, aliasTable);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testUpdate_GenericType() throws Exception {
        System.out.println("update");
        Crisis e = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        instance.update(e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testUpdate_List() throws Exception {
        System.out.println("update");
        List<Crisis> entityCollection = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        instance.update(entityCollection);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of save method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testSave_GenericType() throws Exception {
        System.out.println("save");
        Crisis e = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        instance.save(e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of merge method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testMerge_GenericType() throws Exception {
        System.out.println("merge");
        Crisis e = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        instance.merge(e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of merge method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testMerge_List() throws Exception {
        System.out.println("merge");
        List<Crisis> entityCollection = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        instance.merge(entityCollection);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of save method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testSave_List() throws Exception {
        System.out.println("save");
        List<Crisis> entityCollection = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        instance.save(entityCollection);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testDelete_GenericType() throws Exception {
        System.out.println("delete");
        Crisis e = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        instance.delete(e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testDelete_List() throws Exception {
        System.out.println("delete");
        List<Crisis> entityCollection = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        instance.delete(entityCollection);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteByCriteria method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testDeleteByCriteria() throws Exception {
        System.out.println("deleteByCriteria");
        Criterion criterion = null;
        CrisisResourceFacadeImp instance = new CrisisResourceFacadeImp();
        instance.deleteByCriteria(criterion);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findByCriteria method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testFindByCriteria() throws Exception {
        System.out.println("findByCriteria");
        String columnName = "";
        Long value = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        List<CrisisDTO> expResult = null;
        List<CrisisDTO> result = instance.findByCriteria(columnName, value);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addCrisis method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testAddCrisis() throws Exception {
        System.out.println("addCrisis");
        CrisisDTO crisis = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        CrisisDTO expResult = null;
        CrisisDTO result = instance.addCrisis(crisis);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCrisisByID method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetCrisisByID() throws Exception {
        System.out.println("getCrisisByID");
        Long id = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        CrisisDTO expResult = null;
        CrisisDTO result = instance.findCrisisByID(id);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCrisisByCode method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetCrisisByCode() throws Exception {
        System.out.println("getCrisisByCode");
        String code = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        CrisisDTO expResult = null;
        CrisisDTO result = instance.getCrisisByCode(code);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of editCrisis method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testEditCrisis() throws Exception {
        System.out.println("editCrisis");
        CrisisDTO crisis = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        CrisisDTO expResult = null;
        CrisisDTO result = instance.editCrisis(crisis);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllCrisis method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetAllCrisis() throws Exception {
        System.out.println("getAllCrisis");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        List<CrisisDTO> expResult = null;
        List<CrisisDTO> result = instance.getAllCrisis();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllCrisisWithModelFamilies method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetAllCrisisWithModelFamilies() throws Exception {
        System.out.println("getAllCrisisWithModelFamilies");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        List<CrisisDTO> expResult = null;
        List<CrisisDTO> result = instance.getAllCrisisWithModelFamilies();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllCrisisByUserID method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testGetAllCrisisByUserID() throws Exception {
        System.out.println("getAllCrisisByUserID");
        Long userID = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        List<CrisisDTO> expResult = null;
        List<CrisisDTO> result = instance.getAllCrisisByUserID(userID);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isCrisisExists method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testIsCrisisExists() throws Exception {
        System.out.println("isCrisisExists");
        String crisisCode = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        boolean expResult = false;
        boolean result = instance.isCrisisExists(crisisCode);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of countClassifiersByCrisisCodes method, of class CrisisResourceFacadeImp.
     */
    @Test
    public void testCountClassifiersByCrisisCodes() throws Exception {
        System.out.println("countClassifiersByCrisisCodes");
        List<String> codes = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CrisisResourceFacade instance = (CrisisResourceFacade)container.getContext().lookup("java:global/classes/CrisisResourceFacadeImp");
        HashMap<String, Integer> expResult = null;
        HashMap<String, Integer> result = instance.countClassifiersByCrisisCodes(codes);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
