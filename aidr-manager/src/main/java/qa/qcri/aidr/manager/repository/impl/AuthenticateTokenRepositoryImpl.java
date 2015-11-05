package qa.qcri.aidr.manager.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.persistence.entities.AuthenticateToken;
import qa.qcri.aidr.manager.repository.AuthenticateTokenRepository;

import java.io.Serializable;


/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 5/12/14
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("authenticateTokenRepository")
public class AuthenticateTokenRepositoryImpl extends GenericRepositoryImpl<AuthenticateToken, Serializable> implements AuthenticateTokenRepository {

    @SuppressWarnings("unchecked")
    @Override
    public Boolean isAuthorized(String token) {

        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AuthenticateToken.class);
        criteria.add(Restrictions.eq("token", token));

        AuthenticateToken aidrAuthenticateToken = (AuthenticateToken) criteria.uniqueResult();
        return aidrAuthenticateToken != null;
    }
}
