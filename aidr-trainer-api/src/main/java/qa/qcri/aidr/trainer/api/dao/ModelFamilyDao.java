package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.ModelFamily;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ModelFamilyDao {

    List<CrisisNominalAttributeModel> getActiveCrisisNominalAttribute();
    List<ModelFamily> getActiveCrisisNominalAttributeByCrisisID(Long crisisID);
}
