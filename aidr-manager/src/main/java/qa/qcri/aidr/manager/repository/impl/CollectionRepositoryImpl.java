package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;

import org.springframework.util.StringUtils;
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

    @SuppressWarnings("unchecked")
    @Override
    public List<AidrCollection> getRunningCollections() {
        return getRunningCollections(null, null, null, null, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AidrCollection> getRunningCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);

        LogicalExpression or = Restrictions.or(
                Restrictions.eq("status", CollectionStatus.RUNNING),
                Restrictions.eq("status", CollectionStatus.RUNNING_WARNING)
        );
        criteria.add(or);
        addCollectionSearchCriteria(terms, criteria);
        searchCollectionsAddOrder(sortColumn, sortDirection, criteria);

        if (start != null) {
            criteria.setFirstResult(start);
        }
        if (limit != null) {
            criteria.setMaxResults(limit);
        }

        return (List<AidrCollection>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long getRunningCollectionsCount(String terms) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        LogicalExpression or = Restrictions.or(
                Restrictions.eq("status", CollectionStatus.RUNNING),
                Restrictions.eq("status", CollectionStatus.RUNNING_WARNING)
        );
        criteria.add(or);
        addCollectionSearchCriteria(terms, criteria);

        ScrollableResults scroll = criteria.scroll();
        int i = scroll.last() ? scroll.getRowNumber() + 1 : 0;
        return Long.valueOf(i);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AidrCollection> getStoppedCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);

        criteria.add(Restrictions.ne("status", CollectionStatus.RUNNING));
        criteria.add(Restrictions.ne("status", CollectionStatus.RUNNING_WARNING));
        addCollectionSearchCriteria(terms, criteria);
        searchCollectionsAddOrder(sortColumn, sortDirection, criteria);

        if (start != null) {
            criteria.setFirstResult(start);
        }
        if (limit != null) {
            criteria.setMaxResults(limit);
        }

        return (List<AidrCollection>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long getStoppedCollectionsCount(String terms) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        criteria.add(Restrictions.ne("status", CollectionStatus.RUNNING));
        criteria.add(Restrictions.ne("status", CollectionStatus.RUNNING_WARNING));
        addCollectionSearchCriteria(terms, criteria);

        ScrollableResults scroll = criteria.scroll();
        int i = scroll.last() ? scroll.getRowNumber() + 1 : 0;
        return Long.valueOf(i);
    }

    private void addCollectionSearchCriteria(String terms, Criteria criteria) {
        if (StringUtils.hasText(terms)){
            String wildcard ='%'+ URLDecoder.decode(terms.trim())+'%';

            LogicalExpression orNameCode = Restrictions.or(
                    Restrictions.ilike("name", wildcard),
                    Restrictions.ilike("code", wildcard)
            );

            LogicalExpression orAll = Restrictions.or(
                    orNameCode,
                    Restrictions.ilike("track", wildcard)
            );

            criteria.add(orAll);
        }
    }

    private void searchCollectionsAddOrder(String sortColumn, String sortDirection, Criteria criteria) {
        if (StringUtils.hasText(sortColumn)) {
            if ("user".equals(sortColumn)){
                sortColumn = "user.userName";
                criteria.createAlias("user", "user");
            }
            Order order;
            if ("ASC".equals(sortDirection)){
                order = Order.asc(sortColumn);
            } else {
                order = Order.desc(sortColumn);
            }
            criteria.addOrder(order);
        }
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
