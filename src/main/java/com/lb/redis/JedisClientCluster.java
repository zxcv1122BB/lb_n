package com.lb.redis;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisCluster;

@Service(value="jedisClient")
public class JedisClientCluster implements JedisClient {
	
	@Autowired
	private JedisCluster jedisCluster;
	
	
	
	@Override
	public Boolean hash_exists(String key, String field) {
		return jedisCluster.hexists(key,field);
	}

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public Boolean exists(String key) {
		return jedisCluster.exists(key);
	}
	
	
	public Long pexpireAt(String key, long millisecondsTimestamp) {
		return jedisCluster.pexpireAt(key, millisecondsTimestamp);
	}
	
	@Override
	public Long expire(String key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public Long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return jedisCluster.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedisCluster.hget(key, field);
	}

	@Override
	public Long hdel(String key, String... field) {
		return jedisCluster.hdel(key, field);
	}

	@Override
	public Long del(String name) {
		return jedisCluster.del(name);
	}

	@Override
	public String set(byte[] key, byte[] value) {
		return jedisCluster.set(key,value);
	}

	@Override
	public byte[] get(byte[] key) {
		return jedisCluster.get(key);
	}

	@Override
	public Long del(byte[] name) {
		return jedisCluster.del(name);
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		return jedisCluster.hgetAll(key);
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		return jedisCluster.hmget(key, fields);
	}

	@Override
	public Long decr(String key) {
		return jedisCluster.decr(key);
	}

	@Override
	public boolean update(String key, String value) {
		if (jedisCluster.exists(key)) {
			jedisCluster.set(key, value);
			if (value.equals(jedisCluster.get(key))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	

}
