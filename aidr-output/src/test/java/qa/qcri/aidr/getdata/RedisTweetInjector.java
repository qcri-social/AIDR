package qa.qcri.aidr.getdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

public class RedisTweetInjector {

	private URI host = null;
	private int port = 6379;
	private Jedis jedis = null;

	private Config config;
	private int sleepDuration = 0;		// default value - no sleep

	public RedisTweetInjector() {
		config = new Config();
		try {
			this.host = new URI(config.host);
		} catch (URISyntaxException e) {
			System.err.println("Unable to create host URI for: " + config.host.toString());
			e.printStackTrace();
		}
		this.port = config.port;
	}

	public RedisTweetInjector(String host, int port) {
		try {
			this.host = new URI(host);
		} catch (URISyntaxException e) {
			System.err.println("Unable to create host URI for: " + host.toString());
			e.printStackTrace();
		}
		this.port = port;
		config = new Config();
	}


	/** 
	 * 
	 * @return 1 on successful connection to Redis, -1 on failure
	 */
	public int setupRedisConnection() {
		try {
			jedis = new Jedis(host.toString(), port, 30000);
			jedis.connect();
		} catch (Exception e) {
			System.err.println("Failed to connect to Redis");
			e.printStackTrace();
			return -1;
		}
		return 1;
	}

	public Jedis getJedis() {
		return this.jedis;
	}

	public static List<String> getClassifiedFileVolumes(String collectionCode) {

		String filesPath = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/";
		List<String> fileNames = new ArrayList();
		File folder = new File(filesPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String currentFileName = listOfFiles[i].getName();
				if (StringUtils.contains(currentFileName, collectionCode)) {
					if (!(StringUtils.contains(currentFileName, ".csv"))) { //do not consider CSV files here, only consider JSON files
						if (StringUtils.startsWith(currentFileName, "Classified_")
								&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol"))
							fileNames.add(currentFileName);
					}
				}
			}
		}
		return fileNames;
	}

	public void publishTweet(String tweet) {
		jedis.publish(config.channelPrefix+config.collectionCode, tweet);
		try {
			Thread.sleep(sleepDuration);
		} catch (InterruptedException e) {
			System.err.println("Thread sleep interrupted for thread: " + Thread.currentThread().getName());
		}
	}

	public int readDataForCollectionAndPublish(int duration) {
		BufferedReader br = null;
		int count = 0;
		List<String> fileNames = getClassifiedFileVolumes(config.collectionCode);

		Long startTime = System.currentTimeMillis();
		Long currentTime = startTime;
		do {
			for (String file : fileNames) {
				String fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH + config.collectionCode + "/output/" + file;
				System.out.println(config.collectionCode + ": Reading file " + fileLocation);
				try {
					br = new BufferedReader(new FileReader(fileLocation));
					String tweet;
					while ((tweet = br.readLine()) != null) {
						publishTweet(tweet);
						++count;
					}
					br.close();
				} catch (FileNotFoundException e) {
					System.err.println("File not found: " + file);
				} catch (IOException e) {
					System.err.println("IO Exception on file: " + file);
				}
			}
		} while ((currentTime - startTime) < duration);
		return count;
	}

	public void injectTweets() {
		int duration = config.duration * 1000 * 60;		// in milliseconds
		if (config.tweets_per_sec > 0) {
			sleepDuration = 1000 / config.tweets_per_sec;
		}
		System.out.println("Total tweets published in time " + config.duration 
				+ "mins = " + readDataForCollectionAndPublish(duration));
	}

	public static void main(String args[]) throws Exception {
		RedisTweetInjector injector = new RedisTweetInjector();
		injector.setupRedisConnection();
		//injector.getJedis().publish(injector.config.channelPrefix+injector.config.collectionCode, "this is a test message");
		
		injector.injectTweets();
	}
}
