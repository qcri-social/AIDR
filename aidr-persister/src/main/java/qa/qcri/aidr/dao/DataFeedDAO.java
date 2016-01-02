package qa.qcri.aidr.dao;

import org.springframework.stereotype.Repository;

import qa.qcri.aidr.entity.DataFeed;

@Repository
public class DataFeedDAO extends AbstractDao<DataFeed, Long> {

	protected DataFeedDAO() {
		super(DataFeed.class);
	}

}
