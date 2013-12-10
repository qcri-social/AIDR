package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.entity.Crisis;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CrisisService {

    Crisis findByCrisisID(Long id);
    CrisisJsonModel findByOptimizedCrisisID(Long id);
    List<Crisis> findByCriteria(String columnName, String value);
    List<Crisis> findByCriteria(String columnName, Long value);
    List<Crisis> findAllActiveCrisis();
    List<CrisisNominalAttributeModel> getAllActiveCrisisNominalAttribute();
}
