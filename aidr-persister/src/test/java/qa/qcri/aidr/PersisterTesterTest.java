package qa.qcri.aidr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import qa.qcri.aidr.redis.JedisConnectionPool;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;
import qa.qcri.aidr.utils.Publisher;
import redis.clients.jedis.Jedis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PersisterTesterTest {
	private static Logger logger = Logger.getLogger(PersisterTesterTest.class.getName());
	private static PersisterConfigurator configProperties = PersisterConfigurator.getInstance();
	private static String BASE_URI;
	public static String collectionCode;
	public static Long nItems;

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
				fail("Error in reading config properties file: " + config);
			}
		}
		BASE_URI = configProperties.getProperty(PersisterConfigurationProperty.PERSISTER_REST_URI);
		nItems = Long.valueOf(System.getProperty("nItems"));
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = df.format(new Date());
		collectionCode = timestamp+"-persister-test";

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	//Deleting the persister directory
		String filepath = configProperties.getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH);
		File folder = new File(filepath + collectionCode);
		if(folder.exists()){
			FileUtils.deleteDirectory(folder);
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOrder() throws UnsupportedEncodingException{	
		testCollectorPersister();
		testTaggerPersister();
		testCsvGenerator();
	}
	
	public void testCollectorPersister() throws UnsupportedEncodingException {

		final String CHANNEL_NAME = configProperties.getProperty(PersisterConfigurationProperty.FETCHER_CHANNEL)+collectionCode;
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		WebTarget webResource;	

		//collectorPersister/start
		logger.info("Starting persister for the collection : "+collectionCode);
		webResource = client.target(BASE_URI+"persister/start?collectionCode="+URLEncoder.encode(collectionCode, "UTF-8"));
		Response response =  webResource.request(MediaType.APPLICATION_JSON).get();
		String filepath = configProperties.getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH);
		String jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());
		assertEquals( "Started persisting to " + filepath, jsonResponse);


		//Writing to redis n/2 items
		JedisConnectionPool connObject=null;
		connObject = new JedisConnectionPool();
		Jedis publisherJedis = connObject.getJedisConnection();
		new Publisher(publisherJedis, CHANNEL_NAME,nItems/2).start();
		connObject.close(publisherJedis);

		//collectorPersister/status 
		webResource = client.target(BASE_URI+"persister/start?collectionCode="+URLEncoder.encode(collectionCode, "UTF-8"));
		response =  webResource.request(MediaType.APPLICATION_JSON).get();
		jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());
		assertEquals("A persister is already running for this collection code ["+collectionCode+"]", jsonResponse);

		//Writing to redis n/2 items	
		connObject = new JedisConnectionPool();
		publisherJedis = connObject.getJedisConnection();
		new Publisher(publisherJedis, CHANNEL_NAME,(nItems-nItems/2)).start();
		connObject.close(publisherJedis);

		//collectorPersister/stop
		logger.info("Stopping persister for the collection : "+collectionCode);
		webResource = client.target(BASE_URI+"persister/stop?collectionCode="+URLEncoder.encode(collectionCode, "UTF-8"));
		response =  webResource.request(MediaType.APPLICATION_JSON).get();
		jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());
		assertEquals("Persistance of [" + collectionCode + "] has been stopped.", jsonResponse);

		//collectorPersister/status
		webResource = client.target(BASE_URI+"persister/stop?collectionCode="+URLEncoder.encode(collectionCode, "UTF-8"));
		response =  webResource.request(MediaType.APPLICATION_JSON).get();
		jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());
		assertEquals("Unable to locate a running persister with the given collection code:[" + collectionCode + "]", jsonResponse);

		//Read the directory where the collector persister file should be located
		File folderLocation = new File(filepath + collectionCode);
		if(!folderLocation.exists()){
			fail("Persister directory doesn't exist");
		}
		File[] files = folderLocation.listFiles();
		int count = 0;

		for (File file : files) {
			if(Pattern.matches(collectionCode+"_[0-9]{8}_vol-[0-9]+.json", file.getName())){
				try(FileInputStream fstream = new FileInputStream(file);) {
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					while (br.readLine() != null)   {
						count++;
					}	
				} catch (FileNotFoundException e) {
					logger.error("Persister file doesn't exist in testCollectorPersisterFileItems");
					fail("Persister file doesn't exist in testCollectorPersisterFileItems");
				} catch (IOException e) {
					logger.error("IOException in persister file in testCollectorPersisterFileItems");
					fail("IOException in persister file in testCollectorPersisterFileItems");
				}
			}
		}
		if(count!=nItems){
			fail("No. of lines in JSON file ="+count+" doesn't match with total no. of lines in the redis.");
		}
	}
	
	public void testTaggerPersister() throws UnsupportedEncodingException {

		final String CHANNEL_NAME = configProperties.getProperty(PersisterConfigurationProperty.TAGGER_CHANNEL)+collectionCode;
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		WebTarget webResource;	
		

		//taggerPersister/start
		logger.info("Starting tagger persister for the collection : "+collectionCode);
		webResource = client.target(BASE_URI+"taggerPersister/start?collectionCode="+URLEncoder.encode(collectionCode, "UTF-8"));
		Response response =  webResource.request(MediaType.APPLICATION_JSON).get();
		String jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());
		String filepath = configProperties.getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH);
		assertEquals( "Started tagger persisting to " + filepath, jsonResponse);


		//Writing to redis n/2 items
		JedisConnectionPool connObject=null;
		connObject = new JedisConnectionPool();
		Jedis publisherJedis = connObject.getJedisConnection();
		new Publisher(publisherJedis, CHANNEL_NAME,nItems/2).start();
		connObject.close(publisherJedis);


		//taggerPersister/status 
		webResource = client.target(BASE_URI+"taggerPersister/start?collectionCode="+URLEncoder.encode(collectionCode, "UTF-8"));
		response =  webResource.request(MediaType.APPLICATION_JSON).get();
		jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());
		assertEquals("A tagger persister is already running for this collection code [" + collectionCode + "]", jsonResponse);


		//Writing to redis n/2 items	
		connObject = new JedisConnectionPool();
		publisherJedis = connObject.getJedisConnection();
		new Publisher(publisherJedis, CHANNEL_NAME,(nItems-nItems/2)).start();
		connObject.close(publisherJedis);


		//taggerPersister/stop
		logger.info("Stopping tagger persister for the collection : "+collectionCode);
		webResource = client.target(BASE_URI+"taggerPersister/stop?collectionCode="+URLEncoder.encode(collectionCode, "UTF-8"));
		response =  webResource.request(MediaType.APPLICATION_JSON).get();
		jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());
		assertEquals("Tagger Persistance of [" + collectionCode + "] has been stopped.", jsonResponse);


		//taggerPersister/status
		webResource = client.target(BASE_URI+"taggerPersister/stop?collectionCode="+URLEncoder.encode(collectionCode, "UTF-8"));
		response =  webResource.request(MediaType.APPLICATION_JSON).get();
		jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());
		assertEquals("Unable to locate a running tagger persister with the given collection code:[" + collectionCode + "]", jsonResponse);


		//Read the directory where the collector persister file should be located
		File folderLocation = new File(filepath + collectionCode);
		if(!folderLocation.exists()){
			fail("Persister directory doesn't exist");
		}
		File[] files = folderLocation.listFiles();
		int count = 0;

		for (File file : files) {
			if(Pattern.matches( "Classified_"+collectionCode+"_[0-9]{8}_vol-[0-9]+.json", file.getName())){
				try(FileInputStream fstream = new FileInputStream(file);) {
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					while (br.readLine()!= null)   {
						count++;
					}	
				} catch (FileNotFoundException e) {
					logger.error("Persister file doesn't exist in testCollectorPersisterFileItems");
					fail("Persister file doesn't exist in testCollectorPersisterFileItems");
				} catch (IOException e) {
					logger.error("IOException in persister file in testCollectorPersisterFileItems");
					fail("IOException in persister file in testCollectorPersisterFileItems");
				}
			}
		}
		if(count!=nItems){
			fail("No. of lines in JSON file ="+count+" doesn't match with total no. of lines in the redis.");
		}
	}
	

	public void testCsvGenerator() throws UnsupportedEncodingException{
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		WebTarget webResource;	

		logger.info("Generating csv for the collection : "+collectionCode);
		webResource = client.target(BASE_URI+"taggerPersister/genCSV?collectionCode="+URLEncoder.encode(collectionCode, "UTF-8"));
		Response response =  webResource.request(MediaType.APPLICATION_JSON).get();
		String jsonResponse = response.readEntity(String.class);
		assertEquals(200, response.getStatus());

		JsonParser parser = new JsonParser();
		JsonObject jsonObj = (JsonObject) parser.parse(jsonResponse);
		
		Long count = 0L;
		if(jsonObj.get("url")!=null){
			try {
				String fileDownloadLink = jsonObj.get("url").getAsString();
				URL link = new URL(fileDownloadLink);
				try(InputStreamReader inputStreamReader = new InputStreamReader(link.openStream());){
					BufferedReader br = new BufferedReader(inputStreamReader);
					while (br.readLine()!= null)   {
						count++;
					}	
				}
				catch (IOException e) {
					logger.error("IOException while reading the csv file");
					fail("IOException while reading the csv file");
				}
			} catch (MalformedURLException e) {
				logger.error("Error in the downloadable link for csv file");
				fail("Error in the downloadable link for csv file");
			}
		}
		else{
			logger.error("Download link for csv file does not exist");
			fail("Download link for csv file does not exist");
		}
		
		count--;  //Since first line in csv file contain column names
		
		Long exportLimit = Long.valueOf(configProperties.getProperty(PersisterConfigurationProperty.TWEETS_EXPORT_LIMIT_100K));
		if(count!=Math.min(nItems, exportLimit)){
			fail("No. of lines in CSV file ="+count+" doesn't match with total no. of lines.");
		}
	}
}
