package qa.qcri.aidr.trainer.pybossa.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.pybossa.dao.ClientAppAnswerDao;
import qa.qcri.aidr.trainer.pybossa.entity.ClientAppAnswer;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/20/13
 * Time: 1:39 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClientAppAnswerDaoImpl extends AbstractDaoImpl<ClientAppAnswer, String> implements ClientAppAnswerDao {

    protected ClientAppAnswerDaoImpl(){
        super(ClientAppAnswer.class);
    }

    @Override
    public ClientAppAnswer findClientAppAnswerByID(Long clientAppID) {
        return findByCriterionID(Restrictions.eq("clientAppID", clientAppID));  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addClientAppAnswer(Long clientAppID, String answerJson, int cutOffValue) {
        ClientAppAnswer clientAppAnswer = new ClientAppAnswer(clientAppID, answerJson);
        clientAppAnswer.setVoteCutOff(cutOffValue);
        save(clientAppAnswer);
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
