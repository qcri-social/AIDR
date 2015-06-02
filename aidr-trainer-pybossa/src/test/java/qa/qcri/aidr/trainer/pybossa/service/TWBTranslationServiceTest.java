package qa.qcri.aidr.trainer.pybossa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.*;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.entity.TaskTranslation;
import qa.qcri.aidr.trainer.pybossa.format.impl.TranslationRequestModel;
import qa.qcri.aidr.trainer.pybossa.service.impl.PybossaWorker;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 2/27/15
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})

public class TWBTranslationServiceTest {
    @Autowired
    TranslationService translationService;

    @Autowired
    ClientAppService clientAppService;

    @Autowired
    private ClientAppRunWorker pybossaWorker;

    private static final long TEST_CLIENT_ID = 3;
    private static final String NEW_CLIENT_APP_ID = "1211";
    private static final long TEST_TWB_PROJECT_ID = 5681;
    private static final String LONG_CODE = "This_is_a_long_answer_code_loooooooong";
    @Test
    public void testPybossaWorker() throws Exception {
        pybossaWorker.processTaskRunImport();
    }

    @Test
    public void testPullAllTranslationResponses() throws Exception {

        translationService.pullAllTranslationResponses(new Long(NEW_CLIENT_APP_ID), TEST_TWB_PROJECT_ID);
        assert(true);

    }


    @Test
    public void testPushAllTranslations() {

        ClientApp clientApp = clientAppService.getAllClientAppByClientID(new Long(TEST_CLIENT_ID)).get(0);
        Long tcProjectId = new Long(TEST_TWB_PROJECT_ID);
/*        if (clientApp.getTcProjectId() != null) {
            tcProjectId = clientApp.getTcProjectId();
        }
*/
        List translations = generateTestTranslationTasks(NEW_CLIENT_APP_ID, true, 6);
        Long clientAppId = new Long(NEW_CLIENT_APP_ID);
        List checkTranslations = translationService.findAllTranslationsByClientAppIdAndStatus(clientAppId, TaskTranslation.STATUS_NEW, 100);

        assert(checkTranslations.size() > 0);

        Map result = translationService.pushAllTranslations(clientAppId, new Long(TEST_TWB_PROJECT_ID), 0, 5);
        assertNotNull(result);

        List<TaskTranslation> inProgressTranslations = translationService.findAllTranslationsByClientAppIdAndStatus(clientAppId, TaskTranslation.STATUS_IN_PROGRESS, 100);
        assert(inProgressTranslations.size() > (checkTranslations.size()-5));
        Iterator<TaskTranslation> itr = inProgressTranslations.iterator();
        while (itr.hasNext()) {
            TaskTranslation translation = itr.next();
            assert(translation.getStatus().equals(TaskTranslation.STATUS_IN_PROGRESS));
        }

        Iterator<TaskTranslation> itr2 = checkTranslations.iterator();
        while (itr2.hasNext()) {
            TaskTranslation translation = itr2.next();
            //translationService.delete(translation);
        }

    }


    @Test
    public void testPushTranslationRequest() {
        TranslationRequestModel model = new TranslationRequestModel();
        model.setContactEmail("test@test.com");
        model.setTitle("Request from Unit Test");
        model.setSourceLanguage("eng");
        String[] targets = {"fra","esl"};
        model.setTargetLanguages(targets);
        model.setSourceWordCount(100); //random test
        model.setInstructions("Unit test instructions");
        model.setDeadline(new Date());
        model.setUrgency("high");
        model.setProjectId(TEST_TWB_PROJECT_ID);// hard coded for now

        model.setCallbackURL("https://www.example.com/my-callback-url");
        List translations = generateTestTranslationTasks(NEW_CLIENT_APP_ID, false, 2);
        model.setTranslationList(translations);

        Map result = translationService.pushTranslationRequest(model);
        assertNotNull(result);
    }


    @Test
    public void testPushDocumentForRequest() {
        TranslationRequestModel model = new TranslationRequestModel();
        model.setContactEmail("test@test.com");
        model.setTitle("Request from Unit Test");
        model.setSourceLanguage("eng");
        String[] targets = {"fra","esl"};
        model.setTargetLanguages(targets);
        long[] documentIds = {125549};
        model.setSourceDocumentIds(documentIds);
        model.setSourceWordCount(100); //random test
        model.setInstructions("Unit test instructions");
        model.setDeadline(new Date());
        model.setUrgency("high");
        model.setProjectId(TEST_TWB_PROJECT_ID);// hard coded for now

        model.setCallbackURL("https://www.example.com/my-callback-url");

        List translations = generateTestTranslationTasks(NEW_CLIENT_APP_ID, false, 2);
        model.setTranslationList(translations);

        Map result = translationService.pushDocumentForRequest(model);
        assertNotNull(result.get("document_id"));
    }


