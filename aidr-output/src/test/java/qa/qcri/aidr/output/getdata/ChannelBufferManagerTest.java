package qa.qcri.aidr.output.getdata;

import static org.junit.Assert.*;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class ChannelBufferManagerTest {
	//
	static ChannelBufferManager channelBufferManager;
	//
	@BeforeClass
	public static void setUp() throws Exception {
		channelBufferManager = new ChannelBufferManager();
		String channelRegEx = "aidr_predict.*";
		channelBufferManager.initiateChannelBufferManager(channelRegEx);
	}
	//
	@Test
	public void getLastMessagesTest() {
		String channelName = "aidr_predict.mock_collection";
		int msgCount = 2; 
		List<String> list = channelBufferManager.getLastMessages(channelName, msgCount);
		//
		assertNotNull(list);
		boolean isString = false;
		String stringList = list.get(0);
		if(stringList != null)
			isString = true;
		assertEquals(true, isString);
	}
	//
	@Test
	public void getActiveChannelsListTest() {
		Set<String> set = channelBufferManager.getActiveChannelsList();
		//
		assertNotNull(set);
		assertEquals("aidr_predict.mock_collection", set.iterator().next());
	}
	//
	@Test
	public void getActiveChannelCodesTest() {
		Set<String> set = channelBufferManager.getActiveChannelCodes();
		//
		assertNotNull(set);
		assertEquals("mock_collection", set.iterator().next());
	}
	//
	@Test
	public void getLatestFromAllChannelsTest() {
		List<String> list = channelBufferManager.getLatestFromAllChannels(2);
		//
		assertNotNull(list);
		String actualString = list.get(0);
		boolean isActualString = false;
		if(actualString != null)
			isActualString = true;
		assertEquals(true, isActualString);
	}
}
