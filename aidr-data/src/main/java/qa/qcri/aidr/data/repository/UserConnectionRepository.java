package qa.qcri.aidr.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.data.persistence.entity.UserConnection;

@Repository
public interface UserConnectionRepository extends CrudRepository<UserConnection, Long>{

	List<UserConnection> findByProviderIdAndUserId(String providerId, String userId);


}
