// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class NominalAttributeDependentLabel.
 * @see .NominalAttributeDependentLabel
 * @author Hibernate Tools
 */
@Stateless
public class NominalAttributeDependentLabelHome {

	private static final Log log = LogFactory
			.getLog(NominalAttributeDependentLabelHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(NominalAttributeDependentLabel transientInstance) {
		log.debug("persisting NominalAttributeDependentLabel instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(NominalAttributeDependentLabel persistentInstance) {
		log.debug("removing NominalAttributeDependentLabel instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public NominalAttributeDependentLabel merge(
			NominalAttributeDependentLabel detachedInstance) {
		log.debug("merging NominalAttributeDependentLabel instance");
		try {
			NominalAttributeDependentLabel result = entityManager
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NominalAttributeDependentLabel findById(
			NominalAttributeDependentLabelId id) {
		log.debug("getting NominalAttributeDependentLabel instance with id: "
				+ id);
		try {
			NominalAttributeDependentLabel instance = entityManager.find(
					NominalAttributeDependentLabel.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
