package qa.qcri.aidr.trainer.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.UsersDao;
import qa.qcri.aidr.trainer.api.entity.Users;
import qa.qcri.aidr.trainer.api.service.UsersService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/20/13
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("usersService")
@Transactional(readOnly = true)
public class UsersServiceImpl implements UsersService{

    @Autowired
    private UsersDao usersDao;

    @Override
    public Users findUserByName(String name) {
        return usersDao.findUserByName(name);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
