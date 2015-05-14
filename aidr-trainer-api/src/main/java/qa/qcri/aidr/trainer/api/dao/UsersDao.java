package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.Users;

/**
 * Methods to manage users.
 * 
 * @author jlucas
 */
public interface UsersDao {

    Users findUserByName(String name);
}
