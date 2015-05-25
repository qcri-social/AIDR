/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

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
import qa.qcri.aidr.dbmanager.dto.UsersDTO;

/**
 *
 * @author nalemadi
 */
public class UsersResourceFacadeImpTest {
    //
    static UsersResourceFacadeImp usersResourceFacadeImp;
    static EntityManager entityManager;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        usersResourceFacadeImp = new UsersResourceFacadeImp();
        entityManager = Persistence.createEntityManagerFactory("ProjectDBManagerTest-ejbPU").createEntityManager();
	usersResourceFacadeImp.setEntityManager(entityManager);
    }
    
    @AfterClass
    public static void shutDown() throws Exception {
	usersResourceFacadeImp.getEntityManager().close();
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Test of getUserByName method, of class UsersResourceFacadeImp.
     */
    @Test
    public void testGetUserByName() {
        try {
            System.out.println("getUserByName");
            String name = "Pybossa";
            UsersDTO result = usersResourceFacadeImp.getUserByName(name);
            Long userId = result.getUserID();
            //
            assertEquals(Long.valueOf(6), userId);
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(UsersResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Long id = 6L;
            UsersDTO result = usersResourceFacadeImp.getUserById(id);
            assertEquals("Pybossa", result.getName());
        } catch (PropertyNotSetException ex) {
            fail("testGetUserById failed");
           // Logger.getLogger(UsersResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getAllUsersByName method, of class UsersResourceFacadeImp.
     */
    @Test
    public void testGetAllUsersByName() {
        try {
            System.out.println("getAllUsersByName");
            String name = "PopovskiVasko";
            List<UsersDTO> result = usersResourceFacadeImp.getAllUsersByName(name);
            //
            assertEquals("PopovskiVasko", result.get(0).getName());
        } catch (PropertyNotSetException ex) {
            fail("testGetAllUsersByName failed");
            //Logger.getLogger(UsersResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findAllCrisisByUserID method, of class UsersResourceFacadeImp.
     */
    @Test
    public void testFindAllCrisisByUserID() {
        try {
            System.out.println("findAllCrisisByUserID");
            List<CrisisDTO> result = usersResourceFacadeImp.findAllCrisisByUserID(Long.valueOf(10));
            //
            assertEquals(Long.valueOf(10), result.get(0).getUsersDTO().getUserID());
        } catch (PropertyNotSetException ex) {
            fail("testFindAllCrisisByUserID failed");
           // Logger.getLogger(UsersResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of addUser method, of class UsersResourceFacadeImp.
     */
    @Test
    public void testAddUser() {
        System.out.println("addUser");
        UsersDTO newUser = new UsersDTO("userDBTest", "normal");
        //
        entityManager.getTransaction().begin();
        UsersDTO result = usersResourceFacadeImp.addUser(newUser);
        entityManager.getTransaction().commit();
        //
        assertEquals("userDBTest", result.getName());
    }

    /**
     * Test of deleteUser method, of class UsersResourceFacadeImp.
     */
    @Test
    public void testDeleteUser() {
        System.out.println("deleteUser");
        UsersDTO user = new UsersDTO("userDBDelTest", "normal");
        //
        entityManager.getTransaction().begin();
        UsersDTO userToDel = usersResourceFacadeImp.addUser(user);
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();
        int result = usersResourceFacadeImp.deleteUser(userToDel.getUserID());
        entityManager.getTransaction().commit();
        //
        assertEquals(1, result);
    }

    /**
     * Test of findByCriteria method, of class UsersResourceFacadeImp.
     */
    @Test
    public void testFindByCriteria() {
        try {
            System.out.println("findByCriteria");
            String columnName = "role";
            String value = "normal";
            List<UsersDTO> result = usersResourceFacadeImp.findByCriteria(columnName, value);
            assertNotNull(result);
            assertEquals("normal", result.get(0).getRole());
        } catch (PropertyNotSetException ex) {
            fail("The test case is a prototype.");
           // Logger.getLogger(UsersResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (PropertyNotSetException ex) {
            fail("testGetAllUsers failed");
            //Logger.getLogger(UsersResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
