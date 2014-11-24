// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Model.
 * @see .Model
 * @author Hibernate Tools
 */
@Stateless
public class ModelHome {

	private static final Log log = LogFactory.getLog(ModelHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Model transientInstance) {
		log.debug("persisting Model instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Model persistentInstance) {
		log.debug("removing Model instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Model merge(Model detachedInstance) {
		log.debug("merging Model instance");
		try {
			Model result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Model findById(Integer id) {
		log.debug("getting Model instance with id: " + id);
		try {
			Model instance = entityManager.find(Model.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
