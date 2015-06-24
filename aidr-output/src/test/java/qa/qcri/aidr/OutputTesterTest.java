package qa.qcri.aidr;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import qa.qcri.aidr.output.utils.ClassifiedTweetAdapter;
import qa.qcri.aidr.output.utils.JedisConnectionObject;
import qa.qcri.aidr.output.utils.OutputConfigurationProperty;
import qa.qcri.aidr.output.utils.OutputConfigurator;
import qa.qcri.aidr.output.utils.Publisher;
import redis.clients.jedis.Jedis;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OutputTesterTest {
	private static Logger logger = Logger.getLogger(OutputTesterTest.class.getName());
	private static OutputConfigurator configProperties = OutputConfigurator.getInstance();
	private static String BASE_URI;
	public static String collectionCode;
	public static Long nItems;
	private static final String CHANNEL_PREFIX_STRING = configProperties.getProperty(OutputConfigurationProperty.TAGGER_CHANNEL_BASENAME)+".";
	private static final int MAX_MESSAGES_COUNT = Integer.valueOf(configProperties.getProperty(OutputConfigurationProperty.MAX_MESSAGES_COUNT));

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String config = System.getProperty("config");
		Properties properties;
		if(StringUtils.isNotEmpty(config)){
			try (InputStream input = new FileInputStream(config);){
				properties = new Properties();
				properties.load(input);
				for (Object property : properties.keySet()) {
					configProperties.setProperty(property.toString(), properties.get(property).toString());
				}
			} catch (IOException e) {
				logger.error("Error in reading config properties file: " + config, e);
			}
		}
		nItems = Long.valueOf(System.getProperty("nItems"));
		BASE_URI = configProperties.getProperty(OutputConfigurationProperty.OUTPUT_REST_URI);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = df.format(new Date());
		collectionCode = timestamp+"-output-test";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBufferedOutput() throws InterruptedException, JsonParseException, JsonMappingException, IOException {

		final String CHANNEL_NAME = CHANNEL_PREFIX_STRING+collectionCode;
	
		//Writing to redis nItems
		JedisConnectionObject connObject=null;
		connObject = new JedisConnectionObject();
		Jedis publisherJedis = connObject.getJedisResource();
		new Publisher(publisherJedis, CHANNEL_NAME,nItems).start();
		connObject.returnJedis(publisherJedis);
		
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		WebTarget webResource;
		
		//GetBufferedAIDRData
		logger.info("Fetching buffered messages from channel : "+CHANNEL_NAME);
		webResource = client.target(BASE_URI+"crisis/fetch/channel/"+URLEncoder.encode(CHANNEL_NAME, "UTF-8"));
		Response response =  webResource.request(MediaType.APPLICATION_JSON).get();
		String jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());
		
		ObjectMapper mapper = new ObjectMapper();
		List<ClassifiedTweetAdapter> classifiedTweetList = Arrays.asList(mapper.readValue(jsonResponse, ClassifiedTweetAdapter[].class));
		
		//Compare number of messages returned. They should be strictly equals to MAX_MESSAGES_COUNT
		assertEquals("The number of messages returned from the buffer is different from MAX_MESSAGES_COUNT", MAX_MESSAGES_COUNT, classifiedTweetList.size());

		Long count = nItems;
		for (ClassifiedTweetAdapter classifiedTweet : classifiedTweetList) {
			assertEquals("Returned tweet text from the buffer is different .", "Testing the output tester with random text-"+count--, classifiedTweet.getText());
		}
	}
}
