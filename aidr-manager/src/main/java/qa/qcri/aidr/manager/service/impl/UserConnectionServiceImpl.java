package qa.qcri.aidr.manager.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.hibernateEntities.UserConnection;
import qa.qcri.aidr.manager.repository.UserConnectionRepository;
import qa.qcri.aidr.manager.service.UserConnectionService;

import java.util.List;

@Service("userConnectionService")
public class UserConnectionServiceImpl implements UserConnectionService{

	@Inject
	private UserConnectionRepository userConnectionRepository;
	
	@Override
	@Transactional(readOnly=false)
	public void register(UserConnection userConnection) {
		userConnectionRepository.save(userConnection);
	}

    @Override
    @Transactional(readOnly = true)
    public List<UserConnection> getByUserId(String userId) {
        return userConnectionRepository.getByUserId(userId);
    }

    @Override
    @Transactional
    public void update(UserConnection userConnection) {
        userConnectionRepository.update(userConnection);
    }
}
