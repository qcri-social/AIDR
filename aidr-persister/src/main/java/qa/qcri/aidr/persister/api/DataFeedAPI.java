package qa.qcri.aidr.persister.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import qa.qcri.aidr.entity.DataFeed;
import qa.qcri.aidr.service.DataFeedService;

@Path("datafeeds")
@Component
public class DataFeedAPI {
	
	@SuppressWarnings("resource")
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/{code}/by-confidence")
    public Response findbyCollectionCodeAndConfidence(@PathParam("code") String code, 
    		@QueryParam("confidence") double confidence, 
    		@QueryParam("offset") Integer offset,
    		@QueryParam("limit") Integer limit){
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/spring-servlet.xml");
        DataFeedService dataFeedService = (DataFeedService) appContext.getBean("dataFeedService");
		List<DataFeed> listOfdaDataFeeds = dataFeedService.findbyCollectionCodeAndConfidence(code, confidence, offset, limit);
		return Response.ok(getUIWrapper(listOfdaDataFeeds, "", true)).build();
    }
	
	@SuppressWarnings("resource")
	@RequestMapping(value = "/{code}")
    public Response findbyCollectionCode(@PathParam("code") String code, 
    		@QueryParam("offset") Integer offset,
    		@QueryParam("limit") Integer limit){
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/spring-servlet.xml");
        DataFeedService dataFeedService = (DataFeedService) appContext.getBean("dataFeedService");
		List<DataFeed> listOfdaDataFeeds = dataFeedService.findbyCollectionCode(code, offset, limit);
		return Response.ok(getUIWrapper(listOfdaDataFeeds, "", true)).build();
    }
	
	public Map<String, Object> getUIWrapper(List<DataFeed> listOfdaDataFeeds, String message, Boolean success) {
		Map<String, Object> modelMap = new HashMap<String, Object>(4);
		modelMap.put("data", listOfdaDataFeeds);
		modelMap.put("message", message);
		modelMap.put("success", success);		
		return modelMap;
	}
	
}
