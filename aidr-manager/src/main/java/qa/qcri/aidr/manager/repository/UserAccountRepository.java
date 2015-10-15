package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.List;

import qa.qcri.aidr.manager.hibernateEntities.UserAccount;

public interface UserAccountRepository extends GenericRepository<UserAccount, Serializable>{

	public UserAccount fetchByUsername(String username);

	public UserAccount getById(Long id);

    public List<UserAccount> getUsers(String query, Integer start, Integer limit);

    public Long getUsersCount(String query);
}
