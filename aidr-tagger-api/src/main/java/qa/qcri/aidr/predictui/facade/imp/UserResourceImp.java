/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.predictui.entities.Users;
import qa.qcri.aidr.predictui.facade.UserResourceFacade;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Imran
 */
@Stateless
public class UserResourceImp implements UserResourceFacade {

    //@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    //private EntityManager em;

    private static Logger logger = LoggerFactory.getLogger(UserResourceImp.class);
	private static ErrorLog elog = new ErrorLog();
	
	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade remoteUsersEJB;
	
    public UsersDTO addUser(UsersDTO user) {
        try {
            //em.persist(user);
        	remoteUsersEJB.addUser(user);
        } catch (Exception ex) {
            logger.error(elog.toStringException(ex));
            return null;
        }
        return user;

    }

    public UsersDTO getUserByName(String userName) {
        try {
            //Query userQuery = em.createNamedQuery("Users.findByName", Users.class);
            //userQuery.setParameter("name", userName);
        	List<UsersDTO> dto = remoteUsersEJB.getAllUsersByName(userName);
        	if (dto != null && !dto.isEmpty()) {
        		return dto.get(0);
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public UsersDTO getUserByID(Long userID) {
        try {
            return remoteUsersEJB.getUserById(userID);
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }

    }
    
    public List<UsersDTO> getAllUsers() {
        List<UsersDTO> dbUsers = new ArrayList<UsersDTO>();
        try {
            dbUsers = remoteUsersEJB.getAllUsers();
            System.out.println("Fetched users list: " + dbUsers.size());
            return dbUsers;
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
}
