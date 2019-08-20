 package com.lb.openprize.service.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.lb.openprize.service.IDigitalOpenService;
import com.lb.openprize.service.IOpenPrizeService;
import com.lb.openprize.service.IOpenRandomNumService;
import com.lb.plan.service.LotteryPlanService;
import com.lb.redis.JedisClient;
import com.lb.sys.dao.DigitalOpenMapper;
import com.lb.sys.dao.GameBetsPOJOMapper;
import com.lb.sys.dao.GameTypeMapper;
import com.lb.sys.dao.LotteryPlanMapper;
import com.lb.sys.dao.OpenPrizeMapper;
import com.lb.sys.dao.SysConfigureMapper;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.DigitalOpenTools;
import com.lb.sys.tools.JSONUtils;
import com.lb.sys.tools.OpenPrizeUtils;
import com.lb.sys.tools.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 生成系统彩中奖号码 规则: 1.当投注人数少于等于20人时,随机生成一注号码使得中奖率低于100
 * 2.当投注人数大于20人时,随机生成一注号码使得中奖率低于配置的返奖率 若20s内没开完奖则结束计算随机开一注
 */
@Service
public class OpenRandomNumServiceImpl implements IOpenRandomNumService {
	@Autowired
	private GameBetsPOJOMapper gbMapper;
	@Autowired
	private DigitalOpenMapper digitalOpenMapper;
	@Autowired
	private OpenPrizeMapper openPrizeMapper;
	@Autowired
	private SysConfigureMapper sysConfigureMapper;
	@Autowired
	private IDigitalOpenService digitalOpenService;
	@Autowired
	private IOpenPrizeService openPrizeService;
	@Autowired
	private GameTypeMapper gameTypeMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private LotteryPlanMapper lotteryPlanMapper;
	@Autowired
	private LotteryPlanService lps;
	private final String DATE_PATTERN = "yyyyMMdd";
	private final Integer ALL_MINUTE_DAY = 1440;
	private static final String PLAYED_REDIS_KEY = "LB_REDISKEY_PLAYED";
	private static final String DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE = "DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE";
	private static String gameTypeKey = "game_type";
	private BigDecimal betSum = BigDecimal.ZERO;
	private BigDecimal expectBonus = BigDecimal.ZERO;
	private final static Logger logger = LoggerFactory.getLogger(OpenRandomNumServiceImpl.class);
	private static final String COMMA = ",";
	private static final String PK10_LUCK_NUMBERS[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10" };
	private static final String D3_LUCK_NUMBERS[] = { "0","1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private static final String HK6_LUCK_NUMBERS[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
			"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
			"30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47",
			"48", "49" };

	@Override
	public String openSetLuckNum(String issue, String oneTypeId, String luckNumber) {
		String msg = "开奖成功!";
		boolean flag = isFormerIssue(oneTypeId, issue);
		if (flag) {
			// 开奖
			openPrizeService.handleOpenPrize(Integer.parseInt(oneTypeId), issue, luckNumber,
					DateUtils.getDateString(new Date()));
		} else {
			msg = "开奖失败, 此期开奖时间未到!";
		}
		return msg;
	}

	/**
	 * 预设开奖号码
	 * 	 查看open_data中是否有该期
	 * 		有则update 无则insert 
	 */
	@Override
	public String preinstallLuckNum(Map<String, Object> map) {
		String issue = map.get("issue").toString();
		String oneTypeId = map.get("oneTypeId").toString();
		String luckNumber = map.get("luckNumber").toString();
		String msg = null;
		String dbNumber = openPrizeMapper.sureIssueInDb(issue + "_" + oneTypeId);
		// open_data中是否有该期
		if(StringUtil.isBlank(dbNumber)) {
			msg = "预设的期号为: "+ issue + ", 开奖号码为: "+luckNumber + ", 是否确认预设?";
		}else {
			msg = "预设期号"+ issue + "已开奖, 是否确认更新开奖号码?";
		}
		
		if(!StringUtil.isBlank(map.get("sureFlag")) && "1".equals(map.get("sureFlag").toString())) {
			Map<String, Object> issueInfo = openPrizeMapper.selectLuckNum(oneTypeId);
			if (issueInfo != null && !issueInfo.isEmpty()) {
				// 校验所填数据是否符合规范
				if (checkLuckNum(luckNumber, issueInfo) && checkIssue(issue, issueInfo)) {
					Map<String, Object> paramterMap = new HashMap<>();
					String dbMaxIssue = issueInfo.get("issue").toString();
					if(dbMaxIssue.compareTo(issue) < 0) {
						paramterMap.put("is_preinstall", 0);
					}else {
						paramterMap.put("is_preinstall", 2);
					}
					String openTime = DateUtils.getDateString(new Date());
					//从redis中计算预设开奖号码的开奖时间
					String redisHKey = "LS_RedisKey_DigitalColor_Issue_"+oneTypeId;
					String[]  issueArray = issue.split(",");
					List<String> hmget = jedisClient.hmget(redisHKey, issueArray);
					JSONObject fromObject = JSONObject.fromObject(hmget.get(0));
					Integer addSecond = Integer.valueOf(fromObject.get("deadline_second")+"");
					DateTime dateTime =DateTime.parse(fromObject.get("deadline")+"",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss") );
					openTime = dateTime.plusSeconds(addSecond).toString("yyyy-MM-dd HH:mm:ss");
					insertIntoOpenData(paramterMap, oneTypeId, issue, luckNumber, issue + "_" + oneTypeId,openTime);
					msg = "预设开奖号码成功!";
				}else {
					msg = "预设的期号或开奖号码有误, 请符合规范! ";
				}
			}else {
				msg = "基础数据有误";
			}
		}
		return msg;
	}

	/**
	 * (确保预设的数据和正常数据一致) 判断预设的开奖号码是否补0 && 判断预设的开奖号码个数是否正确
	 */
	private boolean checkLuckNum(String luckNumber, Map<String, Object> map) {
		Integer isAddZero = Integer.valueOf(map.get("is_add_zero").toString());
		Integer lucknumLength = Integer.valueOf(map.get("lucknum_length").toString());
		Integer maxNum = Integer.valueOf(map.get("max_num").toString());
		Integer minNum = Integer.valueOf(map.get("min_num").toString());
		Integer isRepeat = Integer.valueOf(map.get("is_repeat").toString());
		String luckNum = luckNumber.replace(",", " ").replace("+", " ");
		String[] numArr = luckNum.split(" ");

		boolean zeroFlag = true;
		boolean rangeFlag = true;
		for (String num : numArr) {
			char[] numChar = num.toCharArray();
			int length = isAddZero == 0 ? 1 : 2;
			if (numChar.length != length) {
				zeroFlag = false;
				break;
			}
			try {
				int number = Integer.parseInt(num);
				if (number < minNum || number > maxNum) {
					rangeFlag = false;
					break;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				rangeFlag = false;
				break;
			}

		}

		Set<String> set = new HashSet<>(Arrays.asList(numArr));
		// 是否补0 && 开奖数字的个数 && 校验每个预设开奖数字的范围 && 是否重复 (只需要考虑开奖数字不允许重复且预设的开奖数字有重复的情况)
		return zeroFlag && (lucknumLength == numArr.length) && rangeFlag
				&& ((isRepeat == 0 && set.size() != numArr.length) ? false : true);
	}

	/**
	 * 校验预设开奖号码的期号是否符合规范
	 */
	private boolean checkIssue(String issue, Map<String, Object> map) {
		// 期号前缀格式
		String prefixFormat = map.get("prefix_format").toString();
		// 最大期
		Integer maxIssue = Integer.valueOf(map.get("max_issue").toString());
		// 后缀长度
		Integer suffixLength = Integer.valueOf(map.get("suffix_length").toString());
		String suffixIssue = issue.substring(prefixFormat.length(), issue.length());
		try {
			Integer suffix = Integer.parseInt(suffixIssue);
			if (suffix > maxIssue || suffixIssue.length() != suffixLength) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public void openLuckNum(String oneTypeId, String issue) {
		if (!StringUtil.isBlank(oneTypeId) && !StringUtil.isBlank(issue)) {
			String luckNumber = null;
			// 从open_data查出某一玩法某一期的luckNumber
			String openDataId = issue + "_" + oneTypeId;
			luckNumber = openPrizeMapper.sureIssueInDb(openDataId);
			String openTime = DateUtils.getDateString(new Date());
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			// 如果能查到则此期是预设的luckNumber不为空 按照设置的luckNum开奖
			if (StringUtil.isBlank(luckNumber)) {
				// 从game_bets查出投注某一玩法某一期的人数
				int userNums = gbMapper.getLotteryUserNum(oneTypeId, issue);
				if (userNums > 0) {
					Integer userNumInConfigure = null;
					String returnAwardLessUserNum = null;
					String returnAwardMoreUserNum = null;
					String userNumInConfigureOnOff = "0";
					String returnAwardLessUserNumOnOff = "0";
					String returnAwardMoreUserNumOnOff = "0";
					// 需要从sys_configure表中查出人数 <人数的返奖率 >人数的返奖率
					List<Map<String, Object>> configure = sysConfigureMapper.getFfcConfigure();
					for (Map<String, Object> map : configure) {
						if ("userNum".equals(map.get("configure"))) {
							userNumInConfigure = Integer.parseInt(map.get("sys_config1").toString());
							userNumInConfigureOnOff = map.get("on_off").toString();
						}
						if ("returnAwardLessUserNum".equals(map.get("configure"))) {
							returnAwardLessUserNum = map.get("sys_config1").toString();
							returnAwardLessUserNumOnOff = map.get("on_off").toString();
						}
						if ("returnAwardMoreUserNum".equals(map.get("configure"))) {
							returnAwardMoreUserNum = map.get("sys_config1").toString();
							returnAwardMoreUserNumOnOff = map.get("on_off").toString();
						}
					}
					//判断系统彩投注配置是否开启，如果未开启 则随机生成某一彩种开奖号，反之按照相应算法生成。
					if("0".equals(userNumInConfigureOnOff) && "0".equals(returnAwardLessUserNumOnOff)&&
							"0".equals(returnAwardMoreUserNumOnOff)) {
						logger.info("系统彩投注配置未开启，则随机生成 开奖号码。。。");
						luckNumber = getRandomLuckNum(oneTypeId);
					}else {
						if (userNumInConfigure != null && !StringUtil.isBlank(returnAwardLessUserNum)
								&& !StringUtil.isBlank(returnAwardMoreUserNum)) {
							// 从game_bets查出投注某一玩法某一期的全部订单
							List<Map<String, Object>> userLotteryInfo = gbMapper.getUserLotteryInfo(oneTypeId, issue);
							if (userNums <= userNumInConfigure) {
								luckNumber = getRealLuckNum(oneTypeId, userLotteryInfo, returnAwardLessUserNum);
							} else if (userNums > userNumInConfigure) {
								luckNumber = getRealLuckNum(oneTypeId, userLotteryInfo, returnAwardMoreUserNum);
							}
						}
					}
					insertIntoOpenData(paramMap, oneTypeId, issue, luckNumber, openDataId,openTime);
				} else {
					luckNumber = getRandomLuckNum(oneTypeId);
					paramMap.put("is_preinstall", 1);
					insertIntoOpenData(paramMap, oneTypeId, issue, luckNumber, openDataId,openTime);
				}
				// 入库
			} else {
				logger.info("期号:" + issue + ",使用预设的开奖号码" + luckNumber + "进行开奖");
				digitalOpenMapper.updateOpenData(openDataId);
			}
			// 开奖
			openPrizeService.handleOpenPrize(Integer.parseInt(oneTypeId), issue, luckNumber,
					DateUtils.getDateString(new Date()));
			/**
			 * 新增计划
			List<Map<String, Object>> allPlay = null;
			Map<String, Object> map = new HashMap<>();
			map.put("one_type_id", oneTypeId);
			map.put("issue", issue);
			map.put("luck_number", luckNumber);
			if (jedisClient.hash_exists(PLAYED_REDIS_KEY, oneTypeId)) {
				String json = jedisClient.hget(PLAYED_REDIS_KEY, oneTypeId);
				allPlay = JSONArray.fromObject(json);
			} else {
				allPlay = lotteryPlanMapper.getAllPlay(map);
				if (allPlay != null && allPlay.size() > 0) {
					jedisClient.hset(PLAYED_REDIS_KEY, oneTypeId, JSON.toJSONString(allPlay));
				}
			}
			if (allPlay != null && !allPlay.isEmpty()) {
				lps.createLotteryData(map);
			}*/
		}
	}

	private void insertIntoOpenData(Map<String, Object> paramterMap, String oneTypeId, String issue, String luckNumber,
			String openDataId,String openTime) {
		String gameName = null;
		if (jedisClient.hash_exists(gameTypeKey, oneTypeId)) {
			String json = jedisClient.hget(gameTypeKey, oneTypeId);
			Map<String, Object> map = JSONUtils.jsonToMap(json);
			gameName = map.get("gameName").toString();
		} else {
			gameName = gameTypeMapper.selectGameNameById(oneTypeId);
		}
		// 入库 open_data
		paramterMap.put("id", openDataId);
		paramterMap.put("issue", issue);
		paramterMap.put("one_type_id", oneTypeId);
		paramterMap.put("luck_number", luckNumber);
		paramterMap.put("open_time", openTime);
		paramterMap.put("type_name_CN", gameName);
		paramterMap.put("data_str", getRandomDwd(luckNumber,oneTypeId));
		digitalOpenService.insertDigitalOpenData(paramterMap);
		logger.info(loggerInfo(oneTypeId) + "入库成功,期号为:" + issue);
	}

	/**
	 * 根据彩种获取随机开奖号码 一分时时彩：得到一个随机的五位的开奖号码 33 一分快3：得到一个随机的三位的开奖号码 34
	 * 极速PK10：得到一个1-10的开奖号码 37 十分六合彩：得到一个六位的正码和一个特码的开奖号码
	 */
	private String getRandomLuckNum(String oneTypeId) {
		Random rand = new Random();
		StringBuilder sb = new StringBuilder();
		List<String> luckNumberList = null;
		switch (oneTypeId) {
		case "33":
			for (int i = 0; i < 3; i++) {
				sb.append(COMMA);
				sb.append(rand.nextInt(6) + 1);
			}
			break;
		case "34":
			luckNumberList = Arrays.asList(PK10_LUCK_NUMBERS);
			Collections.shuffle(luckNumberList);
			for (String luchNumber : luckNumberList) {
				sb.append(COMMA).append(luchNumber);
			}
			break;
		case "37":
			luckNumberList = Arrays.asList(HK6_LUCK_NUMBERS);
			Collections.shuffle(luckNumberList);
			for (int i = 0; i < 6; i++) {
				sb.append(COMMA).append(luckNumberList.get(i));
			}
			sb.append("+").append(luckNumberList.get(luckNumberList.size() - 1));
			break;
		case "38":
			luckNumberList = Arrays.asList(D3_LUCK_NUMBERS);
			Collections.shuffle(luckNumberList);
			for (int i = 0; i < 3; i++) {
				sb.append(COMMA).append(luckNumberList.get(i));
			}
			break;
		case "39":
			luckNumberList = Arrays.asList(PK10_LUCK_NUMBERS);
			Collections.shuffle(luckNumberList);
			for (String luchNumber : luckNumberList) {
				sb.append(COMMA).append(luchNumber);
			}
			break;
		case "40":
			luckNumberList = Arrays.asList(D3_LUCK_NUMBERS);
			Collections.shuffle(luckNumberList);
			for (int i = 0; i < 3; i++) {
				sb.append(COMMA).append(luckNumberList.get(i));
			}
			break;
		default:
			int randNumber = rand.nextInt(100000);
			String luckNum = String.format("%05d", randNumber);
			for (int i = 0; i < luckNum.length(); i++) {
				sb.append(COMMA);
				sb.append(luckNum.charAt(i));
			}
			break;
		}
		return sb.delete(0, 1).toString();
	}

	private synchronized void betAdd(BigDecimal num) {
		betSum = betSum.add(num);
	}

	private synchronized void bonusAdd(BigDecimal num) {
		expectBonus = expectBonus.add(num);
	}

	// 返奖率和实际中奖率对比 挑出一注开奖号码使得实际中奖率低于返奖率
	private String getRealLuckNum(String oneTypeId, List<Map<String, Object>> userLotteryInfo, String returnAward) {
		String luckNumber = null;
		long usedTimes = 0;
		while (true) {
			betSum = BigDecimal.ZERO;
			expectBonus = BigDecimal.ZERO;
			long startTime = System.currentTimeMillis();
			String tempNum = getRandomLuckNum(oneTypeId);

			// 开线程跑
			ExecutorService excut = Executors.newFixedThreadPool(50);
			for (Map<String, Object> map : userLotteryInfo) {
				excut.execute(new Runnable() {
					public void run() {
						BigDecimal betCoin = new BigDecimal(String.valueOf(map.get("amount")));
						betAdd(betCoin);
						BigDecimal bonus = getExpectBonus(map, tempNum);
						bonusAdd(bonus);
					}
				});
			}
			excut.shutdown();
			while (true) {
				if (excut.isTerminated()) {
					break;
				}
			}

			BigDecimal winRate = expectBonus.divide(betSum, 3, BigDecimal.ROUND_HALF_UP);

			if (winRate.doubleValue() < Double.parseDouble(returnAward) / 100) {
				luckNumber = tempNum;
				break;
			}
			long endTime = System.currentTimeMillis();
			long everyTime = endTime - startTime;
			usedTimes += everyTime;
			// 20s内没跑出开奖号码 随机开一注
			if (usedTimes >= 20000) {
				luckNumber = getRandomLuckNum(oneTypeId);
				break;
			}
		}
		return luckNumber;
	}

	// 根据开奖号码算出奖金
	private BigDecimal getExpectBonus(Map<String, Object> map, String luckNumber) {
		// 三级玩法
		String id = String.valueOf(map.get("playedid"));
		// 注数
		Long action_num = (Long) map.get("action_num");
		// 投注串内容
		String betInfo = String.valueOf(map.get("action_data_str"));
		// 投注金额
		BigDecimal betCoin = new BigDecimal(String.valueOf(map.get("amount")));
		// 单注金额
		BigDecimal sigleCoin = betCoin.divide(new BigDecimal(action_num));
		// 赔率
		String odds = String.valueOf(map.get("bet_odds"));
		String methodStr = openPrizeMapper.selectGamePlayedInfo(id);
		Class<?> clazz = null;
		try {
			clazz = OpenPrizeUtils.class;
		} catch (Exception e) {
			e.printStackTrace();
		}

		int winNum = 0;
		try {
			Method method = clazz.getMethod(methodStr, String.class, String.class);
			winNum = (Integer) method.invoke(clazz.newInstance(), betInfo, luckNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new BigDecimal(winNum).multiply(sigleCoin).multiply(new BigDecimal(odds));
	}

	private  boolean isFormerIssue(String oneTypeId, String issue) {
		String currentIssue = getCurrentIssue(oneTypeId);
		if (issue.compareTo(currentIssue) < 0) {
			return true;
		} else {
			return false;
		}
	}

	private  String getCurrentIssue(String oneTypeId) {
		String issue="";
		switch (oneTypeId) {
			case "35"://二分时时彩
				issue = generateIssue(3, 2, oneTypeId);
				break;
			case "36"://五分时时彩
				issue = generateIssue(3, 5, oneTypeId);
				break;
			case "37"://十分六合彩
				issue = generateIssue(3, 10, oneTypeId);
				break;
			case "38"://极速3D
				issue = generateIssue(3, 10, oneTypeId);
				break;
			case "39"://极速赛车
				issue = generateIssue(4, 3, oneTypeId);
				break;
			case "40"://新加坡28
				issue = generateIssue(4, 3, oneTypeId);
				break;
			default:
				issue = generateIssue(4, 1, oneTypeId);
				break;
		}
		return issue;
	}

	@Override
	public List<Map<String, String>> getSystemColorType() {
		return gameTypeMapper.selectSysColorType();
	}

	@Override
	public List<Map<String, Object>> querySystemColorNum(Map<String, Object> map) {
		return gameTypeMapper.selectSystemColorNum(map);
	}

	@Override
	public String updateSetLuckNum(Map<String, Object> map) {
		String msg = null;
		if (map != null && !map.isEmpty() && !StringUtil.isBlank(map.get("luckNumber"))
				&& !StringUtil.isBlank(map.get("oneTypeId"))) {
			String luckNumber = map.get("luckNumber").toString();
			String oneTypeId = map.get("oneTypeId").toString();
			Map<String, Object> issueInfo = openPrizeMapper.selectLuckNum(oneTypeId);
			boolean numFlag = checkLuckNum(luckNumber, issueInfo);
			if (numFlag) {
				int num = openPrizeMapper.updateSetLuckNum(map);
				if (num > 0) {
					msg = "更新开奖号码成功!";
				} else {
					msg = "更新开奖号码失败!";
				}
			} else {
				msg = "开奖号码设置错误!";
			}
		} else {
			msg = "参数不允许设置为空!";
		}
		return msg;
	}

	public String loggerInfo(String oneTypeId) {
		String name = null;
		if (oneTypeId != null) {
			switch (oneTypeId) {
			case "32":
				name = "一分时时彩";
				break;
			case "33":
				name = "一分快3";
				break;
			case "34":
				name = "极速PK10";
				break;
			case "35":
				name = "二分时时彩";
				break;
			case "36":
				name = "五分时时彩";
				break;
			case "37":
				name = "十分六合彩";
				break;
			case "38":
				name = "极速3D";
				break;
			case "39":
				name = "三分PK10";
				break;
			case "40":
				name = "新加坡28";
				break;
			default:
				break;
			}
		}
		return name;
	}
	
	
	/**
	 * 根据彩种获取相应的走势图
	 */
	private String getRandomDwd(String opneNo,String oneTypeId) {
		String dwd = "";
		switch (oneTypeId) {
		case "32":
			//一分时时彩
		case "35":
			//二分时时彩
		case "36":
			//五分时时彩
			String sscPwd = jedisClient.hget(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,oneTypeId);
			dwd = DigitalOpenTools.getOpenDataStr(opneNo, sscPwd, 0,10,oneTypeId);
			jedisClient.hset(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,oneTypeId,dwd);
			dwd = "{\"dwd\":\""+dwd+"\"}";
			break;
		case "33":
			//一分快3
			String k3pwd = jedisClient.hget(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,"33");
			dwd = DigitalOpenTools.getOpenDataStr(opneNo, k3pwd,3,18,"33");
			jedisClient.hset(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,"33",dwd);
			dwd = "{\"dwd\":\""+dwd+"\"}";
			break;
		case "34":
			//一分PK10
		case "39":
			//三分PK10
			String p10pwd = jedisClient.hget(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,oneTypeId);
			dwd = DigitalOpenTools.getOpenDataStr(opneNo, p10pwd,1,10,oneTypeId);
			jedisClient.hset(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,oneTypeId,dwd);
			dwd = "{\"dwd\":\""+dwd+"\"}";
			break;
		case "38":
			//十分福彩3D
			String d3pwd = jedisClient.hget(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,"38");
			dwd = DigitalOpenTools.getOpenDataStr(opneNo, d3pwd, 0,10,"38");
			jedisClient.hset(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,"38",dwd);
			dwd = "{\"dwd\":\""+dwd+"\"}";
			break;
		case "40":
			//新加坡28
			String kl28pwd = jedisClient.hget(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,"40");
			dwd = DigitalOpenTools.getOpenDataStr(opneNo, kl28pwd, 0,27,"40");
			jedisClient.hset(DIGITAL_SYSTEM_COLOR_OPEN_DATA_ISSUE,"40",dwd);
			dwd = "{\"dwd\":\""+dwd+"\"}";
			break;
		default:
			dwd = "";
			break;
		}
		return dwd;
	}
	
	public String generateIssue(int suffixLength,int intervalMinute,String oneTypeId) {
		if(StringUtil.isBlank(oneTypeId) || intervalMinute<=0 || suffixLength<=0) {
			String error = "LB->com.lb.open.job.OpenRandomNumJob参数异常:  ID1:"+oneTypeId+"后缀长度:"+suffixLength+",开期分钟间隔:"+intervalMinute;
			throw new RuntimeException(error);
		}
		String issue="";
		//后缀长度格式，长度不足左补0
		String suffixFormat = "%0"+suffixLength+"d";
		
		DateTime now = new  DateTime();	
		//now = DateTime.parse("2018-12-26 00:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
		//期号前缀		
		String issuePrefix= now.toString(DATE_PATTERN);		
		
		int minuteOfDay = now.getMinuteOfDay();
		int times = minuteOfDay/intervalMinute;
		//期号后缀		
		String issueSuffix = String.format(suffixFormat, times);
		//仅适用于全天固定开售期，跨天实际为昨天的最后一起结束时间
		if(minuteOfDay==0) {
			issuePrefix = now.minusDays(1).toString(DATE_PATTERN);
			int times_day = ALL_MINUTE_DAY/intervalMinute;
			issueSuffix = String.format(suffixFormat,times_day);
		}		
		issue = issuePrefix+issueSuffix;
		//openLuckNum(oneTypeId, issue);	
		return issue;
	}
}
