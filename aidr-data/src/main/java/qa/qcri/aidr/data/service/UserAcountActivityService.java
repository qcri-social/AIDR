package qa.qcri.aidr.data.service;

import java.util.Date;
import java.util.List;

import qa.qcri.aidr.data.persistence.entity.UserAccountActivity;

public interface UserAcountActivityService {

	public void save(UserAccountActivity userAccountActivity);
	public List<UserAccountActivity> fetchByUserName(String username);
	public List<UserAccountActivity> findByAccountIdandActivityDate(Long id, Date fromDate, Date toDate);

}
