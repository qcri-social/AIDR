package qa.qcri.aidr.trainer.api.service;

import java.util.List;

import qa.qcri.aidr.dbmanager.dto.CustomUiTemplateDTO;
import qa.qcri.aidr.dbmanager.entities.misc.CustomUiTemplate;
import qa.qcri.aidr.trainer.api.entity.ClientApp;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/2/14
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CustomUITemplateService {
    public List<CustomUiTemplateDTO> getCustomTemplateSkinType(Long crisisID);
    List<CustomUiTemplate> getCustomTemplateForLandingPage (Long crisisID);
    List<CustomUiTemplate> getCustomTemplateByCrisis(Long crisisID);
    void updateCustomTemplateByCrisis(Long crisisID, int customUIType) throws Exception ;
    void updateCustomTemplateByAttribute(Long crisisID, Long attributeID, int customUIType, int skinType) throws Exception;
    String assembleTPybossaJson(ClientApp clientApp, String key, String value) throws Exception;
	List<CustomUiTemplateDTO> getTemplateByAttribute(Long crisisID,Long attributeID);
	List<CustomUiTemplateDTO> getTemplateByAttributeAndType(Long crisisID, Long attributeID, Integer status, Integer type);
	List<CustomUiTemplateDTO> getCustomUiTemplateByCrisisWithType(Long crisisID, Integer templateType);
}
