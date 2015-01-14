package qa.qcri.aidr.collector.api;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.ResponseWrapper;
import qa.qcri.aidr.collector.utils.ConfigProperties;
import twitter4j.FilterQuery;

public class TwitterCollectorAPIRequestValidationTest {
	
	private TwitterCollectorAPI instance;

	@Before
	public void setUp() throws Exception {
		instance = new TwitterCollectorAPI();
	}

	@Test
	public void testStartTask() {
		Response r1 = instance.startTask(new CollectionTask());
		ResponseWrapper w1 = (ResponseWrapper) r1.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_ERROR"), w1.getStatusCode());

		CollectionTask t2 = new CollectionTask();
		t2.setToTrack("earthquake");
		Response r2 = instance.startTask(t2);
		ResponseWrapper w2 = (ResponseWrapper) r2.getEntity();
		assertEquals(ConfigProperties.getProperty("STATUS_CODE_COLLECTION_ERROR"), w2.getStatusCode());
	}

}
