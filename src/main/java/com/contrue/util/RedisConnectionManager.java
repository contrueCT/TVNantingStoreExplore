package com.contrue.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author confff
 */
public class RedisConnectionManager {

    private static final JedisPool jedisPool;

    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(8);
            config.setMaxIdle(8);
            config.setMinIdle(0);

            jedisPool = new JedisPool(config,"localhost", 6379, 2000, "123456");
        } catch (Exception e) {
            SystemLogger.logError("Redis连接池初始化失败", e);
            throw new RuntimeException(e);
        }
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static void closePool() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }


}
