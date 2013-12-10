package qa.qcri.aidr.trainer.api.Jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/30/13
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class JedisDataStore {
    String REDIS_HOST="localhost";
    static JedisPool jedisPool;

    /* REDIS */
    public static Jedis getJedisConnection() throws Exception {
        try {
            if (jedisPool == null) {

                jedisPool = new JedisPool(new JedisPoolConfig(), "localhost",6379);

            }
            return jedisPool.getResource();
        } catch (Exception e) {
            System.out
                    .println("Could not establish Redis connection. Is the Redis server running?");
            throw e;
        }
    }

    public static void close(Jedis resource) {
        jedisPool.returnResource(resource);
    }


}
