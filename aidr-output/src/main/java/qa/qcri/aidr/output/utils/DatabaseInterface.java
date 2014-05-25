package qa.qcri.aidr.output.utils;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.output.entity.AidrCollection;

import java.util.List;

import javax.persistence.EntityManager;

public interface DatabaseInterface {
	
	public Session getCurrentSession();
	
	public AidrCollection getById(Integer id);
	public AidrCollection getByCriteria(Criterion criterion);
	
	public List<AidrCollection> getAll();
	public List<AidrCollection> getAllByCriteria(Criterion criterion);
	
	public EntityManager getEntityManager();
}
	
