package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.entity.Collection;
import qa.qcri.aidr.trainer.api.entity.NominalLabel;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CrisisService {

    Collection findByCrisisID(Long id);
    CrisisJsonModel findByOptimizedCrisisID(Long id);
    List<Collection> findByCriteria(String columnName, String value);
    List<Collection> findByCriteria(String columnName, Long value);
    List<Collection> findAllActiveCrisis();
    List<CrisisNominalAttributeModel> getAllActiveCrisisNominalAttribute();
    Set<NominalLabel> getNominalLabelByCrisisID(Long crisisID, Long nominalAtrributeID);
    List<Collection> findActiveCrisisInfo();
}
