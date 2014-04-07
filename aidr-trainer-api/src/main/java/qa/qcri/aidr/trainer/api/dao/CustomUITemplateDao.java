package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.CustomUITemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/2/14
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CustomUITemplateDao extends AbstractDao<CustomUITemplate, String> {

    List<CustomUITemplate> getTemplateByCrisisWithStatus(Long crisisID, Integer status);
    List<CustomUITemplate> getTemplateByCrisis(Long crisisID);
    List<CustomUITemplate> getTemplateByAttribute(Long crisisID,Long attributeID);
    List<CustomUITemplate> getTemplateByAttributeWithStatus(Long crisisID,Long attributeID, Integer status);
    List<CustomUITemplate> getTemplateByAttributeAndType(Long crisisID,Long attributeID, Integer status, Integer type);

    void updateTemplateStatus(Long customUITemplateID, int status);
}
