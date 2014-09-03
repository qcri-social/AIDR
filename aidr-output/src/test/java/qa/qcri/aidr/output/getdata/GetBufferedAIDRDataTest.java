package qa.qcri.aidr.output.getdata;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.mock.web.MockServletContext;

public class GetBufferedAIDRDataTest {
	//
	static GetBufferedAIDRData getBufferedAIDRData;
	//
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
		//
		assertNotNull(statusString);
		assertTrue(statusString.contains("mock_collection"));
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
		//
		assertNotNull(statusString);
	}
	//
	@Test
	public void getBufferedAIDRDataTest() {
		String channelCode = "mock_collection";
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
		String queryString = "{\"queryType\":\"date_query\",\"comparator\":\"is_before\",\"timestamp\":1575339860}";
		String channelCode = "mock_collection";
		String callbackName = null;
		int count = 1;
		//System.out.println("masterCBManager: " + getBufferedAIDRData.masterCBManager);
		Response response = getBufferedAIDRData.getBufferedAIDRDataPostFilter(queryString, channelCode, callbackName, count);
		String statusString = (String)response.getEntity();
		//
		assertNotNull(statusString);
		assertEquals("[{}]", statusString);
	}
	//
	@Test
	public void isChannelPresentTest() {
		String channel = "aidr_predict.mock_collection";
		//System.out.println("masterCBManager: " + getBufferedAIDRData.masterCBManager);
		boolean result = getBufferedAIDRData.isChannelPresent(channel);
		//
		assertTrue(result);
	}
	//
	@Test
	public void restartFetchServiceTest() {
		String passcode = "sysadmin2013";
		Response clientResponse = getBufferedAIDRData.restartFetchService(passcode);
		String statusString = (String)clientResponse.getEntity();
		//
		assertNotNull(statusString);
		assertEquals("{\"aidr-output fetch service\":\"RESTARTED\"}", statusString);
	}
}
