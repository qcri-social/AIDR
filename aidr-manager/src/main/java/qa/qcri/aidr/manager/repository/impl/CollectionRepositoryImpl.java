package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.util.CollectionStatus;

@Repository("collectionRepository")
@Transactional
public class CollectionRepositoryImpl extends GenericRepositoryImpl<Collection, Serializable> implements CollectionRepository{
	private Logger logger = Logger.getLogger(CollectionRepositoryImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> searchByName(String query, Long userId) throws Exception {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.add(Restrictions.ilike("name", query, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("owner.id", userId));
		return (List<Collection>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> getPaginatedDataForPublic(final Integer start, final Integer limit, final Enum statusValue) {
		//        Workaround as criteria query gets result for different managers and in the end we get less then limit records.
		List<BigInteger> collectionIds = (List<BigInteger>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {

				String sql = " SELECT DISTINCT c.id FROM collection c" +
						" WHERE (c.publicly_listed = 1 and c.status = :statusValue) " +
						" order by c.start_date DESC, c.created_at DESC LIMIT :start, :limit ";


				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setParameter("start", start);
				sqlQuery.setParameter("limit", limit);
				sqlQuery.setParameter("statusValue", statusValue.ordinal());
				List<BigInteger> ids = (List<BigInteger>) sqlQuery.list();

				return ids != null ? ids : Collections.emptyList();
			}
		});

		List<Collection> a = new ArrayList<Collection>();

		for(int i =0; i < collectionIds.size(); i++){
			Collection collection =	this.findById(collectionIds.get(i).longValue());
			a.add(collection) ;
		}
		return a;

	}

	@Override
	public Integer getPublicCollectionsCount(final Enum statusValue) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {

				String sql = " SELECT count(distinct c.id) FROM collection c" +
						" WHERE (c.publicly_listed = 1 and c.status = :statusValue) " ;

				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setParameter("statusValue", statusValue.ordinal());
				BigInteger total = (BigInteger) sqlQuery.uniqueResult();
				return total != null ? total.intValue() : 0;
			}
		});
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> getPaginatedData(final Integer start, final Integer limit, final UserAccount user, final boolean onlyTrashed) {
		/*List<Collection> result = new ArrayList<Collection>();
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.add(Restrictions.eq("owner.id", user.getId()));
		if(onlyTrashed) {
			criteria.add(Restrictions.eq("status", CollectionStatus.TRASHED));
		} else {
			criteria.add(Restrictions.ne("status", CollectionStatus.TRASHED));
		}
		result =  criteria.list();*/
		
		final Long userId = user.getId();
		final String conditionTrashed;
		if (onlyTrashed) {
			conditionTrashed = "=";
		} else {
			conditionTrashed = "!=";
		}   
		
		List<Object[]> collections = (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				String sql = " SELECT DISTINCT c.id, c.status FROM collection c " +
						" LEFT OUTER JOIN collection_collaborator c_m " +
						" ON c.id = c_m.collection_id " +
						" WHERE ((c.owner_id =:userId OR c_m.account_id = :userId) AND c.status " + conditionTrashed + " :statusValue) " +
						" ORDER BY Case c.status When :status1 Then 1 When :status2 Then 1 Else 3 End, " +
						" Case c.start_date When null Then 1 Else c.start_date*-1  End " +
						" LIMIT :start, :limit ";

				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setParameter("userId", userId);
				sqlQuery.setParameter("start", start);
				sqlQuery.setParameter("limit", limit);
				sqlQuery.setParameter("status1", CollectionStatus.RUNNING.ordinal());
				sqlQuery.setParameter("status2", CollectionStatus.RUNNING_WARNING.ordinal());
				sqlQuery.setParameter("statusValue", CollectionStatus.TRASHED.ordinal());
				List<Object[]> ids = (List<Object[]>) sqlQuery.list();

				return ids != null ? ids : Collections.emptyList();
			}
		});

