package qa.qcri.aidr.predictdb.ejb.local.task.bean;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictdb.ejb.local.task.AbstractTaskManagerService;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.criteria.JoinType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * @author Koushik
 *
 */
@Stateless(name = "AbstractTaskManagerServiceBean")
public class AbstractTaskManagerServiceBean<E, I extends Serializable> implements ServletContextListener, AbstractTaskManagerService<E,I> {

	@PersistenceContext(unitName = "qa.qcri.aidr.taskmanager-EJBS") 
	protected EntityManager em;
	//@PersistenceContext(unitName = "qa.qcri.aidr.taskmanager-EJBS") org.hibernate.Session session;

	//@PersistenceContext(unitName = "qa.qcri.aidr.taskmanager-EJBS") SessionFactory sessionFactory;
	private Logger logger = Logger.getLogger(AbstractTaskManagerServiceBean.class);
	private ErrorLog elog = new ErrorLog();

	private Class<E> entityClass;
	private SessionFactory sessionFactory;

	public AbstractTaskManagerServiceBean(Class<E> entityClass) {
		this.entityClass = entityClass;
		//getSessionFactory();
	}

	public AbstractTaskManagerServiceBean() {}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public int setEntityManager(EntityManager em) {
		try {
			if (null == this.em) { 
				this.em = em;
				logger.info("EntityManager set to new value: " + this.em);
				return 1;
			} else 
				logger.info("Skipping setter, since EntityManager already initialized to :" + this.em);
			return 0;
		} catch (Exception e) {
			logger.error("EntityManager setting exception : " + em);
			logger.error(elog.toStringException(e));
			return -1;
		}
	}

