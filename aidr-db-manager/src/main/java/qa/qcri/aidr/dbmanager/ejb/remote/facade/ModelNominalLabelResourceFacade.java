package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelNominalLabelDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabel;

@Remote
public interface ModelNominalLabelResourceFacade extends CoreDBServiceFacade<ModelNominalLabel, Long> {

	public List<ModelNominalLabelDTO> getAllModelNominalLabels();

	public List<ModelNominalLabelDTO> getAllModelNominalLabelsByModelID(Long modelID, String crisisCode);

	public void saveModelNominalLabel(ModelNominalLabelDTO modelNominalLabel) throws PropertyNotSetException;

	public ModelNominalLabelDTO editModelNominalLabel(ModelNominalLabelDTO modelNominalLabel) throws PropertyNotSetException;

	public Integer deleteModelNominalLabel(ModelNominalLabelDTO modelNominalLabel) throws PropertyNotSetException;

	public ModelNominalLabelDTO getModelNominalLabelByID(Long nominalLabelID) throws PropertyNotSetException;

	public Boolean isModelNominalLabelExists(Long nominalLabelID) throws PropertyNotSetException;

	public ModelNominalLabelDTO addModelNominalLabel(ModelNominalLabelDTO modelNominalLabel);
}
