package com.lb.redis;

import java.util.List;
import java.util.Map;

public interface JedisClient {
	
	Boolean hash_exists(String key,String field);

	String set(String key, String value);
	
	String set(byte[] key, byte[] value);
	
	String get(String key);
	
	byte[] get(byte[] key);
	
	Boolean exists(String key);
	
	Long expire(String key, int seconds);
	
	Long ttl(String key);
	
	Long incr(String key);
	
	Long decr(String key);
	
	Long hset(String key, String field, String value);
	
	String hget(String key, String field);
	
	Long hdel(String key, String... field);
	
	Long del(String name);
	
	Long del(byte[] name);

	Map<String, String> hgetAll(String key);

	List<String> hmget(String key, String...fields);

	Long pexpireAt(String key, long millisecondsTimestamp);
	
	boolean update(String key, String value) ;
	

}
