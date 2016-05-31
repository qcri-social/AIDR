package qa.qcri.aidr.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.dao.FacebookDataFeedDAO;
import qa.qcri.aidr.entity.FacebookDataFeed;

@Service
public class FacebookDataFeedService{

    protected static Logger logger = Logger.getLogger(FacebookDataFeedService.class);

    @Autowired
    FacebookDataFeedDAO facebookDataFeedDAO;

    @Transactional(readOnly = false)
    public Long persist(FacebookDataFeed facebookJson){
    	facebookJson.setUpdatedAt(new Date());
    	if(facebookJson.getId()==null){
    		facebookJson.setCreatedAt(facebookJson.getUpdatedAt());
    	}
    	Long fbDataFeedId = (Long) facebookDataFeedDAO.save(facebookJson);
    	return fbDataFeedId;
    }
    
}
