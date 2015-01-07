package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

public class StrictLocationFilterTest {
	
	private Properties p;
	StrictLocationFilter f;

	@Before
	public void setUp() throws Exception {
		p = new Properties();
		p.load(getClass().getResourceAsStream("/qa/qcri/aidr/collector/api/tweets.properties"));
		f = new StrictLocationFilter();
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
}
