package qa.qcri.aidr.trainer.api.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;

import qa.qcri.aidr.trainer.api.dao.AbstractDao;
import qa.qcri.aidr.trainer.api.util.ErrorLog;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractDaoImpl<E, I extends Serializable> implements AbstractDao<E,I> {

	private Class<E> entityClass;

	protected AbstractDaoImpl(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	private static Logger logger = Logger.getLogger(AbstractDaoImpl.class);
	private static ErrorLog elog = new ErrorLog();

	@Autowired
	private SessionFactory sessionFactory;

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public E findById(I id) {
		return (E) getCurrentSession().get(entityClass, id);
	}

	@Override
	public void saveOrUpdate(E e) {
		Session session = getCurrentSession();
		session.saveOrUpdate(e);
	}

	@Override
	public void saveOrMerge(E e) {
		Session session = getCurrentSession();
		session.merge(e);
	}

	@Override
	public void save(E e) {
		Session session = getCurrentSession();
		session.save(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.setProjection(Projections.distinct(Projections.property("id")));
		try {
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
			return null;
		}

	}

	@Override
	public void delete(E e) {
		Session session = getCurrentSession();
		session.buildLockRequest(LockOptions.UPGRADE).lock(e);
		session.delete(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByCriteria(Criterion criterion) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(criterion);
		try {
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getMaxOrderByCriteria(Criterion criterion, String orderBy) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(criterion);
		criteria.addOrder(Order.desc(orderBy));
		criteria.setMaxResults(1);
		try {
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByCriteriaByOrder(Criterion criterion, String[] orderBy, Integer count) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(criterion);
		for(int i = 0; i< orderBy.length; i++){
			criteria.addOrder(Order.desc(orderBy[i]));
		}
		if(count != null){
			criteria.setMaxResults(count);
		}
		try {
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByCriteriaWithAliasByOrder(Criterion criterion, String[] orderBy, Integer count, String aliasTable, Criterion aliasCriterion) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(criterion);
		criteria.createAlias(aliasTable, aliasTable, CriteriaSpecification.LEFT_JOIN).add(aliasCriterion);

		for(int i = 0; i< orderBy.length; i++){
			criteria.addOrder(Order.desc(orderBy[i]));
		}
		if(count != null){
			criteria.setMaxResults(count);
		}
		try {
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByCriteria(Criterion criterion, Integer count) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(criterion);

		if(count != null){
			criteria.setMaxResults(count);
		}
		try {
			return criteria.list();
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public E findByCriterionID(Criterion criterion) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(criterion);
		try {
			return (E) criteria.uniqueResult();
		} catch (HibernateException e) {
			logger.error(elog.toStringException(e));
			return null;
		}
	}
}
