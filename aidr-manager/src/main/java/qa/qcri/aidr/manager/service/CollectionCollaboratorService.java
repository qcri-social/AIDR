/**
 * 
 */
package qa.qcri.aidr.manager.service;

import java.util.List;

import qa.qcri.aidr.manager.hibernateEntities.UserAccount;

/**
 * @author Latika
 *
 */
public interface CollectionCollaboratorService {
	List<UserAccount> fetchCollaboratorsByCollection(Long collectionId);
	boolean addCollaboratorToCollection(String collectionCode, Long userId) throws Exception;
	boolean removeCollaboratorFromCollection(Long collectionId, Long userId) throws Exception;
}
