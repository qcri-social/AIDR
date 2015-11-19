package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.persistence.entities.CrisisType;
import qa.qcri.aidr.manager.repository.CrisisTypeRepository;

@Repository
public class CrisisTypeRepositoryImpl extends GenericRepositoryImpl<CrisisType, Serializable> implements CrisisTypeRepository {
	
}
