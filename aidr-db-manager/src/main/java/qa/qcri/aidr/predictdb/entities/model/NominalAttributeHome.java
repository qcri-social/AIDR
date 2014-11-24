// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class NominalAttribute.
 * @see .NominalAttribute
 * @author Hibernate Tools
 */
@Stateless
public class NominalAttributeHome {

	private static final Log log = LogFactory
			.getLog(NominalAttributeHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(NominalAttribute transientInstance) {
		log.debug("persisting NominalAttribute instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(NominalAttribute persistentInstance) {
		log.debug("removing NominalAttribute instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public NominalAttribute merge(NominalAttribute detachedInstance) {
		log.debug("merging NominalAttribute instance");
		try {
			NominalAttribute result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NominalAttribute findById(Integer id) {
		log.debug("getting NominalAttribute instance with id: " + id);
		try {
			NominalAttribute instance = entityManager.find(
					NominalAttribute.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
