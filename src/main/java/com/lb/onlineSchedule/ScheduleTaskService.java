package com.lb.onlineSchedule;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.lb.onlineSchedule.cache.OnlineDetailCache;
import com.lb.sys.dao.LoginDetailMapper;
import com.lb.sys.tools.DateUtils;

@Component
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class ScheduleTaskService implements IScheduleTaskService {

	private final static Log LOGGER = LogFactory.getLog(ScheduleTaskService.class);
	@Autowired
	private LoginDetailMapper loginDetailMapper;
	
	@Value("${ScheduleTaskService.fixedRateTime}")
	private long fixedRateTime ;

	// 上一次的在线记录
	private static ConcurrentHashMap<String, Map<String, String>> lastMap = OnlineDetailCache.getRequestDetail();
	// 上一次的代理在线记录
	private static ConcurrentHashMap<String, Map<String, String>> lastAgencyMap = OnlineDetailCache.getAgencyDetail();

//	@Scheduled(fixedRate = 1000 * 70) // 每隔70秒计时一次
	@Scheduled(cron = "${ScheduleTaskService.cron}")
	public void onloginSchedule() {
		// 最近一次的在线记录
		if (lastMap != null&&!lastMap.isEmpty()) {
			Iterator<String> lastKeys = lastMap.keySet().iterator();
			while (lastKeys.hasNext()) {
				String lastKey = lastKeys.next();
				Map<String, String> map = lastMap.get(lastKey);
				if (DateUtils.getMillisecond(map.get("lastTime"), new Date()) >= fixedRateTime) {
					try {
							Map<String, Object> paramMap = new HashMap<>();
							paramMap.put("username", lastKey);
							paramMap.put("loginTime", map.get("loginTime"));
							paramMap.put("logoutTime", new Date());
							loginDetailMapper.updateByUsernameSelective(paramMap);
							paramMap.put("ip", map.get("ip"));
							Integer result = loginDetailMapper.updateLastTimeAndIp(paramMap);
							LOGGER.info("用户:" + lastKey + "在"+(fixedRateTime/1000)+"秒内心跳包没有访问,将此时记录为退出时间"+paramMap.get("ip"));
							if (result>=0) {//更新最后登录时间成功后,将缓存中的登陆记录删除掉
								lastMap.remove(lastKey);
							}
					}catch (Exception e) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 手动捕获异常后,事物会回滚
						LOGGER.info(e.getMessage());
					}
				}
			}
		}
	}
//	@Scheduled(fixedRate = 1000 * 70) // 每隔70秒计时一次
	@Scheduled(cron = "${ScheduleTaskService.cron}")
	public void onAgencyloginSchedule() {
		// 最近一次的在线记录
		if (lastAgencyMap != null&&!lastAgencyMap.isEmpty()) {
			Iterator<String> lastKeys = lastAgencyMap.keySet().iterator();
			while (lastKeys.hasNext()) {
				String lastKey = lastKeys.next();
				Map<String, String> map = lastAgencyMap.get(lastKey);
				if (DateUtils.getMillisecond(map.get("lastTime"), new Date()) >= fixedRateTime) {
					try {
						Map<String, Object> paramMap = new HashMap<>();
						paramMap.put("username", lastKey);
						paramMap.put("loginTime", map.get("loginTime"));
						paramMap.put("logoutTime", new Date());
						loginDetailMapper.updateAgencyByUsernameSelective(paramMap);
						paramMap.put("ip", map.get("ip"));
						Integer result = loginDetailMapper.updateAgencyLastTimeAndIp(paramMap);
						LOGGER.info("用户:" + lastKey + "在"+(fixedRateTime/1000)+"秒内心跳包没有访问,将此时记录为退出时间"+result);
						if (result>=0) {//更新最后登录时间成功后,将缓存中的登陆记录删除掉
							lastAgencyMap.remove(lastKey);
						}
						/*if(jedisClient == null) {
							jedisClient = (JedisClient) SpringUtil.getBean("jedisClient");
						}
						if(jedisClient.hash_exists("agencySignalLogin",lastKey)) {
							jedisClient.hdel("agencySignalLogin" , lastKey);
						}*/
					}catch (Exception e) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 手动捕获异常后,事物会回滚
						LOGGER.info(e.getMessage());
					}
				}
				
				
			}
		}
	}
	
}