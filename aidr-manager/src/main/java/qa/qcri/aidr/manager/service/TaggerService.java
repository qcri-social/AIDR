package qa.qcri.aidr.manager.service;

import java.util.List;
import java.util.Map;

import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;
import qa.qcri.aidr.manager.dto.ModelHistoryWrapper;
import qa.qcri.aidr.manager.dto.TaggerAttribute;
import qa.qcri.aidr.manager.dto.TaggerCrisis;
import qa.qcri.aidr.manager.dto.TaggerCrisisExist;
import qa.qcri.aidr.manager.dto.TaggerCrisisRequest;
import qa.qcri.aidr.manager.dto.TaggerCrisisType;
import qa.qcri.aidr.manager.dto.TaggerLabel;
import qa.qcri.aidr.manager.dto.TaggerLabelRequest;
import qa.qcri.aidr.manager.dto.TaggerModel;
import qa.qcri.aidr.manager.dto.TaggerModelFamily;
import qa.qcri.aidr.manager.dto.TaggerModelNominalLabel;
import qa.qcri.aidr.manager.dto.TaggerResponseWrapper;
import qa.qcri.aidr.manager.dto.TaggerUser;
import qa.qcri.aidr.manager.dto.TaskAnswer;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;

public interface TaggerService {

	public List<TaggerCrisisType> getAllCrisisTypes() throws AidrException;

	public List<TaggerCrisis> getCrisesByUserId(Integer userId) throws AidrException;

	public String createNewCrises(TaggerCrisisRequest crisis) throws AidrException;

	public java.util.Collection<TaggerAttribute> getAttributesForCrises(Integer crisisID, Integer userId) throws AidrException;

	public TaggerCrisisExist isCrisesExist(String code) throws AidrException;

	public Integer isUserExistsByUsername(String userName) throws AidrException;

	public Integer addNewUser(TaggerUser taggerUser) throws AidrException;

	public Integer addAttributeToCrisis(TaggerModelFamily modelFamily) throws AidrException;

	public TaggerCrisis getCrisesByCode(String code) throws AidrException;

	public TaggerCrisis updateCode(TaggerCrisis crisis) throws AidrException;

	public List<TaggerModel> getModelsForCrisis(Integer crisisID) throws AidrException;

	public List<TaggerModelNominalLabel> getAllLabelsForModel(Integer modelID, String crisisCode) throws AidrException;

	public TaggerAttribute createNewAttribute(TaggerAttribute attribute) throws AidrException;

	public TaggerAttribute getAttributeInfo(Integer id) throws AidrException;

	public TaggerLabel getLabelInfo(Integer id) throws AidrException;

	public boolean deleteAttribute(Integer id) throws AidrException;

	public boolean deleteTrainingExample(Integer id) throws AidrException;

	public boolean removeAttributeFromCrises(Integer modelFamilyID) throws AidrException;

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

	public String loadLatestTweets(String code, String constraints) throws Exception;

	public ModelHistoryWrapper getModelHistoryByModelFamilyID(Integer start, Integer limit, Integer id, String sortColumn,
			String sortDirection) throws Exception;

	public Map<String, Integer> getTaggersForCollections(List<String> collectionCodes) throws Exception;

	public boolean pingTagger() throws AidrException;

	public boolean pingTrainer() throws AidrException;

	public boolean pingAIDROutput() throws AidrException;
	
	public boolean pingPersister() throws AidrException;

	public String getRetainingThreshold() throws AidrException;

	public String getAttributesAndLabelsByCrisisId(Integer id) throws Exception;

	//Added by koushik
	public int trashCollection(Collection collection) throws Exception;

	//Added by koushik
	public int untrashCollection(String collectionCode) throws Exception;

	public String loadLatestTweetsWithCount(String code, int count) throws AidrException;

	//Added by koushik
	Map<String, Integer> countCollectionsClassifiers(List<String> collectionCodes) throws AidrException;

	// Added by koushik
	public Map<String, Object> generateCSVLink(String code) throws AidrException;

	// Added by koushik
	public Map<String, Object> generateTweetIdsLink(String code) throws AidrException;


	//Added by koushik
	public Map<String, Object> generateJSONLink(String code, String jsonType) throws AidrException;

	//Added by koushik
	public Map<String, Object> generateJsonTweetIdsLink(String code, String jsonType) throws AidrException;

	//Added by koushik
	public Map<String, Object> generateJSONFilteredLink(String code,
			String queryString, String jsonType, String userName, Integer count, boolean removeRetweet) throws AidrException;

	//Added by koushik
	public Map<String, Object> generateJsonTweetIdsFilteredLink(String code,
			String queryString, String jsonType, String userName) throws AidrException;

	//Added by koushik
	public Map<String, Object> generateCSVFilteredLink(String code, String queryString, String userName, Integer count, boolean removeRetweet) throws AidrException;

	//Added by koushik
	public Map<String, Object> generateTweetIdsFilteredLink(String code, String queryString, String userName) throws AidrException;
	
	public TaggerResponseWrapper getHumanLabeledDocumentsByCrisisID(Long crisisID, Integer count) throws AidrException;
	public TaggerResponseWrapper getHumanLabeledDocumentsByCrisisCode(String crisisCode, Integer count) throws AidrException;
	public TaggerResponseWrapper getHumanLabeledDocumentsByCrisisIDUserID(Long crisisID, Long userID, Integer count) throws AidrException;
	public TaggerResponseWrapper getHumanLabeledDocumentsByCrisisIDUserName(Long crisisID, String userName, Integer count) throws AidrException;

	public Map<String, Object> downloadHumanLabeledDocumentsByCrisisUserName(String queryString, String crisisCode, String userName, Integer count, String fileType, String contentType) throws AidrException;
	
	public Map<String, Object> updateMicromapperEnabled(String code, Boolean isMicromapperEnabled) throws AidrException;
	public Boolean sendMailService(String subject, String body);
	
	public Long getLabelCount(Long collectionId);

	Map<String, Object> generateTweetIdsOnlyFilteredLink(String code, String queryString, String userName, Integer count, Boolean removeRetweet) throws AidrException;

	Map<String, Object> generateJsonTweetIdsOnlyFilteredLink(String code, String queryString, String jsonType, String userName, Integer exportLimit, Boolean removeRetweet) throws AidrException;
}
