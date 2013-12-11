package qa.qcri.aidr.manager.service;

import qa.qcri.aidr.manager.dto.TrainingDataDTO;
import qa.qcri.aidr.manager.dto.TaggerUser;
import qa.qcri.aidr.manager.dto.TaggerModelFamily;
import qa.qcri.aidr.manager.dto.TaggerCrisisRequest;
import qa.qcri.aidr.manager.dto.TaskAnswer;
import qa.qcri.aidr.manager.dto.TaggerModel;
import qa.qcri.aidr.manager.dto.TaggerLabel;
import qa.qcri.aidr.manager.dto.TaggerModelNominalLabel;
import qa.qcri.aidr.manager.dto.TaggerCrisisType;
import qa.qcri.aidr.manager.dto.TaggerCrisis;
import qa.qcri.aidr.manager.dto.TaggerCrisisExist;
import qa.qcri.aidr.manager.dto.TaggerLabelRequest;
import qa.qcri.aidr.manager.dto.TaggerAttribute;
import qa.qcri.aidr.manager.exception.AidrException;

import java.util.Collection;
import java.util.List;

public interface TaggerService {

    public List<TaggerCrisisType> getAllCrisisTypes() throws AidrException;

    public List<TaggerCrisis> getCrisesByUserId(Integer userId) throws AidrException;

    public String createNewCrises(TaggerCrisisRequest crisis) throws AidrException;

    public Collection<TaggerAttribute> getAttributesForCrises(Integer crisisID, Integer userId) throws AidrException;

    public TaggerCrisisExist isCrisesExist(String code) throws AidrException;

    public Integer isUserExistsByUsername(String userName) throws AidrException;

    public Integer addNewUser(TaggerUser taggerUser) throws AidrException;

    public Integer addAttributeToCrisis(TaggerModelFamily modelFamily) throws AidrException;

    public TaggerCrisis getCrisesByCode(String code) throws AidrException;

    public TaggerCrisis updateCode(TaggerCrisis crisis) throws AidrException;

    public List<TaggerModel> getModelsForCrisis(Integer crisisID) throws AidrException;

    public List<TaggerModelNominalLabel> getAllLabelsForModel(Integer modelID) throws AidrException;

    public TaggerAttribute createNewAttribute(TaggerAttribute attribute) throws AidrException;

    public TaggerAttribute getAttributeInfo(Integer id) throws AidrException;

    public TaggerLabel getLabelInfo(Integer id) throws AidrException;

    public boolean deleteAttribute(Integer id) throws AidrException;

    public TaggerAttribute updateAttribute(TaggerAttribute attribute) throws AidrException;

    public TaggerLabel updateLabel(TaggerLabelRequest label) throws AidrException;

    public TaggerLabel createNewLabel(TaggerLabelRequest label) throws AidrException;

    public TaggerAttribute attributeExists(String code) throws AidrException;

    public List<TrainingDataDTO> getTrainingDataByModelIdAndCrisisId(Integer modelFamilyId,
                                                                     Integer crisisId,
                                                                     Integer start,
                                                                     Integer limit,
                                                                     String sortColumn,
                                                                     String sortDirection) throws AidrException;

    public String getAssignableTask(Integer id, String userName)  throws AidrException;

    public String getTemplateStatus(String code)  throws AidrException;

    public String skipTask(Integer id, String userName)  throws AidrException;

    public boolean saveTaskAnswer(List<TaskAnswer> taskAnswer) throws AidrException;

    public String generateCSVLink(String code) throws Exception;

    public String generateTweetIdsLink(String code) throws Exception;
}
