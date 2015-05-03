package qa.qcri.aidr.common.redis;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LoadShedderTest {

	double FIVE_SECONDS_IN_MINUTES = 5.0/60.0;
	long FIVE_SECONDS_IN_MILLIS = (long)(5.0*1000.0);
	
	double ONE_SECOND_IN_MINUTES = 1.0/60.0;
	long ONE_SECOND_IN_MILLIS = (long)(1.0*1000.0);
	
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * This tests a load shedder that accepts 3 items per period of five seconds.
	 * 
	 * @throws InterruptedException
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testCanProcess5s() throws InterruptedException {
		LoadShedder loadShedder = new LoadShedder(3, FIVE_SECONDS_IN_MINUTES, false, "Test");
		
		for( int i=0; i<3; i++) {
			assertEquals( true, loadShedder.canProcess() );
		}
		assertEquals( false, loadShedder.canProcess() );
		assertEquals( false, loadShedder.canProcess() );
		
		Thread.sleep(FIVE_SECONDS_IN_MILLIS+1l);
		
		for( int i=0; i<3; i++) {
			assertEquals( true, loadShedder.canProcess() );
		}
		assertEquals( false, loadShedder.canProcess() );
		assertEquals( false, loadShedder.canProcess() );
	}
	
	/**
	 * This tests a load shedder that accepts 1 item per second.
	 * 
	 * @throws InterruptedException
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testCanProcess1s() throws InterruptedException {
		LoadShedder loadShedder = new LoadShedder(1, ONE_SECOND_IN_MINUTES, false, "Test");

		for( int i=0; i<3; i++) {
			assertEquals( true, loadShedder.canProcess() );
			assertEquals( false, loadShedder.canProcess() );
			Thread.sleep(ONE_SECOND_IN_MILLIS + 1l);
		}
		
		assertEquals( true, loadShedder.canProcess() );
	}
}
