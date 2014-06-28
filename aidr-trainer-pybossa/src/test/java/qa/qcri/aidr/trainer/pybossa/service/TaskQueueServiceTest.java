package qa.qcri.aidr.trainer.pybossa.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class TaskQueueServiceTest {

    //@Autowired
    //TaskQueueService taskQueueService;

   // @Autowired
   // TaskLogService taskLogService;

    @Test
    public void testCreateTaskQueue() throws Exception {

        //long taskQueueID = 86526;
       // taskLogService.deleteAbandonedTaskLog(taskQueueID);
       // taskQueueService.deleteAbandonedTaskQueue(taskQueueID);

        /**
            JSONParser parser = new JSONParser();
            List<Long> arrayList = new ArrayList<Long>();


            Object obj = parser.parse(new FileReader("/Users/jlucas/Downloads/pybossaDownload/nov9_11_02am.json"));

            JSONArray objs = (JSONArray) obj;

            for(int i = 0; i < objs.size(); i++){
                JSONObject item = (JSONObject)objs.get(i);
                Long userID = (Long) item.get("user_id");
                if(arrayList.size() > 0){
                    if(!arrayList.contains(userID)) {
                        arrayList.add(userID);
                    }
                }
                else{
                    arrayList.add(userID);
                }
                System.out.println("userID;: " + arrayList.size());
            }

         **/

      //  Long clientAppID = new Long(94);
      //  List<TaskQueue> taskQueues =  taskQueueService.getTaskQueueByClientAppStatus(clientAppID,1);
      //  System.out.println("taskQueues : " + taskQueues.size());

       // System.out.println(taskQueueService.getCountTaskQeueByStatus("status", 1));
        //System.out.println(taskQueueService.getTaskQueueByDocument(clientAppID, new Long(359250)));

        /**
        Long taskID = new Long(1);
        Long clientAppID = new Long(1);
        Long docID = new Long(1);
        int status = 1;

        TaskQueue taskQueue = new TaskQueue(taskID, clientAppID, docID, status);
        taskQueueService.createTaskQueue(taskQueue);

        TaskLog taskLog = new TaskLog(taskQueue.getTaskQueueID(), taskQueue.getStatus());
        taskLogService.createTaskLog(taskLog);  **/


    }

}
