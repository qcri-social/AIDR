package qa.qcri.aidr.trainer.api.service;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/11/14
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppSourceService {

    void addExternalDataSouceWithClientAppID(String fileURL, Long clientAppID);
    void addExternalDataSouceWithAppType(String fileURL, Integer appType);
    void addExternalDataSouceWithPlatFormInd(String fileURL, Long micromappersID);
}
