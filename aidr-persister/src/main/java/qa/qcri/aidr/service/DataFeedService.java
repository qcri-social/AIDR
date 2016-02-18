package qa.qcri.aidr.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.dao.DataFeedDAO;
import qa.qcri.aidr.entity.DataFeed;
import qa.qcri.aidr.utils.DataFeedInfo;

@Service
@Transactional(readOnly = false)
public class DataFeedService{

    protected static Logger logger = Logger.getLogger(DataFeedService.class);

    @Autowired
    DataFeedDAO dataFeedDAO;
    
    public void persist(DataFeed twitterJson){
    	twitterJson.setUpdatedAt(new Date());
    	if(twitterJson.getId()==null){
    		twitterJson.setCreatedAt(twitterJson.getUpdatedAt());
    	}
    	dataFeedDAO.save(twitterJson);
    	logger.info("Data saved");
    }
    
    public List<DataFeedInfo> findbyCollectionCode(String code, Integer offset, Integer limit ) {
    	return dataFeedDAO.getAllDataFeedsByCode(code, offset, limit);
    }
    
    public List<DataFeedInfo> findbyCollectionCodeAndConfidence(String code, double confidence, Integer offset, Integer limit) {
    	return dataFeedDAO.getAllDataFeedsByCodeAndConfidence(code, confidence, offset, limit);
    }
}
