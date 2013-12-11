package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.dto.CollectionDataResponse;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.util.CollectionStatus;

@Repository("collectionRepository")
public class CollectionRepositoryImpl extends GenericRepositoryImpl<AidrCollection, Serializable> implements CollectionRepository{

	@SuppressWarnings("unchecked")
	@Override
	public List<AidrCollection> searchByName(String query,Integer userId) throws Exception {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
		criteria.add(Restrictions.ilike("name", query, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("user.id", userId));
		return (List<AidrCollection>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public CollectionDataResponse getPaginatedData(Integer start, Integer limit,Integer userId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		criteria.addOrder(Order.desc("startDate"));
		criteria.addOrder(Order.desc("createdDate"));
		
		Criteria countCriteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
                countCriteria.add(Restrictions.eq("user.id", userId)); // added by Imran to restrict count for the given user
		countCriteria.setProjection(Projections.rowCount());
		Long count = (Long) countCriteria.uniqueResult();
		
		return new CollectionDataResponse((List<AidrCollection>) criteria.list(),count);
	}

	@Override
	public Boolean exist(String code) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
		criteria.add(Restrictions.eq("code", code));
	    AidrCollection collection = (AidrCollection) criteria.uniqueResult();
	    return collection != null;
	}

	@Override
	public AidrCollection getRunningCollectionStatusByUser(Integer userId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
                criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("status", CollectionStatus.RUNNING));
		return (AidrCollection) criteria.uniqueResult();
	}

	@Override
	public AidrCollection getInitializingCollectionStatusByUser(Integer userId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
                criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("status", CollectionStatus.INITIALIZING));
		return (AidrCollection) criteria.uniqueResult();
	}

	@Override
	public AidrCollection start(Integer collectionId) {
		  AidrCollection collection =	this.findById(collectionId);
		  Calendar now = Calendar.getInstance();
		  collection.setStartDate(now.getTime());
//		  collection.setEndDate(null);
		  collection.setStatus(CollectionStatus.RUNNING);
		  this.update(collection);
		  return collection;
	}

	@Override
	public AidrCollection stop(Integer collectionId) {
		  AidrCollection collection =	this.findById(collectionId);
		  Calendar now = Calendar.getInstance();
		  collection.setEndDate(now.getTime());
		  collection.setStatus(CollectionStatus.STOPPED);
		  this.update(collection);
		  return collection;
	}

    @SuppressWarnings("unchecked")
    @Override
    public AidrCollection findByCode(String code) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
        criteria.add(Restrictions.eq("code", code));
        return (AidrCollection) criteria.uniqueResult();
    }

}
