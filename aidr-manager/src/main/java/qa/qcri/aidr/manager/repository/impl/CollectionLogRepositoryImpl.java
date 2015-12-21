package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.dto.CollectionLogDataResponse;
import qa.qcri.aidr.manager.persistence.entities.CollectionLog;
import qa.qcri.aidr.manager.repository.CollectionLogRepository;

@Repository("collectionLogRepository")
public class CollectionLogRepositoryImpl extends GenericRepositoryImpl<CollectionLog, Serializable> implements CollectionLogRepository {

    @SuppressWarnings("unchecked")
    @Override
    public CollectionLogDataResponse getPaginatedData(Integer start, Integer limit) {
        Criteria countCriteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(CollectionLog.class);
        countCriteria.setProjection(Projections.rowCount());
        Long count = (Long) countCriteria.uniqueResult();
        if (count == 0){
            return new CollectionLogDataResponse(Collections.<CollectionLog>emptyList(), count);
        }

        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(CollectionLog.class);
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        criteria.addOrder(Order.desc("startDate"));

        return new CollectionLogDataResponse((List<CollectionLog>) criteria.list(), count);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CollectionLogDataResponse getPaginatedDataForCollection(Integer start, Integer limit, Long collectionId) {
        Criteria countCriteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(CollectionLog.class);
        countCriteria.add(Restrictions.eq("collectionId", collectionId));
        countCriteria.setProjection(Projections.rowCount());
        Long count = (Long) countCriteria.uniqueResult();
        if (count == 0){
            return new CollectionLogDataResponse(Collections.<CollectionLog>emptyList(), count);
        }

        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(CollectionLog.class);
        criteria.add(Restrictions.eq("collectionId", collectionId));
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        criteria.addOrder(Order.desc("startDate"));

        return new CollectionLogDataResponse((List<CollectionLog>) criteria.list(), count);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer countTotalDownloadedItemsForCollection(final Long collectionId) {
        return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                String sql = " select sum(c.count) from collection_log c where c.collection_id = :collectionId ";
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                sqlQuery.setParameter("collectionId", collectionId);
                BigDecimal total = (BigDecimal) sqlQuery.uniqueResult();
                return total != null ? total.intValue() : 0;
            }
        });
    }
        
    @SuppressWarnings("unchecked")
    @Override
    public Map<Integer, Integer> countTotalDownloadedItemsForCollectionIds(final List<Long> ids) {
        return (Map<Integer, Integer>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                String sql = " select c.collection_id as id, " +
                        " sum(c.count) as count " +
                        " from collection_log c " +
                        " where c.collection_id in :ids " +
                        " group by c.collection_id ";
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                sqlQuery.addScalar("id", new IntegerType());
                sqlQuery.addScalar("count", new IntegerType());
                sqlQuery.setParameterList("ids", ids);

                List<Object[]> list = sqlQuery.list();
                Map<Integer, Integer> result = new HashMap<Integer, Integer>();
                for (Object[] row : list) {
                    result.put((Integer) row[0], (Integer) row[1]);
                }

                return result;
            }
        });
    }

    @SuppressWarnings("unchecked")
	@Override
	public Integer countLogsStartedInInterval(final Long collectionId, final Date startDate, final Date endDate) {
    	 return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
             @Override
             public Object doInHibernate(Session session) throws HibernateException {
                 String sql = " select count(*) from collection_log c where c.collection_id = :collectionId and start_date = :startDate ";
                 SQLQuery sqlQuery = session.createSQLQuery(sql);
                 sqlQuery.setParameter("collectionId", collectionId);
                 sqlQuery.setParameter("startDate", startDate);
                 BigInteger total = (BigInteger) sqlQuery.uniqueResult();
                 return total != null ? total.intValue() : 0;
             }
         });
	}
    
    @Override
    public void save(CollectionLog collectionLog) {

		Timestamp now = new Timestamp(System.currentTimeMillis());
		collectionLog.setCreatedAt(now);
    	collectionLog.setUpdatedAt(now);
    	super.save(collectionLog);
    }
}