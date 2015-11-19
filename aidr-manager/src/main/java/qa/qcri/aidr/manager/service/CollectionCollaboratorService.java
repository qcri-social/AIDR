/**
 * 
 */
package qa.qcri.aidr.manager.service;

import java.util.List;

import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;

/**
 * @author Latika
 *
 */
public interface CollectionCollaboratorService {
	List<UserAccount> fetchCollaboratorsByCollection(Long collectionId);
	List<Collection> fetchCollectionsByCollaborator(Long userId, Integer start, Integer limit, boolean trashed);
	boolean addCollaboratorToCollection(String collectionCode, Long userId) throws Exception;
	boolean removeCollaboratorFromCollection(Long collectionId, Long userId) throws Exception;
}
