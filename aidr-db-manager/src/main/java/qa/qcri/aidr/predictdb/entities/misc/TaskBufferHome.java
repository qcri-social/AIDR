// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0

package qa.qcri.aidr.predictdb.entities.misc;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class TaskBuffer.
 * @see .TaskBuffer
 * @author Hibernate Tools
 */
@Stateless
public class TaskBufferHome {

	private static final Log log = LogFactory.getLog(TaskBufferHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(TaskBuffer transientInstance) {
		log.debug("persisting TaskBuffer instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaskBuffer persistentInstance) {
		log.debug("removing TaskBuffer instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public TaskBuffer merge(TaskBuffer detachedInstance) {
		log.debug("merging TaskBuffer instance");
		try {
			TaskBuffer result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TaskBuffer findById(TaskBufferId id) {
		log.debug("getting TaskBuffer instance with id: " + id);
		try {
			TaskBuffer instance = entityManager.find(TaskBuffer.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
