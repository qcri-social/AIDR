package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.CustomUITemplate;
import qa.qcri.aidr.predictui.facade.CustomUITemplateFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/18/14
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless
public class CustomUITemplateFacadeImp implements CustomUITemplateFacade{

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    @Override
    public List<CustomUITemplate> getAllCustomUITemplateByCrisisID(long crisisID) {
        Query query = em.createNamedQuery("CustomUITemplate.findByCrisisD", CustomUITemplate.class);
        query.setParameter("crisisID", crisisID);
        List<CustomUITemplate> customUITemplates = query.getResultList();
        return customUITemplates;
    }

    @Override
    public List<CustomUITemplate> getCustomUITemplateBasedOnTypeByCrisisID(long crisisID, int templateType) {

        System.out.println("getCustomUITemplateBasedOnTypeByCrisisID: " + crisisID + "-" + templateType );
        Query query = em.createNamedQuery("CustomUITemplate.findBasedOnTypeByCrisisD", CustomUITemplate.class);
        query.setParameter("crisisID", crisisID);
        query.setParameter("templateType", templateType);
        List<CustomUITemplate> customUITemplates = query.getResultList();
        return customUITemplates;
    }

    @Override
    public List<CustomUITemplate> getCustomUITemplateBasedOnTypeByCrisisIDAndAttributeID(long crisisID, long attributeID, int templateType) {
        Query query = em.createNamedQuery("CustomUITemplate.findBasedOnTypeByCrisisIDAndAttributeID", CustomUITemplate.class);
        query.setParameter("crisisID", crisisID);
        query.setParameter("templateType", templateType);
        query.setParameter("nominalAttributeID", attributeID);
        List<CustomUITemplate> customUITemplates = query.getResultList();
        return customUITemplates;
    }

    @Override
    public List<CustomUITemplate> getCustomUITemplateByCrisisIDAndAttributeID(long crisisID, long attributeID) {
        Query query = em.createNamedQuery("CustomUITemplate.findByCrisisIDAndAttributeID", CustomUITemplate.class);
        query.setParameter("crisisID", crisisID);
        query.setParameter("nominalAttributeID", attributeID);
        List<CustomUITemplate> customUITemplates = query.getResultList();
        return customUITemplates;
    }

    @Override
    public CustomUITemplate addCustomUITemplate(CustomUITemplate customUITemplate) {
        em.persist(customUITemplate);

        return  customUITemplate;
    }

    @Override
    public CustomUITemplate updateCustomUITemplate(CustomUITemplate currentTemplate, CustomUITemplate updatedTemplate) {
        currentTemplate = em.merge(currentTemplate);
        currentTemplate.setTemplateValue(updatedTemplate.getTemplateValue());
        return currentTemplate;
    }

    @Override
    public CustomUITemplate updateCustomUITemplateStatus(CustomUITemplate currentTemplate, CustomUITemplate updatedTemplate) {
        currentTemplate = em.merge(currentTemplate);
        currentTemplate.setIsActive(updatedTemplate.getIsActive());
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
}
