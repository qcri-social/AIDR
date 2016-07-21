package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import qa.qcri.aidr.common.values.UsageType;
import qa.qcri.aidr.manager.dto.CollectionSummaryInfo;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.service.CollectionLogService;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.util.CollectionStatus;
import qa.qcri.aidr.manager.util.CollectionType;

@Repository("collectionRepository")
@Transactional
public class CollectionRepositoryImpl extends GenericRepositoryImpl<Collection, Serializable> implements CollectionRepository{
	private final Logger logger = Logger.getLogger(CollectionRepositoryImpl.class);
	
	@Autowired
	private CollectionLogService collectionLogService;
	
	@Autowired
	private TaggerService taggerService;

	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> searchByName(String query, Long userId) throws Exception {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.add(Restrictions.ilike("name", query, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("owner.id", userId));
		return criteria.list();
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
				List<BigInteger> ids = sqlQuery.list();

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

	@Override
	public Long getTotalCollectionsCount() {
		return (Long) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {

				String sql = " SELECT count(distinct c.id) FROM collection c";

				SQLQuery sqlQuery = session.createSQLQuery(sql);
				BigInteger total = (BigInteger) sqlQuery.uniqueResult();
				return total != null ? total.longValue() : 0;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> getPaginatedData(final Integer start, final Integer limit, final UserAccount user, final boolean onlyTrashed) {
		
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
				List<Object[]> ids = sqlQuery.list();

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

		return criteria.list();
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

		List<Integer> ids = criteriaIds.list();

		if (ids.size() == 0){
			return Collections.emptyList();
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.in("id", ids));
		searchCollectionsAddOrder(sortColumn, sortDirection, criteria);

		return criteria.list();
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

		List<Integer> ids = criteriaIds.list();

		if (ids.size() == 0){
			return Collections.emptyList();
		}

		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.in("id", ids));
		searchCollectionsAddOrder(sortColumn, sortDirection, criteria);

		return criteria.list();
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
	public List<Collection> getAllCollectionsByUsage(UsageType usageType) {
	
		List<Collection> collections = new ArrayList<Collection>();

		try {
			Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
			criteria.add(Restrictions.eq("usageType", usageType));
			collections = criteria.list();
			
		} catch (HibernateException e) {
			logger.error("Exception in fetching list of collections.", e);
		}
		
		return collections;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CollectionSummaryInfo> getAllCollectionForAidrData() {
		List<CollectionSummaryInfo> listOfCollectionSummaryInfos = new ArrayList<>();
		Map<Long, Long> humanTagCountMap = new HashMap<>();
		
		List<Object[]> humangTagCounts = (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				String sql = "SELECT c.id, COALESCE(count(1),0) as humantagcount"
								+" FROM collection c, document d, document_nominal_label dnl"
								+" WHERE d.documentID = dnl.documentID AND d.crisisID = c.id"
								+" group by c.id;";
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				List<Object[]> data = sqlQuery.list();
				return data != null ? data : Collections.emptyList();
			}
		});
		for (Object[] objects : humangTagCounts) {
			Long collectionId = ((BigInteger) objects[0]).longValue();
			Long humanTagCount = ((BigInteger) objects[1]).longValue();
			humanTagCountMap.put(collectionId, humanTagCount);
		}
		
		List<Object[]> collections = (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				String sql = "SELECT" 
							 	+" c.id, c.code, c.name, c.start_date, c.end_date, c.created_at, c.count, c.status, c.lang_filters, c.track, c.geo, c.publicly_listed, c.provider,"
								+" SUM(aa.classify) AS machinetagcount,"
							    +" account.user_name,"
							    +" ct.name as crisis_type"
							+" FROM"
							    +" account,"
							    +" collection c"
							    +" LEFT JOIN"
							    +" (SELECT "
							        +" mnl.classifiedDocumentCount AS classify, mf.crisisID AS dd"
							    +" FROM"
							        +" model_family mf, model m, model_nominal_label mnl"
							    +" WHERE"
							        +" mf.modelFamilyID = m.modelFamilyID"
							            +" AND m.modelID = mnl.modelID) AS aa ON c.id = aa.dd,"
							     +" collection c2 LEFT JOIN crisis_type ct ON c2.crisis_type = ct.id" 
							+" WHERE"
							    +" account.id = c.owner_id"
							    +" and c2.id = c.id"
							+" GROUP BY c.id;";
				
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				List<Object[]> data = sqlQuery.list();
				return data != null ? data : Collections.emptyList();
			}
		});
		CollectionStatus[] collectionStatus = CollectionStatus.values();
		for (Object[] objects : collections) {
			CollectionSummaryInfo collectionSummaryInfo = new CollectionSummaryInfo();
			Long collectionId = ((BigInteger) objects[0]).longValue();
			collectionSummaryInfo.setCode((String) objects[1]);
			collectionSummaryInfo.setName((String) objects[2]);
			collectionSummaryInfo.setStartDate((Date) objects[3]);
			collectionSummaryInfo.setEndDate((Date) objects[4]);
			collectionSummaryInfo.setCollectionCreationDate((Date) objects[5]);
			try {
				collectionSummaryInfo.setTotalCount(collectionLogService.countTotalDownloadedItemsForCollection(collectionId));
			} catch (Exception e) {
				logger.warn("Error in fetch count from collection log.", e);
				collectionSummaryInfo.setTotalCount((Integer) objects[6]);
			}
			collectionSummaryInfo.setStatus(collectionStatus[(Integer) objects[7]].getStatus());
			collectionSummaryInfo.setLanguage((String) objects[8]);
			collectionSummaryInfo.setKeywords((String) objects[9]);
			collectionSummaryInfo.setGeo((String) objects[10]);
			collectionSummaryInfo.setLabelCount(taggerService.getLabelCount(collectionId));
			collectionSummaryInfo.setPubliclyListed((Boolean) objects[11]);
			collectionSummaryInfo.setProvider((String) objects[12]);
			if(objects[13] == null) objects[13] = new BigDecimal(0);
			collectionSummaryInfo.setMachineTagCount(((BigDecimal) objects[13]).longValue());
			collectionSummaryInfo.setCurator((String) objects[14]);
			collectionSummaryInfo.setCrisisType((String) objects[15]);
			Long humanTagCount = humanTagCountMap.get(collectionId) != null ? humanTagCountMap.get(collectionId) : 0; 
			collectionSummaryInfo.setHumanTaggedCount(humanTagCount);
			
			listOfCollectionSummaryInfos.add(collectionSummaryInfo);
		}
		return listOfCollectionSummaryInfos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> findMicromappersFilteredCollections(boolean micromappersEnabled) {
		
		List<Collection> collections = new ArrayList<Collection>();

		try {
			Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
			criteria.add(Restrictions.eq("micromappersEnabled", micromappersEnabled));
			criteria.add(Restrictions.eq("provider", CollectionType.Twitter));
			collections = criteria.list();
		} catch (HibernateException e) {
			logger.error("Exception in fetching list of collections.", e);
		}
		
		return collections;
	}

	@Override
	public List<String> getEligibleFacebookCollectionsToReRun() {
		
		List<String> collectionCodes = new ArrayList<String>(); 
		@SuppressWarnings("unchecked")
		List<Object[]> collections = (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				String sql = " SELECT c.code FROM collection c " +
						" WHERE c.provider = 'Facebook' AND (c.status = 0 OR c.status = 2 OR c.status = 5 OR c.status = 8) "
						+ "AND date_add(c.last_execution_time, interval c.fetch_interval hour) <= now()";

				SQLQuery sqlQuery = session.createSQLQuery(sql);
				List<Object[]> codes = sqlQuery.list();

				return codes != null ? codes : Collections.emptyList();
			}
		});

		if(collections != null && collections.size() > 0) {
			for(Object col : collections) {
				collectionCodes.add((String) col);
			}
		}
		return collectionCodes;
	}
	
	@Override
	public List<Collection> getUnexpectedlyStoppedCollections(Date today) {
		
		List<Collection> collections = new ArrayList<Collection>();

		try {
			Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Collection.class);
			criteria.add(Restrictions.gt("updatedAt", today))
					.add(Restrictions.geProperty("startDate", "endDate"));
			collections = criteria.list();
			
		} catch (HibernateException e) {
			logger.error("Exception in fetching list of collections.", e);
		}
		
		return collections;
	}
}
