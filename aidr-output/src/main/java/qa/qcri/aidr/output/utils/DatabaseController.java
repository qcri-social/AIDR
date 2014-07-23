package qa.qcri.aidr.output.utils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.output.entity.AidrCollection;

public class DatabaseController implements DatabaseInterface {

	// Debugging
	private static Logger logger = Logger.getLogger(DatabaseController.class.getName());
	private static ErrorLog elog = new ErrorLog();
	
	protected EntityManager em;
	protected EntityManagerFactory emFactory;
	
	public DatabaseController() {
		this.emFactory = Persistence.createEntityManagerFactory("aidr_fetch_manager-PU");
		//logger.debug("Entitymanager Factory: " + emFactory);
		try {
			this.em = emFactory.createEntityManager();
			logger.info("entitymanager: " + em);
		} catch (Exception e) {
			logger.error("Cannot create entitymanager: " + null);
			logger.error(elog.toStringException(e));
		}
	}

	@Override
	public Session getCurrentSession() {
		//System.out.println("[getCurrentSession] em = " + em);
		try {
			Session session = em.unwrap(Session.class);
			//logger.debug("session = " + session);
			return session;
		} catch (Exception e) {
			logger.error("Failed in creating session");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@Override
	public EntityManager getEntityManager() {
		try {
			return em;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AidrCollection getById(Integer id) {
		try {
			return (AidrCollection) getCurrentSession().get(AidrCollection.class, id);
		} catch (Exception e) {
			logger.error("Exception in getting AidrCollection Id = " + id);
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AidrCollection getByCriteria(Criterion criterion) {
		try {
			Criteria criteria = getCurrentSession().createCriteria(AidrCollection.class);
			criteria.add(criterion);
			List resultList = criteria.list();
			if (resultList != null && !resultList.isEmpty()) {
				return (AidrCollection) resultList.get(0); 
			}
		} catch (Exception e) {
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
		} catch (Exception e) {
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
		} catch (Exception e) {
			logger.error("Exception in getting AidrCollection entity list by crtieria");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

}
