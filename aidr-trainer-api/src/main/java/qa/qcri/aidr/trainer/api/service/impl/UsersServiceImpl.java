package qa.qcri.aidr.trainer.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Users;
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
	protected static Logger logger = Logger.getLogger(UsersServiceImpl.class);
	
    @Autowired
    private UsersResourceFacade remoteUsersResourceEJB;

    @Override
    public Users findUserByName(String name) {
        try {
			return remoteUsersResourceEJB.getUserByName(name).toEntity();
		} catch (PropertyNotSetException e) {
			logger.error("Exception while fetching user by userName",e);
			return null;
		} 
    }
}
