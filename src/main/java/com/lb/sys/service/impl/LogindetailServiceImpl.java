package com.lb.sys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.lb.onlineSchedule.cache.OnlineDetailCache;
import com.lb.redis.JedisClient;
import com.lb.sys.dao.LoginDetailMapper;
import com.lb.sys.service.ILogindetailService;
import com.lb.sys.tools.JSONUtils;


@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class LogindetailServiceImpl implements ILogindetailService {

	private final static Log LOGGER = LogFactory.getLog(LogindetailServiceImpl.class);
	@Autowired
	private LoginDetailMapper loginDetailMapper;
	@Autowired
	private JedisClient jedisClient;
	
//	private static final String onlineKey = "onlineHeartbeat";
	private static final String onlineKey = "onlineUsers";

	private static final String downUserKey = "forceUserDown";
	
	public Integer insertSelective(Map<String,Object> loginDetail) {
		
		return loginDetailMapper.insertSelective(loginDetail);
	}

	
	public Integer updateByUsernameSelective(Map<String,Object> loginDetail) {
		
		return loginDetailMapper.updateByUsernameSelective(loginDetail);
	}


	@Override
	public Map<String,Object> getDepositAndWithdrawCount() {
		Integer state = 1;
		Integer depositCount =loginDetailMapper.getDepositCount(state);
		Integer withdrawCount =loginDetailMapper.getWithdrawCount(state);
		Map<String,Object> map =  new HashMap<>();
		map.put("depositCount", depositCount==null?0:depositCount);
		map.put("withdrawCount", withdrawCount==null?0:withdrawCount);
		return map;
	}


	@Override
	public Integer updateLastTimeAndIp(Map<String, Object> param) {
		return loginDetailMapper.updateLastTimeAndIp(param);
	}
	
	/**
	 * 强制下线记录
	 */
	// 更新数据库登陆详情表,更新用户信息表中最后登录时间和ip
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void offlineHandle(String userName) {
		try {
//			ConcurrentHashMap<String, Map<String, String>> requestDetail = OnlineDetailCache.getRequestDetail();
			Map<String, Object> userMap = JSONUtils.jsonToMap(jedisClient.hget(onlineKey, userName));
			Map<String, Object> map = new HashMap<>();
			map.put("username", userName);
//			map.put("loginTime", requestDetail.get(userName).get("loginTime"));
			map.put("loginTime", userMap.get("loginTime"));
			map.put("logoutTime", new Date());
//			map.put("ip", requestDetail.get(userName).get("ip"));
			map.put("ip", userMap.get("ip"));
			loginDetailMapper.updateByUsernameSelective(map);
			loginDetailMapper.updateLastTimeAndIp(map);
//			requestDetail.remove(userName);
			jedisClient.hdel(onlineKey, userName);
			jedisClient.hset(downUserKey, userName, userName);
		}catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 手动捕获异常后,事物会回滚
			LOGGER.info(e.getMessage());
		}
	}


	@Override
	public Integer insertAgencySelective(Map<String, Object> map) {
		return loginDetailMapper.insertAgencySelective(map);
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void agencyofflineHandle(String userName) {
		try {
			ConcurrentHashMap<String, Map<String, String>> agencyDetail = OnlineDetailCache.getAgencyDetail();
			Map<String, Object> map = new HashMap<>();
			map.put("username", userName);
			map.put("loginTime", agencyDetail.get(userName).get("loginTime"));
			map.put("logoutTime",new Date());
			map.put("ip", agencyDetail.get(userName).get("ip"));
			loginDetailMapper.updateAgencyByUsernameSelective(map);
			loginDetailMapper.updateAgencyLastTimeAndIp(map);
			agencyDetail.remove(userName);
		}catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 手动捕获异常后,事物会回滚
			LOGGER.info(e.getMessage());
		}
	}

}
