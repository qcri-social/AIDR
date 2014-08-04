package qa.qcri.aidr.output.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.output.entity.AidrCollection;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface DatabaseInterface {
	
	public AidrCollection getById(Integer id);
	public AidrCollection getByCriteria(Criterion criterion);
	
	public List<AidrCollection> getAll();
	public List<AidrCollection> getAllByCriteria(Criterion criterion);
	
	public EntityManager getEntityManager();
	public Session getCurrentSession();
	public EntityManagerFactory getCurrentEMFactory();
	public int closeCurrentSession(Session session);
	public Map<String, Boolean> getPubliclyListed(Criterion criterion, Set<String> cbSet);
}
	