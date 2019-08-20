package com.lb.betting.service.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.betting.controller.BettingOrderInfoController;
import com.lb.betting.model.BettingInfoDetail;
import com.lb.betting.model.BettingOrderDetail;
import com.lb.betting.model.BettingOrderInfo;
import com.lb.betting.model.BettingTicketDetails;
import com.lb.betting.model.GameType;
import com.lb.betting.model.ItemModel;
import com.lb.betting.model.UserDetail;
import com.lb.betting.service.BettingOrderInfoService;
import com.lb.redis.JedisClient;
import com.lb.sys.dao.BettingOrderInfoMapper;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.GenCombination;
import com.lb.sys.tools.OpenPrizeUtils;
import com.lb.sys.tools.StringUtil;

import net.sf.json.JSONObject;

/**
 * @describe
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class BettingOrderInfoServiceImpl implements BettingOrderInfoService {

	private final static Log LOGGER = LogFactory.getLog(BettingOrderInfoController.class);

	@Autowired
	private BettingOrderInfoMapper bettingOrderInfoMapper;
	@Autowired JedisClient redis;
	@Override
	public List<BettingOrderInfo> queryBettingOrderInfo(Map<String, Object> map) {
		return bettingOrderInfoMapper.queryBettingOrderInfo(map);
	}

	@Override
	public UserDetail queryUserInfo(String userName) {
		return bettingOrderInfoMapper.queryUserInfo(userName);
	}

	@Override
	public List<GameType> queryGameType() {
		return bettingOrderInfoMapper.queryGameType();
	}

	@Override
	public BettingOrderDetail queryBettingOrder(Map<String, Object> map) {
		BettingOrderDetail detail = bettingOrderInfoMapper.queryBettingOrder(map);
		if (detail != null && detail.getActionData() != null) {

			List<BettingInfoDetail> matchList = getMatchList(map, detail.getMatchIds(), detail.getType());
			List<ItemModel> codes = null;
			if (matchList != null && matchList.size() > 0) {
				if (detail.getType() != null) {
					if (detail.getType() <= 3) {
						codes = bettingOrderInfoMapper.getCodeLibrary("QuizType");
					} else {
						codes = bettingOrderInfoMapper.getCodeLibrary("QuizBasket");
					}
				}
				Map<String, BettingInfoDetail> matchMap = matchListToMap(matchList);

				detail.setList(matchInfoCollate(detail.getActionData(), matchMap, codes, detail.getType()));
			}
		}
		return detail;
	}

	@Override
	public int cancelTheOrder(Map<String, Object> map) {
		int result = 0;
		try {
			result = bettingOrderInfoMapper.cancelTheOrder(map);
			if (result > 0) {
				Map<String, Object> infoMap = bettingOrderInfoMapper.getUserBetsumInfo(map);
				if(infoMap!=null) {
					String type = infoMap.get("type")!=null?infoMap.get("type").toString():"";
					if ("1".equals(type) || "2".equals(type) || "3".equals(type)
							|| "4".equals(type)) {
						infoMap.put("betId", map.get("betId"));
						infoMap.put("info", "投注撤单");
						infoMap.put("sysId", map.get("uid"));
						infoMap.put("sysName", map.get("username"));
						infoMap.put("coinOperateType", 12);
						result = bettingOrderInfoMapper.userAccountChange(infoMap);
						if (result > 0) {
							result = bettingOrderInfoMapper.insertCancelCoinLog(infoMap);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<BettingOrderInfo> queryFailOrderInfo(Map<String, Object> map) {
		return bettingOrderInfoMapper.queryFailOrderInfo(map);
	}

	@Override
	public List<BettingTicketDetails> getActionDataResult(Map<String, Object> map) {
		Map<String, Object> resultMap = bettingOrderInfoMapper.getActionDataResult(map);
		List<BettingTicketDetails> ticketDetailsList = null;
		if (resultMap != null && resultMap.get("actionDataResult") != null && resultMap.get("seriesName") != null
				&& resultMap.get("seriesList") != null && resultMap.get("type") != null
				&& resultMap.get("times") != null && resultMap.get("matchIds") != null) {

			String actionData = String.valueOf(resultMap.get("actionDataResult"));
			String seriesName = String.valueOf(resultMap.get("seriesName"));
			String seriesList = String.valueOf(resultMap.get("seriesList"));
			String matchIds = String.valueOf(resultMap.get("matchIds"));
			Integer type = Integer.valueOf(resultMap.get("type").toString());
			Integer times = Integer.valueOf(resultMap.get("times").toString());
			ticketDetailsList = new ArrayList<BettingTicketDetails>();
			if (type == 3) {
				type = 1;
			}
			List<BettingInfoDetail> matchList = getMatchList(map, matchIds, type);
			List<ItemModel> codes = null;
			if (matchList != null && matchList.size() > 0) {
				if (type <= 3) {
					codes = bettingOrderInfoMapper.getCodeLibrary("QuizType");
				} else {
					codes = bettingOrderInfoMapper.getCodeLibrary("QuizBasket");
				}
				Map<String, BettingInfoDetail> matchMap = matchListToMap(matchList);

				// detail.setList(matchInfoCollate(detail.getActionData(), matchMap,codes,
				// detail.getType()));
				getBettingDetail(ticketDetailsList, actionData, seriesList, seriesName, type, times, matchMap, codes);
			}

		}
		return ticketDetailsList;
	}

	/** 将传入数据串放入传入的集合中 */
	@SuppressWarnings("unchecked")
	private void getBettingDetail(List<BettingTicketDetails> ticketDetailsList, String info, String seriesList,
			String seriesName, Integer type, Integer times, Map<String, BettingInfoDetail> matchMap,
			List<ItemModel> codes) {
		if (seriesList != null && !seriesList.trim().equals("")// 判断重要数据是否为空
				&& info != null && !info.trim().equals("") && ticketDetailsList != null) {
			String arr[] = seriesList.split(",");// 将获取的串列表进行分解
			String arr1[] = null;
			if (seriesName != null) {
				arr1 = seriesName.split(",");
			}
			// String src_str = "001*home_win,001*home_lose#002*home_win,002*home_lose";
			// info = info.replaceAll("-", "*");
			String[] str_list = info.split("#");// 调用工具所需分解数据
			String[] str_list_new = null;
			ArrayList<String> fixedResult = new ArrayList<String>();// 有胆组合
			if (str_list != null) {
				ArrayList<String> str_array_new = new ArrayList<String>();// 没胆集合
				for (int i = 0; i < str_list.length; i++) {
					if (str_list[i].startsWith("@")) {
						fixedResult.add(str_list[i]);
					} else {
						str_array_new.add(str_list[i]);
					}
				}
				str_list_new = (String[]) str_array_new.toArray(new String[0]);
			}
			String flag = "";
			for (int x = 0, y = 0; x < arr.length; x++, y++) {
				while (y < arr1.length && arr1[y].equals(flag)) {
					y++;
				}
				flag = arr1[y];
				Integer i = null;
				try {
					i = Integer.valueOf(arr[x].toString());// 获取串
				} catch (Exception e) {
					continue;
				}
				if (str_list.length < i - fixedResult.size()) {
					continue;
				}
				if (i > str_list.length) {// 在当前串比匹配数据个数多时，不再需要循环
					break;
				}
				if (i != null && i > 0) {
					ArrayList<String> result = new ArrayList<String>();
					ArrayList<String> resultNew = null;
					GenCombination.GenCom(str_list_new, i - fixedResult.size(), result);
					// 存在设胆情况
					if (fixedResult.size() != 0) {
						resultNew = new ArrayList<String>();
						// 与设胆比赛笛卡尔组合
						for (String tmp : result) {
							ArrayList<String> resultTmp = new ArrayList<String>();
							ArrayList<String> fixedResultCopy = (ArrayList<String>) fixedResult.clone();
							fixedResultCopy.add(tmp);
							GenCombination.Descartes(fixedResultCopy, resultTmp);
							resultNew.addAll(resultTmp);
						}
					} else {
						resultNew = result;
					}
					for (String str1 : resultNew) {// 将获取到的笛卡尔集进行匹配
						List<Object> list1 = new ArrayList<Object>();
						// String str2 = str1.replace("*","-");
						String str2 = str1.replace("%", "#");
						getMatchDetail(list1, matchMap, codes, str2, type);
						ticketDetailsList.add(new BettingTicketDetails(arr1[y], times, list1));
					}
				}

			}

		}
	}

	/** 根据投注数据串分解获取到具体投注数据 */
	@SuppressWarnings("unused")
	private void getMatchDetail(List<Object> list, Map<String, BettingInfoDetail> matchMap, List<ItemModel> codes,
			String info, Integer type) {

		if (!StringUtil.isBlank(info) && list != null && type != null) {
			String arr1[] = info.split("#");// 将选择的每场比赛分解出来

			// Map<String,Object> ticketDetailsMap = new HashMap<>();
			byte guts = 0;
			for (String str1 : arr1) {
				if (str1.startsWith("@")) {// 判断是否选择胆
					str1 = str1.replace("@", "");
					guts = 1;
				} else {
					guts = 0;
				}

				String arr2[] = str1.split(",");// 分解出选择每场比赛所选择的投注内容
				for (String str2 : arr2) {
					Map<String, Object> map = new HashMap<String, Object>();
					String arr3[] = str2.split("-");// 将比赛id-投注选择-赔率 分解出来
					if (type != 2 && arr3.length >= 3) {
						map.put("odds", arr3[2]);
					}
					map.put("quizOptions", codeToName(codes, null, arr3[1], 1));

					// 根据比赛id获取到比赛的队伍信息
					BettingInfoDetail bettingInfoDetail = matchMap.get(arr3[0]);
					if (bettingInfoDetail == null) {
						continue;
					}
					map.put("matchDate", bettingInfoDetail.getMatchDate());
					map.put("matchDateTime", bettingInfoDetail.getMatchDateTime());
					map.put("matchSessions", bettingInfoDetail.getMatchSessions());
					list.add(map);
				}
			}
		}
	}

	/*
	 * private List<BettingInfoDetail> queryBettingMatchDetail(String
	 * actionData,Byte type) { List<BettingInfoDetail> list = new
	 * ArrayList<BettingInfoDetail>(); if(StringUtils.isBlank(actionData)) { return
	 * list; } String arr1[] = actionData.split("#"); byte guts = 0; for(String str1
	 * : arr1) { if(str1.startsWith("@")) { str1 = str1.replace("@", ""); guts = 1;
	 * }else { guts = 0; } //001-home_win-2.99,001-total_goal3-6.01 String arr2[] =
	 * str1.split(","); for(String str2:arr2) { String arr3[] = str2.split("-");//
	 * 将比赛id-投注选择-赔率 分解出来 String tryStr = null;//用于防止nullPointerException try {
	 * //在选择的玩法为精彩足球时才有赔率 分解异常则跳过 if(type != null && type==1) { tryStr=
	 * arr3[0]+arr3[1]+arr3[2]; }else { tryStr= arr3[0]+arr3[1]; } }catch(Exception
	 * e) { continue; } //根据比赛id获取到比赛的队伍信息 BettingInfoDetail bettingInfoDetail =
	 * null; //type 4为竞彩篮球 if(type != null && type == 4) { bettingInfoDetail =
	 * bettingOrderInfoMapper.queryBasketballDetailList(arr3[1],arr3[0]); }else {
	 * bettingInfoDetail =
	 * bettingOrderInfoMapper.queryBettingDetailList(arr3[1],arr3[0]); }
	 * if(bettingInfoDetail != null) { if(type != null && type==1) {
	 * bettingInfoDetail.setOdds(arr3[2]); } bettingInfoDetail.setGuts(guts);
	 * list.add(bettingInfoDetail); } } } return list; }
	 */

	/** 将比赛列表转为比赛id为key的map */
	private List<BettingInfoDetail> getMatchList(Map<String, Object> map, String matchStr, Integer type) {
		List<BettingInfoDetail> list = null;
		if (!StringUtil.isBlank(matchStr) && type != null) {
			String matchIds[] = matchStr.split(",");
			if (matchIds.length > 0) {
				map.put("matchIds", Arrays.asList(matchIds));
				if (type <= 3) {
					list = bettingOrderInfoMapper.queryBettingDetailList(map);
				} else {
					list = bettingOrderInfoMapper.queryBasketballDetailList(map);
				}
			}
		}
		return list;
	}

	private List<BettingInfoDetail> matchInfoCollate(String actionDataStr, Map<String, BettingInfoDetail> matchMap,
			List<ItemModel> codes, Integer type) {
		List<BettingInfoDetail> rseultList = null;
		if (!StringUtil.isBlank(actionDataStr) && matchMap != null && type != null) {

			if (codes != null) {
				String matchInfos[] = actionDataStr.split("#");
				rseultList = new ArrayList<BettingInfoDetail>();
				String quiz = null;
				for (String matchInfo : matchInfos) {
					BettingInfoDetail model = null;
					String preMatch = null;
					if (!StringUtil.isBlank(matchInfo)) {
						// Map<String,Object> map = new HashMap<String,Object>();
						Byte guts = null;
						if (matchInfo.startsWith("@")) {// 判断是否选择胆
							matchInfo = matchInfo.replace("@", "");
							guts = 1;
						} else {
							guts = 0;
						}
						// map.put("guts", guts);
						String matchs[] = matchInfo.split(",");
						for (String match : matchs) {
							if (!StringUtil.isBlank(match)) {
								String infos[] = match.split("-");

								// 用于存投注竞猜项赔率和竞猜结果
								Map<String, Object> guessMap = new HashMap<String, Object>();
								// 用于比赛结果项和竞猜结果
								Byte quizResults = null;
								String quizOptions = null;
								String letScore = null;
								if (quiz == null || model == null || !infos[1].startsWith(quiz)) {
									// list = new ArrayList<Map<String,Object>>();
									quiz = infos[1].substring(0, 1);
									if (matchMap.get(infos[0]) != null) {
										model = matchMap.get(infos[0]).clone();
									} else {
										continue;
									}

									if (model == null) {
										continue;
									}
									model.setGuts(guts);
									rseultList.add(model);
								}

								// 在选择的玩法为精彩足球时才有赔率 分解异常则跳过
								if (type != 2 && infos.length > 2) {
									guessMap.put("odds", infos[2]);// 赔率
								}
								if (type == 2) {// 获取传统竞猜项以及竞猜结果
									switch (infos[1]) {
									case "Y1":// 主队总进球 1801804-Y1-0
										if (model.getHomeScoreTradition() != null
												&& model.getHomeScoreTradition().equals(infos[2])) {
											quizResults = 1;
										} else {
											quizResults = 0;
										}
										model.setQuizSign("主队进球");
										// guessMap.put("quizSign", "主队进球");
										guessMap.put("quizName", infos[2]);
										break;
									case "Y2":// 客队总进球
										if (model.getAwayScoreTradition() != null
												&& model.getAwayScoreTradition().equals(infos[2])) {
											quizResults = 1;
										} else {
											quizResults = 0;
										}
										model.setQuizSign("客队进球");
										// guessMap.put("quizSign", "客队进球");
										guessMap.put("quizName", infos[2]);
										break;
									case "Z1":// 半场胜负平 1801807-Z1-3
										if (model.getHalfResultTratidion() != null
												&& model.getHalfResultTratidion().equals(infos[2])) {
											quizResults = 1;
										} else {
											quizResults = 0;
										}
										model.setQuizSign("半场");
										// guessMap.put("quizSign", "半场");
										guessMap.put("quizName", changeTraditionResult(infos[2]));
										break;
									case "Z2":// 全场
										if (model.getFullResultTradition() != null
												&& model.getFullResultTradition().equals(infos[2])) {
											quizResults = 1;
										} else {
											quizResults = 0;
										}
										model.setQuizSign("全场");
										// guessMap.put("quizSign", "全场");
										guessMap.put("quizName", changeTraditionResult(infos[2]));
										break;
									default:
										if (model.getMatchResult() != null
												&& infos[2].equals(model.getMatchResult().toString())) {
											quizResults = 1;
										} else {
											quizResults = 0;
										}
										guessMap.put("quizName", changeTraditionResult(infos[2]));
										break;
									}

									guessMap.put("quizOptions", infos[2]);// 竞猜项
								} else {
									quizOptions = codeToName(codes, null, infos[1], 2);
									if (type > 3) {
										if (infos.length >= 4) {
											letScore = infos[3].replace("P", "").replace("M", "-");
											quizOptions = quizOptions + "@" + letScore;
										}
									}
									quizResults = checkQuizResult(quiz, quizOptions, model);
									if (letScore != null) {
										guessMap.put("letScore", letScore);// 篮球让分
									}
									guessMap.put("quizName", codeToName(codes, null, infos[1], 1));
									guessMap.put("quizOptions", quizOptions);// 竞猜项
								}

								guessMap.put("quizRxp1", infos[1]);// 竞猜项代号
								guessMap.put("quizResults", quizResults);
								/* 是否重新存入对象标记 */
								boolean flag = false;
								List<Map<String, Object>> guessList = model.getGuessList();
								if (guessList == null) {
									flag = true;
									guessList = new ArrayList<Map<String, Object>>();
								}
								guessList.add(guessMap);
								if (flag) {
									model.setGuessList(guessList);
								}
								if (preMatch == null) {
									preMatch = infos[0];
								}
								if (type == 2) {
									// 获取传统足球的竞猜结果
									if (model.getMatchResult() != null
											&& (model.getResultList() == null || model.getResultList().size() <= 0)) {
										String check = matchInfo + ",";
										List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
										Map<String, Object> checkMap = new HashMap<String, Object>();

										switch (infos[1]) {
										case "Y1":// 主队总进球 1801804-Y1-0
											if (check.contains("-" + model.getHomeScoreTradition() + ",")) {
												checkMap.put("quizResults", 1);
											} else {
												checkMap.put("quizResults", 0);
											}
											// checkMap.put("quizSign", "主队进球");
											checkMap.put("quizName", model.getHomeScoreTradition());
											break;
										case "Y2":// 客队总进球
											if (check.contains("-" + model.getAwayScoreTradition() + ",")) {
												checkMap.put("quizResults", 1);
											} else {
												checkMap.put("quizResults", 0);
											}
											// checkMap.put("quizSign", "客队进球");
											checkMap.put("quizName", model.getAwayScoreTradition());
											break;
										case "Z1":// 半场胜负平 1801807-Z1-3
											if (check.contains("-" + model.getHalfResultTratidion() + ",")) {
												checkMap.put("quizResults", 1);
											} else {
												checkMap.put("quizResults", 0);
											}
											// checkMap.put("quizSign", "半场");
											checkMap.put("quizName",
													changeTraditionResult(model.getHalfResultTratidion()));
											break;
										case "Z2":// 全场
											if (check.contains("-" + model.getFullResultTradition() + ",")) {
												checkMap.put("quizResults", 1);
											} else {
												checkMap.put("quizResults", 0);
											}
											// checkMap.put("quizSign", "全场");
											checkMap.put("quizName",
													changeTraditionResult(model.getFullResultTradition()));
											break;
										default:
											if (check.contains("-" + model.getMatchResult() + ",")) {
												checkMap.put("quizResults", 1);
											} else {
												checkMap.put("quizResults", 0);
											}
											checkMap.put("quizName",
													changeTraditionResult(model.getMatchResult().toString()));
											break;
										}
										resultList.add(checkMap);
										model.setResultList(resultList);
									}
								} else {
									checkWinResult(codes, quiz, quizOptions, model);
								}
							}
						}
					}
				}
			}
		}
		if (rseultList != null) {
			rseultList = rseultList.stream().sorted((o1, o2) -> {
				if (o1 == null || o1.getMatchSessions() == null) {
					return -1;
				} else if (o2 == null || o2.getMatchSessions() == null) {
					return 1;
				}
				return Integer.valueOf(o1.getMatchSessions().hashCode())
						.compareTo(Integer.valueOf(o2.getMatchSessions().hashCode()));
			}).collect(Collectors.toList());
		}
		return rseultList;
	}

	/** 转换传统足球的竞猜结果 3胜 1平 0负 */
	private String changeTraditionResult(String info) {
		if (info != null) {
			if (info.equals("3")) {
				return "胜";
			} else if (info.equals("1")) {
				return "平";
			} else if (info.equals("0")) {
				return "负";
			}
		}
		return null;
	}

	/** 获取开奖结果 eg:(List<ItemModel> A home_win model) */
	private void checkWinResult(List<ItemModel> codes, String quiz, String quizOptions, BettingInfoDetail model) {
		List<Map<String, Object>> list = null;
		if (quiz != null && model != null) {
			// 标记是否赋值
			boolean flag = false;
			list = model.getResultList();
			if (list == null) {
				list = new ArrayList<Map<String, Object>>();
				flag = true;
			}
			if (flag) {
				switch (quiz) {
				case "A":// 足彩 - 不让球结果
					setWinResult(model.getNotLetball(), list, codes);
					break;
				case "B":// 足彩 - 让球结果
					setWinResult(model.getLetball(), list, codes);
					break;
				case "C":// 足彩 - 半场结果
					setWinResult(model.getHalfCourt(), list, codes);
					break;
				case "D":// 足彩 - 比分结果或者单场比分结果
					String winResultD = model.getScoreResult();
					if (winResultD == null) {
						winResultD = model.getScoreResultBj();
					}
					setWinResult(winResultD, list, codes);
					break;
				case "E":// 足彩 - 总进球
					setWinResult(model.getTotalGoal(), list, codes);
					break;
				case "F":// 足彩 - 上下单双
					setWinResult(model.getUpDown(), list, codes);
					break;
				case "G":// 篮彩 - 全场比分结果
					setWinResult(model.getScoreResult(), list, codes);
					break;
				case "H":// 篮彩 - 让分胜负
					setWinResult(model.getLetScore(), list, codes);
					break;
				case "I":// 篮彩 - 大小分
					setWinResult(model.getSizeScore(), list, codes);
					break;
				case "J":// 篮彩 -胜分差
					setWinResult(model.getWinScore(), list, codes);
					break;
				}
			}

			for (Map<String, Object> map : list) {
				if (map.get("quizOptions") != null) {
					if (map.get("quizOptions").toString().equals(quizOptions)) {
						map.put("quizResults", 1);
					} else {
						if (map.get("quizResults") == null || !"1".equals(map.get("quizResults").toString())) {
							map.put("quizResults", 0);
						}
					}
				}

			}
			if (flag) {
				model.setResultList(list);
			}
		}
	}

	/** 给比赛结果赋值 */
	private void setWinResult(String info, List<Map<String, Object>> list, List<ItemModel> codes) {
		if (!StringUtil.isBlank(info) && list != null && codes != null) {
			String totalArr[] = info.split("#");
			String arr1[] = totalArr[0].split("@");
			String arr2[] = null;
			// 当总数据数组的长度大于1时，证明有分在该字符串中
			if (totalArr.length > 1) {
				arr2 = totalArr[1].split("@");
				if (arr1.length != arr2.length) {
					arr2 = null;
				}
			}
			for (int i = 0; i < arr1.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("quizName", codeToName(codes, arr1[i], null, 1));
				// 当让分数的数组存在时，存入让分数
				if (arr2 != null) {
					map.put("letScore", arr2[i].replace("+", ""));
					map.put("quizOptions", arr1[i] + "@" + map.get("letScore"));
				} else {
					map.put("quizOptions", arr1[i]);
				}
				list.add(map);
			}

		}
	}

	/** 获取竞猜结果 eg:(A home_win ...) */
	private Byte checkQuizResult(String quiz, String quizOptions, BettingInfoDetail model) {
		// quizResults
		if (quiz != null && model != null) {
			switch (quiz) {
			case "A":
				if (!StringUtil.isBlank(model.getNotLetball()) && model.getNotLetball().contains(quizOptions)) {
					return 1;
				}
				break;
			case "B":
				if (!StringUtil.isBlank(model.getLetball()) && model.getLetball().contains(quizOptions)) {
					return 1;
				}
				break;
			case "C":
				if (!StringUtil.isBlank(model.getHalfCourt()) && model.getHalfCourt().contains(quizOptions)) {
					return 1;
				}
				break;
			case "D":
				if (!StringUtil.isBlank(model.getScoreResult()) && model.getScoreResult().contains(quizOptions)) {
					return 1;
				}
				if (!StringUtil.isBlank(model.getScoreResultBj()) && model.getScoreResultBj().contains(quizOptions)) {
					return 1;
				}
				break;
			case "E":
				if (!StringUtil.isBlank(model.getTotalGoal()) && model.getTotalGoal().contains(quizOptions)) {
					return 1;
				}
				break;
			case "F":
				if (!StringUtil.isBlank(model.getUpDown()) && model.getUpDown().contains(quizOptions)) {
					return 1;
				}
				break;
			case "G":
				if (!StringUtil.isBlank(model.getScoreResult()) && model.getScoreResult().contains(quizOptions)) {
					return 1;
				}
				break;
			case "H":
				if (!StringUtil.isBlank(model.getLetScore())) {
					String letScoreArr[] = model.getLetScore().split("#");
					String arr1[] = letScoreArr[0].split("@");
					String arr2[] = letScoreArr[1].split("@");
					if (arr1.length == arr2.length) {
						for (int i = 0; i < arr1.length; i++) {
							if ((arr1[i] + "@" + (arr2[i].replace("+", ""))).equals(quizOptions)) {
								return 1;
							}
						}
					}
				}
				break;
			case "I":
				if (!StringUtil.isBlank(model.getSizeScore())) {
					// eg:letscore_lose@letscore_lose#-20@-22
					String SizeScoreArr[] = model.getSizeScore().split("#");
					String arr1[] = SizeScoreArr[0].split("@");
					String arr2[] = SizeScoreArr[1].split("@");
					if (arr1.length == arr2.length) {
						for (int i = 0; i < arr1.length; i++) {
							if ((arr1[i] + "@" + (arr2[i].replace("+", ""))).equals(quizOptions)) {
								return 1;
							}
						}
					}
				}
				break;
			case "J":
				if (!StringUtil.isBlank(model.getWinScore()) && model.getWinScore().contains(quizOptions)) {
					return 1;
				}
				break;
			}
		}
		return 0;
	}

	/** type为类型 name不为空时 1为获取name 2为获取rxp rxp1不为空时 1获取name 2获取id 默认获取name */
	private String codeToName(List<ItemModel> codes, String name, String rxp1, int type) {
		if (codes != null && codes.size() > 0) {
			if (name != null) {
				for (ItemModel model : codes) {
					if (name.equals(model.getItemId())) {
						if (type == 1) {
							return model.getItemName();
						} else if (type == 2) {
							return model.getRxp1();
						}
						return model.getItemName();
					}
				}
			} else if (rxp1 != null) {
				for (ItemModel model : codes) {
					if (rxp1.equals(model.getRxp1())) {
						if (type == 1) {
							return model.getItemName();
						} else if (type == 2) {
							return model.getItemId();
						}
						return model.getItemName();
					}
				}
			}
		}
		return "";
	}

	private Map<String, BettingInfoDetail> matchListToMap(List<BettingInfoDetail> matchList) {
		Map<String, BettingInfoDetail> resultMap = null;
		if (matchList != null && matchList.size() > 0) {
			resultMap = new HashMap<String, BettingInfoDetail>();
			for (BettingInfoDetail model : matchList) {
				if (model != null) {
					resultMap.put(model.getMatchId(), model);
				}
			}
		}
		return resultMap;
	}

	/**
	 * 开奖回滚操作，将已开奖的数据进行业务回滚 1.开奖内容进行清空处理 2.将投注奖金进行回扣
	 */
	@Override
	public int prizeRollbackOperation(Map<String, Object> map) {
		// 从数据库中根据条件获取所有符合的投注信息及该用户信息
		List<Map<String, Object>> infoList = bettingOrderInfoMapper.getCorrespondingGameBets(map);// 获取对应的投注信息
		if (infoList != null && infoList.size() > 0) {
			int result = 0;

			for (Map<String, Object> infoMap : infoList) {
				// 排除传统的投注记录
				if (!"2".equals(infoMap.get("type").toString())) {
					// 将投注记录进行回滚
					result = bettingOrderInfoMapper.correspondingGameBetsInfo(infoMap);
					// 判断是否有中奖的情况，将已中奖的用户的金额进行回滚，当投注订单是未中奖的情况，将返利进行回滚
					if (result > 0) {
						BigDecimal coin = null;
						// 当投注订单是未中奖的情况，验证是否有投注返利
						if ("0".equals(infoMap.get("status").toString())) {
							if (!StringUtil.isBlank(infoMap.get("betRebate"))) {
								coin = new BigDecimal(infoMap.get("betRebate").toString());
								// 当投注返利大于0时，将投注返利进行回滚
								if (coin.compareTo(BigDecimal.ZERO) > 0) {
									infoMap.put("info", "开奖的投注返利回滚");
									// infoMap.put("coinAfter", infoMap.get("rebateAfter"));
									infoMap.put("minusCoin",
											coin.multiply(new BigDecimal(infoMap.get("amount").toString()))
													.divide(new BigDecimal("100")));
								}
							}
						} else if ("1".equals(infoMap.get("status").toString())) {
							coin = new BigDecimal(infoMap.get("coin").toString());
							infoMap.put("minusCoin", coin);
						}
						if (coin != null && coin.compareTo(BigDecimal.ZERO) > 0) {
							result = bettingOrderInfoMapper.correspondingUserInfo(infoMap);
							if (result <= 0) {
								throw new RuntimeException("回滚用户信息失败");
							}
							// 回滚成功后将信息录入账变记录
							result = bettingOrderInfoMapper.insertCancelCoinLog(infoMap);
							if (result <= 0) {
								throw new RuntimeException("回滚用户信息失败");
							}

							/**
							 * 2018-08-02 代理商返点回滚
							 */
							List<Map<String, Object>> coinLogMap = bettingOrderInfoMapper
									.selectCoinLogByOrderId(infoMap);
							if(coinLogMap != null && coinLogMap.size() > 0) {
								for (Map<String, Object> temp : coinLogMap) {
									temp.put("sysId", map.get("sysId"));
									temp.put("sysName", map.get("sysName"));
								}
								result = bettingOrderInfoMapper.correspondingProxy(coinLogMap);
								if (result <= 0) {
									throw new RuntimeException("回滚代理商返点失败");
								}
								// 回滚成功后将信息录入账变记录
								result = bettingOrderInfoMapper.insertProxyCancelCoinLog(coinLogMap);
								if (result <= 0) {
									throw new RuntimeException("记录回滚代理商返点失败");
								}
							}
						}
					}
				}
			}
			return result;
		}
		return -1;
	}

	@Override
	public Map<String, Object> queryNumbersBettingDetail(Map<String, Object> map) {
		Map<String, Object> queryNumbersBettingDetail = bettingOrderInfoMapper.queryNumbersBettingDetail(map);		
		String key = "LS_RedisKey_DigitalColor_Issue_"+queryNumbersBettingDetail.get("type");		
		String field = queryNumbersBettingDetail.get("actionIssue")+"";
		String issueInfo = redis.hget( key, field);
		if(!StringUtil.isBlank(issueInfo)) {
			JSONObject issueJSON = JSONObject.fromObject(issueInfo);
			queryNumbersBettingDetail.put("expect_open", issueJSON.containsKey("expect_open")?issueJSON.get("expect_open"):"");
		}
		return queryNumbersBettingDetail;
	}

	@Override
	public int prizeOperation(Map<String, Object> map) {
		return bettingOrderInfoMapper.prizeOperation(map);
	}

	@Override
	public int numberPrizeOperation(Map<String, Object> map) {
		return bettingOrderInfoMapper.numberPrizeOperation(map);
	}

	@Override
	public int prizeOperationByBet(Map<String, Object> param) {
		// logger.info("=================处理数字彩开奖============= ");
		Map<String, Object> betInfo = bettingOrderInfoMapper.selectBetInfo(param);
		if (betInfo != null && betInfo.size() > 0) {
			String luckNumber = String.valueOf(betInfo.get("luckNumber"));
			if (luckNumber == null) {
				LOGGER.error("开奖号码不存在：" + betInfo);
				return -2;
			}
			// 上一期订单号
			String beforeOrderId = String.valueOf(betInfo.get("beforeOrderId"));
			boolean flag = true;
			if (!StringUtil.isBlank(beforeOrderId)) {
				String beforeIsCal = betInfo.get("beforeIsCal").toString();
				String beforeStatus = betInfo.get("beforeStatus").toString();
				flag = "1".equals(beforeIsCal) && ("0".equals(beforeStatus) || "1".equals(beforeStatus));
			}
			int result = 0;
			// 判断追号的上一期有没有开奖 如果没开则追号的都不开奖
			if (StringUtil.isBlank(beforeOrderId) || (flag && !StringUtil.isBlank(beforeOrderId))) {
				// 三级玩法
				String id = String.valueOf(betInfo.get("playedId"));
				// 注数
				String actionNum = betInfo.get("actionNum").toString();
				// 订单号
				String orderId = betInfo.get("orderId").toString();
				// 投注串内容
				String betDataStr = String.valueOf(betInfo.get("actionDataStr"));
				// 投注金额
				BigDecimal betCoin = new BigDecimal(betInfo.get("amount").toString());
				// 单注金额
				BigDecimal sigleCoin = betCoin.divide(new BigDecimal(actionNum));
				// 赔率
				String odds = String.valueOf(betInfo.get("betOdds"));
				// 追号
				String chase = String.valueOf(betInfo.get("chase"));

				// 是否中奖停止追号 0: 否 1: 是
				String isStopChase = null;
				if (betInfo.get("isStopChase") != null) {
					isStopChase = betInfo.get("isStopChase").toString();
				}

				Object winResult = null;
				String methodStr = bettingOrderInfoMapper.selectGamePlayedInfo(id);
				if (StringUtil.isBlank(methodStr)) {
					LOGGER.error("计算开奖注数方法不存在：" + betInfo);
					return -3;
				}
				Class<?> clazz = OpenPrizeUtils.class;
				try {
					Method method = clazz.getMethod(methodStr, String.class, String.class);
					winResult = method.invoke(clazz.newInstance(), betDataStr, luckNumber);
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
					return -4;
				}
				if (StringUtil.isBlank(winResult)) {
					LOGGER.error("winResult值为：" + winResult);
					return -4;
				}
				int winNum = 0;
				if (winResult instanceof String) {
					// 处理双色球 大乐透 七星彩
				} else if (winResult instanceof Integer) {
					winNum = Integer.valueOf(winResult.toString());
				}

				Map<String, Object> updateMap = new HashMap<>();
				updateMap.put("get_count", winNum);
				// 需要根据三级玩法来处理赔率
				BigDecimal bonus = new BigDecimal(winNum).multiply(sigleCoin).multiply(new BigDecimal(odds));
				updateMap.put("bonus", bonus);
				Date now = DateUtils.getNowDate(new Date());
				updateMap.put("cal_time", now);
				updateMap.put("update_time", now);
				updateMap.put("open_no", luckNumber);
				updateMap.put("orderId", orderId);
				updateMap.put("is_cal", 1);

				if (winNum > 0) {
					// 中奖
					updateMap.put("statu", 1);
					result = bettingOrderInfoMapper.updateGameBet(updateMap);

					if (result <= 0) {
						LOGGER.error("投注开奖订单状态失败");
						throw new RuntimeException("修改状态失败");
					}

					if ("1".equals(isStopChase)) {
						// 撤销追号的单
						Map<String, Object> chaseMap = new HashMap<>();
						chaseMap.put("chase", chase);
						chaseMap.put("statu", 2);
						chaseMap.put("update_time", now);
						chaseMap.put("isStopChase", isStopChase);
						bettingOrderInfoMapper.updateGameBet(chaseMap);
					}
				} else {
					// 未中奖
					updateMap.put("statu", 0);
					result = bettingOrderInfoMapper.updateGameBet(updateMap);
					if (result <= 0) {
						LOGGER.error("修改投注订单状态失败");
						throw new RuntimeException("修改状态失败");
					}
				}
			}

			return result;
		} else {
			return -1;
		}

	}

	@Override
	public Map<String, Object> getRecentlyBetCensus() {
		return bettingOrderInfoMapper.getRecentlyBetCensus();
	}

	@Override
	public Map<String, Object> getRecentlyDepositCensus() {
		return bettingOrderInfoMapper.getRecentlyDepositCensus();
	}

	@Override
	public Map<String, Object> getRecentlyWithdrawCensus() {
		return bettingOrderInfoMapper.getRecentlyWithdrawCensus();
	}

	@Override
	public Map<String, Object> getRecentlyCoinUpdateCensus() {
		return bettingOrderInfoMapper.getRecentlyCoinUpdateCensus();
	}
}
