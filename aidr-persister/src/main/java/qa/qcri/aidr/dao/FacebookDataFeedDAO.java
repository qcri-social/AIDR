package qa.qcri.aidr.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.entity.FacebookDataFeed;

@Repository
public class FacebookDataFeedDAO extends AbstractDao<FacebookDataFeed, Long> {
	
	private final Logger logger = Logger.getLogger(DataFeedDAO.class);
	
	protected FacebookDataFeedDAO() {
		super(FacebookDataFeed.class);
	}
}
