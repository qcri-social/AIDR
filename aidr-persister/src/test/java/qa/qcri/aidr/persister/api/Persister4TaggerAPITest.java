package qa.qcri.aidr.persister.api;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

public class Persister4TaggerAPITest {
	//
	static Persister4TaggerAPI persister4TaggerAPI;
	static String collectionCode = "2014-05-emsc_landslides_2014";
	//
	@BeforeClass
	public static void setUp() {
		persister4TaggerAPI = new Persister4TaggerAPI();
	}
	//
/*	@Test
	public void testGenerateCSVFromLastestJSON() throws UnknownHostException {
		// returns NullPointerException, this methods search for some files in "output" folder which is not created
		int exportLimit = 100;
		Response response = persister4TaggerAPI.generateCSVFromLastestJSON(collectionCode, exportLimit);
		//
		assertEquals("", response.toString());
	}*/
	//
	@Test
	public void testGenerateCSVFromLastestJSONFiltered() throws UnknownHostException {
		int exportLimit = 50;
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1495339860 }, { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1272339860 }, { \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, { \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, { \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(queryString, collectionCode, exportLimit);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	//
/*	@Test
	public void testGenerateTweetsIDSCSVFromAllJSON() throws UnknownHostException {
		// returns NullPointerException, because this method is calling deprecated method
		Boolean downloadLimited = true;
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSON(collectionCode, downloadLimited);
		//
		assertEquals("", response.toString());
	}*/
	//
	@Test
	public void testGenerateTweetsIDSCSVFromAllJSONFiltered() throws UnknownHostException {
		//String queryString = "{\"constraints\": []}";
		String queryString = "{ \"constraints\": [ { \"queryType\": \"date_query\", \"comparator\": \"is_before\", \"timestamp\": 1495339860 }, { \"queryType\": \"date_query\", \"comparator\": \"is_after\", \"timestamp\": 1272339860 }, { \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"praying\", \"comparator\": \"is\", \"min_confidence\": 0.8 }, { \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": \"030_info\", \"comparator\": \"is_not\" }, { \"queryType\": \"classifier_query\", \"classifier_code\": \"informative_pray_personal\", \"label_code\": null, \"comparator\": \"has_confidence\", \"min_confidence\": 0.5 } ] }";
		
		Boolean downloadLimited = true;
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(queryString, collectionCode, downloadLimited);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	//
	@Test
	public void testGenerateJSONFromLastestJSON() throws UnknownHostException {
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		Response response = persister4TaggerAPI.generateJSONFromLastestJSON(collectionCode, exportLimit, jsonType);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	//
/*	@Test
	public void testGenerateTweetsIDSJSONFromAllJSON() throws UnknownHostException {
		// this method returns NullPointerException because it's calling deprecated methods
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSON(collectionCode, downloadLimited, jsonType);
		//
		assertEquals("", response.toString());
	}*/
	//
	@Test
	public void testGenerateJSONFromLastestJSONFiltered() throws UnknownHostException {
		//
		String queryString = "{\"constraints\": []}";
		int exportLimit = 50;
		String jsonType = "TEXT_JSON";
		Response response = persister4TaggerAPI.generateJSONFromLastestJSONFiltered(queryString, collectionCode, exportLimit, jsonType);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	//
	@Test
	public void testGenerateTweetsIDSJSONFromAllJSONFiltered() throws UnknownHostException {
		//
		String queryString = "{\"constraints\": []}";
		Boolean downloadLimited = true;
		String jsonType = "TEXT_JSON";
		Response response = persister4TaggerAPI.generateTweetsIDSJSONFromAllJSONFiltered(queryString, collectionCode , downloadLimited, jsonType);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}", response.toString());
	}
	//
	@Test
	public void testGenerateCSVFromLastestJSONFiltered2() throws UnknownHostException {
		int exportLimit = 50;
		Response response = persister4TaggerAPI.generateCSVFromLastestJSONFiltered(collectionCode, exportLimit);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=false, closed=false, buffered=false}", response.toString());
	}
	//
	@Test
	public void testGenerateTweetsIDSCSVFromAllJSONFiltered2() throws UnknownHostException {
		Boolean downloadLimited = true;
		Response response = persister4TaggerAPI.generateTweetsIDSCSVFromAllJSONFiltered(collectionCode, downloadLimited);
		//
		assertEquals("OutboundJaxrsResponse{status=200, reason=OK, hasEntity=false, closed=false, buffered=false}", response.toString());
	}
}
