package qa.qcri.aidr.manager.repository.impl;

import qa.qcri.aidr.manager.dto.CollectionLogDataResponse;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;
import qa.qcri.aidr.manager.repository.CollectionLogRepository;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository("collectionLogRepository")
public class CollectionLogRepositoryImpl extends GenericRepositoryImpl<AidrCollectionLog, Serializable> implements CollectionLogRepository {

    @SuppressWarnings("unchecked")
    @Override
    public CollectionLogDataResponse getPaginatedData(Integer start, Integer limit) {
        Criteria countCriteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollectionLog.class);
        countCriteria.setProjection(Projections.rowCount());
        Long count = (Long) countCriteria.uniqueResult();
        if (count == 0){
            return new CollectionLogDataResponse(Collections.<AidrCollectionLog>emptyList(), count);
        }

        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollectionLog.class);
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        criteria.addOrder(Order.desc("startDate"));

        return new CollectionLogDataResponse((List<AidrCollectionLog>) criteria.list(), count);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CollectionLogDataResponse getPaginatedDataForCollection(Integer start, Integer limit, Integer collectionId) {
        Criteria countCriteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollectionLog.class);
        countCriteria.add(Restrictions.eq("collectionID", collectionId));
        countCriteria.setProjection(Projections.rowCount());
        Long count = (Long) countCriteria.uniqueResult();
        if (count == 0){
            return new CollectionLogDataResponse(Collections.<AidrCollectionLog>emptyList(), count);
        }

        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrCollectionLog.class);
        criteria.add(Restrictions.eq("collectionID", collectionId));
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        criteria.addOrder(Order.desc("startDate"));

        return new CollectionLogDataResponse((List<AidrCollectionLog>) criteria.list(), count);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer countTotalDownloadedItemsForCollection(final Integer collectionId) {
        return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String sql = " select sum(c.count) from AIDR_COLLECTION_LOG c where c.collectionID = :collectionId ";
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                sqlQuery.setParameter("collectionId", collectionId);
                BigDecimal total = (BigDecimal) sqlQuery.uniqueResult();
                return total != null ? total.intValue() : 0;
            }
        });

    }

}