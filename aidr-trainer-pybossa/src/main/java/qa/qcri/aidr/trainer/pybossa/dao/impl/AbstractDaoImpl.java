package qa.qcri.aidr.trainer.pybossa.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import qa.qcri.aidr.trainer.pybossa.dao.AbstractDao;
import qa.qcri.aidr.trainer.pybossa.util.ErrorLog;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractDaoImpl<E, I extends Serializable> implements AbstractDao<E,I> {

	private static Logger logger = Logger.getLogger(AbstractDaoImpl.class);
	private static ErrorLog elog = new ErrorLog();

	private Class<E> entityClass;

	protected AbstractDaoImpl(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	@Qualifier("sessionFactory")
	@Autowired
	private SessionFactory sessionFactory;



	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}


	@SuppressWarnings("unchecked")
	@Override
	public E findById(I id) {
		return (E) getCurrentSession().get(entityClass, id);
	}

	@Override
	public void saveOrUpdate(E e) {
		getCurrentSession().saveOrUpdate(e);
	}

	@Override
	public void save(E e) {
		getCurrentSession().save(e);
	}


	@Override
	public void delete(E e) {
		getCurrentSession().delete(e);
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

	/**  do not use this method. it is for app live testing **/
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.setProjection(Projections.distinct(Projections.property("crisisID")));

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
}

