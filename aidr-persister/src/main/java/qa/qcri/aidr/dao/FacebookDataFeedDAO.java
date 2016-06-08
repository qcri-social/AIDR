package qa.qcri.aidr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.entity.FacebookDataFeed;

@Repository
public class FacebookDataFeedDAO extends AbstractDao<FacebookDataFeed, Long> {
	
	private final Logger logger = Logger.getLogger(DataFeedDAO.class);
	
	protected FacebookDataFeedDAO() {
		super(FacebookDataFeed.class);
	}

	public List<FacebookDataFeed> getAllDataFeedsByCode(String code, Integer exportLimit) {
		Criteria criteria = getCurrentSession().createCriteria(FacebookDataFeed.class);
        criteria.add(Restrictions.eq("code", code));
        criteria.setMaxResults(exportLimit);
		return criteria.list();
	}
}
