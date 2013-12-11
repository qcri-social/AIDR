package qa.qcri.aidr.manager.service;

import java.util.List;

import qa.qcri.aidr.manager.hibernateEntities.AidrMaster;

public interface MasterService {

	public List<AidrMaster> getAll() throws Exception;
	public void update(AidrMaster master) throws Exception;
	public void delete(AidrMaster master) throws Exception;
	public void create(AidrMaster master) throws Exception;
	public AidrMaster findById(Integer id) throws Exception; 
	public AidrMaster findByKey(String key) throws Exception; 
}
