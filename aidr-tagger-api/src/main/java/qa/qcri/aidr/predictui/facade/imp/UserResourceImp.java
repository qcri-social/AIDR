/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import qa.qcri.aidr.common.logging.ErrorLog;
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

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    private static Logger logger = LoggerFactory.getLogger(UserResourceImp.class);
	private static ErrorLog elog = new ErrorLog();
	
	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade remoteUsersEJB;
	
    public Users addUser(Users user) {
        try {
            em.persist(user);
        } catch (Exception ex) {
            logger.error(elog.toStringException(ex));
            return null;
        }
        return user;

    }

    public Users getUserByName(String userName) {
        Users dbUser;
        try {
            Query userQuery = em.createNamedQuery("Users.findByName", Users.class);
            userQuery.setParameter("name", userName);
            if ((userQuery.getResultList().isEmpty())) {
                return null;
            } else {
                dbUser = (Users) userQuery.getSingleResult();
                return dbUser;
            }

        } catch (NoResultException ex) {
            return null;
        }

    }
    
    public Users getUserByID(Integer userID) {
        Users dbUser;
        try {
            Query userQuery = em.createNamedQuery("Users.findByUserID", Users.class);
            userQuery.setParameter("userID", userID);
            if ((userQuery.getResultList().isEmpty())) {
                return null;
            } else {
                dbUser = (Users) userQuery.getSingleResult();
                return dbUser;
            }

        } catch (NoResultException ex) {
            return null;
        }

    }
    
    public List<Users> getAllUsers() {
        List<Users> dbUsers = null;
        try {
            Query userQuery = em.createNamedQuery("Users.findAll", Users.class);
            dbUsers = (List<Users>) userQuery.getResultList();
            System.out.println("Fetched users list: " + dbUsers.size());
            return dbUsers;
        } catch (NoResultException ex) {
            return null;
        }

    }
}
