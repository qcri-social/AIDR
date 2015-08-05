/**
 * 
 * 
 * This class implements the core functionalities of save, delete, find and update operations on tables of
 * the aidr_predict DB for local EJB access only. 
 *
 * @author Koushik
 */

package qa.qcri.aidr.dbmanager.ejb.local.facade.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;



@Stateless(name = "CoreDBServiceFacadeImp")
public class CoreDBServiceFacadeImp<E extends Serializable, I extends Serializable> implements CoreDBServiceFacade<E,I> {

	@PersistenceContext(unitName = "qa.qcri.aidr.dbmanager-EJBS") 
	protected EntityManager em;

	private static final Logger logger = Logger.getLogger("db-manager-log");

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
			logger.error("getEntityManager failed", e);
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
			logger.error("EntityManager setting exception : " + em, e);
			throw new HibernateException("setEntityManager failed");
		}
	}

	@Override
	public Session getCurrentSession() {
		try { 
			return em.unwrap(Session.class);
		} catch (Exception e) {
			logger.error("getCurrentSession failed", e);
			throw new HibernateException("getCurrentSession failed");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getById(I id) {
		try {
			return (E) getCurrentSession().get(entityClass, id);
		} catch (Exception e) {
			logger.error("getById failed, id = " + id, e);
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
			logger.error("getByCriterionID failed, criteria = " + criterion.toString(), e);
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
			logger.error("getByCriteria failed, criteria = " + criterion.toString(), e);
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
			return fetchedList;
		} catch (Exception e) {
			logger.error("getAll failed", e);
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
			logger.error("getAllByCriteria failed, criteria = " + criterion.toString(), e);
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
			logger.error("getByCriteriaWithLimit failed, criteria = " + criterion.toString(), e);
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
			logger.error("getByCriteriaWithLimit failed, criteria = " + criterion.toString(), e);
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
		try {	
			fetchedList = criteria.list();
			return fetchedList;
		} catch (Exception e) {
			logger.error("getByCriteriaWithAliasByOrder failed, criteria = " + criterion.toString(), e);
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
		try {	
			fetchedList = criteria.list();
			return fetchedList;
		} catch (Exception e) {
			logger.error("getByCriteriaWithInnerJoinByOrder failed, criteria = " + criterion.toString(), e);
			throw new HibernateException("getByCriteriaWithInnerJoinByOrder failed, criteria = " + criterion.toString());
		}
	}

	@Override
	public void update(E e) {
		try {
			Session session = getCurrentSession();
			session.saveOrUpdate(e);
			session.flush();
			session.evict(e);
		} catch (Exception ex) {
			logger.error("Update failed", ex);
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
			logger.error("Update list failed", ex);
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
			logger.error("Save failed", ex);
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
			logger.error("Merge failed", ex);
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
			logger.error("Merge list failed", ex);
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
			logger.error("Save list failed", ex);
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
			logger.error("Delete failed", ex);
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
			logger.error("Delete list failed", ex);
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
			logger.error("Delete by criteria failed", ex);
			throw new HibernateException("Delete by criteria failed");
		}
	}
}
