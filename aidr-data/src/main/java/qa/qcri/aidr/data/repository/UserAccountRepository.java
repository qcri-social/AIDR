package qa.qcri.aidr.data.repository;

import org.springframework.data.repository.CrudRepository;

import qa.qcri.aidr.data.persistence.entity.UserAccount;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long>{

	public UserAccount findByUserName(String userName);

}
