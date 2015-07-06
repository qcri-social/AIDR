package qa.qcri.aidr.predict.featureextraction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by noora on 5/20/15.
 */
public class FeatureExtractorTest {
   // private static FeatureExtractor featureExtractor;
   /* private static final String remoteEJBJNDIName = TaggerConfigurator.getInstance().getProperty(TaggerConfigurationProperty.REMOTE_TASK_MANAGER_JNDI_NAME);
    private static TaskManagerRemote<DocumentDTO, Long> taskManager = null;*/

    @Before
    public void setUp() throws Exception {
       // featureExtractor = new FeatureExtractor();
       /* if (taskManager != null) {
            return;
        }
        //
        try {
            long startTime = System.currentTimeMillis();
            InitialContext ctx = new InitialContext();
            System.out.println("value of remote EJB: "+remoteEJBJNDIName);
            taskManager = (TaskManagerRemote<DocumentDTO, Long>) ctx.lookup(remoteEJBJNDIName);
            System.out.println("taskManager: " + taskManager + ", time taken to initialize = " + (System.currentTimeMillis() - startTime));
            if (taskManager != null) {
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }*/
    }

    @After
    public void tearDown() throws Exception {

    }

   /* @Test
    public void testProcessItem() throws Exception {
        DocumentDTO docDTO = taskManager.getTaskById(5464909L);
        Document doc1 = new TaggerDocument(docDTO);
        featureExtractor.processItem(doc1);
        String test = docDTO.getWordFeatures();
        //
        assertEquals("FollowFilter", test);

    }*/

   @Test
    public void testGetWordsInStringWithBigrams() throws Exception {
	   String[] actual = FeatureExtractor.getWordsInStringWithBigrams("hello what is your name", false);
	   //
	   String[] expected = {"hello", "your_name", "is", "hello_what", "what", "name", "your", "is_your", "what_is"};
	   assertArrayEquals(expected, actual);
    }
   
/*
    @Test
    public void testNaiveStemming() throws Exception {

    }

    @Test
    public void testIsStopword() throws Exception {

    }*/
}