package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.Users;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/20/13
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UsersDao {

    Users findUserByName(String name);
}
