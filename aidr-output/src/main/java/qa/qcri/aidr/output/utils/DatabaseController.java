package qa.qcri.aidr.output.utils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.output.entity.AidrCollection;

public class DatabaseController implements DatabaseInterface {

	protected EntityManager em;
	protected EntityManagerFactory emFactory;
	
	public DatabaseController() {
		this.emFactory = Persistence.createEntityManagerFactory("aidr_fetch_manager-PU");
		System.out.println("Entitymanager Factory: " + emFactory);
		try {
			this.em = emFactory.createEntityManager();
			System.out.println("entitymanager: " + em);
		} catch (Exception e) {
			System.err.println("Cannot create entitymanager: " + null);
			e.printStackTrace();
		}
	}

	@Override
	public Session getCurrentSession() {
		//System.out.println("[getCurrentSession] em = " + em);
		try {
			Session session = em.unwrap(Session.class);
			//System.out.println("[getCurrentSession] session = " + session);
			return session;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public EntityManager getEntityManager() {
		try {
			return em;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AidrCollection getById(Integer id) {
		try {
			return (AidrCollection) getCurrentSession().get(AidrCollection.class, id);
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return null;
	}

}
