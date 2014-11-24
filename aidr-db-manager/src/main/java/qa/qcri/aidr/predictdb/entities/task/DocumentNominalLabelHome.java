// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.task;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class DocumentNominalLabel.
 * @see .DocumentNominalLabel
 * @author Hibernate Tools
 */
@Stateless
public class DocumentNominalLabelHome {

	private static final Log log = LogFactory
			.getLog(DocumentNominalLabelHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(DocumentNominalLabel transientInstance) {
		log.debug("persisting DocumentNominalLabel instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(DocumentNominalLabel persistentInstance) {
		log.debug("removing DocumentNominalLabel instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public DocumentNominalLabel merge(DocumentNominalLabel detachedInstance) {
		log.debug("merging DocumentNominalLabel instance");
		try {
			DocumentNominalLabel result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DocumentNominalLabel findById(DocumentNominalLabelId id) {
		log.debug("getting DocumentNominalLabel instance with id: " + id);
		try {
			DocumentNominalLabel instance = entityManager.find(
					DocumentNominalLabel.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
