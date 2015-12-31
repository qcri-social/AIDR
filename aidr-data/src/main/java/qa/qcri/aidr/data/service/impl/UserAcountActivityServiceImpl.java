package qa.qcri.aidr.data.service.impl;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.data.persistence.entity.UserAccountActivity;
import qa.qcri.aidr.data.repository.UserAccountActivityRepository;
import qa.qcri.aidr.data.repository.UserAccountRepository;
import qa.qcri.aidr.data.repository.UserAccountRoleRepository;
import qa.qcri.aidr.data.service.UserAcountActivityService;


@Service("userAccountActivityService")
public class UserAcountActivityServiceImpl implements UserAcountActivityService{

	@Autowired
	private UserAccountRepository userRepository;
	
	@Autowired
    private UserAccountRoleRepository userRoleRepository;
	
	@Autowired
    private UserAccountActivityRepository userAccountActivityRepository;
    
	@Override
	@Transactional(readOnly=false)
	public void save(UserAccountActivity userAccountActivity) {
		userAccountActivityRepository.save(userAccountActivity);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<UserAccountActivity> fetchByUserName(String username) {
		return userAccountActivityRepository.findByUserName(username);
	}
	
	@Override
	public List<UserAccountActivity> findByAccountIdandActivityDate(Long id, Date fromDate, Date toDate) {
		return userAccountActivityRepository.findByAccountIdandActivityDate(id, fromDate, toDate);
	}
		
}
