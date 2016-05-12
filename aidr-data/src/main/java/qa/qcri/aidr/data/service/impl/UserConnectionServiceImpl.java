package qa.qcri.aidr.data.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.data.persistence.entity.UserConnection;
import qa.qcri.aidr.data.repository.UserConnectionRepository;
import qa.qcri.aidr.data.service.UserConnectionService;

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
    public List<UserConnection> getByProviderIdAndUserId (String providerId, String userId) {
        return userConnectionRepository.findByProviderIdAndUserId(providerId, userId);
    }

    @Override
    @Transactional
    public void update(UserConnection userConnection) {
        userConnectionRepository.save(userConnection);
    }
}
