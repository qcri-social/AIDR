package qa.qcri.aidr.predictdb.ejb.local.task;

import java.util.List;

import javax.ejb.Local;

@Local
public interface UsersService extends AbstractTaskManagerService<Users, Long>{
    Users findUserByName(String name);
    Users findUserByID(Long id);
    
    List<Users> findAllUsersByName(String name);
}