    private List generateTestTranslationTasks(String clientAppId, boolean persist, int number) {
        List<TaskTranslation> list = new ArrayList<TaskTranslation>();
        int loops = number/2;

        long id = 10;

        if (loops == 0) {loops = 1;}

        for (int i=0; i<loops; i++) {
            TaskTranslation translation = new TaskTranslation(id++, clientAppId, "63636", null, null, null, null, id, "Je m'appelle Jacques", TaskTranslation.STATUS_NEW);
            if (persist) {
                translationService.createTranslation(translation);
            }
            TaskTranslation translation2 = new TaskTranslation(id++, clientAppId, "63636", "Fred Jones", "22.22", "33.33", "http://google.com", id, "被害のダウンタウンの多くがあります", TaskTranslation.STATUS_NEW);
            if (persist) {
                translationService.createTranslation(translation2);
            }

            list.add(translation);
            list.add(translation2);
        }
        return list;

    }

    @Test
    public void testCreateWithCharset() throws Exception {
        TaskTranslation translation3 = new TaskTranslation(100l, "1000", "63636", null, null, null, null, 100l, "被害のダウンタウンの多くがあります", TaskTranslation.STATUS_NEW);
        translationService.createTranslation(translation3);
        TaskTranslation retrieve = translationService.findById(translation3.getTranslationId());
        assertNotNull(retrieve);
        assert(translation3.getOriginalText().equals(retrieve.getOriginalText()));
        assertNotNull(retrieve.getAuthor());
        assertNotNull(retrieve.getOriginalText());
    }

    @Test
    public void testCreateAndUpdateTranslation() throws Exception {
        int initialSize = translationService.findAllTranslations().size();
        TaskTranslation translation2 = new TaskTranslation(100l, "1000", "63636", null, null, null, null, 100l, "Je m'appelle Jacques", TaskTranslation.STATUS_NEW);
        translationService.createTranslation(translation2);
        assertNotNull(translation2.getAuthor());
        assertNotNull(translation2.getUrl());

    	String newVal = "TEST";
        translation2.setStatus(newVal);
        translation2.setAnswerCode(LONG_CODE);
    	translationService.updateTranslation(translation2);
        translation2 = translationService.findById(translation2.getTranslationId());
    	// we would really need to flush and clear the hibernate session for this next validation
    	assertEquals(newVal, translation2.getStatus());
    	translationService.delete(translation2);
    	assertEquals(initialSize, translationService.findAllTranslations().size());
    }

    @Test
    public void testFindByTaskId() throws Exception {
        TaskTranslation translation = new TaskTranslation();
        translation.setTaskId(new Long(898));
        translationService.createTranslation(translation);

        TaskTranslation found = translationService.findByTaskId(new Long(898));
        assertNotNull(found);
        translationService.delete(found);


    }

    @Test
    public void testFindByClientAppIdAndStatus () throws Exception {
        //ClientApp clientApp = clientAppService.getAllClientAppByClientID(new Long(TEST_CLIENT_ID)).get(0);
        String testStatus = "TESTING324";
        List<TaskTranslation> translations = generateTestTranslationTasks(NEW_CLIENT_APP_ID, true, 4);
        TaskTranslation taskTranslation = translations.get(0);
//        taskTranslation.setStatus(testStatus);
        translationService.updateTranslation(taskTranslation);

        List testList = translationService.findAllTranslationsByClientAppIdAndStatus(new Long(NEW_CLIENT_APP_ID), testStatus, 1000);
        assert(testList.size() == 1);

        String newClientAppId = "976";
        taskTranslation = translations.get(1);
        taskTranslation.setClientAppId(newClientAppId);
        translationService.updateTranslation(taskTranslation);


        testList = translationService.findAllTranslationsByClientAppIdAndStatus(new Long(newClientAppId), TaskTranslation.STATUS_NEW, 1000);
        assert(testList.size() == 1);
        Iterator<TaskTranslation> itr = translations.iterator();
        while (itr.hasNext()) {
            TaskTranslation translation = itr.next();
            //translationService.delete(translation);
        }


    }


    @Test
    public void testCharset() throws Exception {
        JSONObject info = new JSONObject();
        info.put("tweet", "à¤­à¥à¤¯à¤¾à¤•à¥à¤¤à¥‡ à¤°à¥‹à¤—");
        String tweet = (String)info.get("tweet");// maintain encoding - new String(info.getString("tweet").getBytes("ISO-8859-1"), "UTF-8");
        String encodedTweet = new String (tweet.getBytes("ISO-8859-1"), "UTF-8");
        assert encodedTweet != null;
    }



}
