package qa.qcri.aidr.persister.api;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

public class Persister4CollectionAPITest {

	static Persister4CollectionAPI persister4CollectionAPI;
	static String collectionCode = "2014-05-emsc_landslides_2014";
	//
	@BeforeClass
	public static void setUp() {
		persister4CollectionAPI = new Persister4CollectionAPI();
	}
	
	@Test
	public void mainTest() {
		Response startResponse = startPersisterTest();
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", startResponse.toString());
		try {
			Thread.sleep(900000); // 15 minutes
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//
		Response stopResponse = stopPersisterTest();
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", stopResponse.toString());
	}
	
	public Response startPersisterTest() {
		String chanelName = "aidr_predict";
		return persister4CollectionAPI.startPersister(chanelName, collectionCode);
	}
	//
	
	public Response stopPersisterTest() {
		return persister4CollectionAPI.stopPersister(collectionCode);
	}
}
