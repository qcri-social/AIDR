package qa.qcri.aidr.task.ejb;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.task.entities.Users;


@Local
public interface UsersService extends AbstractTaskManagerService<Users, Long>{
    Users findUserByName(String name);
    Users findUserByID(Long id);
    
    List<Users> findAllUsersByName(String name);
}
