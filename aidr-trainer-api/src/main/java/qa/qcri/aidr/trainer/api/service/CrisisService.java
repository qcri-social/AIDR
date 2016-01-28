package qa.qcri.aidr.trainer.api.service;

import java.util.List;
import java.util.Set;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CrisisService {

    CollectionDTO findByCrisisID(Long id);
    CrisisJsonModel findByOptimizedCrisisID(Long id) throws PropertyNotSetException;
    List<Collection> findByCriteria(String columnName, Object value);
   // List<Collection> findByCriteria(String columnName, Long value);
    List findAllActiveCrisis();
    List<CrisisNominalAttributeModel> getAllActiveCrisisNominalAttribute();
    Set<NominalLabelDTO> getNominalLabelByCrisisID(Long crisisID, Long nominalAtrributeID);
    List<CollectionDTO> findActiveCrisisInfo();
	List<CollectionDTO> findByCrisisCode(String crisisCode);
}
