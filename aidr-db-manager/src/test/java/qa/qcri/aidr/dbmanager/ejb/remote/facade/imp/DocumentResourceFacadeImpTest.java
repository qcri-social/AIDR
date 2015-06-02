/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;

/**
 *
 * @author nalemadi
 */
public class DocumentResourceFacadeImpTest {
    //
    static DocumentResourceFacadeImp documentResourceFacadeImp;
    static CrisisResourceFacadeImp crisisResourceFacadeImp;
    static EntityManager entityManager;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        documentResourceFacadeImp = new DocumentResourceFacadeImp();
        crisisResourceFacadeImp = new CrisisResourceFacadeImp();
        entityManager = Persistence.createEntityManagerFactory("ProjectDBManagerTest-ejbPU").createEntityManager();
	documentResourceFacadeImp.setEntityManager(entityManager);
        crisisResourceFacadeImp.setEntityManager(entityManager);
    }
    
    @AfterClass
    public static void shutDown() throws Exception {
	documentResourceFacadeImp.getEntityManager().close();
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of addDocument method, of class DocumentResourceFacadeImp.
     */
  /*  @Test
    public void testAddDocument() {
        try {
            System.out.println("addDocument");
            String tweet =  "\"filter_level\":\"medium\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null,"
                    +"\"id\":445125937915387905,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:14:28 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null,"
                    +"\"text\":\"'Those in the #cockpit' behind #missing #flight? http://t.co/OYHvM1t0CT\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"cockpit\",\"indices\":[14,22]},{\"text\":\"missing\","
                    +"\"indices\":[31,39]},{\"text\":\"flight\",\"indices\":[40,47]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://www.cnn.com/2014/03/15/world/asia/malaysia-airlines-plane/index.html\""
                    +",\"indices\":[49,71],\"display_url\":\"cnn.com/2014/03/15/wor\u2026\",\"url\":\"http://t.co/OYHvM1t0CT\"}],\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\""
                    +",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"\",\"favorited\":false,"
                    +"\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445125937915387905\",\"user\":{\"location\":\"Mexico, Distrito Federal. \",\"default_profile\":true,\"statuses_count\":1033,"
                    +"\"profile_background_tile\":false,\"lang\":\"en\",\"profile_link_color\":\"0084B4\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/135306436/1394809176\",\"id\":135306436,\"following\":null,"
                    +"\"favourites_count\":6,\"protected\":false,\"profile_text_color\":\"333333\",\"description\":\"Licenciado en derecho, he ocupado cargos dentro de la industria privada as\u00ED como dentro de la Administraci\u00F3n P\u00FAblica, tanto local (GDF), como Federal.\","
                    +"\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"C0DEED\",\"name\":\"Leonardo Larraga\",\"profile_background_color\":\"C0DEED\",\"created_at\":\"Tue Apr 20 23:12:25 +0000 2010\","
                    +"\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":726,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/440767007290429441/GkHsYcJj_normal.jpeg\","
                    +"\"geo_enabled\":false,\"profile_background_image_url\":\"http://abs.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://abs.twimg.com/images/themes/theme1/bg.png\","
                    +"\"follow_request_sent\":null,\"url\":\"http://instagram.com/larraga_ld\",\"utc_offset\":-21600,\"time_zone\":\"Mexico City\",\"notifications\":null,\"friends_count\":150,\"profile_use_background_image\":true,"
                    +"\"profile_sidebar_fill_color\":\"DDEEF6\",\"screen_name\":\"larraga_ld\",\"id_str\":\"135306436\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/440767007290429441/GkHsYcJj_normal.jpeg\","
                    +"\"is_translator\":false,\"listed_count\":0}}";
            String word = "{\"words\":[\"#prayformh370\"]}";
            DocumentDTO doc = new DocumentDTO();
            CrisisDTO crisis = crisisResourceFacadeImp.findCrisisByID(53L);
            doc.setCrisisDTO(crisis);
            doc.setHasHumanLabels(true);
            doc.setIsEvaluationSet(true);
            doc.setLanguage("en");
            doc.setDoctype("Tweet");
            doc.setData(tweet);
            doc.setWordFeatures(word);
            doc.setValueAsTrainingSample(0.5);
            entityManager.getTransaction().begin();
            DocumentDTO result = documentResourceFacadeImp.addDocument(doc);
           // entityManager.getTransaction().commit();
            //
            assertNotNull(result);
            assertEquals(tweet, result.getData());
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of deleteNoLabelDocument method, of class DocumentResourceFacadeImp.
     */
 /*   @Test
    public void testDeleteNoLabelDocument_DocumentDTO() {
        System.out.println("deleteNoLabelDocument");
        String delTweet = "TestDelNoLabel\"filter_level\":\"medium\",\"retweeted\":false,\"in_reply_to_screen_name\":null,\"possibly_sensitive\":false,\"truncated\":false,\"lang\":\"en\",\"in_reply_to_status_id_str\":null"
                +",\"id\":445127926129111040,\"in_reply_to_user_id_str\":null,\"in_reply_to_status_id\":null,\"created_at\":\"Sun Mar 16 09:22:22 +0000 2014\",\"favorite_count\":0,\"place\":null,\"coordinates\":null"
                +",\"text\":\"#pray #for #MH370 http://t.co/LriBnI3AY4\",\"contributors\":null,\"geo\":null,\"entities\":{\"hashtags\":[{\"text\":\"pray\",\"indices\":[0,5]},{\"text\":\for\",\"indices\":[6,10]}"
                +",{\"text\":\"MH370\",\"indices\":[11,17]}],\"symbols\":[],\"urls\":[{\"expanded_url\":\"http://instagram.com/p/lmSm_8iRMl/\",\"indices\":[18,40],\"display_url\":\"instagram.com/p/lmSm_8iRMl/\",\"url\":\"http://t.co/LriBnI3AY4\"}]"
                +",\"user_mentions\":[]},\"aidr\":{\"crisis_code\":\"2014-03-mh370\",\"doctype\":\"twitter\",\"crisis_name\":\"Malaysia Airlines flight #MH370\"},\"source\":\"\u003ca href=\"http://instagram.com\" rel=\"\","
                +"\"favorited\":false,\"retweet_count\":0,\"in_reply_to_user_id\":null,\"id_str\":\"445127926129111040\",\"user\":{\"location\":\"Ammar \\ 0 \\/ 3 Farah \",\"default_profile\":false,\"statuses_count\":9545,\"profile_background_tile\":true\""
                +",\"lang\":\"en\",\"profile_link_color\":\"2C7BA5\",\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/375056560/1393492465\",\"id\":375056560,\"following\":null,\"favourites_count\":308,\"protected\":false,\"profile_text_color\":\"F06D5F\","
                +"\"description\":\"....forever and ever.... In Shaa Allah\",\"verified\":false,\"contributors_enabled\":false,\"profile_sidebar_border_color\":\"DEDDE3\",\"name\":\"just tweeet!\",\"profile_background_color\":\"8C618F\",\"created_at\":\"Sat Sep 17 12:33:48 +0000 2011\","
                +"\"is_translation_enabled\":false,\"default_profile_image\":false,\"followers_count\":360,\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/438965259756265472/ybQJLjpg_normal.jpeg\",\"geo_enabled\":true,\"profile_background_image_url\":\"http://pbs.twimg.com/profile_background_images/378800000092201753/9c1ca7a12995ca568c2d505154c38496.png\","
                +"\"profile_background_image_url_https\":\"https://pbs.twimg.com/profile_background_images/378800000092201753/9c1ca7a12995ca568c2d505154c38496.png\",\"follow_request_sent\":null,\"url\":null,\"utc_offset\":28800,\"time_zone\":\"Kuala Lumpur\",\"notifications\":null,\"friends_count\":446,"
                +"\"profile_use_background_image\":true,\"profile_sidebar_fill_color\":\"FFDB73\",\"screen_name\":\"_farammar_\",\"id_str\":\"375056560\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/438965259756265472/ybQJLjpg_normal.jpeg\","
                + "\"is_translator\":false,\"listed_count\":0}}";
        DocumentDTO doc = new DocumentDTO();
        doc.setHasHumanLabels(false);
        doc.setIsEvaluationSet(true);
        doc.setLanguage("en");
        doc.setDoctype("Tweet");
        doc.setData(delTweet);
        doc.setValueAsTrainingSample(0.5);
        entityManager.getTransaction().begin();
        DocumentDTO delDoc = documentResourceFacadeImp.addDocument(doc);
        entityManager.getTransaction().commit();
        int expResult = 1;
        int result = documentResourceFacadeImp.deleteNoLabelDocument(delDoc);
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteNoLabelDocument method, of class DocumentResourceFacadeImp.
     */
 /*   @Test
    public void testDeleteNoLabelDocument_List() {
        System.out.println("deleteNoLabelDocument");
        List<DocumentDTO> collection = null;
        int result = documentResourceFacadeImp.deleteNoLabelDocument(collection);
        assertEquals(1, result);
    }

    /**
     * Test of deleteUnassignedDocument method, of class DocumentResourceFacadeImp.
     */
 /*   @Test
    public void testDeleteUnassignedDocument() {
        System.out.println("deleteUnassignedDocument");
        DocumentDTO document = null;
        int result = documentResourceFacadeImp.deleteUnassignedDocument(document);
        assertEquals(1, result);
    }

    /**
     * Test of deleteUnassignedDocumentCollection method, of class DocumentResourceFacadeImp.
     */
 /*   @Test
    public void testDeleteUnassignedDocumentCollection() {
        try {
            System.out.println("deleteUnassignedDocumentCollection");
            List<DocumentDTO> collection = null;
            int result = documentResourceFacadeImp.deleteUnassignedDocumentCollection(collection);
            assertEquals(1, result);
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testDeleteUnassignedDocumentCollection failed");
        }
    }

    /**
     * Test of deleteStaleDocuments method, of class DocumentResourceFacadeImp.
     */
 /*   @Test
    public void testDeleteStaleDocuments() {
        System.out.println("deleteStaleDocuments");
        String joinType = "";
        String joinTable = "";
        String joinColumn = "";
        String sortOrder = "";
        String[] orderBy = null;
        String maxTaskAge = "";
        String scanInterval = "";
        int result = documentResourceFacadeImp.deleteStaleDocuments(joinType, joinTable, joinColumn, sortOrder, orderBy, maxTaskAge, scanInterval);
        assertEquals(1, result);
    }

    /**
     * Test of editDocument method, of class DocumentResourceFacadeImp.
     */
 /*   @Test
    public void testEditDocument() {
        try {
            System.out.println("editDocument");
            DocumentDTO doc = null;
            DocumentDTO expResult = null;
            DocumentDTO result = documentResourceFacadeImp.editDocument(doc);
            assertEquals(expResult, result);
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testEditDocument failed");
        }
    }

    /**
     * Test of deleteDocument method, of class DocumentResourceFacadeImp.
     */
 /*   @Test
    public void testDeleteDocument() {
        System.out.println("deleteDocument");
        //DocumentDTO doc = documentResourceFacadeImp.findDocumentByID(4579250L);
        Integer result = documentResourceFacadeImp.deleteDocument(null);
        assertEquals(Integer.valueOf(1), result);
    }

    /**
     * Test of findByCriteria method, of class DocumentResourceFacadeImp.
     */
    @Test
    public void testFindByCriteria() {
        try {
            System.out.println("findByCriteria");
            String columnName = "isEvaluationSet";
            boolean value = true;
            List<DocumentDTO> result = documentResourceFacadeImp.findByCriteria(columnName, value);
            assertEquals(true, result.get(0).getIsEvaluationSet());
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of findDocumentByID method, of class DocumentResourceFacadeImp.
     */
    @Test
    public void testFindDocumentByID() {
        try {
            System.out.println("findDocumentByID");
            Long id = (long)4579281;
            DocumentDTO result = documentResourceFacadeImp.findDocumentByID(id);
            assertEquals(Long.valueOf(4579281), result.getDocumentID());
            assertEquals(Long.valueOf(117), result.getCrisisDTO().getCrisisID());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testFindDocumentByID failed");
        }
    }

    /**
     * Test of findDocumentsByCrisisID method, of class DocumentResourceFacadeImp.
     */
    @Test
    public void testFindDocumentsByCrisisID() {
        try {
            System.out.println("findDocumentsByCrisisID");
            Long crisisId = (long)117;
            List<DocumentDTO> result = documentResourceFacadeImp.findDocumentsByCrisisID(crisisId);
            assertEquals(Long.valueOf(117), result.get(0).getCrisisDTO().getCrisisID());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testFindDocumentsByCrisisID failed");
        }
    }

    /**
     * Test of getAllDocuments method, of class DocumentResourceFacadeImp.
     */
    @Test
    public void testGetAllDocuments() {
        try {
            System.out.println("getAllDocuments");
            List<DocumentDTO> result = documentResourceFacadeImp.getAllDocuments();
            assertNotNull(result);
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testGetAllDocuments failed");
        }
    }

    /**
     * Test of findLabeledDocumentsByCrisisID method, of class DocumentResourceFacadeImp.
     */
    @Test
    public void testFindLabeledDocumentsByCrisisID() {
        try {
            System.out.println("findLabeledDocumentsByCrisisID");
            Long crisisId = (long)125;
            List<DocumentDTO> result = documentResourceFacadeImp.findLabeledDocumentsByCrisisID(crisisId);
            assertNotNull(result);
            assertEquals(true, result.get(0).getHasHumanLabels());
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testFindLabeledDocumentsByCrisisID failed");
        }
    }

    /**
     * Test of findUnLabeledDocumentsByCrisisID method, of class DocumentResourceFacadeImp.
     */
    @Test
    public void testFindUnLabeledDocumentsByCrisisID() {
        try {
            System.out.println("findUnLabeledDocumentsByCrisisID");
            Long crisisId = (long)117;
            List<DocumentDTO> result = documentResourceFacadeImp.findUnLabeledDocumentsByCrisisID(crisisId);
            assertNotNull(result);
            //assertEquals(false, result.get(0).getHasHumanLabels());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex); 
            fail("testFindUnLabeledDocumentsByCrisisID failed");
        }
    }
    
    
    /**
     * Test of getDocumentCollectionForNominalLabel method, of class DocumentResourceFacadeImp.
     */
    @Test
    public void testGetDocumentCollectionForNominalLabel() {
        try {
            System.out.println("getDocumentCollectionForNominalLabel");
            Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("documentID", Long.valueOf(4581475)))
				.add(Restrictions.eq("hasHumanLabels",true));
            List<DocumentDTO> result = documentResourceFacadeImp.getDocumentCollectionForNominalLabel(criterion);
            //
            assertNotNull(result);
            assertEquals(Long.valueOf(122), result.get(0).getCrisisDTO().getCrisisID());
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testGetDocumentCollectionForNominalLabel failed");
        }
    }

    /**
     * Test of getDocumentCollectionWithNominalLabelData method, of class DocumentResourceFacadeImp.
     */
   /* @Test
    public void testGetDocumentCollectionWithNominalLabelData() {
        try {
            System.out.println("getDocumentCollectionWithNominalLabelData");
            Long nominalLabelID = 320L;
            List<DocumentDTO> result = documentResourceFacadeImp.getDocumentCollectionWithNominalLabelData(nominalLabelID);
            //
            assertNotNull(result);
            assertEquals(Long.valueOf(320), result.get(0).getNominaLabel().getNominalLabelId());
        } catch (Exception ex) {
           // Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testGetDocumentCollectionWithNominalLabelData failed: "+ex);
        }
    }
    
    /**
     * Test of getDocumentWithAllFieldsByID method, of class DocumentResourceFacadeImp.
     */
    @Test
    public void testGetDocumentWithAllFieldsByID() {
        try {
            System.out.println("getDocumentWithAllFieldsByID");
            Long id = 4579269L;
            DocumentDTO result = documentResourceFacadeImp.getDocumentWithAllFieldsByID(id);
            //
            assertEquals(id, result.getDocumentID());
        } catch (PropertyNotSetException ex) {
           // Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testGetDocumentWithAllFieldsByID failed");
        }
    }
    
    /**
     * Test of isDocumentExists method, of class DocumentResourceFacadeImp.
     */
    @Test
    public void testIsDocumentExists() {
        try {
            System.out.println("isDocumentExists");
            Long id = 4579281L;
            boolean result = documentResourceFacadeImp.isDocumentExists(id);
            assertEquals(true, result);
            //
            boolean result2 = documentResourceFacadeImp.isDocumentExists(1L);
            assertEquals(false, result2);
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("testIsDocumentExists failed");
        }
    }
    
    /**
     * Test of updateHasHumanLabel method, of class DocumentResourceFacadeImp.
     */
 /*   @Test
    public void testUpdateHasHumanLabel() {
        try {
            System.out.println("updateHasHumanLabel");
            DocumentDTO document = documentResourceFacadeImp.findDocumentByID(4579252L);
            documentResourceFacadeImp.updateHasHumanLabel(document);
            //
            assertEquals(true, document.getHasHumanLabels());
        } catch (PropertyNotSetException ex) {
            //Logger.getLogger(DocumentResourceFacadeImpTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("updateHasHumanLabel failed");
        }
    }*/
}
