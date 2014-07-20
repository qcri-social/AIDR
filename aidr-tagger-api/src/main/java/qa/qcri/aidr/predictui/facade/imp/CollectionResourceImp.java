/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import qa.qcri.aidr.predictui.entities.AidrCollection;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.facade.CollectionResourceFacade;

/**
 *
 * @author Imran
 */
@Stateless
public class CollectionResourceImp implements CollectionResourceFacade {

	@PersistenceContext(unitName = "qa.qcri.aidr.collectorManager-PU")
	private EntityManager em;

	public List<AidrCollection> getAllRunningCollectionsByUserID(int userID) {
		List<AidrCollection> collections = new ArrayList<AidrCollection>();
		Query q = em.createNativeQuery("SELECT id, code, name, user_id "
				+ " FROM AIDR_COLLECTION"
				+ " WHERE user_id = :user_id AND status = 0");    
		q.setParameter("user_id", + userID );
		try {
			List<Object[]> queryRes = q.getResultList();
			AidrCollection collection;
			for (Object[] row : queryRes) {
				collection =  new AidrCollection();
				collection.setId(( (Integer)row[0]).intValue());
				collection.setCode((String) row[1]);
				collection.setName((String) row[2]);
				collection.setUserID(( (Integer)row[3]).intValue());
				collections.add(collection);
			}

			return collections;
		} catch (NoResultException e) {
			return null;
		}
	}
}
