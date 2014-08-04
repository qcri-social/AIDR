package qa.qcri.aidr.output.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;

import qa.qcri.aidr.output.entity.AidrCollection;

public class DatabaseController implements DatabaseInterface {

	// Debugging
	private static Logger logger = Logger.getLogger(DatabaseController.class.getName());
	private static ErrorLog elog = new ErrorLog();

	protected static EntityManager em;
	protected static EntityManagerFactory emFactory;

	//protected static SessionFactory sessionFactory = null;

	public DatabaseController() {
		try {
			emFactory = Persistence.createEntityManagerFactory("aidr_fetch_manager-PU");
			logger.debug("Entitymanager Factory: " + emFactory);

			em = emFactory.createEntityManager();
			logger.info("entitymanager: " + em);

			//sessionFactory = em.unwrap(SessionFactory.class);
		} catch (HibernateException e) {
			logger.error("Cannot create entitymanager: " + null);
			logger.error(elog.toStringException(e));
			//sessionFactory.close();
			em.close();
			emFactory.close();
		} 
	}

	@Override
	public EntityManagerFactory getCurrentEMFactory() {
		return emFactory;
	}

	@Override
	public Session getCurrentSession() {
		try {
			Session session = em.unwrap(Session.class);
			//Session session = sessionFactory.getCurrentSession();
			System.out.println("[getCurrentSession] session = " + session);
			return session;
		} catch (HibernateException e) {
			logger.error("Failed in creating session with em: " + em);
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public EntityManager getEntityManager() {
		try {
			return em;
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AidrCollection getById(Integer id) {
		try {
			return (AidrCollection) getCurrentSession().get(AidrCollection.class, id);
		} catch (HibernateException e) {
			logger.error("Exception in getting AidrCollection Id = " + id);
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Boolean> getPubliclyListed(Criterion criterion, Set<String> cbSet) {

		Map<String, Boolean> statusMap = new HashMap<String, Boolean>(cbSet.size());
		try {
			Session session = getCurrentSession();
			Criteria criteria = null;
			criteria = getCurrentSession().createCriteria(AidrCollection.class).add(criterion);
			List<Object> resultList = criteria.list();
			if (null == resultList) {
				return null;
			}
			for (Object obj: resultList) {
				AidrCollection collection = (AidrCollection) obj;
				statusMap.put(collection.getCode(), collection.getPubliclyListed());
			}
			//closeCurrentSession(session);

		} catch (HibernateException e) {
			logger.error("Exception in getting AidrCollection entity List by criteria");
			logger.error(elog.toStringException(e));
			return null;
		}
		return statusMap;
	}

	@Override
	public int closeCurrentSession(Session session) {
		try {
			session.close();
			return 1;
		} catch (HibernateException e) {
			e.printStackTrace();
			return -1;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public AidrCollection getByCriteria(Criterion criterion) {
		try {
			Session session = getCurrentSession();
			Criteria criteria = getCurrentSession().createCriteria(AidrCollection.class);
			criteria.add(criterion);
			Object result = criteria.uniqueResult();
			if (result != null) {
				return (AidrCollection) result; 
			}
		} catch (HibernateException e) {
			logger.error("Exception in getting AidrCollection entity by crtieria");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AidrCollection> getAll() {
		try {
			Criteria criteria = getCurrentSession().createCriteria(AidrCollection.class);
			return criteria.list();
		} catch (HibernateException e) {
			logger.error("Exception in getting entire AidrCollection");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AidrCollection> getAllByCriteria(Criterion criterion) {
		try {
			Criteria criteria = getCurrentSession().createCriteria(AidrCollection.class);
			criteria.add(criterion);
			return criteria.list();
		} catch (HibernateException e) {
			logger.error("Exception in getting AidrCollection entity list by crtieria");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

}
