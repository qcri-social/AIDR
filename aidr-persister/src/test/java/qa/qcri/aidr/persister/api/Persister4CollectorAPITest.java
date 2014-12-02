package qa.qcri.aidr.persister.api;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

public class Persister4CollectorAPITest {
	//
	static Persister4CollectorAPI persister4CollectorAPI;
	static String collectionCode = "2014-05-emsc_landslides_2014";
	//
	@BeforeClass
	public static void setUp() {
		persister4CollectorAPI = new Persister4CollectorAPI();
	}
	//
	@Test
	public void generateCSVFromLastestJSONTest() throws UnknownHostException {
		int exportLimit= 50;
		//
		Response response = persister4CollectorAPI.generateCSVFromLastestJSON(collectionCode, exportLimit);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	//
	@Test
	public void generateTweetsIDSCSVFromAllJSONTest() throws UnknownHostException {
		Boolean downloadLimited = false;
		Response response = persister4CollectorAPI.generateTweetsIDSCSVFromAllJSON(collectionCode , downloadLimited);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	//
	@Test
	public void generateJSONFromLastestJSONTest() throws UnknownHostException {
		String jsonType = "TEXT_JSON";
		Response response = persister4CollectorAPI.generateJSONFromLastestJSON(collectionCode, jsonType);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	//
	@Test
	public void generateTweetsIDSJSONFromAllJSONTest() throws UnknownHostException {
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		Response response = persister4CollectorAPI.generateTweetsIDSJSONFromAllJSON(collectionCode, downloadLimited, jsonType);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
}
