package qa.qcri.aidr.output.getdata;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.mock.web.MockServletContext;

public class TestGetBufferedAIDRData {
	static GetBufferedAIDRData getBufferedAIDRData;
	@BeforeClass
	public static void setUp() throws Exception {
		getBufferedAIDRData = new GetBufferedAIDRData();
		
		MockServletContext sc = new MockServletContext("");
		ServletContextEvent event = new ServletContextEvent(sc);
		
		//ServletContextEvent sce = new ServletContextEvent(null);
		getBufferedAIDRData.contextInitialized(event);
	}
	//
	@Test
	public void getActiveChannelsListTest() {
		Response response = getBufferedAIDRData.getActiveChannelsList();
		String statusString = (String)response.getEntity();
		System.out.println(statusString);
		assertNotNull(statusString);
		assertFalse(statusString.contains("mock_collection"+new Date()));
	}
	//
	@Test
	public void getLatestBufferedAIDRDataTest() {
		String callbackName = null;
		int count = 1;
		float confidence = (float) 0.7;
		boolean balanced_sampling = true;
		Response response = getBufferedAIDRData.getLatestBufferedAIDRData(callbackName, count, confidence, balanced_sampling);
		String statusString = (String)response.getEntity();
		assertNotNull(statusString);
	}
	//
	@Test
	public void getBufferedAIDRDataTest() {
		String channelCode = "mock_collection"+new Date();
		String callbackName = null;
		int count = 1;
		Response clientResponse = getBufferedAIDRData.getBufferedAIDRData(channelCode, callbackName, count);
		String statusString = (String)clientResponse.getEntity(); 
		//
		assertNotNull(statusString);
		assertEquals("[{}]", statusString);
	}
	//
	@Test
	public void getBufferedAIDRDataPostFilterTest() {

		String channelCode = null;
		String callbackName = null;
		int count = 1;
	
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 },"
		+ " { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 } ] }";
	
		Response response = getBufferedAIDRData.getBufferedAIDRDataPostFilter(queryString, channelCode, callbackName, count);
		String statusString = (String)response.getEntity();
		//
		assertNotNull(statusString);
		assertEquals("[{}]", statusString);
	
		queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1427375693 }, "
		+ "{ \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1427352427 }, "
		+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, "
		+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, "
		+ "{ \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
	
	
		//System.out.println("masterCBManager: " + getBufferedAIDRData.masterCBManager);
		response = getBufferedAIDRData.getBufferedAIDRDataPostFilter(queryString, channelCode, callbackName, count);
		statusString = (String)response.getEntity();
		//
		assertNotNull(statusString);
		assertEquals("[{}]", statusString);
	}
	//
	@Test
	public void isChannelPresentTest() {
		String channel = "aidr_predict.mock_collection"+new Date();
		//System.out.println("masterCBManager: " + getBufferedAIDRData.masterCBManager);
		boolean result = getBufferedAIDRData.isChannelPresent(channel);
		//
		assertFalse(result);
	}
	//
	@Test
	public void restartFetchServiceTest() {
		String passcode = "sysadmin2013";
		Response clientResponse = getBufferedAIDRData.restartFetchService(passcode);
		String statusString = (String)clientResponse.getEntity();
		assertNotNull(statusString);
		assertEquals("{\"aidr-output fetch service\":\"RESTARTED\"}", statusString);
	}
}
