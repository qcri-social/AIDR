package qa.qcri.aidr.trainer.pybossa.dao;

import qa.qcri.aidr.trainer.pybossa.entity.TaskTranslation;

import java.util.List;

/**
 * 
 * @author dan landy
 *
 */
public interface TaskTranslationDao extends AbstractDao<TaskTranslation, String>  {

	TaskTranslation findTranslationByID(Long translationId);
    TaskTranslation findTranslationByTaskID(Long taskId);
    List<TaskTranslation> findAllTranslationsByClientAppIdAndStatus(Long clientAppId, String status, Integer count);
    List<TaskTranslation> findAllTranslations();
    List<TaskTranslation> findAllTranslationsByTaskID(Long taskId);

}
