package qa.qcri.aidr.trainer.pybossa.service;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/10/13
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Communicator {
    public int sendPost(String data, String url);
    public String sendGet(String url);
    public int sendPut(String data, String url);
    public int sendDelete(String data, String url);
    public String sendPostGet(String data, String url);
}