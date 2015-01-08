package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.dbmanager.dto.CustomUiTemplateDTO;
import qa.qcri.aidr.predictui.facade.CustomUITemplateFacade;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by Koushik
 */

@Stateless
public class CustomUITemplateFacadeImp implements CustomUITemplateFacade{

	//@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	//private EntityManager em;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CustomUiTemplateResourceFacade remoteCustomUiTemplateEJB;
	
	private static Logger logger = Logger.getLogger(CustomUITemplateFacadeImp.class);
	
	@Override
	public List<CustomUiTemplateDTO> getAllCustomUITemplateByCrisisID(long crisisID) {
		try {
			List<CustomUiTemplateDTO> customUITemplates = remoteCustomUiTemplateEJB.getAllCustomUITemplateByCrisisID(crisisID);
			return customUITemplates;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<CustomUiTemplateDTO> getCustomUITemplateBasedOnTypeByCrisisID(long crisisID, int templateType) {

		logger.info("getCustomUITemplateBasedOnTypeByCrisisID: " + crisisID + "-" + templateType );
		try {
			List<CustomUiTemplateDTO> customUITemplates = remoteCustomUiTemplateEJB.getCustomUITemplateBasedOnTypeByCrisisID(crisisID, templateType);
			return customUITemplates;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<CustomUiTemplateDTO> getCustomUITemplateBasedOnTypeByCrisisIDAndAttributeID(long crisisID, long attributeID, int templateType) {
		try {
			List<CustomUiTemplateDTO> customUITemplates = remoteCustomUiTemplateEJB.getCustomUITemplateBasedOnTypeByCrisisIDAndAttributeID(crisisID, attributeID, templateType);
			return customUITemplates;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<CustomUiTemplateDTO> getCustomUITemplateByCrisisIDAndAttributeID(long crisisID, long attributeID) {
		try {
			List<CustomUiTemplateDTO> customUITemplates = remoteCustomUiTemplateEJB.getCustomUITemplateByCrisisIDAndAttributeID(crisisID, attributeID);
			return customUITemplates;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public CustomUiTemplateDTO addCustomUITemplate(CustomUiTemplateDTO customUITemplate) {
		try {
			CustomUiTemplateDTO dto = remoteCustomUiTemplateEJB.addCustomUITemplate(customUITemplate);
			return  dto;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public CustomUiTemplateDTO updateCustomUITemplate(CustomUiTemplateDTO currentTemplate, CustomUiTemplateDTO updatedTemplate) {
		try {
		CustomUiTemplateDTO dto = remoteCustomUiTemplateEJB.updateCustomUITemplate(currentTemplate, updatedTemplate);
		return  dto;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public CustomUiTemplateDTO updateCustomUITemplateStatus(CustomUiTemplateDTO currentTemplate, CustomUiTemplateDTO updatedTemplate) {
		try {
			CustomUiTemplateDTO dto = remoteCustomUiTemplateEJB.updateCustomUITemplateStatus(currentTemplate, updatedTemplate);
			return  dto;
			} catch (Exception e) {
				return null;
			}
	}

	@Override
	public void deleteCustomUITemplateBasedOnTypeByCrisisID(long crisisID, int type) {
		try {
			remoteCustomUiTemplateEJB.deleteCustomUITemplateBasedOnTypeByCrisisID(crisisID, type);
		} catch (Exception e) {
			logger.error("exception", e);
		}
	}

	@Override
	public void deleteCustomUITemplateByCrisisID(long crisisID) {
		try {
			remoteCustomUiTemplateEJB.deleteCustomUITemplateByCrisisID(crisisID);
		} catch (Exception e) {
			logger.error("exception", e);
		}
	}
}
