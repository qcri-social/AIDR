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

import qa.qcri.aidr.service.DataFeedService;
import qa.qcri.aidr.utils.DataFeedInfo;

@Path("/collection")
@Component
public class DataFeedAPI {

	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path(value = "/{code}/feeds")
    public List<DataFeedInfo> findbyCollectionCode(@PathParam("code") String code, 
    		@QueryParam("offset") Integer offset,
    		@QueryParam("limit") Integer limit){
		
		offset = (offset != null) ? offset : 0;
		limit = (limit != null) ? limit :1500;
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/spring-servlet.xml");
        DataFeedService dataFeedService = (DataFeedService) appContext.getBean("dataFeedService");
        return dataFeedService.findbyCollectionCode(code, offset, limit);
    }
	
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/{code}/feeds/by-confidence")
    public List<DataFeedInfo> findbyCollectionCodeAndConfidence(@PathParam("code") String code, 
    		@QueryParam("confidence") Double confidence, 
    		@QueryParam("offset") Integer offset,
    		@QueryParam("limit") Integer limit){
		
		offset = (offset != null) ? offset : 0;
		limit = (limit != null) ? limit :1500;
		confidence = (confidence != null) ? confidence :0.5;
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/spring-servlet.xml");
        DataFeedService dataFeedService = (DataFeedService) appContext.getBean("dataFeedService");
		return dataFeedService.findbyCollectionCodeAndConfidence(code, confidence, offset, limit);
    }
	
}
