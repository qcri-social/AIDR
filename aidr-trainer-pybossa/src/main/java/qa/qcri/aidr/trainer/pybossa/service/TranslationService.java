package  qa.qcri.aidr.trainer.pybossa.service;


import java.util.List;
import java.util.Map;

import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.entity.TaskTranslation;
import qa.qcri.aidr.trainer.pybossa.format.impl.TranslationProjectModel;
import qa.qcri.aidr.trainer.pybossa.format.impl.TranslationRequestModel;

/**
 * Created by kamal on 3/22/15.
 */
public interface TranslationService {
    public Map pushTranslationRequest(TranslationRequestModel request);

    public Map processTranslations(ClientApp clientApp);
    public Map pushAllTranslations(Long clientAppId, Long twbProjectId, long maxTimeToWait, int maxBatchSize);

    public Map pushDocumentForRequest(TranslationRequestModel request);

    public String pullAllTranslationResponses(Long clientAppId, Long twbProjetcId);

    public void createTranslation(TaskTranslation translation);
    public void updateTranslation(TaskTranslation translation);
	public TaskTranslation findById(Long translationId);
    public TaskTranslation findByTaskId(Long taskId);
	public void delete(TaskTranslation translation);
	public List<TaskTranslation> findAllTranslations();
    public List<TaskTranslation> findAllTranslationsByClientAppIdAndStatus(Long clientAppId, String status, Integer count);

}