	@Override
	public Session getCurrentSession() {
		try { 
			return em.unwrap(Session.class);
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	private SessionFactory getSessionFactory() {
		try {
			this.sessionFactory = em.unwrap(SessionFactory.class);
			return this.sessionFactory;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public E getById(I id) {
		try {
			return (E) getCurrentSession().get(entityClass, id);
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getByCriterionID(Criterion criterion) {
		try {
			Criteria criteria = getCurrentSession().createCriteria(entityClass);
			criteria.add(criterion);
			return (E) criteria.uniqueResult();
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getByCriteria(Criterion criterion) {
		try {
			Criteria criteria = getCurrentSession().createCriteria(entityClass);
			criteria.add(criterion);
			List<E> fetchedList = criteria.list();
			return (fetchedList != null && !fetchedList.isEmpty()) ? (E) fetchedList.get(0) : null;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getAll() {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		//criteria.setProjection(Projections.distinct(Projections.property("id")));
		try {	
			List<E> fetchedList = criteria.list();
			return (fetchedList != null && !fetchedList.isEmpty()) ? (List<E>) fetchedList : null;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getAllByCriteria(Criterion criterion) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(criterion);
		try {	
			List<E> fetchedList = criteria.list();
			return (fetchedList != null && !fetchedList.isEmpty()) ? (List<E>) fetchedList : null;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getByCriteriaWithLimit(Criterion criterion, Integer count) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(criterion);
		if(count != null){
			criteria.setMaxResults(count);
		}
		try {	
			List<E> fetchedList = criteria.list();
			return (fetchedList != null && !fetchedList.isEmpty()) ? (List<E>) fetchedList : null;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getByCriteriaByOrder(Criterion criterion, String order, String[] orderBy, Integer count) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(criterion);
		for(int i = 0; i< orderBy.length; i++){
			if (order.equalsIgnoreCase("desc")) {
				criteria.addOrder(Order.desc(orderBy[i]));
			} else {
				criteria.addOrder(Order.asc(orderBy[i]));
			}
		}
		if(count != null){
			criteria.setMaxResults(count);
		}
		try {	
			List<E> fetchedList = criteria.list();
			return (fetchedList != null && !fetchedList.isEmpty()) ? (List<E>) fetchedList : null;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getByCriteriaWithAliasByOrder(Criterion criterion, String order, String[] orderBy, Integer count, String aliasTable, Criterion aliasCriterion) {
		Session session = getCurrentSession();
		logger.info("Entity: " + entityClass + ", current Session = " + session);
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(criterion); 
		criteria.createAlias(aliasTable, aliasTable, org.hibernate.sql.JoinType.LEFT_OUTER_JOIN).add(aliasCriterion);
		if (orderBy != null) {
			for(int i = 0; i< orderBy.length; i++){
				if (order.equalsIgnoreCase("desc")) {
					criteria.addOrder(Order.desc(orderBy[i]));
				} else {
					criteria.addOrder(Order.asc(orderBy[i]));
				}
			}
		}
		if(count != null){
			criteria.setMaxResults(count);
		}
		//System.out.println("fetched List count = " + (fetchedList != null ? fetchedList.size() : null));
		try {	
			List<E> fetchedList = criteria.list();
			return (fetchedList != null && !fetchedList.isEmpty()) ? (List<E>) fetchedList : null;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getByCriteriaWithInnerJoinByOrder(Criterion criterion, String order, String[] orderBy, Integer count, String aliasTable) {
		Session session = getCurrentSession();
		logger.info("Entity: " + entityClass + ", current Session = " + session);
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(criterion); 
		criteria.createAlias(aliasTable, aliasTable, org.hibernate.sql.JoinType.INNER_JOIN);
		if (orderBy != null) {
			for(int i = 0; i< orderBy.length; i++){
				if (order.equalsIgnoreCase("desc")) {
					criteria.addOrder(Order.desc(orderBy[i]));
				} else {
					criteria.addOrder(Order.asc(orderBy[i]));
				}
			}
		}
		if(count != null){
			criteria.setMaxResults(count);
		}
		//System.out.println("fetched List count = " + (fetchedList != null ? fetchedList.size() : null));
		try {	
			List<E> fetchedList = criteria.list();
			return (fetchedList != null && !fetchedList.isEmpty()) ? (List<E>) fetchedList : null;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(E e) {
		Transaction tx = null;
		try {
			Session session = getCurrentSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(e);
			session.flush();
			session.evict(e);
			if (!tx.wasCommitted()) tx.commit();
		} catch (Exception ex) {
			System.out.println("Unable to update entity: " + e);
			logger.error(elog.toStringException(ex));
			ex.printStackTrace();
			tx.rollback();
		}
	}

	@Override
	public void update(List<E> entityCollection) {
		Session session = getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (E e: entityCollection) {
				session.saveOrUpdate(e);
				session.flush();
				session.evict(e);
			}
			if (!tx.wasCommitted()) tx.commit();
		} catch (Exception ex) {
			logger.error(elog.toStringException(ex));
			tx.rollback();
		}
	}

	@Override
	public void save(E e) {
		try {
			Session session = getCurrentSession();
			session.save(e);
			session.flush();
			session.evict(e);

		} catch (Exception ex) {
			System.out.println("Unable to save entity: " + e);
			logger.error(elog.toStringException(ex));
			ex.printStackTrace();
		}

	}

	@Override
	public void merge(E e) {
		try {
			Session session = getCurrentSession();
			session.merge(e);
			session.flush();
			session.evict(e);
		} catch (Exception ex) {
			logger.error(elog.toStringException(ex));
			ex.printStackTrace();
		}

	}

	@Override
	public void merge(List<E> entityCollection) {
		Session session = getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (E e: entityCollection) {
				session.merge(e);
				session.flush();
				session.evict(e);
			}
			if (!tx.wasCommitted()) tx.commit();
		} catch (Exception ex) {
			logger.error(elog.toStringException(ex));
			tx.rollback();
		}

	}

	@Override
	public void save(List<E> entityCollection) {
		Session session = getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (E e: entityCollection) {
				session.save(e);
				session.flush();
				session.evict(e);
			}
			if (!tx.wasCommitted()) tx.commit();
		} catch (Exception ex) {
			logger.error(elog.toStringException(ex));
			tx.rollback();
		}
	}

	@Override
	public void delete(E e) {
		try {
			Session session = getCurrentSession();
			session.buildLockRequest(LockOptions.UPGRADE).lock(e);
			session.delete(e);
			session.flush();
			session.evict(e);
		} catch (Exception ex) {
			logger.error(elog.toStringException(ex));
			ex.printStackTrace();
		}
	}

	@Override
	public void delete(List<E> entityCollection) {
		Session session = getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (E e: entityCollection) {
				session.buildLockRequest(LockOptions.UPGRADE).lock(e);
				session.delete(e);
				session.flush();
				session.evict(e);
			}
			if (!tx.wasCommitted()) tx.commit();
		} catch (Exception ex) {
			logger.error(elog.toStringException(ex));
			tx.rollback();
		}
	}

	@Override
	public void deleteByCriteria(Criterion criterion) {
		try {
			List<E> entityCollection = getAllByCriteria(criterion);
			delete(entityCollection);
		} catch (Exception ex) {
			logger.error(elog.toStringException(ex));
			ex.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			getCurrentSession().close();
			em.close();
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
		}
	}
}
