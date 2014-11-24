// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.misc;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class CustomUiTemplate.
 * @see .CustomUiTemplate
 * @author Hibernate Tools
 */
@Stateless
public class CustomUiTemplateHome {

	private static final Log log = LogFactory
			.getLog(CustomUiTemplateHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(CustomUiTemplate transientInstance) {
		log.debug("persisting CustomUiTemplate instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(CustomUiTemplate persistentInstance) {
		log.debug("removing CustomUiTemplate instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public CustomUiTemplate merge(CustomUiTemplate detachedInstance) {
		log.debug("merging CustomUiTemplate instance");
		try {
			CustomUiTemplate result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CustomUiTemplate findById(Long id) {
		log.debug("getting CustomUiTemplate instance with id: " + id);
		try {
			CustomUiTemplate instance = entityManager.find(
					CustomUiTemplate.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
