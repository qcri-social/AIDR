package qa.qcri.aidr.data.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import qa.qcri.aidr.data.persistence.entity.UserAccountActivity;

public interface UserAccountActivityRepository extends CrudRepository<UserAccountActivity, Long>{

	public List<UserAccountActivity> findByUserName(String userName);
	
	@Query("SELECT u FROM UserAccountActivity u where u.account.id = :id and u.activityDate >= :fromDate and u.activityDate < :toDate") 
	public List<UserAccountActivity> findByAccountIdandActivityDate(@Param("id") Long id, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

}
