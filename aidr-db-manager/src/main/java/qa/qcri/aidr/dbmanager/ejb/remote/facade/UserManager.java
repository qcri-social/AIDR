package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import qa.qcri.aidr.dbmanager.dto.UsersDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/24/14
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserManager {
    public UsersDTO getUserByName(String name) throws Exception;
    public UsersDTO getUserById(Long id) throws Exception;
    public List<UsersDTO> getAllUserByName(String name) throws Exception;
}
