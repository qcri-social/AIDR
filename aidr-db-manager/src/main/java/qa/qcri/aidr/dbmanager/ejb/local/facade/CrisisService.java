package qa.qcri.aidr.dbmanager.ejb.local.facade;


import java.util.List;

import javax.ejb.Local;


@Local
public interface CrisisService extends AbstractTaskManagerService<Crisis, Long> {

	Crisis findByCrisisID(Long id);
	List<Crisis> findByCriteria(String columnName, Long value);
	List<Crisis> findByCriteria(String columnName, String value);
}
