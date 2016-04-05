package qa.qcri.aidr.trainer.pybossa.format.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.entity.TaskTranslation;

/**
 * Created by jlucas on 4/3/16.
 */
public class TranslationResponseModel {

    private TaskTranslation taskTranslation;
    private JSONObject info;
    private JSONArray taskResponse;
    private String answer;
    private ClientApp clientApp;

    public TranslationResponseModel(TaskTranslation taskTranslation, ClientApp clientApp, String answer) {
        this.taskTranslation = taskTranslation;
        this.answer = answer;
        this.clientApp = clientApp;
    }

    public TaskTranslation getTaskTranslation() {
        return taskTranslation;
    }


    public JSONObject getInfo() {
        info = new JSONObject();

        info.put("documentID", taskTranslation.getDocumentId()) ;
        info.put("category", answer) ;
        info.put("aidrID",clientApp.getClient().getAidrUserID()) ;
        info.put("attributeID", clientApp.getNominalAttributeID()) ;
        info.put("crisisID", clientApp.getCrisisID()) ;

        return info;

    }

    public void setInfo(JSONObject info) {
        this.info = info;
    }

    public JSONArray getTaskResponse() {
        taskResponse = new JSONArray();

        JSONObject aTask = new JSONObject();
        aTask.put("project_id", taskTranslation.getClientAppId());
        aTask.put("task_id", taskTranslation.getTaskId());
        aTask.put("info", this.getInfo());

        taskResponse.add(aTask);

        return taskResponse;
    }


}
