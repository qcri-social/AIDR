/**
 * 
 */
package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.CollectionCollaborator;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.repository.CollectionCollaboratorRepository;
import qa.qcri.aidr.manager.util.CollectionStatus;

/**
 * @author Latika
 *
 */
@Repository
public class CollectionCollaboratorRepositoryImpl extends GenericRepositoryImpl<CollectionCollaborator, Serializable> implements CollectionCollaboratorRepository {


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<UserAccount> getCollaboratorsByCollection(Long collectionId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(CollectionCollaborator.class);
		criteria.add(Restrictions.eq("collection.id", collectionId));
		List<CollectionCollaborator> collections = (List<CollectionCollaborator>) criteria.list();
		
		List<UserAccount> collaborators = new ArrayList<UserAccount>();
		if(collections != null) {
			for(CollectionCollaborator collaborator : collections) {
				collaborators.add(collaborator.getAccount());
			}
		}
		return collaborators;
	}

	@Override
	public CollectionCollaborator findByCollaboratorIdAndCollectionId(
			Long userId, Long collectionId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(CollectionCollaborator.class);
		criteria.add(Restrictions.eq("collection.id", collectionId));
		criteria.add(Restrictions.eq("account.id", userId));
		CollectionCollaborator collectionCollaborator = (CollectionCollaborator) criteria.uniqueResult();
		
		return collectionCollaborator;
	}
	
	@Override
	public void save(CollectionCollaborator collectionCollaborator) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		collectionCollaborator.setUpdatedAt(now);
		collectionCollaborator.setCreatedAt(now);
		super.save(collectionCollaborator);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> getCollectionByCollaborator(Long userId, Integer start, Integer limit, boolean trashed) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(CollectionCollaborator.class);
		criteria.createAlias("collection", "col").setFetchMode("col", FetchMode.JOIN);
		criteria.add(Restrictions.eq("account.id", userId));
		
		if(trashed) {
			criteria.add(Restrictions.eq("col.status", CollectionStatus.TRASHED)); 
		} else {
			criteria.add(Restrictions.ne("col.status", CollectionStatus.TRASHED));
		}
		criteria.addOrder(Order.asc("col.status"));
		criteria.addOrder(Order.asc("col.createdAt"));
		if(start != null) {
			criteria.setFirstResult(start);
		}
		if(limit != null) {
			criteria.setMaxResults(limit);
		}
		List<CollectionCollaborator> collectionCollaborators = (List<CollectionCollaborator>) criteria.list();
		
		List<Collection> collections = new ArrayList<Collection>();
		if(collectionCollaborators != null) {
			for(CollectionCollaborator collaborator : collectionCollaborators) {
				collections.add(collaborator.getCollection());
			}
		}
		return collections;
	}

}
