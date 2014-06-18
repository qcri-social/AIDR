package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.entity.ClientAppAnswer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/16/14
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAppAnswerService {
    ClientAppAnswer getClientAppAnswer(Long clientAppID);
}
