
package com.lb.openprize.service.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lb.openprize.service.IOpenPrizeService;
import com.lb.sys.dao.OpenPrizeMapper;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.OpenPrizeUtils;
import com.lb.sys.tools.StringUtil;

@Service
public class OpenPrizeServiceImpl implements IOpenPrizeService {

	@Autowired
	private OpenPrizeMapper openPrizeMapper;
	private final static Logger logger = LoggerFactory.getLogger(OpenPrizeServiceImpl.class);
	private final static List<String> SPECIAL_LIST = Arrays.asList("大小单双".split(""));

	@Override
	public void handleOpenPrize(int oneTypeId, String issue, String luckNumber, String openTime) {
		AtomicInteger errorCount = new AtomicInteger();
		long startTime = System.currentTimeMillis();
		List<Map<String, Object>> betList = openPrizeMapper.selectBetInfo(oneTypeId, issue);

		int dealNum = betList.size();
		if (betList != null && dealNum > 0) {
			logger.info("=================处理oneTypeId为" + oneTypeId + "的数字彩开奖");
			ExecutorService excut = Executors.newFixedThreadPool(50);
			// 处理数据
			for (Map<String, Object> map : betList) {
				excut.execute(new Runnable() {
					public void run() {
						// 上一期期号
						String beforeOrderId = String.valueOf(map.get("before_orderId"));

						Map<String, Object> beforeMap = openPrizeMapper.selectBeforeOrder(beforeOrderId);
						boolean flag = true;
						if (beforeMap != null && beforeMap.size() > 0) {
							String beforeIsCal = beforeMap.get("is_cal").toString();
							String beforeStatus = beforeMap.get("status").toString();
							flag = "1".equals(beforeIsCal) && ("0".equals(beforeStatus) || "1".equals(beforeStatus));
						}

						// 判断追号的上一期有没有开奖 如果没开则追号的都不开奖
						if (StringUtil.isBlank(beforeOrderId) || (flag && !StringUtil.isBlank(beforeOrderId))) {
							// 三级玩法
							String id = String.valueOf(map.get("playedid"));
							// 注数
							Long action_num = (Long) map.get("action_num");
							// 订单号
							String orderId = String.valueOf(map.get("orderId"));
							// 投注串内容
							String betInfo = String.valueOf(map.get("action_data_str"));
							// 投注金额
							BigDecimal betCoin = new BigDecimal(String.valueOf(map.get("amount")));
							// 单注金额
							BigDecimal sigleCoin = betCoin.divide(new BigDecimal(action_num));
							// 赔率
							String odds = String.valueOf(map.get("bet_odds"));
							// 追号
							String chase = String.valueOf(map.get("chase"));

							// 是否中奖停止追号 0: 否 1: 是
							Integer isStopChase = null;
							if (map.get("is_stop_chase") != null) {
								isStopChase = Integer.valueOf(map.get("is_stop_chase").toString());
							}

							Object winResult = null;
							// 2018年12月29日10:36:42 添加代码结束 PCDD_hh_hh
							Map<String, Object> qryGamePlayedInfo = openPrizeMapper.qryGamePlayedInfo(id);
							String code = qryGamePlayedInfo.get("code").toString();
							String methodStr = qryGamePlayedInfo.get("open_invok").toString();

							if ("PCDD_hh_hh".equals(code) && SPECIAL_LIST.contains(betInfo)) {
								logger.info("[PCDD_hh_hh] 三级玩法数据:" + qryGamePlayedInfo.entrySet());
								Object key = qryGamePlayedInfo.get("special_key");
								Object odd = qryGamePlayedInfo.get("special_odds");
								if (!StringUtil.isBlank(key) && !StringUtil.isBlank(odd)) {
									String special_key = key.toString();
									String special_odds = odd.toString();
									List<String> keyList = Arrays.asList(special_key.split("\\|"));
									List<String> oddList = Arrays.asList(special_odds.split("\\|"));
									List<String> betList = Arrays.asList(luckNumber.split(","));
									int sumNum = 0;
									if (null != keyList && null != oddList && keyList.size() == oddList.size()) {
										for (String itemNum : betList) {
											sumNum += Integer.valueOf(itemNum);
										}
									}
									String sum = String.valueOf(sumNum);
									if (keyList.contains(sum)) {
										odds = oddList.get(keyList.indexOf(sum));
										logger.info("[PCDD_hh_hh] 特殊投注项:" + sum + ",特殊赔率:" + odds);
									}

								}
							}
							// 2018年12月29日10:36:42 添加代码截止
							// String methodStr = openPrizeMapper.selectGamePlayedInfo(id);
							Class<?> clazz = null;
							try {
								clazz = OpenPrizeUtils.class;
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								Method method = clazz.getMethod(methodStr, String.class, String.class);
								winResult = method.invoke(clazz.newInstance(), betInfo, luckNumber);
							} catch (Exception e) {
								e.printStackTrace();
							}

							int winNum = 0;
							if (winResult != null) {
								if (winResult instanceof String) {
									// 处理双色球 大乐透
								} else if (winResult instanceof Integer) {
									winNum = Integer.valueOf(winResult.toString());
								}
							}

							Map<String, Object> param = new HashMap<>();
							BigDecimal bonus = new BigDecimal(0);
							if (winNum >= 0) {
								bonus = new BigDecimal(winNum).multiply(sigleCoin).multiply(new BigDecimal(odds));
							} else {
								bonus = sigleCoin;
								winNum = 1;
							}
							param.put("get_count", winNum);
							param.put("bonus", bonus);
							Date now = DateUtils.getNowDate(new Date());
							param.put("cal_time", now);
							param.put("update_time", now);
							param.put("open_no", luckNumber);
							param.put("orderId", orderId);
							Integer is_cal = 1;
							param.put("is_cal", is_cal);

							int result = 0;
							if (winNum != 0) {
								// 中奖
								param.put("statu", 1);
								try {
									result = openPrizeMapper.updateGameBet(param);
								} catch (Exception e) {
									errorCount.getAndIncrement();
									e.printStackTrace();
								}

								if (result == 0) {
									errorCount.getAndIncrement();
								}

								if (isStopChase == 1) {
									// 撤销追号的单
									Map<String, Object> chaseMap = new HashMap<>();
									chaseMap.put("chase", chase);
									chaseMap.put("statu", 2);
									chaseMap.put("update_time", now);
									chaseMap.put("isStopChase", isStopChase);
									try {
										openPrizeMapper.updateGameBet(chaseMap);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								// 未中奖
								param.put("statu", 0);
								try {
									result = openPrizeMapper.updateGameBet(param);
								} catch (Exception e) {
									errorCount.getAndIncrement();
									e.printStackTrace();
								}
								if (result == 0) {
									errorCount.getAndIncrement();
								}
							}
						}
					}
				});
			}
			excut.shutdown();
			while (true) {
				if (excut.isTerminated()) {
					break;
				}
			}
			logger.info("=================处理数字彩开奖完成,共处理" + dealNum + "条");
		}
		long endTime = System.currentTimeMillis();
		long dealTime = endTime - startTime;
		int recordNum = openPrizeMapper.selectOpenRecord(oneTypeId, issue, luckNumber);
		Map<String, Object> map = new HashMap<>();
		map.put("type", oneTypeId);
		map.put("issue", issue);
		map.put("luckNumber", luckNumber);
		map.put("dealNum", dealNum);
		map.put("dealTime", dealTime);
		map.put("dealErrorNum", errorCount.get());
		map.put("openState", 1);
		map.put("openTime", openTime);
		if (recordNum == 0) {
			openPrizeMapper.insertOpenRecord(map);
		} else {
			openPrizeMapper.updateOpenRecord(map);
		}

	}

}
