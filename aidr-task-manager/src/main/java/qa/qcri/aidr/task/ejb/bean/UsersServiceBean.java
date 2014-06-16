package qa.qcri.aidr.task.ejb.bean;


import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.ejb.UsersService;
import qa.qcri.aidr.task.entities.Users;


/**
 * @author Koushik
 */

@Stateless(name="UsersServiceBean")
public class UsersServiceBean extends AbstractTaskManagerServiceBean<Users, Long> implements UsersService{

	protected UsersServiceBean(){
        super(Users.class);
    }

    @Override
    public Users findUserByName(String name) {
        List<Users> usersList = (List<Users>) getAllByCriteria(Restrictions.eq("name", name));
        if(usersList != null){
            if(usersList.size() > 0){
                return usersList.get(0);
            }
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
	
	@Override
	public Users findUserByID(Long id) {
		Users user = getById(id);
		if (user != null) 
			return user;
		return null;
	}

	@Override
	public List<Users> findAllUsersByName(String name) {
		List<Users> usersList = (List<Users>) getAllByCriteria(Restrictions.eq("name", name));
        if(usersList != null){
            if(usersList.size() > 0){
                return usersList;
            }
        }
		return null;
	}
}
