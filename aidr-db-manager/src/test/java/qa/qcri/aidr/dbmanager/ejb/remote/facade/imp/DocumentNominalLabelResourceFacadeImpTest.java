/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

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
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelIdDTO;

/**
 *
 * @author nalemadi
 */
public class DocumentNominalLabelResourceFacadeImpTest {
    static DocumentNominalLabelResourceFacadeImp documentNominalLabelResourceFacadeImp;
    static DocumentResourceFacadeImp documentResourceFacadeImp;
    static EntityManager entityManager;
    
    public DocumentNominalLabelResourceFacadeImpTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        documentNominalLabelResourceFacadeImp = new DocumentNominalLabelResourceFacadeImp();
        documentResourceFacadeImp = new DocumentResourceFacadeImp();
        entityManager = Persistence.createEntityManagerFactory("ProjectDBManagerTest-ejbPU").createEntityManager();
	documentNominalLabelResourceFacadeImp.setEntityManager(entityManager);
        documentResourceFacadeImp.setEntityManager(entityManager);
    }
    
    @AfterClass
    public static void shutDown() throws Exception {
	documentNominalLabelResourceFacadeImp.getEntityManager().close();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of saveDocumentNominalLabel method, of class DocumentNominalLabelResourceFacadeImp.
     */
 /*   @Test
    public void testSaveDocumentNominalLabel() throws Exception {
        System.out.println("saveDocumentNominalLabel");
        DocumentNominalLabelDTO documentNominalLabel = null;
        documentNominalLabelResourceFacadeImp.saveDocumentNominalLabel(documentNominalLabel);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of foundDuplicate method, of class DocumentNominalLabelResourceFacadeImp.
     */
    @Test
    public void testFoundDuplicate() {
        try {
            System.out.println("foundDuplicate");
            DocumentNominalLabelDTO documentNominalLabel = documentNominalLabelResourceFacadeImp.getAllDocuments().get(0);
            boolean result = documentNominalLabelResourceFacadeImp.foundDuplicate(documentNominalLabel);
            assertEquals(false, result);
        } catch (PropertyNotSetException ex) {
            fail("foundDuplicate failed");
            //Logger.getLogger(DocumentNominalLabelResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of addDocument method, of class DocumentNominalLabelResourceFacadeImp.
     */
 /*   @Test
    public void testAddDocument() throws Exception {
        System.out.println("addDocument");
        DocumentNominalLabelDTO doc = null;
        DocumentNominalLabelDTO expResult = null;
        DocumentNominalLabelDTO result = documentNominalLabelResourceFacadeImp.addDocument(doc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of editDocument method, of class DocumentNominalLabelResourceFacadeImp.
     */
 /*   @Test
    public void testEditDocument() throws Exception {
        System.out.println("editDocument");
        DocumentNominalLabelDTO doc = null;
        DocumentNominalLabelDTO expResult = null;
        DocumentNominalLabelDTO result = documentNominalLabelResourceFacadeImp.editDocument(doc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteDocument method, of class DocumentNominalLabelResourceFacadeImp.
     */
 /*   @Test
    public void testDeleteDocument() throws Exception {
        System.out.println("deleteDocument");
        DocumentNominalLabelDTO doc = null;
        Integer expResult = null;
        Integer result = documentNominalLabelResourceFacadeImp.deleteDocument(doc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findByCriteria method, of class DocumentNominalLabelResourceFacadeImp.
     */
    @Test
    public void testFindByCriteria() {
        try {
            System.out.println("findByCriteria");
            String columnName = "documentID";
            Long value = 4579252L;
            // return null value of docID ??
            List<DocumentNominalLabelDTO> result = documentNominalLabelResourceFacadeImp.findByCriteria(columnName, value);
            //
            assertNotNull(result);
            assertEquals(Long.valueOf(320), result.get(0).getNominalLabelDTO().getNominalLabelId());
        } catch (PropertyNotSetException ex) {
            fail("findByCriteria failed");
            //Logger.getLogger(DocumentNominalLabelResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findDocumentByPrimaryKey method, of class DocumentNominalLabelResourceFacadeImp.
     */
    @Test
    public void testFindDocumentByPrimaryKey() {
        try {
            System.out.println("findDocumentByPrimaryKey");
            DocumentNominalLabelIdDTO id = new DocumentNominalLabelIdDTO(4579252L, 322L, 9L);
            // return null vaule of doc ID
            DocumentNominalLabelDTO result = documentNominalLabelResourceFacadeImp.findDocumentByPrimaryKey(id);
            //
            assertEquals(Long.valueOf(4579252), result.getDocumentDTO().getDocumentID());
        } catch (PropertyNotSetException ex) {
            fail("findDocumentByPrimaryKey failed");
            //Logger.getLogger(DocumentNominalLabelResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of isDocumentExists method, of class DocumentNominalLabelResourceFacadeImp.
     */
  /*  @Test
    public void testIsDocumentExists_DocumentNominalLabelIdDTO() throws Exception {
        System.out.println("isDocumentExists");
        DocumentNominalLabelIdDTO id = null;
        boolean expResult = false;
        boolean result = documentNominalLabelResourceFacadeImp.isDocumentExists(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDocumentExists method, of class DocumentNominalLabelResourceFacadeImp.
     */
    @Test
    public void testIsDocumentExists_Long() throws Exception {
        System.out.println("isDocumentExists");
        Long id = 4579255L;
        boolean result = documentNominalLabelResourceFacadeImp.isDocumentExists(id);
        assertEquals(true, result);
        //
        boolean result2 = documentNominalLabelResourceFacadeImp.isDocumentExists(1L);
        assertEquals(false, result2);
    }

    /**
     * Test of getAllDocuments method, of class DocumentNominalLabelResourceFacadeImp.
     */
    @Test
    public void testGetAllDocuments() {
        try {
            System.out.println("getAllDocuments");
            List<DocumentNominalLabelDTO> result = documentNominalLabelResourceFacadeImp.getAllDocuments();
            //
            assertNotNull(result);
        } catch (PropertyNotSetException ex) {
            fail("getAllDocuments failed");
            //Logger.getLogger(DocumentNominalLabelResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findLabeledDocumentByID method, of class DocumentNominalLabelResourceFacadeImp.
     */
    @Test
    public void testFindLabeledDocumentByID() {
        try {
            System.out.println("findLabeledDocumentByID");
            DocumentNominalLabelDTO result = documentNominalLabelResourceFacadeImp.findLabeledDocumentByID(4579256L);
            //
            assertEquals(Long.valueOf(4579256), result.getNominalLabelDTO().getNominalLabelId());
        } catch (PropertyNotSetException ex) {
            fail("findLabeledDocumentByID failed");
            //Logger.getLogger(DocumentNominalLabelResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getLabeledDocumentCollectionForNominalLabel method, of class DocumentNominalLabelResourceFacadeImp.
     */
  /*  @Test
    public void testGetLabeledDocumentCollectionForNominalLabel() throws Exception {
        System.out.println("getLabeledDocumentCollectionForNominalLabel");
        Integer nominalLabelID = null;
        List<DocumentNominalLabelDTO> expResult = null;
        List<DocumentNominalLabelDTO> result = documentNominalLabelResourceFacadeImp.getLabeledDocumentCollectionForNominalLabel(nominalLabelID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
}
