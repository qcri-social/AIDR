package qa.qcri.aidr.output.stream;

import static org.junit.Assert.*;

import java.io.IOException;

import org.glassfish.jersey.server.ChunkedOutput;
import org.junit.BeforeClass;
import org.junit.Test;

public class AsyncStreamTest {
	//
	static AsyncStream asyncStream;
	//
	@BeforeClass
	public static void setUp() throws Exception {
		asyncStream = new AsyncStream();
		asyncStream.contextInitialized(null);
	}
	//
	@Test
	public void streamChunkedResponseTest() throws IOException {
		String channelCode = "mock_collection";
		String callbackName = null;
		Float rate = new Float(-1);
		String duration = "-1";
		ChunkedOutput<String> output = asyncStream.streamChunkedResponse(channelCode, callbackName, rate, duration);
		//
		Class<?> s = output.getRawType();
		assertNotNull(output);
		assertEquals("class java.lang.String", s.toString());
	}
}