		//MEGHNA: To prevent multiple db calls, we get collection id and status from db and update AidrCollection status
		List<Collection> result = new ArrayList<Collection>();
		BigInteger id;
		for(Object[] col : collections)
		{			
			id = (BigInteger)col[0];
			Collection collection =	this.findById(id.longValue());
			collection.setStatus(CollectionStatus.values()[(Integer)col[1]]);
			result.add(collection) ;
		}
		return result;
	}

	@Override
	public Integer getCollectionsCount(final UserAccount user, final boolean onlyTrashed) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				Long userId = user.getId();
				final String conditionTrashed;
				if (onlyTrashed) {
					conditionTrashed = "=";
				} else {
					conditionTrashed = "!=";
				}

				String sql = " select count(distinct c.id) " +
						" FROM collection c " +
						" LEFT OUTER JOIN collection_collaborator c_m " +
						" ON c.id = c_m.collection_id " +
						" WHERE (c.status " + conditionTrashed + " :statusValue and (c.owner_id = :userId or c_m.account_id = :userId)) ";

				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setParameter("userId", userId);
				sqlQuery.setParameter("statusValue", CollectionStatus.TRASHED.ordinal());
				BigInteger total = (BigInteger) sqlQuery.uniqueResult();
				return total != null ? total.intValue() : 0;
			}
		});
	}

	@Override
	public Boolean exist(String code) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.add(Restrictions.eq("code", code));
		criteria.add(Restrictions.ne("status", CollectionStatus.TRASHED));
		Collection collection = (Collection) criteria.uniqueResult();
		return collection != null;
	}

	@Override
	public Boolean existName(String name) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.add(Restrictions.eq("name", name));
		Collection collection = (Collection) criteria.uniqueResult();
		return collection != null;
	}

	@Override
	public Collection getRunningCollectionStatusByUser(Long userId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		//criteria.add(Restrictions.eq("user.id", userId));
		//criteria.add(Restrictions.eq("status", CollectionStatus.RUNNING));
		
		LogicalExpression or = Restrictions.or(
				Restrictions.eq("status", CollectionStatus.RUNNING),
				Restrictions.eq("status", CollectionStatus.RUNNING_WARNING)				
				);
		
		LogicalExpression orAll = Restrictions.or(
				or,
				Restrictions.eq("status", CollectionStatus.WARNING)
				);
		
		/*Is this check needed?
		 * 
		 * LogicalExpression and = Restrictions.and(
				orAll,
				Restrictions.ne("status", CollectionStatus.TRASHED)				
				);*/
		LogicalExpression andAll = Restrictions.and(
				orAll,
				Restrictions.eq("owner.id", userId)
				);
		
		criteria.add(andAll);
		//criteria.add(Restrictions.ne("status", CollectionStatus.TRASHED));
		return (Collection) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> getAllCollectionByUser(Long userId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.add(Restrictions.eq("owner.id", userId));

		return (List<Collection>) criteria.list();
	}

	@Override
	public List<Collection> getRunningCollections() {
		return getRunningCollections(null, null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> getRunningCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) {
		Criteria criteriaIds = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteriaIds.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id"));

		LogicalExpression or = Restrictions.or(
				Restrictions.eq("status", CollectionStatus.RUNNING),
				Restrictions.eq("status", CollectionStatus.RUNNING_WARNING)
				);

		LogicalExpression or2 = Restrictions.or(
				or,
				Restrictions.eq("status", CollectionStatus.INITIALIZING)
				);
		
		LogicalExpression orAll = Restrictions.or(
				or2,
				Restrictions.eq("status", CollectionStatus.WARNING)
				);
		
		LogicalExpression andAll = Restrictions.and(
				orAll,
				Restrictions.ne("status", CollectionStatus.TRASHED)
				);

		criteriaIds.add(andAll);
		addCollectionSearchCriteria(terms, criteriaIds);
		searchCollectionsAddOrder(sortColumn, sortDirection, criteriaIds);

		if (start != null) {
			criteriaIds.setFirstResult(start);
		}
		if (limit != null) {
			criteriaIds.setMaxResults(limit);
		}

		List<Integer> ids = (List<Integer>) criteriaIds.list();

		if (ids.size() == 0){
			return Collections.emptyList();
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.in("id", ids));
		searchCollectionsAddOrder(sortColumn, sortDirection, criteria);

		return (List<Collection>) criteria.list();
	}

	@Override
	public Long getRunningCollectionsCount(String terms) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id"));

		LogicalExpression or = Restrictions.or(
				Restrictions.eq("status", CollectionStatus.RUNNING),
				Restrictions.eq("status", CollectionStatus.RUNNING_WARNING)
				);

		LogicalExpression or2 = Restrictions.or(
				or,
				Restrictions.eq("status", CollectionStatus.INITIALIZING)
				);
		
		LogicalExpression orAll = Restrictions.or(
				or2,
				Restrictions.eq("status", CollectionStatus.WARNING)
				);

		LogicalExpression andAll = Restrictions.and(
				orAll,
				Restrictions.ne("status", CollectionStatus.TRASHED)
				);

		criteria.add(andAll);
		addCollectionSearchCriteria(terms, criteria);

		ScrollableResults scroll = criteria.scroll();
		int i = scroll.last() ? scroll.getRowNumber() + 1 : 0;
		return Long.valueOf(i);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> getStoppedCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) {
		Criteria criteriaIds = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteriaIds.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id"));

		LogicalExpression or = Restrictions.or(
				Restrictions.eq("status", CollectionStatus.STOPPED),
				Restrictions.eq("status", CollectionStatus.NOT_RUNNING)
				);
		
		LogicalExpression orAll = Restrictions.or(
				or,
				Restrictions.eq("status", CollectionStatus.FATAL_ERROR)
				);
		
		criteriaIds.add(orAll);
		addCollectionSearchCriteria(terms, criteriaIds);
		searchCollectionsAddOrder(sortColumn, sortDirection, criteriaIds);

		if (start != null) {
			criteriaIds.setFirstResult(start);
		}
		if (limit != null) {
			criteriaIds.setMaxResults(limit);
		}

		List<Integer> ids = (List<Integer>) criteriaIds.list();

		if (ids.size() == 0){
			return Collections.emptyList();
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.in("id", ids));
		searchCollectionsAddOrder(sortColumn, sortDirection, criteria);

		return (List<Collection>) criteria.list();
	}

	@Override
	public Long getStoppedCollectionsCount(String terms) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id"));
		
		LogicalExpression or = Restrictions.or(
				Restrictions.eq("status", CollectionStatus.STOPPED),
				Restrictions.eq("status", CollectionStatus.NOT_RUNNING)
				);
		
		LogicalExpression orAll = Restrictions.or(
				or,
				Restrictions.eq("status", CollectionStatus.FATAL_ERROR)
				);
		
		criteria.add(orAll);
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
			if ("owner".equals(sortColumn)){
				sortColumn = "owner.userName";
				criteria.createAlias("owner", "owner");
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
	public Collection getInitializingCollectionStatusByUser(Long userId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.add(Restrictions.eq("owner.id", userId));
		criteria.add(Restrictions.eq("status", CollectionStatus.INITIALIZING));
		return (Collection) criteria.uniqueResult();
	}

	@Override
	public Collection start(Long collectionId) {
		Collection collection =	this.findById(collectionId);
		Calendar now = Calendar.getInstance();
		collection.setStartDate(now.getTime());
		//		  collection.setEndDate(null);
		collection.setStatus(CollectionStatus.RUNNING);
		this.update(collection);
		return collection;
	}

	@Override
	public Collection stop(Long collectionId) {
		Collection collection =	this.findById(collectionId);
		Calendar now = Calendar.getInstance();
		collection.setEndDate(now.getTime());
		collection.setStatus(CollectionStatus.STOPPED);
		this.update(collection);
		return collection;
	}

	@Override
	public Collection findByCode(String code) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.add(Restrictions.eq("code", code));
		try {
			return (Collection) criteria.uniqueResult();
		} catch (HibernateException e) {
			logger.error("Hibernate exception while finding a collection by code: "+code + "/t"+e.getStackTrace());
			return null;
		}
	}

	@Override
	public Collection trashCollectionById(Long collectionId) {
		Collection collection = stop(collectionId);
		collection.setStatus(CollectionStatus.TRASHED);
		this.update(collection);

		return collection;
	}

	@Override
	public void update(Collection collection) {
		collection.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		super.update(collection);
	}
	
	@Override
	public void save(Collection collection) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		collection.setUpdatedAt(now);
		collection.setCreatedAt(now);
		super.save(collection);
	}
	
	@Override
	public List<Collection> getAllCollections() {
	
		List<Collection> collections = new ArrayList<Collection>();
		collections = findAll();
		return collections;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> findMicromappersFilteredCollections(boolean micromappersEnabled) {
		
		List<Collection> collections = new ArrayList<Collection>();

		try {
			Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
			criteria.add(Restrictions.eq("micromappersEnabled", micromappersEnabled));
			collections = criteria.list();
			
		} catch (HibernateException e) {
			logger.error("Exception in fetching list of collections.", e);
		}
		
		return collections;
	}
}
