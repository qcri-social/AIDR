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
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import qa.qcri.aidr.getdata.InjectorConfig;
import qa.qcri.aidr.output.utils.JedisConnectionObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;


public class RedisTweetInjector {

	private URI host = null;
	private int port = 6379;
	//private Jedis jedis = null;

	private InjectorConfig config;
	private int sleepDuration = 0;		// default value - no sleep

	private static JedisConnectionObject jedisConn = null;

	public RedisTweetInjector() {
		config = new InjectorConfig();
		try {
			this.host = new URI(config.host);
		} catch (URISyntaxException e) {
			System.err.println("Unable to create host URI for: " + config.host.toString());
			e.printStackTrace();
		}
		this.port = config.port;
		jedisConn = setupJedisConn(this.host.toString(), this.port);
	}

	public RedisTweetInjector(String host, int port) {
		try {
			this.host = new URI(host);
		} catch (URISyntaxException e) {
			System.err.println("Unable to create host URI for: " + host.toString());
			e.printStackTrace();
		}
		this.port = port;
		config = new InjectorConfig();
		jedisConn = setupJedisConn(host, port);
	}

	public static synchronized JedisConnectionObject setupJedisConn(String host, int port) {
		if (null == jedisConn) jedisConn = new JedisConnectionObject(host, port);
		return jedisConn;
	}

	/** 
	 * 
	 * @return non-null Jedis on successful connection to Redis, null on failure
	 */
	public synchronized Jedis setupRedisConnection() {
		Jedis jedis = jedisConn.getJedisResource();
		return jedis;
	}

	public List<String> getClassifiedFileVolumes(String collectionCode) {

		String filesPath = config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/";
		List<String> fileNames = new ArrayList<String>();
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

	public void publishTweet(final Jedis jedis, final String collectionCode, final String tweet) {
		jedis.publish(config.channelPrefix+collectionCode, tweet);
		try {
			Thread.sleep(sleepDuration);
		} catch (InterruptedException e) {
			System.err.println("Thread sleep interrupted for thread: " + Thread.currentThread().getName());
		}
	}

	public int readDataForCollectionAndPublish(final Jedis jedis, final String collectionCode, 
			final long duration) {
		BufferedReader br = null;
		int count = 0;
		List<String> fileNames = getClassifiedFileVolumes(collectionCode);

		long startTime = System.currentTimeMillis();
		long currentTime = startTime;
		while ((currentTime - startTime) < duration)  {
			for (String file : fileNames) {
				String fileLocation = config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + file;
				//System.out.println(Thread.currentThread().getName() + ":: " + collectionCode + ": Reading file " + fileLocation);
				try {
					br = new BufferedReader(new FileReader(fileLocation));
					String tweet;
					while ((tweet = br.readLine()) != null) {
						publishTweet(jedis, collectionCode, tweet);
						++count;
					}
					br.close();
				} catch (FileNotFoundException e) {
					System.err.println("File not found: " + file);
					break;
				} catch (IOException e) {
					System.err.println("IO Exception on file: " + file);
					break;
				}
			}
			currentTime = System.currentTimeMillis();
		} ;
		System.out.println(Thread.currentThread().getName() + ": Done publishing Tweets");
		return count;
	}

	public void injectTweets(final Jedis jedis, final String collectionCode) {
		long duration = config.duration * 60 * 1000;		// in milliseconds
		if (config.tweets_per_sec > 0) {
			sleepDuration = 1000 / config.tweets_per_sec;			
		}
		System.out.println(Thread.currentThread().getName() + ": sleep interval between successive publish, determined from configuration: " + sleepDuration + "ms");
		int count = readDataForCollectionAndPublish(jedis, collectionCode, duration);
		System.out.println(Thread.currentThread().getName() + 
				": Total tweets published in time " + config.duration 
				+ "mins = " + count);
	}

	public void injectSingleTweet(final Jedis jedis, final String collectionCode) {
		long duration = config.duration * 60 * 1000;		// in milliseconds
		if (config.tweets_per_sec > 0) {
			sleepDuration = 1000 / config.tweets_per_sec;			
		}
		System.out.println(Thread.currentThread().getName() + ": sleep interval between successive publish, determined from configuration: " + sleepDuration + "ms");
		long startTime = System.currentTimeMillis();
		long currentTime = startTime;
		while ((currentTime - startTime) < duration)  {
			publishTweet(jedis, collectionCode, config.singleTweet);
			currentTime = System.currentTimeMillis();
		}
	}


	public static void main(String args[]) throws Exception {
		InjectorConfig config = new InjectorConfig();
		final int threadsToSpawn = config.threads;

		for (int i = 0; i < threadsToSpawn; i++) {
			new Thread("<Redis Mock Injector thread " + i + ">"){
				public void run() {
					System.out.println("Thread: " + getName() + " will be spawned...");

					RedisTweetInjector injector = new RedisTweetInjector();
					Jedis jedis = injector.setupRedisConnection();

					try {
						if (jedis != null) {
							InjectorConfig config = new InjectorConfig();
							Random randomGenerator = new Random();
							String collectionCode = config.collection_list.get(
									randomGenerator.nextInt(config.collection_list.size()));

							System.out.println("Thread " + getName() + ":: will use collection: " + collectionCode);
							long startTime = System.currentTimeMillis();
							if (config.useSingleTweet) {
								injector.injectSingleTweet(jedis, collectionCode);
							} else {
								injector.injectTweets(jedis, collectionCode);
							}
							long elapsed = System.currentTimeMillis() - startTime;
							System.out.println("Done thread " + getName() + ", execution time = " + elapsed + "ms");
							//jedis.flushAll();
							jedisConn.returnJedis(jedis);
						} else {
							System.err.println("Couldn't connect to Redis, aborting...");
							System.exit(-1);
						} 
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if (jedis != null) {
								//jedis.flushAll();
								jedisConn.returnJedis(jedis);
							}
						} catch (JedisConnectionException e) {
							System.err.println("REDIS Connection already closed");
						}
						System.out.println("Thread " + getName() + " closed open REDIS connections");
					}
				}
			}.start();

			//injector.getJedis().publish(injector.config.channelPrefix+injector.config.collectionCode, "this is a test message");

		}

	}
}
