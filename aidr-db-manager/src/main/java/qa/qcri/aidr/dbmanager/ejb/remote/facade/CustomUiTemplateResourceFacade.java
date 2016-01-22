package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.dbmanager.dto.CustomUiTemplateDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.CustomUiTemplate;


@Remote
public interface CustomUiTemplateResourceFacade extends CoreDBServiceFacade<CustomUiTemplate, Long> {
	
    public List<CustomUiTemplateDTO> getAllCustomUITemplateByCrisisID(long crisisID);
    public List<CustomUiTemplateDTO> getCustomUITemplateBasedOnTypeByCrisisID(long crisisID, int type);
    public List<CustomUiTemplateDTO> getCustomUITemplateBasedOnTypeByCrisisIDAndAttributeID(long crisisID, long attributeID,int type);
    public List<CustomUiTemplateDTO> getCustomUITemplateBasedOnTypeByCrisisIDAttributeIDAndStatus(long crisisID, long attributeID, int templateType, int status);
    public List<CustomUiTemplateDTO> getCustomUITemplateByCrisisIDAndAttributeID(long crisisID, long attributeID);
    public CustomUiTemplateDTO addCustomUITemplate(CustomUiTemplateDTO customUITemplate);
    public CustomUiTemplateDTO updateCustomUITemplate(CustomUiTemplateDTO currentTemplate, CustomUiTemplateDTO updatedTemplate);
    public CustomUiTemplateDTO updateCustomUITemplateStatus(CustomUiTemplateDTO currentTemplate, CustomUiTemplateDTO updatedTemplate);
    public void deleteCustomUITemplateBasedOnTypeByCrisisID(long crisisID, int type);
    public void deleteCustomUITemplateByCrisisID(long crisisID);
}
