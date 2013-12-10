package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.entity.Users;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/20/13
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UsersService {
    Users findUserByName(String name);
}
