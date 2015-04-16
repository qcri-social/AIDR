/**
 * 
 */
package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * @author koushik
 *
 */
public class TrackFilterTest {
	
	Map<String, String> testCases = null;
	Map<String, Boolean> results = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		testCases = new HashMap<String, String>();
		results = new HashMap<String, Boolean>();
		
		testCases.put("a b, c", "b a");
		testCases.put("\"a b\", c", "b a");
		testCases.put("\"a b\" c", "c a b");
		testCases.put("\"a b\" c", "d a b");
		testCases.put("\"a, b\" c", "a b c");
		testCases.put("\"a, b\" c", "a, b c");
		testCases.put("#a", "a");
		testCases.put("a", "#a");
		testCases.put("aa bb", "naa bbm");
		testCases.put("naa bbm", "naa bbm");
		testCases.put("a,b", "#a#b");
		testCases.put("a,b", "#a##b");
		
		results.put("a b, c", true);
		results.put("\"a b\", c", false);
		results.put("\"a b\" c", true);
		results.put("\"a b\" c", false);
		results.put("\"a, b\" c", false);
		results.put("\"a, b\" c", true);
		results.put("#a", false);
		results.put("a", true);
		results.put("aa bb", false);
		results.put("naa bbm", true);
		results.put("a,b", true);
		results.put("a,b", true);
	}

	@Test
	public void testTrackFilter() {
		for (String q: testCases.keySet()) {
			TrackFilter f = new TrackFilter(q);
			System.out.println("Comparing q = " + q + " with t = " + testCases.get(q) + ": result = " + f.test(testCases.get(q)) + " (" + results.get(q) + ")");
			assertEquals("Failed on test with <q> = " + q + " <t> = " + testCases.get(q), results.get(q) , f.test(testCases.get(q)));
		}
	}

}
