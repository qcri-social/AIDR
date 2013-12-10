package qa.qcri.aidr.trainer.pybossa.service;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/23/13
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppRunWorker {
    void processTaskRunImport() throws Exception;
    void processTaskPublish() throws Exception;
}
