package qa.qcri.aidr.predictui.facade;

import qa.qcri.aidr.predictui.entities.CustomUITemplate;

import javax.ejb.Local;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/18/14
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface CustomUITemplateFacade {
    public List<CustomUITemplate> getAllCustomUITemplateByCrisisID(long crisisID);
    public List<CustomUITemplate> getCustomUITemplateBasedOnTypeByCrisisID(long crisisID, int type);
    public List<CustomUITemplate> getCustomUITemplateBasedOnTypeByCrisisIDAndAttributeID(long crisisID, long attributeID,int type);
    public List<CustomUITemplate> getCustomUITemplateByCrisisIDAndAttributeID(long crisisID, long attributeID);
    public CustomUITemplate addCustomUITemplate(CustomUITemplate customUITemplate);
    public CustomUITemplate updateCustomUITemplate(CustomUITemplate currentTemplate, CustomUITemplate updatedTemplate);
    public CustomUITemplate updateCustomUITemplateStatus(CustomUITemplate currentTemplate, CustomUITemplate updatedTemplate);
    public void deleteCustomUITemplateBasedOnTypeByCrisisID(long crisisID, int type);
    public void deleteCustomUITemplateByCrisisID(long crisisID);

}
