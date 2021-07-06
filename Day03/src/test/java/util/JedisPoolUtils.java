package util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JedisPoolUtils {
    private static JedisPool jedisPool;
    static{
        InputStream is = JedisPoolUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(p.getProperty("maxTotal")));
        config.setMaxIdle(Integer.parseInt(p.getProperty("maxIdle")));
        jedisPool = new JedisPool(config,p.getProperty("host"),Integer.parseInt(p.getProperty("port")));
    }
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
    public void close(Jedis jedis) {
        jedis.close();
    }
}
