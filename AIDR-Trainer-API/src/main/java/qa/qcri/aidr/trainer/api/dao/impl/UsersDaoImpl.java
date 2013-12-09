package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.UsersDao;
import qa.qcri.aidr.trainer.api.entity.Users;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/20/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UsersDaoImpl  extends AbstractDaoImpl<Users, String> implements UsersDao{

    protected UsersDaoImpl(){
        super(Users.class);
    }

    @Override
    public Users findUserByName(String name) {
        List<Users> usersList = findByCriteria (Restrictions.eq("name", name));
        if(usersList != null){
            if(usersList.size() > 0){
                return usersList.get(0);
            }
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
