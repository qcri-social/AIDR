package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.collector.beans.CollectionTask;
import twitter4j.FilterQuery;

public class TwitterStreamTrackerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTask2Query() throws Exception {
		CollectionTask t1 = new CollectionTask();
		FilterQuery q1 = TwitterStreamTracker.task2query(t1);
		assertEquals(new FilterQuery(), q1);

		CollectionTask t2 = new CollectionTask();
		t2.setToTrack("earthquake");
		FilterQuery q2 = TwitterStreamTracker.task2query(t2);
		FilterQuery e2 = new FilterQuery();
		e2.track(new String[]{"earthquake"});
		assertEquals(e2, q2);

		CollectionTask t3 = new CollectionTask();
		t3.setToFollow("12345");
		FilterQuery q3 = TwitterStreamTracker.task2query(t3);
		FilterQuery e3 = new FilterQuery();
		e3.follow(new long[]{12345L});
		assertEquals(e3, q3);
	
		CollectionTask t4 = new CollectionTask();
		t4.setGeoLocation("-122.75,36.8,-121.75,37.8,-74,40,-73,41");
		FilterQuery q4 = TwitterStreamTracker.task2query(t4);
		Field f = FilterQuery.class.getDeclaredField("locations");
		f.setAccessible(true);
		double[][] actual = (double[][]) f.get(q4);
		assertEquals(4, actual.length);
		for (double[] point : actual)
			assertEquals(2, point.length);
		assertEquals(-122.75, actual[0][0], 0.0001);
		assertEquals(  36.8,  actual[0][1], 0.0001);
		assertEquals(-121.75, actual[1][0], 0.0001);
		assertEquals(  37.8,  actual[1][1], 0.0001);
		assertEquals( -74,    actual[2][0], 0.0001);
		assertEquals(  40,    actual[2][1], 0.0001);
		assertEquals( -73,    actual[3][0], 0.0001);
		assertEquals(  41,    actual[3][1], 0.0001);
		
		CollectionTask t5 = new CollectionTask();
		t5.setLanguageFilter("en,fr");
		FilterQuery q5 = TwitterStreamTracker.task2query(t5);
		FilterQuery e5 = new FilterQuery();
		e5.language(new String[]{"en", "fr"});
		assertEquals(e5, q5);
	}

}
