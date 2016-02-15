package qa.qcri.aidr.persister.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import qa.qcri.aidr.entity.DataFeed;
import qa.qcri.aidr.service.DataFeedService;

@Path("datafeeds")
@Component
public class DataFeedAPI {
	
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/{code}/confidence")
    public String findbyCollectionCodeAndConfidence(@PathParam("code") String code, 
    		@QueryParam("confidence") double confidence, 
    		@QueryParam("offset") double offset,
    		@QueryParam("limit") double limit){
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/spring-servlet.xml");
        DataFeedService dataFeedService = (DataFeedService) appContext.getBean("dataFeedService");
		List<DataFeed> listOfdaDataFeeds = dataFeedService.findbyCollectionCodeAndConfidence(code, confidence, offset, limit);
    	return "HelloWorld";
    }
	
	@RequestMapping(value = "/{code}")
    public String findbyCollectionCode(@PathParam("code") String code, 
    		@QueryParam("offset") double offset,
    		@QueryParam("limit") double limit){
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/spring-servlet.xml");
        DataFeedService dataFeedService = (DataFeedService) appContext.getBean("dataFeedService");
		List<DataFeed> listOfdaDataFeeds = dataFeedService.findbyCollectionCode(code, offset, limit);
    	return "HelloWorld";
    }
	
}
