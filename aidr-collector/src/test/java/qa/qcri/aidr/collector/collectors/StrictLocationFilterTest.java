package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.collector.beans.CollectionTask;

public class StrictLocationFilterTest {
	
	private Properties p;
	private StrictLocationFilter f, f2;
	private List<String> tweets = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
		p = new Properties();
		p.load(getClass().getResourceAsStream("/qa/qcri/aidr/collector/api/tweets.properties"));
		CollectionTask task1 = new CollectionTask();
		task1.setGeoLocation("23.27,51.72,31.98,55.57");
		f = new StrictLocationFilter(task1);
		CollectionTask task2 = new CollectionTask();
		task2.setGeoLocation("87.14,49.39,88.02,50.27,"
				+"42.39,37.68,43.05,38.34,"
				+"159.63,52.57,160.51,53.45,"
				+"-72.56,7.69,-71.40,8.85,"
				+"15.79,51.25,16.41,51.87,"
				+"-70.54,-34.62,-69.92,-34.00,"
				+"-92.80,14.22,-91.92,15.10,"
				+"146.83,43.39,147.81,44.37");
		f2 = new StrictLocationFilter(task2);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/qa/qcri/aidr/collector/api/128390298CEA-0000001_20150208_vol-1.json")));
		String line;
		while ((line = br.readLine()) != null) {
			tweets.add(line);
		}
	}
	
	@Test
	public void testWithLocation() {
		String json = p.getProperty("tweet3");
		JsonObject doc = Json.createReader(new StringReader(json)).readObject();
		assertEquals(true, f.test(doc));
	}

	@Test
	public void testWithoutLocation() {
		String json = p.getProperty("tweet1");
		JsonObject doc = Json.createReader(new StringReader(json)).readObject();
		assertEquals(false, f.test(doc));
	}
	
	@Test
	public void testCoordinates() {
		int passed2=0;
		for (String line : tweets) {
			JsonObject doc = Json.createReader(new StringReader(line)).readObject();
			if(f2.test(doc)) passed2 += 1;
		}
		assertEquals(308, passed2);
	}
}
