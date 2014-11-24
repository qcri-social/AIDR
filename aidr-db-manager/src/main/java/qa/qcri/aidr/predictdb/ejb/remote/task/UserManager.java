package qa.qcri.aidr.predictdb.ejb.remote.task;

import qa.qcri.aidr.predictdb.dto.UsersDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/24/14
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserManager {
    public UsersDTO getUserByName(String name);
    public UsersDTO getUserById(Long id);
    public List<UsersDTO> getAllUserByName(String name);
}
