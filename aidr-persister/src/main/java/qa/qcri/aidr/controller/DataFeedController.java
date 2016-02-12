package qa.qcri.aidr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.entity.DataFeed;
import qa.qcri.aidr.service.DataFeedService;

@RestController
public class DataFeedController {
	
	@Autowired
	private DataFeedService dataFeedService;

	@RequestMapping(value = "/datafeeds_by_confidence/{code}")
    public String findbyCollectionCodeAndConfidence(@PathVariable("code") String code, 
    		@RequestParam("confidence") double confidence, 
    		@RequestParam("offset") double offset,
    		@RequestParam("limit") double limit){
		List<DataFeed> listOfdaDataFeeds = dataFeedService.findbyCollectionCodeAndConfidence(code, confidence, offset, limit);
    	return "HelloWorld";
    }
	
	@RequestMapping(value = "/datafeeds/{code}")
    public String findbyCollectionCode(@PathVariable("code") String code, 
    		@RequestParam("confidence") double offset,
    		@RequestParam("confidence") double limit){
		List<DataFeed> listOfdaDataFeeds = dataFeedService.findbyCollectionCode(code, offset, limit);
    	return "HelloWorld";
    }
	
}
