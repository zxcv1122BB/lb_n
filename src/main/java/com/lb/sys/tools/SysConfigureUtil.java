/**
 * 
 */
package com.lb.sys.tools;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lb.redis.JedisClient;
import com.lb.sys.dao.SysConfigureMapper;

/**
 * @author wz
 * @describe 关于redis操作的工具类
 * @date 2017年12月5日
 */
@Component
public class SysConfigureUtil {
	
	private final static Log LOGGER = LogFactory.getLog(SysConfigureUtil.class);
	
	private JedisClient jedisClient;
	
	private void checkRedis() {
		if(jedisClient == null) {
			LOGGER.info("jedisClient未导入，自动获取实例");
			jedisClient = (JedisClient) SpringUtil.getBean("jedisClient");
		}
	}
	public Map<String,Object> getConfigure(JedisClient jedisClient,String configure)
	{
		Map<String,Object> resultMap = null;
		this.jedisClient = jedisClient;
		if(configure == null) {
			LOGGER.info("传入配置名称为空");
			return resultMap;
		}
		if(this.jedisClient == null) {
			checkRedis();
		}
		SysConfigureMapper sysConfigureLimitMapper = null;
		if(this.jedisClient == null) {
//			LOGGER.info("自动获取redis实例失败-----链接数据库获取配置");
			try {
				sysConfigureLimitMapper = (SysConfigureMapper) SpringUtil.getBean(SysConfigureMapper.class);
				if(sysConfigureLimitMapper != null) {
					resultMap = sysConfigureLimitMapper.getSysByConfigure(configure);
				}
			} catch (Exception e) {
				LOGGER.info("链接数据库获取配置失败");
			}
			return resultMap;
		}
		String value = this.jedisClient.hget("sys_configure", configure);
		//LOGGER.info("获取map成功::map="+map);
		if(value != null) {
			resultMap = JSONUtils.toMap(value);
		}
		//LOGGER.info("获取resultMap::resultMap="+resultMap);
		if(value == null || resultMap == null) {
//			LOGGER.info("自动获取redis--resultMap实例失败-----链接数据库获取配置");
			try {
				if(sysConfigureLimitMapper == null) {
					sysConfigureLimitMapper = (SysConfigureMapper) SpringUtil.getBean(SysConfigureMapper.class);
				}
				if(sysConfigureLimitMapper != null) {
					resultMap = sysConfigureLimitMapper.getSysByConfigure(configure);
				}
			} catch (Exception e) {
				LOGGER.info("链接数据库获取配置失败");
			}
		}
		return resultMap;
	}
	public Object getConfigureValue(JedisClient jedisClient,String configure,String field) {
		if(field == null) {
			return null;
		}
		Map<String, Object> map = this.getConfigure(jedisClient, configure);
		if(map != null && "on_off".equals(field)) {
			return map.get(field);
		}else if(map != null && "1".equals(map.get("on_off").toString())) {
			return map.get(field);
		}
		return null;
	}
	public Map<String,Object> getRedisMap(JedisClient jedisClient,String tableName,String key)
	{
		Map<String,Object> resultMap = null;
		
		if(tableName ==null || key == null) {
			return resultMap;
		}
		this.jedisClient = jedisClient;
		if(jedisClient == null) {
			checkRedis();
			
		}
		if(this.jedisClient == null) {
			LOGGER.info("自动获取redis实例失败");
			return resultMap;
		}
		String value = this.jedisClient.hget(tableName, key);
	
		if(value != null) {
			resultMap = JSONUtils.toMap(value);
		}
		return resultMap;
	}
	public Object getRedisValue(JedisClient jedisClient,String tableName,String key,String field) {
		if(field == null) {
			return null;
		}
		Map<String, Object> map = getRedisMap(jedisClient, tableName,key);
		if(map == null) {
			return null;
		}
		return map.get(field);
	}
	
	/**是否使用缓存数据 true 为开启 false为停止*/
	public boolean isUseCache(JedisClient jedisClient) {
		this.jedisClient = jedisClient;
		if(this.jedisClient == null) {
			checkRedis();
		}
		if(this.jedisClient == null) {
			return false;
		}
		String value = null;
		try {
			value = this.jedisClient.get("isUseCacheData");
		} catch (Throwable e) {
			LOGGER.error(e.getMessage());
			return false;
		}
		if(value != null && "1".equals(value)) {
			return true;
		}
		return false;
	}
}
