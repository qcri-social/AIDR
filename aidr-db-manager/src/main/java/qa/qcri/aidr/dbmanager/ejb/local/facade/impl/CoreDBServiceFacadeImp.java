package qa.qcri.aidr.dbmanager.ejb.local.facade.impl;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.*;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * 
 * @author Koushik
 *
 */
@Stateless(name = "CoreDBServiceFacadeImp")
public class CoreDBServiceFacadeImp<E extends Serializable, I extends Serializable> implements CoreDBServiceFacade<E,I> {

	@PersistenceContext(unitName = "qa.qcri.aidr.dbmanager-EJBS") 
	protected EntityManager em;

	private Logger logger = Logger.getLogger("db-manager-log");
	private ErrorLog elog = new ErrorLog();

	private Class<E> entityClass;

	public CoreDBServiceFacadeImp(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public CoreDBServiceFacadeImp() {}

	@Override
	public EntityManager getEntityManager() {
		try {
			return em; 
		} catch (Exception e) {
			throw new HibernateException("getEntityManager failed");
		}
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
			throw new HibernateException("setEntityManager failed");
		}
	}

	@Override
	public Session getCurrentSession() {
		try { 
			return em.unwrap(Session.class);
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			throw new HibernateException("getCurrentSession failed");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getById(I id) {
		try {
			return (E) getCurrentSession().get(entityClass, id);
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			throw new HibernateException("getById failed, id = " + id);
		}
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
			throw new HibernateException("getByCriterionID failed, criteria = " + criterion.toString());
		}
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
			throw new HibernateException("getByCriteria failed, criteria = " + criterion.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getAll() {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		List<E> fetchedList = new ArrayList<E>();
		try {	
			fetchedList  = criteria.list();
			//System.out.println("CoreDBServiceFacade: getAll fetched list size: " + fetchedList.size());
			return fetchedList;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			throw new HibernateException("getAll failed");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getAllByCriteria(Criterion criterion) {
		List<E> fetchedList = new ArrayList<E>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(criterion);
		
		try {	
			fetchedList = criteria.list();
			return fetchedList;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			throw new HibernateException("getAllByCriteria failed, criteria = " + criterion.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getByCriteriaWithLimit(Criterion criterion, Integer count) {
		List<E> fetchedList = new ArrayList<E>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(criterion);
		if(count != null && count > 0){
			criteria.setMaxResults(count);
		}
		try {	
			fetchedList = criteria.list();
			return fetchedList;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			throw new HibernateException("getByCriteriaWithLimit failed, criteria = " + criterion.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getByCriteriaByOrder(Criterion criterion, String order, String[] orderBy, Integer count) {
		List<E> fetchedList = new ArrayList<E>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(criterion);
		for(int i = 0; i< orderBy.length; i++){
			if (order != null && order.equalsIgnoreCase("desc")) {
				criteria.addOrder(Order.desc(orderBy[i]));
			} else {
				criteria.addOrder(Order.asc(orderBy[i]));
			}
		}
		if(count != null && count > 0){
			criteria.setMaxResults(count);
		}
		try {	
			fetchedList = criteria.list();
			return fetchedList;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			throw new HibernateException("getByCriteriaByOrder failed, criteria = " + criterion.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getByCriteriaWithAliasByOrder(Criterion criterion, String order, String[] orderBy, Integer count, String aliasTable, Criterion aliasCriterion) {
		Session session = getCurrentSession();
		List<E> fetchedList = new ArrayList<E>();
		//logger.info("Entity: " + entityClass + ", current Session = " + session);
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(criterion); 
		criteria.createAlias(aliasTable, aliasTable, org.hibernate.sql.JoinType.LEFT_OUTER_JOIN).add(aliasCriterion);
		if (orderBy != null) {
			for(int i = 0; i< orderBy.length; i++){
				if (order != null && order.equalsIgnoreCase("desc")) {
					criteria.addOrder(Order.desc(orderBy[i]));
				} else {
					criteria.addOrder(Order.asc(orderBy[i]));
				}
			}
		}
		if(count != null && count > 0){
			criteria.setMaxResults(count);
		}
		//System.out.println("fetched List count = " + (fetchedList != null ? fetchedList.size() : null));
		try {	
			fetchedList = criteria.list();
			return fetchedList;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			throw new HibernateException("getByCriteriaWithAliasByOrder failed, criteria = " + criterion.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getByCriteriaWithInnerJoinByOrder(Criterion criterion, String order, String[] orderBy, Integer count, String aliasTable, Criterion aliasCriterion) {
		Session session = getCurrentSession();
		List<E> fetchedList = new ArrayList<E>();
		//logger.info("Entity: " + entityClass + ", current Session = " + session);
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(criterion); 
		criteria.createAlias(aliasTable, aliasTable, org.hibernate.sql.JoinType.INNER_JOIN).add(aliasCriterion);
		if (orderBy != null) {
			for(int i = 0; i< orderBy.length; i++){
				if (order != null && order.equalsIgnoreCase("desc")) {
					criteria.addOrder(Order.desc(orderBy[i]));
				} else {
					criteria.addOrder(Order.asc(orderBy[i]));
				}
			}
		}
		if(count != null && count > 0){
			criteria.setMaxResults(count);
		}
		//System.out.println("fetched List count = " + (fetchedList != null ? fetchedList.size() : null));
		try {	
			fetchedList = criteria.list();
			return fetchedList;
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			throw new HibernateException("getByCriteriaWithInnerJoinByOrder failed, criteria = " + criterion.toString());
		}
	}

	@Override
	public void update(E e) {
		Transaction tx = null;
		try {
			Session session = getCurrentSession();
			session.saveOrUpdate(e);
			session.flush();
			session.evict(e);
		} catch (Exception ex) {
			System.out.println("Unable to update entity: " + e);
			logger.error(elog.toStringException(ex));
			ex.printStackTrace();
			throw new HibernateException("Update failed");
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
			throw new HibernateException("Update list failed");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public I save(E e) {
		try {
			Session session = getCurrentSession();
			I id = (I) session.save(e);
			session.flush();
			session.evict(e);
			return id;

		} catch (Exception ex) {
			System.out.println("Unable to save entity: " + e);
			logger.error(elog.toStringException(ex));
			ex.printStackTrace();
			throw new HibernateException("Save failed");
		}

	}

	@Override
	public Object merge(E e) {
		try {
			Session session = getCurrentSession();
			Object o = (Object) session.merge(e);
			session.flush();
			session.evict(e);
			return o;
		} catch (Exception ex) {
			logger.error(elog.toStringException(ex));
			ex.printStackTrace();
			throw new HibernateException("Merge failed");
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
			throw new HibernateException("Merge list failed");
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
			throw new HibernateException("Save list failed");
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
			throw new HibernateException("Delete failed");
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
			throw new HibernateException("Delete list failed");
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
			throw new HibernateException("Delete by criteria failed");
		}
	}
}
