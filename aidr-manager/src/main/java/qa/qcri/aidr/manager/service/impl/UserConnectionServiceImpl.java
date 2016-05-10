package qa.qcri.aidr.manager.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.persistence.entities.UserConnection;
import qa.qcri.aidr.manager.repository.UserConnectionRepository;
import qa.qcri.aidr.manager.service.UserConnectionService;
import qa.qcri.aidr.manager.util.ConstantUtils;

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
    @Transactional
    public void update(UserConnection userConnection) {
        userConnectionRepository.update(userConnection);
    }

	@Override
	@Transactional(readOnly = true)
	public List<UserConnection> getByUserIdAndProviderUserId(String userId,
			String providerUserId) {
		return userConnectionRepository.getByUserIdAndProviderUserId(userId, providerUserId);
	}

	@Override
	@Transactional(readOnly = true)
	public UserConnection getByUserIdAndProviderId(String userId,
			String providerId) {
		return userConnectionRepository.getByUserIdAndProviderId(userId, providerId);
	}

	@Override
	@Transactional(readOnly = true)
	public UserConnection fetchByCombinedUserName(String userName) {
		String provider = userName.substring(0, userName.indexOf(ConstantUtils.USER_NAME_SPLITTER));
		userName = userName.substring(userName.indexOf(ConstantUtils.USER_NAME_SPLITTER)+1);
		return userConnectionRepository.getByUserIdAndProviderId(userName, provider);
	}
}
