/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.entities.Users;
import qa.qcri.aidr.predictui.facade.UserResourceFacade;
import javax.persistence.NoResultException;

/**
 *
 * @author Imran
 */
@Stateless
public class UserResourceImp implements UserResourceFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public Users addUser(Users user) {
        try {
            em.persist(user);
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
            return null;
        }

    }
}
