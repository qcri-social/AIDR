package qa.qcri.aidr.trainer.pybossa.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/23/13
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppCreateWorker {
    void doCreateApp() throws Exception;
    void doAppUpdate(ClientApp clientApp, String appInfoJson,JSONObject attribute, JSONArray labelModel, String crisisCode, String crisisName) throws Exception;
    void doAppTemplateUpdate(ClientApp clientApp, Long nominalAttributeID) throws Exception;
    void doAppDelete() throws Exception;
}
