package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.CustomUITemplateDao;
import qa.qcri.aidr.trainer.api.entity.CustomUITemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/2/14
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CustomUITemplateDaoImpl  extends AbstractDaoImpl<CustomUITemplate, String> implements CustomUITemplateDao {

    protected CustomUITemplateDaoImpl(){
        super(CustomUITemplate.class);
    }

    @Override
    public List<CustomUITemplate> getTemplateByCrisisWithStatus(Long crisisID, Integer status) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",crisisID))
                .add(Restrictions.eq("status", status)));
    }

    @Override
    public List<CustomUITemplate> getTemplateByCrisisWithType(Long crisisID, Integer templateType) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",crisisID))
                .add(Restrictions.eq("templateType", templateType)));
    }


    @Override
    public List<CustomUITemplate> getTemplateByAttributeWithStatus(Long crisisID, Long attributeID, Integer status) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("nominalAttributeID", attributeID))
                .add(Restrictions.eq("crisisID",crisisID))
                .add(Restrictions.eq("status", status)));
    }

    @Override
    public List<CustomUITemplate> getTemplateByAttribute(Long crisisID, Long attributeID) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("nominalAttributeID", attributeID))
                .add(Restrictions.eq("crisisID",crisisID))
                );
    }

    @Override
    public List<CustomUITemplate> getTemplateByCrisis(Long crisisID) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",crisisID))
        );
    }


    @Override
    public List<CustomUITemplate> getTemplateByAttributeAndType(Long crisisID, Long attributeID, Integer status, Integer type) {
        if(status == null){
            return findByCriteria(Restrictions.conjunction()
                    .add(Restrictions.eq("nominalAttributeID", attributeID))
                    .add(Restrictions.eq("crisisID",crisisID))
                    .add(Restrictions.eq("templateType",type))
                    );
        }
        else{
            return findByCriteria(Restrictions.conjunction()
                    .add(Restrictions.eq("nominalAttributeID", attributeID))
                    .add(Restrictions.eq("crisisID",crisisID))
                    .add(Restrictions.eq("templateType",type))
                    .add(Restrictions.eq("status", status)));
        }

    }


    @Override
    public void updateTemplateStatus(Long customUITemplateID, int status) {
        List<CustomUITemplate> customUITemplateList =  findByCriteria(Restrictions.eq("customUITemplateID", customUITemplateID));

        if(customUITemplateList.size() > 0){
            CustomUITemplate customUITemplate = customUITemplateList.get(0);
            customUITemplate.setStatus(status);
            saveOrUpdate(customUITemplate);
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
