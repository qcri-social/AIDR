package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.entity.ClientApp;
import qa.qcri.aidr.trainer.api.entity.CustomUITemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/2/14
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CustomUITemplateService {
    public List<CustomUITemplate> getCustomTemplateSkinType(Long crisisID, Long attributeID);
    List<CustomUITemplate> getCustomTemplateForLandingPage (Long crisisID);
    List<CustomUITemplate> getCustomTemplateByCrisis(Long crisisID);
    void updateCustomTemplateByCrisis(Long crisisID, int customUIType) throws Exception ;
    void updateCustomTemplateByAttribute(Long crisisID, Long attributeID, int customUIType, int skinType) throws Exception;
    String assembleTPybossaJson(ClientApp clientApp, String key, String value) throws Exception;

}
