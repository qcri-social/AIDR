package qa.qcri.aidr.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.hibernateEntities.AidrMaster;
import qa.qcri.aidr.manager.repository.MasterRepository;
import qa.qcri.aidr.manager.service.MasterService;

@Transactional(readOnly=false)
@Service("masterService")
public class MasterServiceImpl implements MasterService {

	@Autowired
	private MasterRepository masterRepository ;

	@Override
	@Transactional(readOnly=true)
	public List<AidrMaster> getAll() throws Exception {
		return masterRepository.findAll();
	}

	@Override
	public void update(AidrMaster master) throws Exception {
		masterRepository.update(master);
	}

	@Override
	public void delete(AidrMaster master) throws Exception {
       masterRepository.delete(master);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_USER_TWITTER')")
	public void create(AidrMaster master) throws Exception {
		masterRepository.save(master);
	}

	@Override
	@Transactional(readOnly=true)
	public AidrMaster findById(Integer id) throws Exception {
		return masterRepository.findById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public AidrMaster findByKey(String key) throws Exception {
		return masterRepository.findByKey(key);
	}

}
