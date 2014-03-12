package qa.qcri.aidr.trainer.pybossa.service;

import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/10/13
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractCommunicator implements Communicator {

    public final String USER_AGENT = "Mozilla/5.0";

    public AbstractCommunicator(Type type) {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Override
    public int sendPost(String data, String url) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String sendGet(String url) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int sendPut(String data, String url) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int sendDelete(String data, String url) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String sendPostGet(String data, String url){
        return null;
    }

    @Override
    public String deleteGet(String url) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}