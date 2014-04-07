package qa.qcri.aidr.manager.dto;

import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/24/14
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class UITemplateResponse {


    private List<UITemplateRequest> modelWrapper;

    public UITemplateResponse(){}
    public List<UITemplateRequest> getModelWrapper() {
        return modelWrapper;
    }

    public void setModelWrapper(List<UITemplateRequest> modelWrapper) {
        this.modelWrapper = modelWrapper;
    }
}
