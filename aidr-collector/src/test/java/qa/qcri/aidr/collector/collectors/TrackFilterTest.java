/**
 * 
 */
package qa.qcri.aidr.collector.collectors;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author koushik
 *
 */
public class TrackFilterTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {}

	@Test
	public void test1() {
		TrackFilter f = null;
		String q = "a b, c";
		String t = "b a";
		
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + true +")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, true , f.test(t));

		q = "\"a b\", c";
		t = "b a";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + false + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, false , f.test(t));


		q = "\"a b\" c";
		t = "c a b";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + true + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, true , f.test(t));

		q = "\"a b\" c";
		t = "d a b";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + false + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, false , f.test(t));

		q = "\"a, b\" c";
		t = "a b c";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + false + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, false , f.test(t));

		q = "\"a, b\" c";
		t = "a, b c";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + true + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, true , f.test(t));

		q = "#a";
		t = "a";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + false + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, false , f.test(t));

		q = "a";
		t = "#a";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + true + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, true , f.test(t));

		q = "aa bb";
		t = "naa bbm";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + false + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, false , f.test(t));

		q = "naa bbm";
		t = "naa bbm";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + true + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, true , f.test(t));

		q = "a,b";
		t = "#a#b";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + false + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, false , f.test(t));

		q = "a,b";
		t = "#a##b";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + false + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, false , f.test(t));
		
		q = "a,b";
		t = "#a #b";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + true + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, true , f.test(t));
		
		q = "a b";
		t = "#a #b";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + true + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, true , f.test(t));
		
		q = "a,b";
		t = "#a ##b";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + true + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, true , f.test(t));
		
		q = "a,b";
		t = "##a ##b";
		f = new TrackFilter(q);
		System.out.println("Comparing q = " + q + " with t = " + t + ": result = " + f.test(t) + " (" + false + ")");
		assertEquals("Failed on test with <q> = " + q + " <t> = " + t, false , f.test(t));
	}
}
