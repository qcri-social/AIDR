package qa.qcri.aidr.common.code;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

/**
 * This class tests that an object mapper can reliably convert JSON to an object, and that it
 * accepts missing fields on its input.
 *
 */
public abstract class JSONWrapperTest {

	public static class MyClass {
		public String aaa;

		public Integer bbb;
	}

	public abstract MyClass readValue(String jsonString, @SuppressWarnings("rawtypes") Class aClass) throws IOException;

	public void testGetObjectMapper() {

		MyClass testObject = null;

		// Success case
		try {
			testObject = readValue("{ \"aaa\": \"hello\", \"bbb\": 42 }", MyClass.class);
		} catch (IOException e) {
			System.err.println(e);
			fail();
		}
		assertEquals("hello", testObject.aaa);
		assertEquals(new Integer(42), testObject.bbb);

		// Malformed JavaScript case, should fail
		try {
			testObject = readValue("XXX aaa: \"hello\", \"bbb\": 42 YYY", MyClass.class);
			fail();
		} catch (IOException e) {

		}

		// Missing field #1
		try {
			testObject = readValue("{ \"aaa\": \"bye\" }", MyClass.class);
		} catch (IOException e) {
			System.err.println(e);
			fail();
		}
		assertEquals("bye", testObject.aaa);
		assertEquals(null, testObject.bbb);

		// Missing field #2
		try {
			testObject = readValue("{ \"bbb\": 123 }", MyClass.class);
		} catch (IOException e) {
			System.err.println(e);
			fail();
		}
		assertEquals(null, testObject.aaa);
		assertEquals(new Integer(123), testObject.bbb);

	}

}
