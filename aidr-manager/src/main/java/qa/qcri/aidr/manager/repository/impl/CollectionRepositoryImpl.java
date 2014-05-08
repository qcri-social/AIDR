package qa.qcri.aidr.manager.repository.impl;

import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.util.CollectionStatus;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Repository("collectionRepository")
public class CollectionRepositoryImpl extends GenericRepositoryImpl<AidrCollection, Serializable> implements CollectionRepository{
    private Logger logger = Logger.getLogger(getClass());

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

              //  logger.info("start: " + start);
              //  logger.info("limit: " + limit);
               // logger.info("statusValue: " + statusValue.ordinal());
                String sql = " SELECT DISTINCT c.id FROM AIDR_COLLECTION c" +
                        " WHERE (c.publiclyListed = 1 and c.status = :statusValue) " +
                        " order by c.startDate DESC, c.createdDate DESC LIMIT :start, :limit ";


                SQLQuery sqlQuery = session.createSQLQuery(sql);
                sqlQuery.setParameter("start", start);
                sqlQuery.setParameter("limit", limit);
                sqlQuery.setParameter("statusValue", statusValue.ordinal());
                List<Integer> ids = (List<Integer>) sqlQuery.list();

                //logger.info("ids count: " + ids.size());
                return ids != null ? ids : Collections.emptyList();
            }
        });

        List<AidrCollection> a = new ArrayList<AidrCollection>();

        //logger.info("collectionIds: " + collectionIds.size());
        for(int i =0; i < collectionIds.size(); i++){
            AidrCollection collection =	this.findById(collectionIds.get(i));
            a.add(collection) ;
        }
        return a;

    }

    @SuppressWarnings("unchecked")
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
	public List<AidrCollection> getPaginatedData(final Integer start, final Integer limit, final UserEntity user) {
        final Integer userId = user.getId();

//        Workaround as criteria query gets result for different managers and in the end we get less then limit records.
        List<Integer> collectionIds = (List<Integer>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String sql = " SELECT DISTINCT c.id FROM AIDR_COLLECTION c " +
                        " LEFT OUTER JOIN AIDR_COLLECTION_TO_MANAGER c_m " +
                        " ON c.id = c_m.id_collection " +
                        " WHERE (c.user_id =:userId OR c_m.id_manager = :userId) " +
                        " order by c.startDate DESC, c.createdDate DESC LIMIT :start, :limit ";


                // query to get never started collection
                String sql2 = " SELECT DISTINCT c.id FROM AIDR_COLLECTION c " +
                        " LEFT OUTER JOIN AIDR_COLLECTION_TO_MANAGER c_m " +
                        " ON c.id = c_m.id_collection " +
                        " WHERE (c.startDate IS NULL and c.status != :statusValue AND (c.user_id =:userId OR c_m.id_manager = :userId)) " +
                        " order by c.createdDate DESC LIMIT :start, :limit ";

                SQLQuery sqlQuery = session.createSQLQuery(sql);
                sqlQuery.setParameter("userId", userId);
                sqlQuery.setParameter("start", start);
                sqlQuery.setParameter("limit", limit);
                //sqlQuery.setParameter("statusValue", CollectionStatus.TRASHED.ordinal());
                List<Integer> ids = (List<Integer>) sqlQuery.list();

                SQLQuery sqlQuery2 = session.createSQLQuery(sql2);
                sqlQuery2.setParameter("userId", userId);
                sqlQuery2.setParameter("start", start);
                sqlQuery2.setParameter("limit", limit);
                sqlQuery2.setParameter("statusValue", CollectionStatus.TRASHED.ordinal());
                List<Integer> ids2 = (List<Integer>) sqlQuery2.list();

                // first, remove duplicate
                if(!ids.isEmpty() && ids != null){
                    ids.removeAll(ids2) ;
                    // add
                    ids2.addAll(ids) ;
                }

                return ids2 != null ? ids2 : Collections.emptyList();
            }
        });

       // Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
       // criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
       // System.out.println("out");
        List<AidrCollection> a = new ArrayList<AidrCollection>();
        for(int i =0; i < collectionIds.size(); i++){
          //  System.out.println(collectionIds.get(i));
            AidrCollection collection =	this.findById(collectionIds.get(i));
            a.add(collection) ;
        }
        // by default, criteria sort is by id asc. so, change logic to get each aidr_collection
       // criteria.add(Restrictions.in("id", collectionIds));
		//criteria.addOrder(Order.desc("startDate"));
		//criteria.addOrder(Order.desc("createdDate"));
        //List<AidrCollection> a = (List<AidrCollection>) criteria.list();
        return a;
		//return (List<AidrCollection>) criteria.list();
	}

    @SuppressWarnings("unchecked")
    @Override
    public Integer getCollectionsCount(final UserEntity user) {
        return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Integer userId = user.getId();

                String sql = " select count(distinct c.id) " +
                        " FROM AIDR_COLLECTION c " +
                        " LEFT OUTER JOIN AIDR_COLLECTION_TO_MANAGER c_m " +
                        " ON c.id = c_m.id_collection " +
                        " WHERE (c.status != :statusValue and (c.user_id = :userId or c_m.id_manager = :userId)) ";

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
                criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("status", CollectionStatus.RUNNING));
		criteria.add(Restrictions.ne("status", CollectionStatus.TRASHED));
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
        Criteria criteriaIds = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
        criteriaIds.setProjection(Projections.projectionList()
                .add(Projections.property("id"), "id"));

        LogicalExpression or = Restrictions.or(
                Restrictions.eq("status", CollectionStatus.RUNNING),
                Restrictions.eq("status", CollectionStatus.RUNNING_WARNING)
        );

        LogicalExpression orAll = Restrictions.or(
                or,
                Restrictions.eq("status", CollectionStatus.INITIALIZING)
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

    @SuppressWarnings("unchecked")
    @Override
    public Long getRunningCollectionsCount(String terms) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
        criteria.setProjection(Projections.projectionList()
                .add(Projections.property("id"), "id"));

        LogicalExpression or = Restrictions.or(
                Restrictions.eq("status", CollectionStatus.RUNNING),
                Restrictions.eq("status", CollectionStatus.RUNNING_WARNING)
        );

        LogicalExpression orAll = Restrictions.or(
                or,
                Restrictions.eq("status", CollectionStatus.INITIALIZING)
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

        criteriaIds.add(Restrictions.ne("status", CollectionStatus.RUNNING));
        criteriaIds.add(Restrictions.ne("status", CollectionStatus.RUNNING_WARNING));
        criteriaIds.add(Restrictions.ne("status", CollectionStatus.INITIALIZING));
        criteriaIds.add(Restrictions.ne("status", CollectionStatus.TRASHED));
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

    @SuppressWarnings("unchecked")
    @Override
    public Long getStoppedCollectionsCount(String terms) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollection.class);
        criteria.setProjection(Projections.projectionList()
                .add(Projections.property("id"), "id"));

        criteria.add(Restrictions.ne("status", CollectionStatus.RUNNING));
        criteria.add(Restrictions.ne("status", CollectionStatus.RUNNING_WARNING));
        criteria.add(Restrictions.ne("status", CollectionStatus.INITIALIZING));
        criteria.add(Restrictions.ne("status", CollectionStatus.TRASHED));
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

	@Override
	public AidrCollection trashCollectionById(Integer collectionId) {
		AidrCollection collection = stop(collectionId);
		collection.setStatus(CollectionStatus.TRASHED);
		this.update(collection);
		
		// TODO: add code for modifying the aidr_predict tables 
		// @koushik: make REST call to aidr-tagger-api?
		return collection;
	}

}
