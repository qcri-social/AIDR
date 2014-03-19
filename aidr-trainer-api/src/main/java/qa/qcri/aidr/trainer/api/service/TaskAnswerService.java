package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.template.TaskAnswerResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 7:50 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TaskAnswerService {
    //insertTaskAnswer
    void insertTaskAnswer(TaskAnswerResponse taskAnswerResponse);
    public TaskAnswerResponse getTaskAnswerResponseData(String taskAnswer);

    public void addToTaskAnswer(TaskAnswerResponse taskAnswerResponse);
    public void addToDocumentNominalLabel(TaskAnswerResponse taskAnswerResponse);
    public void markOnHasHumanTag(long documentID);
    public void removeTaskAssignment(TaskAnswerResponse taskAnswerResponse);
}
