package com.lb.plan.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.lb.plan.service.LotteryPlanService;
import com.lb.redis.JedisClient;
import com.lb.sys.dao.LotteryPlanMapper;
import com.lb.sys.tools.LotteryPlanUtils;
import com.lb.sys.tools.LuckNumUtils;
import com.lb.sys.tools.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class LotteryPlanServiceImpl implements LotteryPlanService {
	
	private final static Logger logger = LoggerFactory.getLogger(LotteryPlanServiceImpl.class);
	
	private static final String PLAN_MODEL = "a b [c] 第d期 e期 等开";
	private static final String ISSUE_REDIS_KEY = "LS_RedisKey_DigitalColor_Issue_";
	private static final String PLAYED_REDIS_KEY = "LB_REDISKEY_PLAYED";

	@Autowired
	private LotteryPlanMapper lotteryPlanMapper;
	
	@Autowired 
	private JedisClient jedisClient;
	
	@SuppressWarnings("unchecked")
	@Override
	public void createLotteryData(Map<String, Object> param) {
		String type = param.get("one_type_id").toString();
		String issue = param.get("issue").toString();
		List<Map<String, Object>> allPlay = null;
		
		if(jedisClient.hash_exists(PLAYED_REDIS_KEY, type)) {
			String json = jedisClient.hget(PLAYED_REDIS_KEY, type);
			allPlay = JSONArray.fromObject(json);
		}else {
			allPlay = lotteryPlanMapper.getAllPlay(param);
			if(allPlay != null && allPlay.size() > 0) {
				jedisClient.hset(PLAYED_REDIS_KEY, type, JSON.toJSONString(allPlay));
			}
		}
		
		if(allPlay != null && !allPlay.isEmpty()) {
			List<Map<String, Object>> plans = lotteryPlanMapper.selectIssuePlan(param);
			if (plans == null || allPlay.size() != plans.size()) {
				if(plans == null) {
					plans = new ArrayList<>();
				}
				for(Map<String,Object> play : allPlay) {
					boolean flag = true;
					for(Map<String,Object> planMap : plans) {
						if((planMap.get("playid") + "").equals(play.get("playid") + "")) {
							flag = false;
							break;
						}
					}
					if(flag) {
						play.put("issue", issue);
						play.put("type", type);
						// 预设计划
						generationPlanInit(play);
						if("1".equals(play.get("valid"))) {
							int result = lotteryPlanMapper.insertLotteryPlan(play);
							if(result > 0) {
								plans.add(play);
							}
						}
					}
				}
			}

			String luckNumber = param.get("luck_number").toString();
			for (Map<String, Object> map : plans) {
				String playid = map.get("playid") + "";
				Map<String,Object> planInfo = lotteryPlanMapper.selectPlanMethod(playid);
				if(planInfo == null) {
					logger.error("无该三级玩法信息("+ playid +")");
					continue;
				}
				String methodStr = planInfo.get("plan_invoke") + "";
				if (!StringUtil.isBlank(methodStr)) {
					String plan_content = map.get("plan_content") + "";
					String plan_num = map.get("plan_num") + "";
					String plan_flash = "0";
					Class<?> clazz = LotteryPlanUtils.class;
					try {
						Method method = clazz.getMethod(methodStr, String.class, String.class);
						int result = (int) method.invoke(clazz.newInstance(), luckNumber, plan_num);
						if (result == 1) {
							plan_content = plan_content.replace("等开", luckNumber + " 中");
							plan_flash = "1";
						} else {
							plan_content = plan_content.replace("等开", luckNumber + " 挂");
							plan_flash = getFlashValue(map);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 准备数据 入库lottery_data
					Map<String, Object> data = new HashMap<>();
					data.put("playid", playid);
					data.put("plan_content", plan_content);
					data.put("plan_flash", plan_flash);
					data.put("type", type);
					data.put("issue", issue);
					data.put("luck_num", luckNumber);
					int result = lotteryPlanMapper.insertLotteryData(data);
					// 生成计划 入库lottery_plan
					if (result > 0) {
						data.put("plan_name", planInfo.get("plan_name"));
						data.put("num_invoke",  planInfo.get("num_invoke"));
						data.put("suffix_length",  planInfo.get("suffix_length"));
						generationPlan(data);
						if("1".equals(data.get("valid"))) {
							lotteryPlanMapper.insertLotteryPlan(data);
						}
					}
				}
			}
			logger.info("彩种:" + type + ", 成功生成最新一期期号的计划");
		}

	}

	private static String getFlashValue(Map<String, Object> plans) {
		String count = plans.get("plan_count") + "";
		if ("3".equals(count)) {
			return "1";
		} else {
			return "0";
		}
	}
	
	/**
	 * 根据开奖生成计划
	 * */
	@SuppressWarnings("unchecked")
	private void generationPlan(Map<String, Object> param) {
		if(param != null && param.get("num_invoke") != null) {
			/**
			 * 记得count++计算及期号生成
			 * 在中奖或者第三期未中奖才会生成下一轮的一批起始期次
			 * */
			//"a b [c] 第d期 e期 等开"
			String a = null;
			String c = null;
			String e = null;
			String d = null;
			int length = 3;
			if(param.get("suffix_length") != null) {
				length = Integer.valueOf(param.get("suffix_length") + "");
			}
			String issueJson = jedisClient.hget(ISSUE_REDIS_KEY + param.get("type") , length > (param.get("issue")+"").length()?"0"+param.get("issue"):param.get("issue") + "");
			String nextIssue = null;
			String startNo = null;
			if(issueJson != null) {
				Map<String,Object> issueMap = JSONObject.fromObject(issueJson);
				nextIssue = issueMap.get("nextIssue") + "";
				if(length > nextIssue.length()) {
					nextIssue = "0"+nextIssue;
				}
				startNo = nextIssue.substring(nextIssue.length() - length, nextIssue.length());
				startNo = startNo.length()>3 && startNo.startsWith("0") ? startNo.substring(1) : startNo ;
			}else {
				logger.error("生成玩法("+ param.get("playid") +")计划失败==plan_name:" + param.get("plan_name"));
				param.put("valid", "0");
				return;
			}
			param.put("issue", nextIssue.startsWith("0")?nextIssue.substring(1):nextIssue);
			if("1".equals(param.get("plan_flash"))) {
				String methodStr = param.get("num_invoke") + "";
				Class<?> clazz = LuckNumUtils.class;
				String result = null;
				try {
					Method method = clazz.getMethod(methodStr);
					result = String.valueOf(method.invoke(clazz.newInstance()));
				} catch (Exception ex) {
					logger.error(ex.getMessage());
					param.put("valid", "0");
					return;
				}
				if(result != null) {
					issueJson = jedisClient.hget(ISSUE_REDIS_KEY + param.get("type") , nextIssue);
					Map<String,Object> issueMap = JSONObject.fromObject(issueJson);
					String lastIssue = issueMap.get("nextIssue") + "";
					issueJson = jedisClient.hget(ISSUE_REDIS_KEY + param.get("type") , lastIssue);
					issueMap = JSONObject.fromObject(issueJson);
					lastIssue = issueMap.get("nextIssue") + "";
					String endNo = lastIssue.substring(lastIssue.length() - length, lastIssue.length());
					endNo = endNo.length()>3 && endNo.startsWith("0") ? endNo.substring(1) : endNo ;
					a = startNo + "-" + endNo;
					c = result;
					d = "1";
				}else {
					logger.error("生成玩法("+ param.get("playid") +")计划失败==result:" + result);
					param.put("valid", "0");
					return;
				}
			}else {
				String planContent = param.get("plan_content") + "";
				if(planContent != null) {
					String regex = "第(\\d+?)期";
					Pattern compile = Pattern.compile(regex);
					Matcher matcher = compile.matcher(planContent);
					int count = 1;
					if (matcher.find()) {
						count = Integer.valueOf(matcher.group(1));
					}
					String datas[] = planContent.split(" ");
					a = datas[0];
					c = planContent.split("\\]")[0].split("\\[")[1];
					d = String.valueOf(count + 1);
				}
				//a plan_count 
			}
			param.put("plan_num", c);
			param.put("plan_count", d);
			e = startNo;
			param.put("plan_content", PLAN_MODEL.replace("a", a).replace("b", param.get("plan_name") + "").replace("c", c)
					.replace("d", d).replace("e", e));
			param.put("valid", "1");
		}else {
			param.put("valid", "0");
			logger.error("生成玩法("+ param.get("playid") +")计划失败");
		}
	}
	/**
	 * 生成初始计划
	 * */
	@SuppressWarnings("unchecked")
	private void generationPlanInit(Map<String, Object> param) {
		if(param != null && param.get("num_invoke") != null) {
			/**
			 * 记得count++计算及期号生成
			 * 在中奖或者第三期未中奖才会生成下一轮的一批起始期次
			 * */
			//"a b [c] 第d期 e期 等开"
			String a = null;
			String c = null;
			String e = null;
			String d = null;
			int length = 3;
			if(param.get("suffix_length") != null) {
				length = Integer.valueOf(param.get("suffix_length") + "");
			}
			String nextIssue = param.get("issue") + "";
			if(length > nextIssue.length()) {
				nextIssue = "0"+nextIssue;
			}
			String startNo = null;
			startNo = nextIssue.substring(nextIssue.length() - length, nextIssue.length());
			startNo = startNo.length()>3 && startNo.startsWith("0") ? startNo.substring(1) : startNo ;
			e = startNo;
			String methodStr = param.get("num_invoke") + "";
			Class<?> clazz = LuckNumUtils.class;
			String result = null;
			try {
				Method method = clazz.getMethod(methodStr);
				result = String.valueOf(method.invoke(clazz.newInstance()));
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				param.put("valid", "0");
				return;
			}
			if(result != null) {
				if(!jedisClient.hash_exists(ISSUE_REDIS_KEY + param.get("type") , nextIssue)) {
					String error = ISSUE_REDIS_KEY + param.get("type")+"_"+nextIssue;
					logger.error("计划生成期号："+error+"，错误。。。。");
					param.put("valid", "0");
					return;
				}
				String issueJson = jedisClient.hget(ISSUE_REDIS_KEY + param.get("type") , nextIssue);
				Map<String,Object> issueMap = JSONObject.fromObject(issueJson);
				String lastIssue = issueMap.get("nextIssue") + "";
				issueJson = jedisClient.hget(ISSUE_REDIS_KEY + param.get("type") , lastIssue);
				issueMap = JSONObject.fromObject(issueJson);
				lastIssue = issueMap.get("nextIssue") + "";
				String endNo = lastIssue.substring(lastIssue.length() - length, lastIssue.length());
				endNo = endNo.length()>3 && endNo.startsWith("0") ? endNo.substring(1) : endNo ;
				a = startNo + "-" + endNo;
				c = result;
				d = "1";
				param.put("plan_num", result);
				param.put("plan_count", "1");
				param.put("plan_flash", "1");
			}else {
				logger.error("生成玩法("+ param.get("playid") +")计划失败==result:" + result);
				param.put("valid", "0");
				return;
			}
			param.put("plan_content", PLAN_MODEL.replace("a", a).replace("b", param.get("plan_name") + "").replace("c", c)
					.replace("d", d).replace("e", e));
			param.put("valid", "1");
		}else {
			param.put("valid", "0");
			logger.error("生成玩法("+ param.get("playid") +")计划失败");
		}
	}
}
