package qa.qcri.aidr.output;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import qa.qcri.aidr.output.getdata.TestGetBufferedAIDRData;

@RunWith(Suite.class)
@SuiteClasses({
//	FilterQueryMatcherTest.class,
//	ChannelBufferManagerTest.class,
	TestGetBufferedAIDRData.class,
//	AsyncStreamTest.class,
//	JedisConnectionObjectTest.class
})

//Test Suite class implemented to run all the test classes in a specified order
public class AllTestSuite {
}
