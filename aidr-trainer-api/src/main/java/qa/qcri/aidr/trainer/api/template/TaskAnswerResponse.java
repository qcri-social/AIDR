package qa.qcri.aidr.trainer.api.template;

import qa.qcri.aidr.trainer.api.entity.DocumentNominalLabel;
import qa.qcri.aidr.trainer.api.entity.TaskAnswer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/23/13
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskAnswerResponse {


    private Long userID;
    private Long documentID;
    private List<TaskAnswer> taskAnswerList = new ArrayList<TaskAnswer>();
    private List<DocumentNominalLabel> documentNominalLabelList = new ArrayList<DocumentNominalLabel>();
    private String jedisJson;

    public TaskAnswerResponse() {}		// gf 3 way - attempting fix
    
    public List<DocumentNominalLabel> getDocumentNominalLabelList() {
        return documentNominalLabelList;
    }

    public void setDocumentNominalLabelList(DocumentNominalLabel documentNominalLabel) {
        this.documentNominalLabelList.add(documentNominalLabel);
    }

    public List<TaskAnswer> getTaskAnswerList() {
        return taskAnswerList;
    }

    public void setTaskAnswerList(TaskAnswer taskAnswer) {
        this.taskAnswerList.add(taskAnswer);
    }

    public String getJedisJson() {
        return jedisJson;
    }

    public void setJedisJson(String jedisJson) {
        this.jedisJson = jedisJson;
    }

    public Long getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Long documentID) {
        this.documentID = documentID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }


}
