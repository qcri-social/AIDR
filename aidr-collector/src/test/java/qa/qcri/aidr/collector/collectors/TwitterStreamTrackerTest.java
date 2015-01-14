package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.lang.reflect.Field;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.collector.beans.CollectionTask;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamTrackerTest {

	@Before
	public void setUp() throws Exception {
	}

	public void twitterFlow() {
		FilterQuery query = new FilterQuery();
		query.track("minsk".split(","));
		query.locations(new double[][]{new double[]{23.38,51.21}, new double[]{32.84,56.26}});
		
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().setDebugEnabled(false)
				.setJSONStoreEnabled(true)
				.setOAuthConsumerKey("hEIRfBfxNt8HlnfmRGkwrqIGb")
				.setOAuthConsumerSecret("swVbpPSxJsSFP0tifvQ1vVxhMDY3bpfdZT8nHOOfC8EIKMzTuD")
				.setOAuthAccessToken("2222724937-A3TtpvkuwQoiD7PNE5XnC8Z5HbpsZDTX2dCEDDD")
				.setOAuthAccessTokenSecret("TmwuKuXXeW0H6gmbOJSmsmL0p7yBgI2X9aYjQr15kvnUY");
		
		CollectionTask task = new CollectionTask();
		task.setCollectionCode("code");
		
		TwitterStream twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {
				// System.out.println(status.getUser().getName() + " : " + status.getText());
				// System.out.println(TwitterObjectFactory.getRawJSON(status));
				String json = TwitterObjectFactory.getRawJSON(status);
				JsonObject doc = Json.createReader(new StringReader(json)).readObject();
				System.out.println(doc.get("coordinates"));
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
			}

			@Override
			public void onStallWarning(StallWarning warning) {
			}
		};
		twitterStream.addListener(listener);
		twitterStream.filter(query);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
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
