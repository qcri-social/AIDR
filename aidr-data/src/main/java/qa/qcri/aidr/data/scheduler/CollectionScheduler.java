package qa.qcri.aidr.data.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.data.config.Configurations;
import qa.qcri.aidr.data.model.CollectionSummaryInfo;
import qa.qcri.aidr.data.service.CollectionSummaryService;

import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class CollectionScheduler {

	private Logger logger = Logger.getLogger(CollectionScheduler.class);
	
	@Autowired
	private CollectionSummaryService collectionSummaryService;
	
	@Scheduled(fixedDelay = 60 * 60 * 1000) // 10 minutes - in milliseconds
	private void scheduledTaskUpdateAidrData() {

		HttpURLConnection con = null;
		StringBuffer response = new StringBuffer();

        try {
        	
        	URL connectionURL = new URL(Configurations.getCollectionDataAPI());

			con = (HttpURLConnection) connectionURL.openConnection();

			if (HttpStatus.OK.value() != con.getResponseCode()) {
				throw new RuntimeException("Failed : HTTP error code : " + con.getResponseCode());
			}

			BufferedReader in = new BufferedReader(
                  new InputStreamReader(con.getInputStream(),"UTF-8"));
          
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
        
			in.close();
			
			ObjectMapper mapper = new ObjectMapper();
	    	CollectionSummaryInfo[] summaryInfos = mapper.readValue(response.toString(), CollectionSummaryInfo[].class);
	    	
	    	if(summaryInfos.length > 0) {
	    		collectionSummaryService.saveUpdateCollectionSummary(Arrays.asList(summaryInfos));
	    	} else {
	    		logger.info("No collection to update/insert");
	    	}
  	
        } catch (IOException e) {
			logger.warn("Error in fetching data from aidr server.", e);
		} catch (Exception e) {
			logger.warn("Error in fetching data from aidr server.", e);
		}
	}
	
}
