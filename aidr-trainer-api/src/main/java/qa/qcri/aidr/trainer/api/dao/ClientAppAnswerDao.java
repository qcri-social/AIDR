package qa.qcri.aidr.trainer.api.dao;

import java.util.List;

import qa.qcri.aidr.trainer.api.entity.ClientAppAnswer;

/**
 * 
 * @author jlucas
 */
public interface ClientAppAnswerDao {

    List<ClientAppAnswer> getClientAppAnswer(Long clientAppID);

}
