package qa.qcri.aidr.manager.service;

import qa.qcri.aidr.manager.dto.UITemplateRequest;
import qa.qcri.aidr.manager.exception.AidrException;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/23/14
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UITemplateService {

    UITemplateRequest updateTemplate(UITemplateRequest uiTemplateRequest) throws AidrException;
    String getTemplatesByCrisisID(long crisisID) throws AidrException;
    String getCrisisChildrenElement(Integer id) throws AidrException;
}
