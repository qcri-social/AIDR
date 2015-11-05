/**
 * 
 */
package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.List;

import qa.qcri.aidr.manager.hibernateEntities.UserAccount;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.CollectionCollaborator;

/**
 * @author Latika
 *
 */
public interface CollectionCollaboratorRepository extends GenericRepository<CollectionCollaborator, Serializable>{
	List<UserAccount> getCollaboratorsByCollection(Long collectionId);
	CollectionCollaborator findByCollaboratorIdAndCollectionId(Long userId, Long collectionId);
	List<Collection> getCollectionByCollaborator(Long userId, Integer start, Integer limit, boolean trashed);
}
