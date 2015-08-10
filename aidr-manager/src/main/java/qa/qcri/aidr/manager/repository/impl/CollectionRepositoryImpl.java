package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.sql.SQLException;
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
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.util.CollectionStatus;

@Repository("collectionRepository")
public class CollectionRepositoryImpl extends GenericRepositoryImpl<AidrCollection, Serializable> implements CollectionRepository{
	private Logger logger = Logger.getLogger(CollectionRepositoryImpl.class);

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
	public List<AidrCollection> getPaginatedDataForPublic(final Integer start, final Integer limit, final Enum statusValue) {
		//        Workaround as criteria query gets result for different managers and in the end we get less then limit records.
		List<Integer> collectionIds = (List<Integer>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String sql = " SELECT DISTINCT c.id FROM AIDR_COLLECTION c" +
						" WHERE (c.publiclyListed = 1 and c.status = :statusValue) " +
						" order by c.startDate DESC, c.createdDate DESC LIMIT :start, :limit ";


				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setParameter("start", start);
				sqlQuery.setParameter("limit", limit);
				sqlQuery.setParameter("statusValue", statusValue.ordinal());
				List<Integer> ids = (List<Integer>) sqlQuery.list();

				return ids != null ? ids : Collections.emptyList();
			}
		});

		List<AidrCollection> a = new ArrayList<AidrCollection>();

		for(int i =0; i < collectionIds.size(); i++){
			AidrCollection collection =	this.findById(collectionIds.get(i));
			a.add(collection) ;
		}
		return a;

	}

	@Override
	public Integer getPublicCollectionsCount(final Enum statusValue) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String sql = " SELECT count(distinct c.id) FROM AIDR_COLLECTION c" +
						" WHERE (c.publiclyListed = 1 and c.status = :statusValue) " ;

				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setParameter("statusValue", statusValue.ordinal());
				BigInteger total = (BigInteger) sqlQuery.uniqueResult();
				return total != null ? total.intValue() : 0;
			}
		});
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<AidrCollection> getPaginatedData(final Integer start, final Integer limit, final UserEntity user, final boolean onlyTrashed) {
		final Integer userId = user.getId();
		final String conditionTrashed;
		if (onlyTrashed) {
			conditionTrashed = "=";
		} else {
			conditionTrashed = "!=";
		}      
/*		List<Integer> collectionIds = (List<Integer>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = " SELECT DISTINCT c.id FROM AIDR_COLLECTION c " +
						" LEFT OUTER JOIN AIDR_COLLECTION_TO_MANAGER c_m " +
						" ON c.id = c_m.id_collection " +
						" WHERE ((c.user_id =:userId OR c_m.id_manager = :userId) AND c.status " + conditionTrashed + " :statusValue) " +
						" order by c.startDate IS NULL DESC, c.startDate DESC, c.createdDate DESC LIMIT :start, :limit ";

				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setParameter("userId", userId);
				sqlQuery.setParameter("start", start);
				sqlQuery.setParameter("limit", limit);
				sqlQuery.setParameter("statusValue", CollectionStatus.TRASHED.ordinal());
				List<Integer> ids = (List<Integer>) sqlQuery.list();

				return ids != null ? ids : Collections.emptyList();
			}
		});
		for(Integer id : collectionIds){
			AidrCollection collection =	this.findById(id);
			result.add(collection) ;
		}
		*/		
		
		//Workaround as criteria query gets result for different managers and in the end we get less than limit records.
		List<Object[]> collections = (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = " SELECT DISTINCT c.id,c.status FROM AIDR_COLLECTION c " +
						" LEFT OUTER JOIN AIDR_COLLECTION_TO_MANAGER c_m " +
						" ON c.id = c_m.id_collection " +
						" WHERE ((c.user_id =:userId OR c_m.id_manager = :userId) AND c.status " + conditionTrashed + " :statusValue) " +
						" order by c.startDate IS NULL DESC, c.startDate DESC, c.createdDate DESC LIMIT :start, :limit ";

				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setParameter("userId", userId);
				sqlQuery.setParameter("start", start);
				sqlQuery.setParameter("limit", limit);
				sqlQuery.setParameter("statusValue", CollectionStatus.TRASHED.ordinal());
				List<Object[]> ids = (List<Object[]>) sqlQuery.list();

				return ids != null ? ids : Collections.emptyList();
			}
		});

		//MEGHNA: To prevent multiple db calls, we get collection id and status from db and update AidrCollection status
		List<AidrCollection> result = new ArrayList<AidrCollection>();
		Integer id;
		for(Object[] col : collections)
		{			
			id = (Integer)col[0];
			AidrCollection collection =	this.findById(id);
			collection.setStatus(CollectionStatus.values()[(Integer)col[1]]);
			result.add(collection) ;
		}
		
		return result;
	}

	@Override
	public Integer getCollectionsCount(final UserEntity user, final boolean onlyTrashed) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Integer userId = user.getId();
				final String conditionTrashed;
				if (onlyTrashed) {
					conditionTrashed = "=";
				} else {
					conditionTrashed = "!=";
				}

				String sql = " select count(distinct c.id) " +
						" FROM AIDR_COLLECTION c " +
						" LEFT OUTER JOIN AIDR_COLLECTION_TO_MANAGER c_m " +
						" ON c.id = c_m.id_collection " +
						" WHERE (c.status " + conditionTrashed + " :statusValue and (c.user_id = :userId or c_m.id_manager = :userId)) ";

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
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
		criteria.add(Restrictions.eq("code", code));
		criteria.add(Restrictions.ne("status", CollectionStatus.TRASHED));
		AidrCollection collection = (AidrCollection) criteria.uniqueResult();
		return collection != null;
	}

	@Override
	public Boolean existName(String name) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.ne("status", CollectionStatus.TRASHED));
		AidrCollection collection = (AidrCollection) criteria.uniqueResult();
		return collection != null;
	}

	@Override
	public AidrCollection getRunningCollectionStatusByUser(Integer userId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
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
				Restrictions.eq("user.id", userId)
				);
		
		criteria.add(andAll);
		//criteria.add(Restrictions.ne("status", CollectionStatus.TRASHED));
		return (AidrCollection) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AidrCollection> getAllCollectionByUser(Integer userId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
		criteria.add(Restrictions.eq("user.id", userId));

		return (List<AidrCollection>) criteria.list();
	}

	@Override
	public List<AidrCollection> getRunningCollections() {
		return getRunningCollections(null, null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AidrCollection> getRunningCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) {
		Criteria criteriaIds = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
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

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.in("id", ids));
		searchCollectionsAddOrder(sortColumn, sortDirection, criteria);

		return (List<AidrCollection>) criteria.list();
	}

	@Override
	public Long getRunningCollectionsCount(String terms) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
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
	public List<AidrCollection> getStoppedCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) {
		Criteria criteriaIds = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
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

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.in("id", ids));
		searchCollectionsAddOrder(sortColumn, sortDirection, criteria);

		return (List<AidrCollection>) criteria.list();
	}

	@Override
	public Long getStoppedCollectionsCount(String terms) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
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

	@Override
	public AidrCollection findByCode(String code) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
		criteria.add(Restrictions.eq("code", code));
		try {
			return (AidrCollection) criteria.uniqueResult();
		} catch (HibernateException e) {
			logger.error("Hibernate exception while finding a collection by code: "+code + "/t"+e.getStackTrace());
			return null;
		}
	}

	@Override
	public AidrCollection trashCollectionById(Integer collectionId) {
		AidrCollection collection = stop(collectionId);
		collection.setStatus(CollectionStatus.TRASHED);
		this.update(collection);

		return collection;
	}

}
