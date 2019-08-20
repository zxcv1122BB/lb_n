package com.lb.redis;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//@Service
public class JedisClientPool implements JedisClient {

//	@Autowired
	private JedisPool jedisPool;

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}

	public boolean update(String key, String value) {
		Jedis js = jedisPool.getResource();
		if (js.exists(key)) {
			js.set(key, value);
			if (value.equals(js.get(key))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public String set(byte[] key, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = jedis.exists(key);
		jedis.close();
		return result;
	}

	/**
	 * 设置某一个Key的过期时间
	 */
	@Override
	public Long pexpireAt(String key, long millisecondsTimestamp) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.pexpireAt(key, millisecondsTimestamp);
		jedis.close();
		return result;
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.expire(key, seconds);
		jedis.close();
		return result;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.ttl(key);
		jedis.close();
		return result;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incr(key);
		jedis.close();
		return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(key, field, value);
		jedis.close();
		return result;
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.hget(key, field);
		jedis.close();
		return result;
	}

	@Override
	public Long hdel(String key, String... field) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hdel(key, field);
		jedis.close();
		return result;
	}

	@Override
	public Long del(String name) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(name);
		jedis.close();
		return result;
	}

	@Override
	public byte[] get(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		byte[] result = jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public Long del(byte[] name) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(name);
		jedis.close();
		return result;
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		Jedis jedis = jedisPool.getResource();
		Map<String, String> result = jedis.hgetAll(key);
		jedis.close();
		return result;
	}

	public List<String> hmget(String key, String... fields) {
		Jedis jedis = jedisPool.getResource();
		List<String> valueList = jedis.hmget(key, fields);
		jedis.close();
		return valueList;
	}

	@Override
	public Long decr(String key) {
		Jedis jedis = jedisPool.getResource();
		return jedis.decr(key);
	}

	@Override
	public Boolean hash_exists(String key, String field) {
		// TODO Auto-generated method stub
		return null;
	}
}
