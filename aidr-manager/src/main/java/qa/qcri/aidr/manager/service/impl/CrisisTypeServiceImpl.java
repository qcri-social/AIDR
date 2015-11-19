package qa.qcri.aidr.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.manager.persistence.entities.CrisisType;
import qa.qcri.aidr.manager.repository.CrisisTypeRepository;
import qa.qcri.aidr.manager.service.CrisisTypeService;

@Service
public class CrisisTypeServiceImpl implements CrisisTypeService {
	
	@Autowired
	CrisisTypeRepository crisisTypeRepository;
	
	public CrisisType getById(Long id) {
		return crisisTypeRepository.findById(id);
	}
}
