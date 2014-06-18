package qa.qcri.aidr.trainer.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.ClientAppAnswerDao;
import qa.qcri.aidr.trainer.api.entity.ClientAppAnswer;
import qa.qcri.aidr.trainer.api.service.ClientAppAnswerService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/16/14
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("clientAppAnswerService")
@Transactional(readOnly = true)
public class ClientAppAnswerServiceImpl implements ClientAppAnswerService{

    protected static Logger logger = Logger.getLogger("clientAppAnswerService");

    @Autowired
    ClientAppAnswerDao clientAppAnswerDao;

    @Override
    public ClientAppAnswer getClientAppAnswer(Long clientAppID) {
        List<ClientAppAnswer> answers = clientAppAnswerDao.getClientAppAnswer(clientAppID);
        if(answers.size() > 0)
            return answers.get(0);
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
