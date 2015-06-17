/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.predictui.facade.UserResourceFacade;

/**
 *
 * @author Imran
 */
@Stateless
public class UserResourceImp implements UserResourceFacade {

    //@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    //private EntityManager em;

    private static Logger logger = Logger.getLogger(UserResourceImp.class);
	private static ErrorLog elog = new ErrorLog();
	
	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade remoteUsersEJB;
	
    public UsersDTO addUser(UsersDTO user) {
        try {
            //em.persist(user);
        	remoteUsersEJB.addUser(user);
        	return remoteUsersEJB.getUserByName(user.getName());
        } catch (Exception ex) {
            logger.error("exception:", ex);
            return null;
        }
    }

    public UsersDTO getUserByName(String userName) {
        try {
            //Query userQuery = em.createNamedQuery("Users.findByName", Users.class);
            //userQuery.setParameter("name", userName);
        	logger.info("Querying remote EJB for user name: " + userName);
        	List<UsersDTO> dto = remoteUsersEJB.getAllUsersByName(userName);
        	logger.info("Fetched userslist size: " + (dto != null ? dto.size() : "null"));
        	if (dto != null && !dto.isEmpty()) {
        		return dto.get(0);
        	}
        } catch (Exception e) {
        	logger.error("exception:", e);
        }
        return null;
    }
    
    public UsersDTO getUserByID(Long userID) {
        try {
            return remoteUsersEJB.getUserById(userID);
        } catch (Exception e) {
        	logger.error("exception:", e);
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
        	logger.error("exception:", e);
            return null;
        }
    }
}
