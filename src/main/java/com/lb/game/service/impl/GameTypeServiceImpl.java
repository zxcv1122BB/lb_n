package com.lb.game.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.game.model.GameType;
import com.lb.game.model.GameTypeExample;
import com.lb.game.service.GameTypeService;
import com.lb.sys.dao.DigitalGamePlayMapper;
import com.lb.sys.dao.GameTypeMapper;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisCluster;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class GameTypeServiceImpl implements GameTypeService {
	final static String UPDATE_CANADA28_STANDARD_DATE = "UPDATE_CANADA28_STANDARD_DATE";

	final static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	final static String TIME_PATTERN = "HH:mm:ss";
	final static String DATE_PATTERN = "yyyy-MM-dd";
	final static String DEFAULT_START_DATETIME = "2018-12-03 00:03:00";
	final static Integer DEFAULT_START_ISSUE = 2362246;

	final static String COMPARE_START_TIME = "20:00:00";
	final static String COMPARE_COMMON_END_TIME = "21:00:00";
	final static String COMPARE_MONDAY_END_TIME = "22:00:00";

	final static String APPEND_MONDAY_TIME = " 21:57:30";
	final static String APPEND_COMMON_TIME = " 20:57:30";

	final static Integer PLUS_SECOND = 210;
	final static String redisKeyOfDigitalIssue = "LS_RedisKey_DigitalColor_Issue_";
	final static Logger logger = LoggerFactory.getLogger(GameTypeServiceImpl.class);
	@Autowired
	private GameTypeMapper gtm;
	@Autowired
	private GameTypeMapper gameMapper;
	@Autowired
	private DigitalGamePlayMapper digital;
	@Autowired
	private JedisCluster jedis;

	@Override
	public List<GameType> selectByExample(GameTypeExample example) {
		return gtm.selectByExample(example);
	}

	@Override
	public List<Map<String, Object>> qryAllGameType() {
		// TODO Auto-generated method stub
		return gameMapper.qryAllGameType();
	}

	@Override
	public boolean batchCanada28Issue(List<Map<String, Object>> gameTypeList, Map<String, Object> paramMap) {
		jedis.del(UPDATE_CANADA28_STANDARD_DATE);
		jedis.del(redisKeyOfDigitalIssue+"41");
		DateTime deadlineDateTime = new DateTime();
		//String todayDate = deadlineDateTime.toString(DATE_PATTERN);
		String todayDateTime = deadlineDateTime.toString(DATETIME_PATTERN);
		Map<String, Object> game_type_map = new HashMap<>();
		for (Map<String, Object> map2 : gameTypeList) {
			if ("41".equals(map2.get("gameID") + "")) {
				game_type_map = map2;
				break;
			}
		}
		Map<String, JSONObject> allIssueMap = new TreeMap<>();
		String gameName = game_type_map.get("gameName") + "";
		Integer deadline_second = Integer.valueOf(game_type_map.get("deadline") + "");
		Integer delayOpen_second = Integer.valueOf(game_type_map.get("delay_open") + "");
		Integer id1 = Integer.valueOf(game_type_map.get("gameID") + "");

		if (null == paramMap.get("start_time") || paramMap.get("current_issue") == null) {
			logger.error("玩法表配置数据异常:" + paramMap.entrySet());
			return false;
		}
		int ruleIssue = Integer.valueOf(paramMap.get("current_issue") + "");
		String ruleStartStr = paramMap.get("start_time") + "";

		DateTime nowDateTime = DateTime.parse(ruleStartStr, DateTimeFormat.forPattern(DATETIME_PATTERN));
		/**
		 * 截止生成时间,结束生成期号 2018年12月18日11:39:11 修改逻辑-->>> [1]
		 * game_type表中-起始期时间在[20:57,24:00] 根据周一21:57、周二至周日20:57后第一期历史开奖的时间为起始期生成
		 * 到第二天的20:00 [2] game_type表中-起始期时间在[00:00,20:57) 到今天的20:00
		 * 
		 */
		String ruleTime = nowDateTime.toString(TIME_PATTERN);// 起始期开始时间
		String deadline = nowDateTime.toString(DATE_PATTERN) + " " + COMPARE_START_TIME;// 截止日期时间
		
		// 起始期时间>=20:57,截止时间为第二天的20:00
		logger.info("ruleTime:"+ruleTime+",APPEND_COMMON_TIME:"+APPEND_COMMON_TIME+",Flag:"+ruleTime.compareTo(APPEND_COMMON_TIME));
		if (ruleTime.compareTo(APPEND_COMMON_TIME.trim()) >= 0) {
			String deadlineDate = deadlineDateTime.toString(DATE_PATTERN);// 1220
			if (nowDateTime.plusDays(1).toString(DATE_PATTERN).compareTo(deadlineDate) >= 0) {
				deadline = nowDateTime.plusDays(1).toString(DATE_PATTERN)+" " + COMPARE_START_TIME;
			} else {
				logger.error("配置起始期的明天与deadline校验失败:" + nowDateTime.plusDays(1).toString(DATETIME_PATTERN) + ">>>"
						+ deadline);
				return false;
			}
		}
		int addIssue = ruleIssue;
		String previousIssue = "";
		// 此处后续添加超时控制代码
		while (true) {
			//int daysWeek = nowDateTime.getDayOfWeek();
			//String itemStartDate = nowDateTime.toString(DATE_PATTERN);
			//String itemStartTime = nowDateTime.toString(TIME_PATTERN);
			String itemStartDateTime = nowDateTime.toString(DATETIME_PATTERN);
			// 当前期开始时间日期>截止日期，视为跨天终止生成
			if (itemStartDateTime.compareTo(deadline) >= 0) {
				logger.info("当前批次-加拿大28期号生成结束。参数:截止期开始时间" + itemStartDateTime + ",截止日期:" + deadline);
				break;
			}
			// plus时间到本期结束时间
			nowDateTime = nowDateTime.plusSeconds(PLUS_SECOND);
			// 当前期的结束日期、时间、日期时间
			String itemEndDateTime = nowDateTime.toString(DATETIME_PATTERN);
			JSONObject jo = new JSONObject();
			jo.put("end_time", itemEndDateTime);
			jo.put("saleStatus", "ON_SALE");
			jo.put("deadline_second", deadline_second);
			jo.put("start_time", nowDateTime.minusSeconds(deadline_second+PLUS_SECOND).toString(DATETIME_PATTERN));
			jo.put("deadline", nowDateTime.minusSeconds(deadline_second).toString(DATETIME_PATTERN));
			jo.put("expect_open", nowDateTime.plusSeconds(delayOpen_second).toString(DATETIME_PATTERN));
			if (itemStartDateTime.compareTo(ruleStartStr) > 0)
				addIssue += 1;
			jo.put("issue", String.valueOf(addIssue));
			// 完整周期内第一期与上一个完整周期连续上
			if (StringUtils.isEmpty(previousIssue)) {
				JSONObject hisMaxIssueJSON = getHisMaxIssueById1(id1, addIssue + "");
				if (null != hisMaxIssueJSON && hisMaxIssueJSON.size() > 0) {
					hisMaxIssueJSON.put("nextIssue", String.valueOf(addIssue));
					jedis.hset(redisKeyOfDigitalIssue, hisMaxIssueJSON.getString("issue"), hisMaxIssueJSON.toString());
					previousIssue = hisMaxIssueJSON.getString("issue");
				} else {
					previousIssue = "";
				}
			} else {
				allIssueMap.get(previousIssue).put("nextIssue", String.valueOf(addIssue));
			}
			jo.put("previousIssue", previousIssue);
			jo.put("nextIssue", "");
			jo.put("gameName", gameName);
			previousIssue = String.valueOf(addIssue);
			/*
			 * if(updateStandardFlag && itemStartDate.compareTo(yesterdayDateStr)==0) {
			 * jo.put("id1", id1); int num = digital.updateStartIssueInfo(jo);
			 * logger.error("更新加拿大28起始期数据,表game_type更新条数:"+num+",参数:"+jo.entrySet());
			 * if(num==1) { jedis.set(UPDATE_CANADA28_STANDARD_DATE, yesterdayDateStr);
			 * updateStandardFlag=false;//无需再更新 } }
			 */
			allIssueMap.put(addIssue + "", jo);
		}
		allIssueMap.forEach((key, valueMap) -> {
			jedis.hset(redisKeyOfDigitalIssue + id1, key, valueMap.toString());
		});
		int num = digital.updateGameType(paramMap);
		logger.info("状态:"+num+",修改起始期:"+paramMap.entrySet());
		if(num==1) {
			jedis.set(UPDATE_CANADA28_STANDARD_DATE, todayDateTime);
			logger.info("加拿大28更新成功:"+paramMap.entrySet());
		}
			
		return true;
	}

	public JSONObject getHisMaxIssueById1(int id1, String issue) {
		Map<String, String> hgetAll = jedis.hgetAll(redisKeyOfDigitalIssue + id1);
		Set<String> keySet = hgetAll.keySet();
		if (null == keySet || keySet.size() == 0)
			return null;
		String issuesStr = keySet.toString().replace("[", "").replace("]", "").replace(" ", "");
		List<String> arrayList = new ArrayList<>(Arrays.asList(issuesStr.split(",")));
		Collections.sort(arrayList);
		Collections.reverse(arrayList);

		String previousIssue = "";
		for (String itemIssue : arrayList) {
			if (itemIssue.compareTo(issue) < 0) {
				previousIssue = itemIssue;
				break;
			}

		}
		String value = hgetAll.get(previousIssue);
		if (StringUtils.isEmpty(value))
			return null;
		return JSONObject.fromObject(value);
	}
}
