package com.lb.sys.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lb.sys.tools.ParseProFileUtil;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**  
 */
@Configuration
@EnableCaching
public class JedisClusterConfig {

	Logger logger = LoggerFactory.getLogger(JedisClusterConfig.class);

	/** redis集群节点 */
	@Value("${spring.redis.pool.nodes}")
	private String nodes;
	/** 连接超时时间 */
	@Value("${spring.redis.pool.timeout}")
	private int timeout;
	/** 重连次数 */
	@Value("${spring.redis.pool.maxAttempts}")
	private int maxAttempts;
	// 可用连接实例的最大数量
	@Value("${spring.redis.pool.maxTotal}")
	private int maxTotal;
	// 最大空闲时间
	@Value("${spring.redis.pool.maxIdle}")
	private int maxIdle;
	// 最小空闲时间
	@Value("${spring.redis.pool.minIdle}")
	private int minIdle;
	// 最大等待时间
	@Value("${spring.redis.pool.maxWaitMillis}")
	private long maxWaitMillis;
	// 开启jmx
	@Value("${spring.redis.pool.jmxEnabled}")
	private boolean jmxEnabled;

	@Bean
	public JedisCluster getJedisCluster() {
		Map<String, String> map = new HashMap<>();
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("windows")) {
			map = ParseProFileUtil.parseProFile("dbData.properties", true);
		} else if (os.contains("linux")) {
			map = ParseProFileUtil.getProperties("/ls/application/conf/dbData.properties", true);
		}
		nodes = nodes.replace("@reidsURL@", map.get("reidsURL"));
		System.err.println(nodes);
		String[] serverArray = nodes.split(",");// 获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
		Set<HostAndPort> nodes = new HashSet<>();
		for (String ipPort : serverArray) {
			String[] ipPortPair = ipPort.split(":");
			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
		}
		return new JedisCluster(nodes, timeout, maxAttempts, getPoolConfig());
	}

	private GenericObjectPoolConfig getPoolConfig() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		// 可用连接实例的最大数目，默认值为8；
		poolConfig.setMaxTotal(maxTotal);
		// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMinIdle(minIdle);
		// JedisPool.borrowObject最大等待时间
		poolConfig.setMaxWaitMillis(maxWaitMillis);
		// 开启jmx
		poolConfig.setJmxEnabled(jmxEnabled);
		return poolConfig;
	}

}