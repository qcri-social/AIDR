package qa.qcri.aidr.trainer.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 9:32 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class TaskAnswerServiceTest {

    @Autowired
    private TaskAnswerService taskAnswerService;

    @Autowired
    private CrisisService crisisService;



    @Test
    public void testTaskAnswerOne() throws Exception{

        String data = "[{\"info\": {\"category\": \"Turkey,Tartus,Damascus\", \"text\": \"Spiegel: \\\"Esad'in gitmesi zor, ABD Suriye'nin bolunmesine kendini alistiriyor\\\".  http://t.co/AKCNpk4z3u Gene kabak bize patladi iyi mi...\", \"crisisID\": 14, \"documentID\": 359315, \"tweetid\": \"2.76E17\"}, \"user_id\": 1, \"links\": [\"<link rel='parent' title='app' href='http://localhost:5000/api/app/1'/>\", \"<link rel='parent' title='task' href='http://localhost:5000/api/task/558'/>\"], \"task_id\": 558, \"created\": \"2013-09-16T11:48:35.663822\", \"finish_time\": \"2013-09-16T11:48:35.663845\", \"calibration\": null, \"app_id\": 1, \"user_ip\": null, \"link\": \"<link rel='self' title='taskrun' href='http://localhost:5000/api/taskrun/38'/>\", \"timeout\": null, \"id\": 38}]";

        String data2 = "[{\"info\": {\"category\": \"Turkey,Tartus,Damascus\", \"crisisID\": 14, \"text\":\"#syria\n#????_???????_????????\n#??????_??????\n??? ???? ????? ?????? ????? ??? ???? ????: {?????? ????????... http://t.co/tUXEZJOXW\",\"tweetid\": \"2.76E17\", \"aidrID\": 2, \"documentID\": 359305}, \"user_id\": 1, \"task_id\": 1353, \"created\": \"2013-09-23T08:30:48.345627\", \"finish_time\": \"2013-09-23T08:30:48.345651\", \"calibration\": null, \"app_id\": 81, \"user_ip\": null, \"timeout\": null, \"id\": 234}]";
        String category =  "Turkey";
        String[] categorySet = category.split("\\,");

        System.out.println(categorySet.length);
    }


    public void testTaskAnswerTwo() throws Exception{
        String data = "[{\"info\": {\"category\": \"Caution or Advice.\", \"text\": \"Spiegel: \\\"Esad'in gitmesi zor, ABD Suriye'nin bolunmesine kendini alistiriyor\\\".  http://t.co/AKCNpk4z3u Gene kabak bize patladi iyi mi...\", \"crisisID\": 14, \"documentID\": 359315, \"tweetid\": \"2.76E17\"}, \"user_id\": 1, \"links\": [\"<link rel='parent' title='app' href='http://localhost:5000/api/app/1'/>\", \"<link rel='parent' title='task' href='http://localhost:5000/api/task/558'/>\"], \"task_id\": 558, \"created\": \"2013-09-16T11:48:35.663822\", \"finish_time\": \"2013-09-16T11:48:35.663845\", \"calibration\": null, \"app_id\": 1, \"user_ip\": null, \"link\": \"<link rel='self' title='taskrun' href='http://localhost:5000/api/taskrun/38'/>\", \"timeout\": null, \"id\": 38}]";

        String answer="[{\"attributeID\":15,\"labelID\":4294967295},{\"attributeID\":17,\"labelID\":4294967295},{\"attributeID\":18,\"labelID\":4294967295},{\"attributeID\":20,\"labelID\":4294967295}]";

       // taskAnswerService.insertTaskAnswer(data);
    }


    public void testInsertTaskAnswer() throws Exception {
      //  String answer="[{\"attributeID\":15,\"labelID\":4294967295},{\"attributeID\":17,\"labelID\":4294967295},{\"attributeID\":18,\"labelID\":4294967295},{\"attributeID\":20,\"labelID\":4294967295}]";
      //  TaskAnswer taskAnswer = new TaskAnswer(new Long(359323), new Long(1), answer);

      //  taskAnswerService.insertTaskAnswer("testing");
       // taskAnswer.setAnswer('[{"attributeID":15,"labelID":4294967295},{"attributeID":17,"labelID":4294967295},{"attributeID":18,"labelID":4294967295},{"attributeID":20,"labelID":12}]');
   /**
        String inputData = "[{\"info\": {\"category\": \"Caution or Advice.\", \"text\": \"Spiegel: \\\"Esad'in gitmesi zor, ABD Suriye'nin bolunmesine kendini alistiriyor\\\".  http://t.co/AKCNpk4z3u Gene kabak bize patladi iyi mi...\", \"crisisID\": 14, \"documentID\": 359315, \"tweetid\": \"2.76E17\"}, \"user_id\": 1, \"links\": [\"<link rel='parent' title='app' href='http://localhost:5000/api/app/1'/>\", \"<link rel='parent' title='task' href='http://localhost:5000/api/task/558'/>\"], \"task_id\": 558, \"created\": \"2013-09-16T11:48:35.663822\", \"finish_time\": \"2013-09-16T11:48:35.663845\", \"calibration\": null, \"app_id\": 1, \"user_ip\": null, \"link\": \"<link rel='self' title='taskrun' href='http://localhost:5000/api/taskrun/38'/>\", \"timeout\": null, \"id\": 38}]";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(inputData);

        JSONArray jsonObject = (JSONArray) obj;
        Iterator itr= jsonObject.iterator();


        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();
            JSONObject info = (JSONObject)featureJsonObj.get("info");
           // String category = (String)info.get("category");
            Long crisisID = (Long)info.get("crisisID");
            Long documentID = (Long)info.get("documentID");

            Crisis crisis = crisisService.findByCrisisID(crisisID) ;
            Set<ModelFamily> modelFamilySet= crisis.getModelFamilySet();

            JSONArray taskAnswerJson = new JSONArray();
            String category ="Food and water";
            for (ModelFamily modelFamily : modelFamilySet){
                Set<NominalLabel> nominalLabelSet = modelFamily.getNominalAttribute().getNominalLabelSet();
                for (NominalLabel nominalLabel : nominalLabelSet){
                    JSONObject taskAnswerElement = new JSONObject();
                    String labelName =  nominalLabel.getName();
                    if(labelName.equalsIgnoreCase(category)){
                        //{"attributeID":15,"labelID":4294967295}
                       Long attributeID = nominalLabel.getNominalAttributeID();
                       Long labelID = nominalLabel.getNorminalLabelID();
                       taskAnswerElement.put("attributeID", attributeID) ;
                       taskAnswerElement.put("labelID", labelID) ;
                       taskAnswerJson.add(taskAnswerElement);
                       break;
                    }
                   System.out.print("labelName: "   + labelName + "\n");
                }
            }
            TaskAnswer taskAnswer = new TaskAnswer(documentID, new Long(1), taskAnswerJson.toJSONString());
          //  taskAnswerDao.insertTaskAnswer(taskAnswer);
            System.out.print("category: " + category + " crisisID:" + crisisID+  "   documentID:" + documentID);

        }
        **/
    }
}
