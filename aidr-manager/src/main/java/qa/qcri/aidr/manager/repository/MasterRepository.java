package qa.qcri.aidr.manager.repository;

import java.io.Serializable;

import qa.qcri.aidr.manager.hibernateEntities.AidrMaster;

public interface MasterRepository extends GenericRepository<AidrMaster, Serializable>{

	public AidrMaster findByKey(String key);
}
