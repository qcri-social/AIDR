/**
 * 
 */
package qa.qcri.aidr.manager.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.CollectionCollaborator;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.repository.CollectionCollaboratorRepository;
import qa.qcri.aidr.manager.service.CollectionCollaboratorService;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.UserService;

/**
 * @author Latika
 *
 */
@Service
public class CollectionCollaboratorServiceImpl implements CollectionCollaboratorService {

	private Logger logger = Logger.getLogger(CollectionCollaboratorServiceImpl.class);
	
	@Autowired
	private CollectionCollaboratorRepository collaboratorRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CollectionService collectionService;
	
	@Override
	public List<UserAccount> fetchCollaboratorsByCollection(Long collectionId) {
		return collaboratorRepository.getCollaboratorsByCollection(collectionId);
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean addCollaboratorToCollection(String collectionCode, Long userId) throws Exception {
		
        try {
			Collection collection = collectionService.findByCode(collectionCode);
			CollectionCollaborator collaborator = collaboratorRepository.findByCollaboratorIdAndCollectionId(userId, collection.getId());
			if(collaborator == null) {
				CollectionCollaborator collectionCollaborator = new CollectionCollaborator();
				collectionCollaborator.setAccount(userService.getById(userId));
				collectionCollaborator.setCollection(collection);
				collaboratorRepository.save(collectionCollaborator);
				return true;
			} 
		} catch (Exception e) {
			logger.error("Error in adding collaborator to collection", e);
			throw e;
		}
        
        return false;
        
	}

	@Override
	@Transactional(readOnly = false)
	public boolean removeCollaboratorFromCollection(Long collectionId,	Long userId) throws Exception {
		
		try {
			if(collectionId != null) {
				CollectionCollaborator collaborator = collaboratorRepository.findByCollaboratorIdAndCollectionId(userId, collectionId);
				if(collaborator != null) {
					collaboratorRepository.delete(collaborator);
				}
				return true;
			}
		
		} catch (Exception e) {
			logger.error("Error in adding collaborator to collection", e);
			throw e;
		}
		
		return false;
	}

	@Override
	public List<Collection> fetchCollectionsByCollaborator(Long userId, Integer start, Integer limit, boolean trashed) {
		return collaboratorRepository.getCollectionByCollaborator(userId, start, limit, trashed);	
	}

}
