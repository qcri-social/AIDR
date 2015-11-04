/**
 * 
 */
package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.hibernateEntities.UserAccount;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.CollectionCollaborator;
import qa.qcri.aidr.manager.repository.CollectionCollaboratorRepository;

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

}
