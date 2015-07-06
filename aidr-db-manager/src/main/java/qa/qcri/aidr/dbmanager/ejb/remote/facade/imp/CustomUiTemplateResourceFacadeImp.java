package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.dbmanager.dto.CustomUiTemplateDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CustomUiTemplateResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.CustomUiTemplate;

/**
*
* @author Koushik
*/
@Stateless(name="CustomUiTemplateResourceFacadeImp")
public class CustomUiTemplateResourceFacadeImp extends CoreDBServiceFacadeImp<CustomUiTemplate, Long> implements CustomUiTemplateResourceFacade {
	private static Logger logger = Logger.getLogger("aidr-db-manager");
	
	protected CustomUiTemplateResourceFacadeImp(){
		super(CustomUiTemplate.class);
	}

	@Override
	public List<CustomUiTemplateDTO> getAllCustomUITemplateByCrisisID(long crisisID) {
		Criterion criterion = Restrictions.eq("crisisID", crisisID);
		
		List<CustomUiTemplateDTO> dtoList = null;
		try {
			List<CustomUiTemplate> customUITemplates = this.getAllByCriteria(criterion);
			if (customUITemplates != null) {
				dtoList = new ArrayList<CustomUiTemplateDTO>(0);
				for (CustomUiTemplate c: customUITemplates) {
					CustomUiTemplateDTO dto = new CustomUiTemplateDTO(c);
					dtoList.add(dto);
				}
			}
			return dtoList;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<CustomUiTemplateDTO> getCustomUITemplateBasedOnTypeByCrisisID(long crisisID, int templateType) {

		logger.info("getCustomUITemplateBasedOnTypeByCrisisID: " + crisisID + "-" + templateType );
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisID",crisisID))
				.add(Restrictions.eq("templateType", templateType));
		
		List<CustomUiTemplateDTO> dtoList = null;
		try {
			List<CustomUiTemplate> customUITemplates = this.getAllByCriteria(criterion);
			if (customUITemplates != null) {
				dtoList = new ArrayList<CustomUiTemplateDTO>(0);
				for (CustomUiTemplate c: customUITemplates) {
					CustomUiTemplateDTO dto = new CustomUiTemplateDTO(c);
					dtoList.add(dto);
				}
			}
			return dtoList;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<CustomUiTemplateDTO> getCustomUITemplateBasedOnTypeByCrisisIDAndAttributeID(long crisisID, long attributeID, int templateType) {
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisID",crisisID))
				.add(Restrictions.eq("templateType", templateType))
				.add(Restrictions.eq("nominalAttributeID", attributeID));
		
		List<CustomUiTemplateDTO> dtoList = null;
		try {
			List<CustomUiTemplate> customUITemplates = this.getAllByCriteria(criterion);
			if (customUITemplates != null) {
				dtoList = new ArrayList<CustomUiTemplateDTO>(0);
				for (CustomUiTemplate c: customUITemplates) {
					CustomUiTemplateDTO dto = new CustomUiTemplateDTO(c);
					dtoList.add(dto);
				}
			}
			return dtoList;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<CustomUiTemplateDTO> getCustomUITemplateByCrisisIDAndAttributeID(long crisisID, long attributeID) {
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisID",crisisID))
				.add(Restrictions.eq("nominalAttributeID", attributeID));
		
		List<CustomUiTemplateDTO> dtoList = null;
		try {
			List<CustomUiTemplate> customUITemplates = this.getAllByCriteria(criterion);
			if (customUITemplates != null) {
				dtoList = new ArrayList<CustomUiTemplateDTO>(0);
				for (CustomUiTemplate c: customUITemplates) {
					CustomUiTemplateDTO dto = new CustomUiTemplateDTO(c);
					dtoList.add(dto);
				}
			}
			return dtoList;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public CustomUiTemplateDTO addCustomUITemplate(CustomUiTemplateDTO customUITemplate) {
		CustomUiTemplate c = customUITemplate.toEntity();
		em.persist(c);
		em.flush();
		em.refresh(c);
		return new CustomUiTemplateDTO(c);
	}

	@Override
	public CustomUiTemplateDTO updateCustomUITemplate(CustomUiTemplateDTO currentTemplate, CustomUiTemplateDTO updatedTemplate) {
		CustomUiTemplate c = em.merge(currentTemplate.toEntity());
		currentTemplate = new CustomUiTemplateDTO(c);
		currentTemplate.setTemplateValue(updatedTemplate.getTemplateValue());
		return currentTemplate;
	}

	@Override
	public CustomUiTemplateDTO updateCustomUITemplateStatus(CustomUiTemplateDTO currentTemplate, CustomUiTemplateDTO updatedTemplate) {
		CustomUiTemplate c = em.merge(currentTemplate.toEntity());
		currentTemplate = new CustomUiTemplateDTO(c);
		currentTemplate.setIsActive(updatedTemplate.isIsActive());
		return currentTemplate;
	}

	@Override
	public void deleteCustomUITemplateBasedOnTypeByCrisisID(long crisisID, int type) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void deleteCustomUITemplateByCrisisID(long crisisID) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
	
	public int deleteCustomUiTemplateById(long customUiTemplateId){
		try {
			CustomUiTemplateDTO customUiTemplateDTO = getCustomUITemplateByID(customUiTemplateId);
			Object managed = em.merge(customUiTemplateDTO.toEntity());
			em.remove(managed); 
			return 1;
			} catch (Exception e) {
				return 0;
		}
	}
	
	public CustomUiTemplateDTO getCustomUITemplateByID(long customUiTemplateId) {
		Criterion criterion =Restrictions.eq("customUitemplateId",customUiTemplateId);
		CustomUiTemplate customUiTemplate = this.getByCriteria(criterion);
		return (new CustomUiTemplateDTO(customUiTemplate));
	}
}
