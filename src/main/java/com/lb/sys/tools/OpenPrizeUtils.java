package com.lb.sys.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * 开奖工具类
 * 
 * @author ASUS
 *
 */
public class OpenPrizeUtils {
	// 将string类型的数组转换成int
	public static int[] StringToInt(String[] arrs) {
		int[] ints = new int[arrs.length];
		for (int i = 0; i < arrs.length; i++) {
			ints[i] = Integer.parseInt(arrs[i]);
		}
		return ints;
	}

	/**
	 * 用于双色球的开奖 双色球 一等奖 6红1蓝 二等奖 6红 三等奖 5红1蓝 四等奖 5红/4红1蓝 五等奖 4红/3红1蓝 六等奖
	 * 2红1蓝/1红1蓝/1蓝
	 * 
	 * @param bet
	 *            投注串 格式 1,2,3,4,5,6|6 或1,2,3|4,5,6,7|7
	 * @param target
	 *            开奖号码 格式1,2,3,4,5,6|7
	 * @return 结果样式 6:1@6:0@5:1@5:0@5:1@5:0@5:1@5:0@5:1@5:0@4:1@4:0
	 */
	public static String openPrize4Ssq(String bet, String target) {
		if (bet == null || "".equals(bet) || target == null || "".equals(target)) {
			return "0:0";
		}
		String[] targets = target.split("\\|"); // 1,2,3,4,5,6 7
		String[] bets = bet.split("\\|"); // 1,2,3 4,5,6 7 1,2,3,4,5,6,7 7

		List<List<String>> redList = new ArrayList<>();
		List<List<String>> blueList = new ArrayList<>();
		if (bets.length == 2) {
			// 情况一:普通投注 将bets[0]和bets[1]列出所有组合
			redList = getCombination(bets[0], 6);
			blueList = getCombination(bets[1], 1);
		} else if (bets.length == 3) {
			// 情况二:胆拖投注 将bets[1]和bets[2]列出所有组合 bets[1]的组合根据bets[0]的数量决定
			redList = getCombination(bets[1], 6 - bets[0].split(",").length);
			blueList = getCombination(bets[2], 1);
		} else {
			return "0:0";
		}

		// 判断红球和蓝球的投注串不为空
		if (redList == null || redList.size() == 0 || blueList == null || blueList.size() == 0) {
			return "0:0";
		}

		if (bets.length == 3) {
			String[] danStr = bets[0].split(",");
			for (int j = 0; j < danStr.length; j++) {
				for (int i = 0; i < redList.size(); i++) {
					List<String> tuo = redList.get(i);
					tuo.add(danStr[j]);
				}
			}
		}

		String result = openPrizeResult(targets, redList, blueList);
		return getSsqResult(result);
	}

	private static String getSsqResult(String kj) {
		String[] kjs = kj.split("@");
		List<String> resultList = new ArrayList<>();
		for (int i = 0; i < kjs.length; i++) {
			String str = kjs[i];
			switch (str) {
			case "6:1":
				resultList.add("一等奖");
				break;
			case "6:0":
				resultList.add("二等奖");
				break;
			case "5:1":
				resultList.add("三等奖");
				break;
			case "5:0":
				resultList.add("四等奖");
				break;
			case "4:1":
				resultList.add("四等奖");
				break;
			case "4:0":
				resultList.add("五等奖");
				break;
			case "3:1":
				resultList.add("五等奖");
				break;
			case "2:1":
				resultList.add("六等奖");
				break;
			case "1:1":
				resultList.add("六等奖");
				break;
			case "0:1":
				resultList.add("六等奖");
				break;
			default:
				break;
			}
		}
		return StringUtils.join(resultList, ",");
	}

	/**
	 * 用于大乐透的开奖
	 * 
	 * @param bet
	 *            投注串 格式 1,2,3,4,5,6|5,6 或1,2,3|4,5,6,7|7,8
	 * @param target
	 *            开奖号码 格式1,2,3,4,5|6,7
	 * @return 结果样式 5:2@4:2......@4:2
	 */
	public static String openPrize4Lotto(String bet, String target) {
		if (bet == null || "".equals(bet) || target == null || "".equals(target)) {
			return "0:0";
		}
		String[] targets = target.split("[|]"); // 1,2,3,4,5,6 7
		String[] bets = bet.split("[|]"); // 1,2,3 4,5,6 7 1,2,3,4,5,6,7 7

		List<List<String>> beforeList = new ArrayList<>();
		List<List<String>> afterList = new ArrayList<>();
		if (bets.length == 2) {
			// 情况一:普通投注 将bets[0]和bets[1]列出所有组合
			beforeList = getCombination(bets[0], 5);
			afterList = getCombination(bets[1], 2);
		} else if (bets.length == 4) {
			// 情况二:胆拖投注 将bets[1]和bets[2]列出所有组合 bets[1]的组合根据bets[0]的数量决定
			beforeList = getCombination(bets[1], 5 - bets[0].split(",").length);
			afterList = getCombination(bets[3], 2 - bets[2].split(",").length);
		} else {
			return "0:0";
		}

		// 判断红球和蓝球的投注串不为空
		if (beforeList.size() == 0 || afterList.size() == 0) {
			return "0:0";
		}

		if (bets.length == 4) {
			String[] beforeDanStr = bets[0].split(",");
			for (int j = 0; j < beforeDanStr.length; j++) {
				for (int i = 0; i < beforeList.size(); i++) {
					List<String> tuo = beforeList.get(i);
					tuo.add(beforeDanStr[j]);
				}
			}
			String[] afterDanStr = bets[2].split(",");
			for (int j = 0; j < afterDanStr.length; j++) {
				for (int i = 0; i < afterList.size(); i++) {
					List<String> tuo = afterList.get(i);
					tuo.add(afterDanStr[j]);
				}
			}
		}
		return openPrizeResult(targets, beforeList, afterList);
	}

	private static String openPrizeResult(String[] targets, List<List<String>> redList, List<List<String>> blueList) {
		StringBuilder result = new StringBuilder();
		String[] redTargets = targets[0].split(",");
		String[] blueTargets = targets[1].split(",");

		int blueSize = blueList.size();
		int redSize = redList.size();
		for (int i = 0; i < blueSize; i++) {
			List<String> blue = blueList.get(i);
			String[] blueArr = blue.toArray(new String[blue.size()]);
			for (int j = 0; j < redSize; j++) {
				List<String> red = redList.get(j);
				String[] redArr = red.toArray(new String[red.size()]);

				int redSameCount = getCount4Same(redArr, redTargets);
				int blueSameCount = getCount4Same(blueArr, blueTargets);

				result.append(redSameCount).append(":").append(blueSameCount);
				if (j != redSize - 1) {
					result.append("@");
				}
			}
			if (i != blueSize - 1) {
				result.append("@");
			}
		}
		return result.toString();
	}

	/**
	 * 用于计算两个数组中重复字符的个数
	 * 
	 * @param a
	 *            数组a
	 * @param b
	 *            数组b
	 * @return 两个数组中重复字符的个数
	 */
	private static int getCount4Same(Object[] a, Object[] b) {
		Object[] newA = getNotRepateNum(a);
		Object[] newB = getNotRepateNum(b);
		int count = 0;
		for (int i = 0; i < newA.length; i++) {
			for (int j = 0; j < newB.length; j++) {
				if (newA[i].equals(newB[j])) {
					count++;
					break;
				}
			}
		}
		return count;
	}

	private static Object[] getNotRepateNum(Object[] arr) {
		List<Object> list = Arrays.asList(arr);
		return new HashSet<>(list).toArray();
	}

	/**
	 * 获得所有的组合情况
	 * 
	 * @param str
	 *            投注串
	 * @param n
	 *            需要的最少选择数
	 * @return
	 */
	private static List<List<String>> getCombination(String str, int n) {
		String[] array = str.split(",");
		int m = array.length;
		// 初始化移位法需要的数组
		byte[] bits = new byte[m];
		for (int i = 0; i < bits.length; i++) {
			bits[i] = i < n ? (byte) 1 : (byte) 0;
		}
		boolean find = false;

		List<List<String>> result = new ArrayList<>();
		do {
			List<String> tempList = new ArrayList<>();
			// 找到10，换成01
			for (int i = 0; i < bits.length; i++) {
				if (bits[i] == (byte) 1) {
					tempList.add(array[i]);
				}
			}
			result.add(tempList);

			find = false;

			for (int i = 0; i < m - 1; i++) {
				if (bits[i] == 1 && bits[i + 1] == 0) {
					find = true;
					bits[i] = 0;
					bits[i + 1] = 1;
					// 如果第一位为0，则将第i位置之前的1移到最左边，如为1则第i位置之前的1就在最左边，无需移动
					if (bits[0] == 0) {
						// O(n)复杂度使1在前0在后
						for (int k = 0, j = 0; k < i; k++) {
							if (bits[k] == 1) {
								byte temp = bits[k];
								bits[k] = bits[j];
								bits[j] = temp;
								j++;
							}
						}
					}
					break;
				}
			}
		} while (find);
		return result;
	}

	// 0,1|1,2|1,2==>01,12,12
	public static String getBettingStr(String str) {
		if (StringUtils.isNotEmpty(str)) {
			str = str.replaceAll(",", "").replaceAll("\\|", ",");
		}
		return str;
	}

	/**
	 * @describe:计算复式的方法
	 * @param betData
	 * @param kjData
	 * @return:
	 */
	private static int fs(String betData, String kjData) {
		Set<String> bets = ArrangeUtils.getGroup(betData, 2);
		String[] kj = kjData.split("\\|");
		Set<String> collect = bets.stream().filter(bet -> ArrangeUtils.isSameArray(bet.split(""), kj))
				.collect(Collectors.toSet());
		return collect.size();
	}

	/**
	 * @author jh 2018年3月5日下午12:09:05
	 * @describe:将结果封装成int返回出去
	 * @param result
	 * @return:
	 */
	public static int parseResultInt(String method, String betData, String kjData) {
		String result = ScriptEngineUtil.execute(method, getBettingStr(betData), getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--五星直选--复式
	 * @param betData:"0,7,8|1,8|1,7,8|2,9|2,7"
	 * @param kjData:"0|1|1|2|2"
	 * @return:
	 */
	public static int dxwf5f(String betData, String kjData) {
		return parseResultInt("dxwf5f", betData, kjData);
	}

	/**
	 * @describe:时时彩--五星直选--单式
	 * @param betData:9|1|1|2|2
	 * @param kjData:9|1|1|2|2
	 * @return:
	 */
	public static int dxwf5d(String betData, String kjData) {
		return parseResultInt("dxwf5d", betData, kjData);
	}

	/**
	 * @describe:时时彩--五星组选--组选120
	 * @param betData:2|1|0|3|9
	 * @param kjData:9|0|1|3|2
	 * @return:
	 */
	public static int dxwf5z120(String betData, String kjData) {
		return parseResultInt("dxwf5z120", betData, kjData);
	}

	/**
	 * @describe:时时彩--五星组选--组选60
	 * @param betData:0,1,7|1,2,6,8
	 * @param kjData:6|1|1|8|2
	 * @return:
	 */
	public static int dxwf5z60(String betData, String kjData) {
		return parseResultInt("dxwf5z60", betData, kjData);
	}

	/**
	 * @describe:时时彩--五星组选--组选30
	 * @param betData:1,2|1,2,3,4,7,8
	 * @param kjData:2|1|1|2|7
	 * @return:
	 */
	public static int dxwf5z30(String betData, String kjData) {
		return parseResultInt("dxwf5z30", betData, kjData);
	}

	/**
	 * @describe:时时彩--五星组选--组选20
	 * @param betData:0,1|0,1,2,9
	 * @param kjData:1|1|1|2|9
	 * @return:
	 */
	public static int dxwf5z20(String betData, String kjData) {
		return parseResultInt("dxwf5z20", betData, kjData);
	}

	/**
	 * @describe:时时彩--五星组选--组选10
	 * @param betData:0,1|0,1,2,9
	 * @param kjData:1|1|1|2|9
	 * @return:
	 */
	public static int dxwf5z10(String betData, String kjData) {
		return parseResultInt("dxwf5z10", betData, kjData);
	}

	/**
	 * @describe:时时彩--五星组选--组选5
	 * @param betData:1,2,8,9|2,3,9
	 * @param kjData:1|1|1|1|2
	 * @return:
	 */
	public static int dxwf5z5(String betData, String kjData) {
		return parseResultInt("dxwf5z5", betData, kjData);
	}

	/**
	 * @describe:时时彩--后四直选--复式
	 * @param betData:1,2,8,9|2,3,9
	 * @param kjData:1|1|1|1|2
	 * @return:
	 */
	public static int dxwfH4f(String betData, String kjData) {
		return parseResultInt("dxwfH4f", betData, kjData);
	}

	/**
	 * @describe:时时彩--后四直选--单式
	 * @param betData:1,2,8,9|2,3,9
	 * @param kjData:1|1|1|1|2
	 * @return:
	 */
	public static int dxwfH4d(String betData, String kjData) {
		return parseResultInt("dxwfH4d", betData, kjData);
	}

	/**
	 * @describe:时时彩--前四直选--复式
	 * @param betData:1,2,8,9|2,3,9
	 * @param kjData:1|1|1|1|2
	 * @return:
	 */
	public static int dxwfQ4f(String betData, String kjData) {
		return parseResultInt("dxwfQ4f", betData, kjData);
	}

	/**
	 * @describe:时时彩--前四直选--单式
	 * @param betData:1,2,8,9|2,3,9
	 * @param kjData:1|1|1|1|2
	 * @return:
	 */
	public static int dxwfQ4d(String betData, String kjData) {
		return parseResultInt("dxwfQ4d", betData, kjData);
	}

	/**
	 * @describe:时时彩--任选--任四复式
	 * @param betData:1,2,8,9|2,3,9|||
	 * @param kjData:1|1|1|1|2
	 * @return:
	 */
	public static int sscrx4f(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscrx4f", getBettingStr(betData), getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--任选--任四单式
	 * @param betData:1,2,8,9|2,3,9
	 * @param kjData:1|1|1|1|2
	 * @return:
	 */
	public static int dxwfR4d(String betData, String kjData) {
		return parseResultInt("dxwfR4d", betData, kjData);
	}

	/**
	 * @describe:时时彩--后四组选--组选24
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int dxwf4z24(String betData, String kjData) {
		return parseResultInt("dxwf4z24", betData, kjData);
	}

	/**
	 * @describe:时时彩--前四组选--组选24
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int dxwfQ4z24(String betData, String kjData) {
		return parseResultInt("dxwfQ4z24", betData, kjData);
	}

	/**
	 * @describe:时时彩--后四组选--组选12
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int dxwf4z12(String betData, String kjData) {
		return parseResultInt("dxwf4z12", betData, kjData);
	}

	/**
	 * @describe:时时彩--前四组选--组选12
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int dxwfQ4z12(String betData, String kjData) {
		return parseResultInt("dxwfQ4z12", betData, kjData);
	}

	/**
	 * @describe:时时彩--后四组选--组选6
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int dxwf4z6(String betData, String kjData) {
		return parseResultInt("dxwf4z6", betData, kjData);
	}

	/**
	 * @describe:时时彩--前四组选--组选6
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int dxwfQ4z6(String betData, String kjData) {
		return parseResultInt("dxwfQ4z6", betData, kjData);
	}

	/**
	 * @describe:时时彩--后四组选--组选4
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int dxwf4z4(String betData, String kjData) {
		return parseResultInt("dxwf4z4", betData, kjData);
	}

	/**
	 * @describe:时时彩--前四组选--组选4
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int dxwfQ4z4(String betData, String kjData) {
		return parseResultInt("dxwfQ4z4", betData, kjData);
	}

	/**
	 * @describe:时时彩--前三直选--复式
	 * @param betData:8|3|2
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxwfQ3f(String betData, String kjData) {
		return parseResultInt("sxwfQ3f", betData, kjData);
	}

	/**
	 * @describe:时时彩--前三直选--单式
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxwfQ3d(String betData, String kjData) {
		return parseResultInt("sxwfQ3d", betData, kjData);
	}

	/**
	 * @describe:时时彩--中三直选--复式
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxwfz3fs(String betData, String kjData) {
		return parseResultInt("sxwfz3fs", betData, kjData);
	}

	/**
	 * @describe:时时彩--中三直选--单式
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxwfz3ds(String betData, String kjData) {
		return parseResultInt("sxwfz3ds", betData, kjData);
	}

	/**
	 * @describe:时时彩--后三直选--复式
	 * @param betData:0,1,6|0,7,9|0,4,6
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxwfH3f(String betData, String kjData) {
		return parseResultInt("sxwfH3f", betData, kjData);
	}

	/**
	 * @describe:时时彩--后三直选--单式
	 * @param betData:8|3|2|4|5
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxwfH3d(String betData, String kjData) {
		return parseResultInt("sxwfH3d", betData, kjData);
	}

	/**
	 * @describe:时时彩--任三--复式
	 * @param betData:1,8|1,6|2|3,6|1,6,9
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxwfR3f(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxwfR3f", getBettingStr(betData), getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--任三--单式
	 * @param betData:万位,千位,百位,十位,个位
	 *            | 125
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxwfR3d(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxwfR3d", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--后三直选--直选和值
	 * @param betData:2,12,15,16,21
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxH3zxhz(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxH3zxhz", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--后三直选--直选跨度
	 * @param betData:2,12,15,16,21
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxH3zxkd(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxH3zxkd", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--后三直选--和值尾数
	 * @param betData:2,12,15,16,21
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxH3zxws(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxH3zxws", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--中三直选--直选和值
	 * @param betData:2,12,15,16,21
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxZ3zxhz(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxZ3zxhz", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--中三直选--直选跨度
	 * @param betData:2,12,15,16,21
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxZ3zxkd(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxZ3zxkd", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--中三直选--和值尾数
	 * @param betData:2,12,15,16,21
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxZ3zxws(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxZ3zxws", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--前三直选--直选和值
	 * @param betData:2,12,15,16,21
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxQ3zxhz(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxQ3zxhz", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--前三直选--直选跨度
	 * @param betData:2,12,15,16,21
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxQ3zxkd(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxQ3zxkd", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--前三直选--和值尾数
	 * @param betData:2,12,15,16,21
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxQ3zxws(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxQ3zxws", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--三星--后三组选组三
	 * @param betData:1,2,7,8,9
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxH3z3(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxH3z3", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--三星--后三组选组六
	 * @param betData:0,1,2,3,6,7,8
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxH3z6(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxH3z6", betData.replaceAll(",", ""), getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--三星--中三组选组三
	 * @param betData:0,1,2,6,7,9
	 * @param kjData:1|2|7|7|8
	 * @return:
	 */
	public static int sxzxz3z3(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxz3z3", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--三星--中三组选组六
	 * @param betData:0,1,2,3,6,7,8
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxz3z6(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxz3z6", betData.replaceAll(",", ""), getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--三星--前三组选组三
	 * @param betData:0,1,2,6,7,9
	 * @param kjData:1|2|7|7|8
	 * @return:
	 */
	public static int sxzxQ3z3(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxQ3z3", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--三星--前三组选组六
	 * @param betData:0,1,2,3,6,7,8
	 * @param kjData:1|2|3|4|5
	 * @return:
	 */
	public static int sxzxQ3z6(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxQ3z6", betData.replaceAll(",", ""), getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--三星--任三组三
	 * @param betData:万位,千位,百位,十位,个位
	 *            | 1,2,3,4,5,7,8,9
	 * @param kjData:1|2|7|7|8
	 * @return:
	 */
	public static int sxzxR3z3(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxR3z3", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--三星--任三组三
	 * @param betData:万位,千位,百位,十位,个位
	 *            | 1,2,3,4,5,7,8,9
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sxzxR3z6(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sxzxR3z6", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--后二直选--复式
	 * @param betData:0,2,6,7|1,2,8,9
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int rxwfH2f(String betData, String kjData) {
		return parseResultInt("rxwfH2f", betData, kjData);
	}

	/**
	 * @describe:时时彩--后二直选--单式
	 * @param betData:1|2
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int rxwfH2d(String betData, String kjData) {
		return parseResultInt("rxwfH2d", betData, kjData);
	}

	/**
	 * @describe:时时彩--前二直选--复式
	 * @param betData:0,2,6,7|1,2,8,9
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int rxwfQ2f(String betData, String kjData) {
		return parseResultInt("rxwfQ2f", betData, kjData);
	}

	/**
	 * @describe:时时彩--前二直选--单式
	 * @param betData:1|2
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int rxwfQ2d(String betData, String kjData) {
		return parseResultInt("rxwfQ2d", betData, kjData);
	}

	/**
	 * @describe:时时彩--前二直选--直选和值
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscq2zhixhz(String betData, String kjData) {
		String[] bets = betData.split(",");
		String[] kjs = kjData.split("\\|");
		String m = Integer.parseInt(kjs[0]) + Integer.parseInt(kjs[1]) + "";
		int count = 0;
		for (String bet : bets) {
			if (m.equals(bet)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @describe:时时彩--后二直选--直选和值
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscH2zhixhz(String betData, String kjData) {
		return sscq2zhixhz(betData, kjData.substring(6, kjData.length()));
	}

	/**
	 * @describe:时时彩--前二直选--直选跨度
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscq2zhixkd(String betData, String kjData) {
		String[] bets = betData.split(",");
		String[] kjs = kjData.substring(0, 3).split("\\|");
		Arrays.sort(kjs);
		String m = Integer.parseInt(kjs[1]) - Integer.parseInt(kjs[0]) + "";
		int count = 0;
		for (String bet : bets) {
			if (m.equals(bet)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @describe:时时彩--后二直选--直选跨度
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscH2zhixkd(String betData, String kjData) {
		return sscq2zhixkd(betData, kjData.substring(6, kjData.length()));
	}

	/**
	 * @describe:时时彩--前二直选--和值尾数
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscq2zhixws(String betData, String kjData) {
		String[] bets = betData.split(",");
		String[] kjs = kjData.split("\\|");
		String m = Integer.parseInt(kjs[0]) + Integer.parseInt(kjs[1]) + "";
		String ws = m.charAt(m.length() - 1) + "";
		int count = 0;
		for (String bet : bets) {
			if (ws.equals(bet)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @describe:时时彩--后二直选--和值尾数
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscH2zhixws(String betData, String kjData) {
		return sscq2zhixws(betData, kjData.substring(6, kjData.length()));
	}

	/**
	 * @describe:时时彩--后二组选--复式
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscH2zuxfs(String betData, String kjData) {
		return fs(betData, kjData.substring(6, kjData.length()));
	}

	/**
	 * @describe:时时彩--后二组选--单式
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscH2zuxds(String betData, String kjData) {
		return fs(betData, kjData.substring(6, kjData.length()));
	}

	/**
	 * @describe:时时彩--前二组选--复式
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscq2zuxfs(String betData, String kjData) {
		return fs(betData, kjData.substring(0, 3));
	}

	/**
	 * @describe:时时彩--前二组选--单式
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscq2zuxds(String betData, String kjData) {
		return fs(betData, kjData.substring(0, 3));
	}

	/**
	 * @describe:三星不定位--三星一码
	 * @param args:
	 */
	public static int sscbdw1m(String betData, String kjData) {
		if (StringUtils.isEmpty(betData) || StringUtils.isEmpty(kjData)) {
			return 0;
		}
		String[] bets = betData.split(",");
		int count = 0;
		for (String bet : bets) {
			if (kjData.contains(bet)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-后三一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwH3x1m(String betData, String kjData) {
		return sscbdw1m(betData, kjData.substring(4, kjData.length()));
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-中三一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwZ3x1m(String betData, String kjData) {
		return sscbdw1m(betData, kjData.substring(2, 7));
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-前三一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwq3x1m(String betData, String kjData) {
		return sscbdw1m(betData, kjData.substring(0, 5));
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-前四一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwq4x1m(String betData, String kjData) {
		return sscbdw1m(betData, kjData.substring(0, 7));
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-后四一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwH4x1m(String betData, String kjData) {
		return sscbdw1m(betData, kjData.substring(2, kjData.length()));
	}

	/**
	 * @describe:时时彩--不定位--五星不定位-五星一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdw5x1m(String betData, String kjData) {
		return sscbdw1m(betData, kjData);
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-后三二码
	 * @param betData
	 * @param kjData
	 * @return:
	 */
	private static int sscbdwnm(String betData, String kjData, int n) {
		Set<String> bets = ArrangeUtils.getGroup(betData, n);
		String[] kj = kjData.split("\\|");
		Set<String> collect = bets.stream().filter(bet -> ArrangeUtils.getSameCount(bet.split(""), kj) >= n)
				.collect(Collectors.toSet());
		return collect.size();
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-后三二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwH3x2m(String betData, String kjData) {
		return sscbdwnm(betData, kjData.substring(4, kjData.length()), 2);
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-中三二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwZ3x2m(String betData, String kjData) {
		return sscbdwnm(betData, kjData.substring(2, 7), 2);
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-前三二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwQ3x2m(String betData, String kjData) {
		return sscbdwnm(betData, kjData.substring(0, 5), 2);
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-前四二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwQ4x2m(String betData, String kjData) {
		return sscbdwnm(betData, kjData.substring(0, 7), 2);
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-后四二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwH4x2m(String betData, String kjData) {
		return sscbdwnm(betData, kjData.substring(2, kjData.length()), 2);
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-前四三码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwQ4x3m(String betData, String kjData) {
		return sscbdwnm(betData, kjData.substring(0, 7), 3);
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-后四三码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwH4x3m(String betData, String kjData) {
		return sscbdwnm(betData, kjData.substring(2, kjData.length()), 3);
	}

	/**
	 * @describe:时时彩--不定位--五星不定位-五星二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdw5x2m(String betData, String kjData) {
		return sscbdwnm(betData, kjData, 2);
	}

	/**
	 * @describe:时时彩--不定位--五星不定位-五星三码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdw5x3m(String betData, String kjData) {
		return sscbdwnm(betData, kjData, 3);
	}

	/**
	 * @describe:时时彩--不定位--五星不定位-五星四码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdw5x4m(String betData, String kjData) {
		return sscbdwnm(betData, kjData, 4);
	}

	/**
	 * @describe:时时彩--任选--任二复式
	 * @param betData:1,8|1|3||
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int rxwfR2f(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("rxwfR2f", getBettingStr(betData), getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--任选--任二复式
	 * @param betData:万位,千位,百位,十位
	 *            | 12
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int rxwfR2d(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("rxwfR2d", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--任选--任二组选
	 * @param betData:万位,千位,百位,十位
	 *            | 0,1,9,2
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int rxwfR2zx(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("rxwfR2zx", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--趣味--特殊--一帆风顺
	 * @param betData:万位,千位,百位,十位
	 *            | 0,1,9,2
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int qwwfyffs(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("qwwfyffs", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--趣味--特殊--好事成双
	 * @param betData:0,1,9,2
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscfhscs(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscfhscs", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--趣味--特殊--三星报喜
	 * @param betData:0,1,9,2
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int qwwfsxbx(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("qwwfsxbx", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--趣味--特殊--四季发财
	 * @param betData:0,1,9,2
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int qwwfsjfc(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("qwwfsjfc", betData, getBettingStr(kjData));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--龙虎--公共方法
	 * @param betData
	 * @param a:开奖其中某个位上的数字
	 * @param b:开奖其中某个位上的数字
	 * @return:
	 */
	public static int ssclh(String betData, char a, char b) {
		int kjresult = Integer.parseInt(a + "") - Integer.parseInt(b + "");
		String result = "";
		if (kjresult > 0) {
			result = "龙";
		} else if (kjresult < 0) {
			result = "虎";
		} else if (kjresult == 0) {
			result = "和";
		}
		return betData.contains(result) ? 1 : 0;
	}

	/**
	 * @describe:时时彩--龙虎--万千
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhwq(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(0), kjData.charAt(2));
	}

	/**
	 * @describe:时时彩--龙虎--万百
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhwb(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(0), kjData.charAt(4));
	}

	/**
	 * @describe:时时彩--龙虎--万十
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhws(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(0), kjData.charAt(6));
	}

	/**
	 * @describe:时时彩--龙虎--万个
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhwg(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(0), kjData.charAt(8));
	}

	/**
	 * @describe:时时彩--龙虎--千百
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhqb(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(2), kjData.charAt(4));
	}

	/**
	 * @describe:时时彩--龙虎--千十
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhqs(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(2), kjData.charAt(6));
	}

	/**
	 * @describe:时时彩--龙虎--千个
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhqg(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(2), kjData.charAt(8));
	}

	/**
	 * @describe:时时彩--龙虎--百十
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhbs(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(4), kjData.charAt(6));
	}

	/**
	 * @describe:时时彩--龙虎--百个
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhbg(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(4), kjData.charAt(8));
	}

	/**
	 * @describe:时时彩--龙虎--十个
	 * @param betData:龙,虎,和
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssclhsg(String betData, String kjData) {
		return ssclh(betData, kjData.charAt(6), kjData.charAt(8));
	}

	/**
	 * @describe:时时彩--特殊号--公共方法
	 * @param betData:投注数据
	 * @param kjData:开奖数据
	 * @return:
	 */
	public static int ssctsh(String betData, String kjData) {
		String[] kj = kjData.split("\\|");
		int[] kjs = new int[kj.length];
		for (int i = 0; i < kj.length; i++) {
			kjs[i] = Integer.parseInt(kj[i]);
		}
		Arrays.sort(kjs);
		int i = 0;
		List<String> list = new ArrayList<>();
		if (kjs[i] == kjs[i + 1] || kjs[i + 2] == kjs[i + 1]) {
			if (betData.indexOf("对子") != -1 && !(kj[i] == kj[i + 1] && kj[i + 1] == kj[i + 2])) {
				list.add("对子");
			}
		}
		// if (kjs[i] == (kjs[i + 1] - 1)&&kjs[i + 1] != (kjs[i + 2] - 1)||kjs[i] !=
		// (kjs[i + 1] - 1)&&kjs[i + 1] == (kjs[i + 2] - 1)) {
		// if (betData.indexOf("半顺") != -1) {
		// list.add("半顺");
		// }
		// }
		if (kjs[i] == (kjs[i + 1] - 1) && kjs[i + 1] == (kjs[i + 2] - 1)) {
			if (betData.indexOf("顺子") != -1) {
				list.add("顺子");
			}
		}
		if (kj[i] == kj[i + 1] && kj[i + 1] == kj[i + 2]) {
			if (betData.indexOf("豹子") != -1) {
				list.add("豹子");
			}
		}
		// if (kjs[i] < (kjs[i + 1] - 1)&&kjs[i + 1] < (kjs[i + 2] - 1)) {
		// if (betData.indexOf("杂六") != -1) {
		// list.add("杂六");
		// }
		// }
		return list.size();
	}

	/**
	 * @describe:时时彩--特殊号--前三
	 * @param betData:豹子,顺子,对子,半顺,杂六
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssctshq3(String betData, String kjData) {
		return ssctsh(betData, kjData.substring(0, 5));
	}

	/**
	 * @describe:时时彩--特殊号--中三
	 * @param betData:豹子,顺子,对子,半顺,杂六
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssctshz3(String betData, String kjData) {
		return ssctsh(betData, kjData.substring(2, 7));
	}

	/**
	 * @describe:时时彩--特殊号--后三
	 * @param betData:豹子,顺子,对子,半顺,杂六
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssctshh3(String betData, String kjData) {
		return ssctsh(betData, kjData.substring(4, kjData.length()));
	}

	/**
	 * @describe:时时彩--斗牛--斗牛
	 * @param betData:牛牛,牛九,牛八,牛七,牛六,牛五,牛四,牛三,牛二,牛一,无牛
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdndn(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscdndn", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--大小单双--总和
	 * @param betData:大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdszh(String betData, String kjData) {
		String[] kjs = kjData.split("\\|");
		int sum = 0;
		for (String k : kjs) {
			sum += Integer.parseInt(k);
		}
		List<String> list = new ArrayList<>();
		if (sum >= 23 && sum <= 45) {// 大
			list.add("大");
		}
		if (sum >= 0 && sum <= 22) {// 小
			list.add("小");
		}
		if (sum % 2 == 0) {// 双
			list.add("双");
		}
		if (sum % 2 != 0) {// 单
			list.add("单");
		}
		return list.stream().filter(n -> betData.contains(n)).collect(Collectors.toList()).size();
	}

	/**
	 * @describe:时时彩--大小单双--定位公共方法
	 * @param betData:大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsdw(String betData, char kjData) {
		int sum = Integer.parseInt(kjData + "");
		List<String> list = new ArrayList<>();
		if (sum >= 5 && sum <= 9) {// 大
			list.add("大");
		}
		if (sum >= 0 && sum <= 4) {// 小
			list.add("小");
		}
		if (sum % 2 == 0) {// 双
			list.add("双");
		}
		if (sum % 2 != 0) {// 单
			list.add("单");
		}
		return list.stream().filter(n -> betData.contains(n)).collect(Collectors.toList()).size();
	}

	/**
	 * @describe:时时彩--大小单双--万位
	 * @param betData:大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsdwww(String betData, String kjData) {
		return sscdxdsdw(betData, kjData.charAt(0));
	}

	/**
	 * @describe:时时彩--大小单双--千位
	 * @param betData:大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsdwqw(String betData, String kjData) {
		return sscdxdsdw(betData, kjData.charAt(2));
	}

	/**
	 * @describe:时时彩--大小单双--百位
	 * @param betData:大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsdwbw(String betData, String kjData) {
		return sscdxdsdw(betData, kjData.charAt(4));
	}

	/**
	 * @describe:时时彩--大小单双--个位
	 * @param betData:大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsdwsw(String betData, String kjData) {
		return sscdxdsdw(betData, kjData.charAt(6));
	}

	/**
	 * @describe:时时彩--大小单双--个位
	 * @param betData:大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsdwgw(String betData, String kjData) {
		return sscdxdsdw(betData, kjData.charAt(8));
	}

	/**
	 * @describe:时时彩--大小单双--前二
	 * @param betData:大,小,单,双|大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsq2(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscdxds", betData, kjData.substring(0, 3));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--大小单双--后二
	 * @param betData:大,小,单,双|大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsh2(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscdxds", betData, kjData.substring(6, 9));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--大小单双--前三
	 * @param betData:大,小,单,双|大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsq3(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscdxds", betData, kjData.substring(0, 5));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--大小单双--后三
	 * @param betData:大,小
	 *            单,双|大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdsh3(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscdxds", betData, kjData.substring(4, 9));
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return (int) Double.parseDouble(result);
	}

	/**
	 * @describe:时时彩--大小单双--串关
	 * @param betData:大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscdxdscg(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscdxdscg", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--前二--组选和值
	 * @param betData:1,2,3
	 * @return:kjData:2|2|0|2|2
	 */
	public static int q2zxhz(String betData, String kjData) {
		String[] kjs = kjData.substring(0, 3).split("\\|");
		String sum = Integer.parseInt(kjs[0]) + Integer.parseInt(kjs[1]) + "";
		if (betData.contains(sum)) {
			return 1;
		}
		return 0;
	}

	/**
	 * @describe:三位组选和值-公共方法
	 * @param param
	 * @return:
	 */
	public static int h3zxhz(String betData, String kjData, int type) {
		if (betData == null || "".equals(betData)) {
			return 0;
		}
		Set<String> set = new HashSet<>();
		String[] target = betData.split(",");
		for (String str : target) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					for (int k = 0; k < target.length; k++) {
						if ((i + j + k + "").equals(str)) {
							String bet = "" + i + j + k;
							if (ArrangeUtils.filterSameArray(bet.split("")).length == type) {
								String[] betss = new String[] { i + "", j + "", k + "" };
								Arrays.sort(betss);
								String betstr = StringUtils.join(betss);
								set.add(betstr);
							}
						}
					}
				}
			}
		}
		String kj = ArrangeUtils.sortStr(kjData.replaceAll("\\|", ""));
		Set<String> collect = set.stream().filter(f -> f.equals(kj)).collect(Collectors.toSet());
		return collect.size();
	}

	/**
	 * @describe:后三--组三和值
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int h3z3hz(String betData, String kjData) {
		return h3zxhz(betData, kjData.substring(4, kjData.length()), 2);
	}

	/**
	 * @describe:后三--组六和值
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int h3z6hz(String betData, String kjData) {
		return h3zxhz(betData, kjData.substring(4, kjData.length()), 3);
	}

	/**
	 * @describe:前三--组三和值
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int q3z3hz(String betData, String kjData) {
		return h3zxhz(betData, kjData.substring(0, 5), 2);
	}

	/**
	 * @describe:前三--组六和值
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int q3z6hz(String betData, String kjData) {
		return h3zxhz(betData, kjData.substring(0, 5), 3);
	}

	/**
	 * @describe:三位包胆的情况;:abc 36;abb 9;aab 9;
	 * @param betData
	 * @param kjData
	 * @return:
	 */
	private static int ssc3zxbd(String betData, String kjData, int type) {
		if (StringUtils.isEmpty(betData)) {
			return 0;
		}
		if (kjData.contains(betData)) {
			kjData = kjData.replaceAll("\\|", "");
			if (type == 3) {// 组三包胆
				if (kjData.matches(".*(.).*\\1.*") && !kjData.matches("(\\w)\\1{2,}")) {// 是否有重复的值
					return 1;
				}
			}
			if (type == 6) {// 组六包胆
				if (!kjData.matches(".*(.).*\\1.*")) {// 是否有重复的值
					return 1;
				}
			}
			if (type == 2) {// 前二包胆
				return 1;
			}
		}
		return 0;
	}

	/**
	 * @describe:后三--组选包胆,组三包胆
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int h3zxbdz3(String betData, String kjData) {
		return ssc3zxbd(betData, kjData.substring(4, kjData.length()), 3);
	}

	/**
	 * @describe:后三--组选包胆,组六包胆
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int h3zxbdz6(String betData, String kjData) {
		return ssc3zxbd(betData, kjData.substring(4, kjData.length()), 6);
	}

	/**
	 * @describe:前三--组选包胆--组三
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int q3zxbdz3(String betData, String kjData) {
		return ssc3zxbd(betData, kjData.substring(0, 5), 3);
	}

	/**
	 * @describe:前三--组选包胆--组三
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int q3zxbdz6(String betData, String kjData) {
		return ssc3zxbd(betData, kjData.substring(0, 5), 6);
	}

	/**
	 * @describe:前二组选包胆
	 * @param betData:4
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int q2zxbd(String betData, String kjData) {
		return ssc3zxbd(betData, kjData.substring(0, 3), 2);
	}

	/**
	 * @describe:任选2--直选和值
	 * @param betData:万位,千位,百位,十位,个位
	 *            |0,1,2,3,4,5,6,7,8,9,10,18
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	public static int sscr2zxhz(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscr2zxhz", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:任选2--组选和值 (去掉对子项)
	 * @param betData:万位,千位,百位,十位,个位
	 *            |0,1,2,3,4,5,6,7,8,9,10,18
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	public static int sscr2zuxhz(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscr2zuxhz", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:任选3--直选和值
	 * @param betData:万位,千位,百位,十位,个位
	 *            |0,1,2,3,4,5,6,7,8,9,10,18
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	public static int sscr3zxhz(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscr3zxhz", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:任选3--组3和值
	 * @param betData:万位,千位,百位,十位,个位
	 *            |0,1,2,3,4,5,6,7,8,9,10,18
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	public static int sscr3zu3hz(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscr3zu3hz", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:任选3--组6和值
	 * @param betData:万位,千位,百位,十位,个位
	 *            |0,1,2,3,4,5,6,7,8,9,10,18
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	public static int sscr3zu6hz(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscr3zu6hz", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:任选3--组选24
	 * @param betData:万位,千位,百位,十位,个位
	 *            |0,1,2,3,4,5,6,7,8,9,10,18
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	public static int sscr4zx24(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscr4zx24", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:任选3--组选12
	 * @param betData:万位,千位,百位,十位,个位
	 *            |0,1,2,3,4,5|6,7,8,9,10,18
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	public static int sscr4zx12(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscr4zx12", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/* 福彩3d三码开奖方法 */
	/**
	 * 福彩3d三码直选复式
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4|1,2,3,4|1,2,3,4
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dFs(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] betArr = bet.split("\\|");
			String[] kjArr = kj.split(",");
			if (betArr[0].contains(kjArr[0]) && betArr[1].contains(kjArr[1]) && betArr[2].contains(kjArr[2])) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 福彩3d三码直选直选和值
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4,5,6
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dHz(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] betArr = bet.split(",");
			String[] kjArr = kj.split(",");
			int target = 0;
			for (String str : kjArr) {
				target += Integer.parseInt(str);
			}
			for (String str : betArr) {
				if (Integer.parseInt(str) == target) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d三码组选组三
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4,5,6
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dZ3(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			Object[] newKjArr = filterArray(kjArr);
			if (kjArr.length == 3 && newKjArr.length == 2) {
				List<List<String>> betList = getCombination(bet, 2);
				for (List<String> list : betList) {
					if (getCount4Same(list.toArray(), newKjArr) == 2) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d三码组选组六
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4,5,6
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dZ6(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			Object[] newKjArr = filterArray(kjArr);
			List<List<String>> betList = getCombination(bet, 3);
			for (List<String> list : betList) {
				if (getCount4Same(list.toArray(), newKjArr) == 3) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d三码组三和值
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4,5,6
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dZ3hz(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			String[] betArr = bet.split(",");
			if (kjArr.length == 3 && filterArray(kjArr).length == 2) {
				int target = 0;
				for (String str : kjArr) {
					target += Integer.parseInt(str);
				}
				for (String str : betArr) {
					if (target == Integer.parseInt(str)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d三码组六和值
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4,5,6
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dZ6hz(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			String[] betArr = bet.split(",");
			if (kjArr.length == 3 && filterArray(kjArr).length == 3) {
				int target = 0;
				for (String str : kjArr) {
					target += Integer.parseInt(str);
				}
				for (String str : betArr) {
					if (target == Integer.parseInt(str)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d二码前二码直选复式
	 * 
	 * @param bet
	 *            投注串 eg: 1,2|1,2,3,4
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dQ2f(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			String[] betArr = bet.split("\\|");
			if (kjArr.length == 3) {
				if (betArr[0].contains(kjArr[0]) && betArr[1].contains(kjArr[1])) {
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * 福彩3d二码前二码组选复式
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dZQ2f(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			Object[] beforeTwo = { kjArr[0], kjArr[1] };
			if (kjArr.length == 3 && !kjArr[0].equals(kjArr[1])) {
				List<List<String>> betList = getCombination(bet, 2);
				for (List<String> list : betList) {
					if (getCount4Same(list.toArray(), beforeTwo) == 2) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d二码后二码直选复式
	 * 
	 * @param bet
	 *            投注串 eg: 1,2|1,2,3,4
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dH2f(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			String[] betArr = bet.split("\\|");
			if (kjArr.length == 3) {
				if (betArr[0].contains(kjArr[1]) && betArr[1].contains(kjArr[2])) {
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * 福彩3d二码后二码组选复式
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dZH2f(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			Object[] beforeTwo = { kjArr[1], kjArr[2] };
			if (kjArr.length == 3 && !kjArr[1].equals(kjArr[2])) {
				List<List<String>> betList = getCombination(bet, 2);
				for (List<String> list : betList) {
					if (getCount4Same(list.toArray(), beforeTwo) == 2) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d定位胆
	 * 
	 * @param bet
	 *            投注串 eg: |1,2|
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dDwd(String betData, String kjData) {
		String[] bets = betData.split("\\|");
		String[] kj = kjData.split(",");
		int count = 0;
		for (int i = 0; i < bets.length; i++) {
			if (bets[i].contains(kj[i])) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @describe:任选4--组选6
	 * @param betData:千位,百位,十位,个位|0,1,2
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	/*
	 * public static int sscr4zx6(String betData,String kjData) { String
	 * result=ScriptEngineUtil.execute("sscr4zx6",betData,kjData); if
	 * (StringUtils.isEmpty(result)) { return 0; } return Integer.parseInt(result);
	 * }
	 */
	public static int sscr4zx6(String betData, String kjData) {
		int result = 0;
		if (!StringUtil.isBlank(betData) && !StringUtil.isBlank(kjData)) {
			// String[] places = { "万位", "千位", "百位", "十位", "个位" };
			List<String> places = new ArrayList<String>();
			places.add("万位");
			places.add("千位");
			places.add("百位");
			places.add("十位");
			places.add("个位");
			String[] betArr = betData.split("\\|");
			// 组合取出所有ab
			List<List<String>> group = getCombination(betArr[1], 2);
			// 得到所有位数组合情况
			List<List<String>> allPlaceList = getCombination(betArr[0], 4);

			// 根据位数取出开奖该位上的数字
			String[] kjArr = kjData.split("\\|");
			int[] indexes = new int[4];
			for (List<String> list : allPlaceList) {
				List<String> target = new ArrayList<String>();
				for (int i = 0; i < list.size(); i++) {
					if (places.contains(list.get(i))) {
						indexes[i] = places.indexOf(list.get(i));
					}
				}
				for (int index : indexes) {
					target.add(kjArr[index]);
				}
				// 判断取出来的数字是不是aabb类型
				boolean flag = false;
				List<String> newList = new ArrayList<String>(new HashSet<String>(target));
				if (newList.size() == 2) {
					for (String string : newList) {
						target.remove(string);
					}
					flag = newList.containsAll(target) && target.containsAll(newList);
				}
				// group中有没有该组合 若有count++
				if (flag && group.contains(newList)) {
					result++;
				}
			}
		}
		return result;
	}

	/**
	 * @describe:任选4--组选4
	 * @param betData:千位,百位,十位,个位|0,1,2|0,1,2
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	public static int sscr4zx4(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscr4zx4", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * @describe:时时彩--龙虎
	 * @param betData:万千:万|万千:千|万百:万|万百:百
	 * @param kjData:4|6|2|8|5
	 * @return:
	 */
	public static int sscrlh(String betData, String kjData) {
		String result = ScriptEngineUtil.execute("sscrlh", betData, kjData);
		if (StringUtils.isEmpty(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	/**
	 * 福彩3d不定胆 一码不定位
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3d1mbdw(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			List<String> kjList = Arrays.asList(kjArr);
			String[] betArr = bet.split(",");
			for (String string : betArr) {
				if (kjList.contains(string)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d不定胆 二码不定位
	 * 
	 * @param bet
	 *            投注串 eg: 1,2,3,4
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3d2mbdw(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			List<List<String>> betList = getCombination(bet, 2);
			for (List<String> list : betList) {
				if (getCount4Same(kj.split(","), list.toArray()) == 2) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d大小单双 前二大小单双
	 * 
	 * @param bet
	 *            投注串 eg: 大,小,单,双|大,小,单,双
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dQ2dxds(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			List<String> bai = dxds(Integer.parseInt(kjArr[0]));
			List<String> shi = dxds(Integer.parseInt(kjArr[1]));
			String[] betArr = bet.split("\\|");
			String[] baiBet = betArr[0].split(",");
			String[] shiBet = betArr[1].split(",");

			for (int i = 0; i < baiBet.length; i++) {
				for (int j = 0; j < shiBet.length; j++) {
					if (bai.contains(baiBet[i]) && shi.contains(shiBet[j])) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 福彩3d大小单双 后二大小单双
	 * 
	 * @param bet
	 *            投注串 eg: 大,小,单,双|大,小,单,双
	 * @param kj
	 *            开奖串 eg: 1,2,3
	 * @return 中奖注数
	 */
	public static int fc3dH2dxds(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjArr = kj.split(",");
			List<String> shi = dxds(Integer.parseInt(kjArr[1]));
			List<String> ge = dxds(Integer.parseInt(kjArr[2]));
			String[] betArr = bet.split("\\|");
			String[] shiBet = betArr[0].split(",");
			String[] geBet = betArr[1].split(",");

			for (int i = 0; i < shiBet.length; i++) {
				for (int j = 0; j < geBet.length; j++) {
					if (shi.contains(shiBet[i]) && ge.contains(geBet[j])) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 判断数字是大小单双的方法
	 * 
	 * @param num
	 * @return
	 */
	private static List<String> dxds(int num) {
		List<String> result = new ArrayList<String>();
		if (num % 2 == 0) {
			result.add("双");
		} else {
			result.add("单");
		}

		if (num >= 0 && num <= 4) {
			result.add("小");
		} else if (num >= 5 && num <= 9) {
			result.add("大");
		}
		return result;
	}

	/**
	 * 过滤数组中的重复元素
	 * 
	 * @return
	 */
	private static Object[] filterArray(String[] arr) {
		Set<String> set = new HashSet<>(Arrays.asList(arr));
		return set.toArray();
	}

	/**
	 * @describe:快3--和值--和值--
	 * @param betData:3
	 * @param kjData:1,2,4
	 * @return:中奖所对应得赔率，未中奖则返回0
	 */
	public static int k3hz(String betData, String kjData) {
		if (betData == null || "".equals(betData) || kjData == null || "".equals(kjData)) {
			return 0;
		}
		// 3-10 小 11-18 大
		String[] kj = kjData.split(",");
		int count = 0;
		try {
			int val = Integer.valueOf(kj[0]) + Integer.valueOf(kj[1]) + Integer.valueOf(kj[2]);
			String ds = val % 2 == 0 ? "双" : "单";
			String dx = val >= 11 ? "大" : "小";
			String regEx = "[\u4e00-\u9fa5]";// 验证是否含有汉字
			Pattern pat = Pattern.compile(regEx);
			Matcher matcher = pat.matcher(betData);
			if (matcher.find()) {
				if (betData.equals(ds)) {
					count = 1;
				} else if (betData.equals(dx)) {
					count = 1;
				}
			} else {
				if (Integer.valueOf(betData) == val) {
					count = 1;
				}
			}
		} catch (Exception e) {

		}
		return count;
	}

	/**
	 * @describe:快3--三连号--通选--
	 * @param betData:666,111,222
	 * @param kjData:1
	 *            2 4
	 * @return:中奖注数
	 */
	public static int k33ltx(String betData, String kjData) {
		if (betData == null || "".equals(betData) || kjData == null || "".equals(kjData)) {
			return 0;
		}
		int count = 0;
		try {
			if (betData.equals("三连号通选")) {
				// 开奖号码排序
				String[] kj = kjData.split(",");
				Arrays.sort(kj);
				int[] kj_int = StringToInt(kj);
				if (kj_int[1] - kj_int[0] == 1 && kj_int[2] - kj_int[1] == 1) {
					count = 1;
				}
			}
		} catch (Exception e) {
		}
		return count;
	}

	/**
	 * @describe:快3--三同号--单选--
	 * @param betData:111,222,333,444,555,666
	 * @param kjData:1
	 *            1 1
	 * @return:中奖注数
	 */
	public static int k33tdx(String betData, String kjData) {
		if (betData == null || "".equals(betData) || kjData == null || "".equals(kjData)) {
			return 0;
		}
		// 判断是否为三同号
		HashMap<Object, Object> hashMap = getBetKeyCount(kjData.split(","));
		if (hashMap.size() != 1) {
			return 0;
		}
		// 开奖号码处理，去除,
		String kj = kjData.replaceAll(",", "");
		String[] bets = betData.split(",");
		int count = 0;
		for (String bet : bets) {
			if (bet.equals(kj)) {
				count = 1;
			}
		}
		return count;
	}

	/**
	 * @describe:快3--三同号--通选--
	 * @param betData:111,222,333,444,555,666
	 * @param kjData:1
	 *            1 1
	 * @return:中奖注数
	 */
	public static int k33ttx(String betData, String kjData) {
		if (betData == null || "".equals(betData) || kjData == null || "".equals(kjData)) {
			return 0;
		}
		int count = 0;
		if (betData.equals("三同号通选")) {
			// 判断是否为三同号
			String[] kj = kjData.split(",");
			HashMap<Object, Object> hashMap = getBetKeyCount(kj);
			if (hashMap.size() == 1) {
				count = 1;
			}
		}
		return count;
	}

	/**
	 * @describe:快3--三不同号--胆拖/标准--
	 * @param betData:1,2|4,5
	 *            或者1,4,5
	 * @param kjData:1
	 *            1 1
	 * @return:中奖注数
	 */
	public static int k33bt(String betData, String kjData) {
		if (betData == null || "".equals(betData) || kjData == null || "".equals(kjData)) {
			return 0;
		}
		int count = 0;
		try {
			String[] kj = kjData.split(",");
			// 开奖号码处理，判断是否为三同号
			HashMap<Object, Object> hashMap = getBetKeyCount(kj);
			if (hashMap.size() != 3) {
				return 0;
			}
			// 得到投注注数组合
			Set<String> group = new HashSet<>();
			if (betData.contains("|")) {
				String[] split = betData.split("\\|");
				// 胆吗数组
				String[] dArr = split[0].split(",");
				// 拖码数组
				String[] tArr = split[1].split(",");
				if (dArr.length > 2) {// 胆最多设置2个
					return 0;
				}
				int same = getSameCount(dArr, tArr);
				if (same > 0) {// 胆吗与拖码相同的个数，如相同则投注数据错误
					return 0;
				}
				if (dArr.length == 1) {
					// 得到拖码组合
					Set<String> set = ArrangeUtils.getGroup(split[1], 2);
					for (String s : dArr) {
						for (String t : set) {
							group.add(s + t);
						}
					}
				} else if (dArr.length == 2) {
					for (String t : tArr) {
						group.add(dArr[0] + "" + dArr[1] + "" + t);
					}
				}

			} else {
				group = ArrangeUtils.getGroup(betData, 3);
			}
			for (String string : group) {
				int isCount = 0;
				for (int i = 0; i < string.length(); i++) {
					String betStr = string.substring(i, i + 1);
					for (String tr : kj) {
						if (tr.equals(betStr)) {
							isCount += 1;
							break;
						}
					}
				}
				// 投注的3个字符都包含在开奖号码里面，则中奖，中奖注数加1
				if (isCount == 3) {
					count += 1;
					break;
				}
			}
		} catch (Exception e) {
		}
		return count;
	}

	private static int getSameCount(String[] a, String[] b) {
		int count = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (a[i].equals(b[j])) {
					count++;
					break;
				}
			}
		}
		return count;
	}

	/**
	 * @describe:快3--二同号--单选-
	 * @param betData:1|4,5
	 * @param kjData:1
	 *            1 2
	 * @return:中奖注数
	 */
	public static int k32t(String betData, String kjData) {
		if (betData == null || "".equals(betData) || kjData == null || "".equals(kjData)) {
			return 0;
		}
		int count = 0;
		try {
			String[] kjArr = kjData.split(",");
			// 开奖号码处理，判断是否为二同号
			HashMap<Object, Object> hashMap = getBetKeyCount(kjArr);
			if (hashMap.size() != 2) {// 开奖号码不能为豹子
				return 0;
			}
			// 得到投注注数组合
			String[] betStr = betData.split("\\|");
			// 得到不同号数组
			String[] btArr = betStr[1].split(",");
			// 得到二同号数组
			String[] etArr = betStr[0].split(",");
			// 得到二同号投注组合
			List<String> betArr = new ArrayList<>();
			for (int i = 0; i < etArr.length; i++) {
				for (int j = 0; j < btArr.length; j++) {
					betArr.add(etArr[i].substring(0, 1) + "," + etArr[i].substring(1, 2) + "," + btArr[j]);
				}
			}
			for (String string : betArr) {
				HashMap<Object, Object> hash_str = getBetKeyCount(string.split(","));
				int isCount = 0;
				for (Object v : hash_str.keySet()) {
					Object value = hash_str.get(v);
					for (Object k : hashMap.keySet()) {
						Object k_value = hashMap.get(k);
						if (value.toString().equals("2") && k_value.toString().equals("2")) {// 同号比较
							if (v.toString().equals(k.toString())) {
								isCount++;
							}
						}
						if (value.toString().equals("1") && k_value.toString().equals("1")) {// 不同号比较
							if (v.toString().equals(k.toString())) {
								isCount++;
							}
						}
					}
				}
				// 同号相等并且不同号也相等，则中奖，中奖注数加1
				if (isCount == 2) {
					count++;
				}
			}
		} catch (Exception e) {
		}
		return count;
	}

	/**
	 * @describe:快3--二同号--复选--
	 * @param betData:1,4,5
	 * @param kjData:1
	 *            1 2 可以为豹子
	 * @return:中奖注数
	 */
	public static int k32tfx(String betData, String kjData) {
		if (betData == null || "".equals(betData) || kjData == null || "".equals(kjData)) {
			return 0;
		}
		int count = 0;
		try {
			String[] kj = kjData.split(",");
			HashMap<Object, Object> hashMap = getBetKeyCount(kj);
			if (hashMap.size() == 3) {// 开奖三个号码都不同，则不中奖
				return 0;
			}
			// 得到投注注数组合
			String[] betStr = betData.split(",");
			for (String string : betStr) {
				int isCount = 0;
				String tr = string.substring(0, 1);
				for (String kjStr : kj) {
					if (kjStr.equals(tr)) {
						isCount += 1;
					}
				}
				if (isCount >= 2) {
					count += 1;
				}
			}
		} catch (Exception e) {
		}
		return count;
	}

	/**
	 * @describe:快3--二不同号--标准/胆拖--
	 * @param betData:1,4,5
	 *            或者 1|4,5
	 * @param kjData:1
	 *            1 2
	 * @return:中奖注数
	 */
	public static int k32bt(String betData, String kjData) {
		if (betData == null || "".equals(betData) || kjData == null || "".equals(kjData)) {
			return 0;
		}
		int count = 0;
		try {
			String[] kj = kjData.split(",");
			// 开奖号码处理，判断是否为二不同号
			HashMap<Object, Object> hashMap = getBetKeyCount(kj);
			if (hashMap.size() == 1) {
				return 0;
			}
			// 得到投注注数组合
			Set<String> group = new HashSet<>();
			if (betData.contains("|")) {// 胆拖玩法
				// 得到投注注数组合
				String[] betStr = betData.split("\\|");
				// 得到胆码
				String betEt = betStr[0];
				// 得到拖码字符串组合
				String betBt = betStr[1];
				// 得到拖码字符数组
				String[] etArr = betBt.split(",");
				for (int i = 0; i < etArr.length; i++) {
					group.add(betEt + etArr[i]);
				}
			} else {// 标准玩法
				group = ArrangeUtils.getGroup(betData, 2);
			}
			// 得到中奖注数
			for (String string : group) {
				int isCount = 0;
				for (int i = 0; i < string.length(); i++) {
					String betStr = string.substring(i, i + 1);
					for (String kjStr : kj) {
						if (kjStr.equals(betStr)) {
							isCount += 1;
							break;
						}
					}
				}
				// 投注的2个字符都包含在开奖号码里面，则中奖，中奖注数加1
				if (isCount == 2) {
					count += 1;
				}
			}
		} catch (Exception e) {
		}
		return count;
	}

	private static HashMap<Object, Object> getBetKeyCount(String[] kj) {
		HashMap<Object, Object> hashMap = new HashMap<>();
		for (int i = 0; i < kj.length; i++) {
			if (hashMap.containsKey(kj[i])) {
				hashMap.put(kj[i], Integer.valueOf(hashMap.get(kj[i]).toString()) + 1);
			} else {
				hashMap.put(kj[i], 1);
			}
		}
		return hashMap;
	}

	/** ====================广东11选5 计算开奖结果方法 start ===================== */

	/**
	 * 11选5--直选复式-二码专用
	 * 
	 * @param betData
	 *            投注号码
	 * @param kjData
	 *            开奖号码
	 * @param index1
	 *            开奖号码下标1
	 * @param index2
	 *            开奖号码下标2
	 * @return
	 */
	public static int gd11x52Zhixfushi(String betData, String kjData, int index1, int index2) {
		if (betData == null || kjData == null) {
			return 0;
		}
		String[] kj = kjData.split(",");
		Integer zj = 0;
		String[] bets = betData.split("\\|");
		String[] ones = bets[0].split(",");// 第一位
		String[] twos = bets[1].split(",");// 第二位
		if (Arrays.asList(ones).contains(kj[index1])) {
			zj++;
		}
		if (Arrays.asList(twos).contains(kj[index2])) {
			zj++;
		}
		return zj == 2 ? 1 : 0;
	}

	/**
	 * 11选5--组选复式-二码专用
	 * 
	 * @param betData
	 *            投注号码
	 * @param kjData
	 *            开奖号码
	 * @param index1
	 *            开奖号码下标1
	 * @param index2
	 *            开奖号码下标2
	 * @return
	 */
	public static int gd11x52Zuxfushi(String betData, String kjData, int index1, int index2) {
		if (betData == null || kjData == null) {
			return 0;
		}
		String[] kj = kjData.split(",");
		// 不同位置的开奖号码
		String kj_str = kj[index1] + "," + kj[index2];
		List<List<String>> kjLists = getCombination(kj_str, 2);
		List<List<String>> betLists = getCombination(betData, 2);
		int count = 0;
		for (List<String> kjList : kjLists) {
			if (!kjList.get(0).equals(kjList.get(1))) {
				for (List<String> betList : betLists) {
					if (betList.containsAll(kjList) && kjList.containsAll(betList)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 11选5--组选胆拖-二码专用
	 * 
	 * @param betData
	 *            投注号码
	 * @param kjData
	 *            开奖号码
	 * @param index1
	 *            开奖号码下标1
	 * @param index2
	 *            开奖号码下标2
	 * @return
	 */
	public static int gd11x52ZuxDtuo(String betData, String kjData, int index1, int index2) {
		if (betData == null || kjData == null) {
			return 0;
		}
		String[] kj = kjData.split(",");
		if (!betData.contains("|")) {
			return 0;
		}
		// 不同位置的开奖号码
		String kj_str = kj[index1] + "," + kj[index2];
		String[] bets = betData.split("\\|");
		String dan = bets[0];
		int count = 0;
		// 判断开奖号码是否包含胆
		if (kj_str.indexOf(dan) != -1) {
			count++;
		}
		if (count < 1) {
			return 0;
		}
		count = 0;
		String[] tou = bets[1].split(",");
		for (String t : tou) {
			if (kj_str.indexOf(t) != -1) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @describe:11选5--二码-前二直选
	 * @param betData
	 *            投注号码：01,02
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Q2Zx(String betData, String kjData) {
		return gd11x52Zhixfushi(betData, kjData, 0, 1);
	}

	/**
	 * @describe:11选5--二码-前二组选
	 * @param betData
	 *            投注号码：07,08
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Q2x(String betData, String kjData) {
		return gd11x52Zuxfushi(betData, kjData, 0, 1);
	}

	/**
	 * @describe:11选5--二码-前二组选胆拖
	 * @param betData
	 *            投注号码：01,11|02,03,04,05,06,07,08,09,10
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Q2zDt(String betData, String kjData) {
		return gd11x52ZuxDtuo(betData, kjData, 0, 1);
	}

	/**
	 * @describe:11选5--二码-后二直选
	 * @param betData
	 *            投注号码：01,02
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5H2(String betData, String kjData) {
		return gd11x52Zhixfushi(betData, kjData, 3, 4);
	}

	/**
	 * @describe:11选5--二码-后二组选
	 * @param betData
	 *            投注号码：07,08
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5H2z(String betData, String kjData) {
		return gd11x52Zuxfushi(betData, kjData, 3, 4);
	}

	/**
	 * @describe:11选5--二码-后二组选胆拖
	 * @param betData
	 *            投注号码：(01 11)02 03 04 05 06 07 08 09 10
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5H2zDt(String betData, String kjData) {
		return gd11x52ZuxDtuo(betData, kjData, 3, 4);
	}

	/**
	 * 11选5--直选复式-三码专用
	 * 
	 * @param betData
	 *            投注号码
	 * @param kjData
	 *            开奖号码
	 * @param index1
	 *            开奖号码下标1
	 * @param index2
	 *            开奖号码下标2
	 * @param index3
	 *            开奖号码下标3
	 * @return
	 */
	public static int gd11x53Zhixfushi(String betData, String kjData, int index1, int index2, int index3) {
		if (betData == null || kjData == null) {
			return 0;
		}
		String[] kj = kjData.split(",");
		Integer zj = 0;
		String[] bets = betData.split("\\|");
		String[] ones = bets[0].split(",");
		String[] twos = bets[1].split(",");
		String[] threes = bets[2].split(",");
		if (Arrays.asList(ones).contains(kj[index1])) {
			zj++;
		}
		if (Arrays.asList(twos).contains(kj[index2])) {
			zj++;
		}
		if (Arrays.asList(threes).contains(kj[index3])) {
			zj++;
		}
		return zj == 3 ? 1 : 0;
	}

	/**
	 * @describe:11选5--三码-前三直选
	 * @param betData
	 *            投注号码：01,02,03
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Q3(String betData, String kjData) {
		return gd11x53Zhixfushi(betData, kjData, 0, 1, 2);
	}

	/**
	 * @describe:11选5--三码-中三直选
	 * @param betData
	 *            投注号码：01,02,03
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Z3(String betData, String kjData) {
		return gd11x53Zhixfushi(betData, kjData, 1, 2, 3);
	}

	/**
	 * @describe:11选5--三码-后三直选
	 * @param betData
	 *            投注号码：01,02,03
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5H3(String betData, String kjData) {
		return gd11x53Zhixfushi(betData, kjData, 2, 3, 4);
	}

	/**
	 * 11选5--组选复式-三码专用
	 * 
	 * @param betData
	 *            投注号码
	 * @param kjData
	 *            开奖号码
	 * @param index1
	 *            开奖号码下标1
	 * @param index2
	 *            开奖号码下标2
	 * @param index3
	 *            开奖号码下标3
	 * @return
	 */
	public static int gd11x53Zuxfushi(String betData, String kjData, int index1, int index2, int index3) {
		if (betData == null || kjData == null) {
			return 0;
		}
		String[] kj = kjData.split(",");
		int count = 0;
		// 不同位置的开奖号码
		String kj_str = kj[index1] + "," + kj[index2] + "," + kj[index3];
		List<List<String>> kjLists = getCombination(kj_str, 3);
		List<List<String>> betLists = getCombination(betData, 3);
		for (List<String> kjList : kjLists) {
			if (!kjList.get(0).equals(kjList.get(1))) {
				for (List<String> betList : betLists) {
					if (betList.containsAll(kjList) && kjList.containsAll(betList)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * @describe:11选5--三码-前三组选
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Q3z(String betData, String kjData) {
		return gd11x53Zuxfushi(betData, kjData, 0, 1, 2);
	}

	/**
	 * @describe:11选5--三码-中三组选
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Z3z(String betData, String kjData) {
		return gd11x53Zuxfushi(betData, kjData, 1, 2, 3);
	}

	/**
	 * @describe:11选5--三码-后三组选
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5H3z(String betData, String kjData) {
		return gd11x53Zuxfushi(betData, kjData, 2, 3, 4);
	}

	/**
	 * 11选5--组选胆拖-三码专用
	 * 
	 * @param betData
	 *            投注号码
	 * @param kjData
	 *            开奖号码
	 * @param index1
	 *            开奖号码下标1
	 * @param index2
	 *            开奖号码下标2
	 * @param index3
	 *            开奖号码下标3
	 * @return
	 */
	public static int gd11x53ZuxDtuo(String betData, String kjData, int index1, int index2, int index3) {
		if (betData == null || kjData == null) {
			return 0;
		}
		String[] kj = kjData.split(",");
		if (!betData.contains("|")) {
			return 0;
		}
		String kj_str = kj[index1] + "," + kj[index2] + "," + kj[index3];
		String[] bets = betData.split("\\|");
		String[] dan = bets[0].split(",");
		int count = 0;
		if (dan.length == 1) {// 一胆拖
			// 判断开奖号码是否包含胆
			if (kj_str.indexOf(dan[0]) == -1) {
				return 0;
			}
			// 判断开奖号码是否包含拖码
			int isCount = 0;
			String[] tou_code = bets[1].split(",");
			for (String tou : tou_code) {
				if (kj_str.indexOf(tou) != -1) {
					isCount++;
				}
			}
			if (isCount == 2) {
				count++;
			}

		} else if (dan.length == 2) {// 二胆拖
			int isCount = 0;
			// 判断开奖号码是否包含胆
			for (String d : dan) {
				if (kj_str.indexOf(d) != -1) {
					isCount++;
				}
			}
			if (isCount != 2) {
				return 0;
			}
			// 判断开奖号码是否包含拖码
			String[] tou_code = bets[1].split(",");
			for (String tou : tou_code) {
				if (kj_str.indexOf(tou) != -1) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * @describe:11选5--三码-前三组选胆拖
	 * @param betData
	 *            投注号码：(01)02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Q3zDt(String betData, String kjData) {
		return gd11x53ZuxDtuo(betData, kjData, 0, 1, 2);
	}

	/**
	 * @describe:11选5--三码-中三组选胆拖
	 * @param betData
	 *            投注号码：(01)02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Z3zDt(String betData, String kjData) {
		return gd11x53ZuxDtuo(betData, kjData, 1, 2, 3);
	}

	/**
	 * @describe:11选5--三码-后三组选胆拖
	 * @param betData
	 *            投注号码：(01)02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5H3zDt(String betData, String kjData) {
		return gd11x53ZuxDtuo(betData, kjData, 2, 3, 4);
	}

	/**
	 * 11选5--不定位-三码专用
	 * 
	 * @param betData
	 *            投注号码
	 * @param kjData
	 *            开奖号码
	 * @param index1
	 *            开奖号码下标1
	 * @param index2
	 *            开奖号码下标2
	 * @param index3
	 *            开奖号码下标3
	 * @return
	 */
	public static int gd11x5bdw3(String betData, String kjData, int index1, int index2, int index3) {
		if (betData == null || kjData == null) {
			return 0;
		}
		int count = 0;
		String[] kj = kjData.split(",");
		String[] bets = betData.split(",");
		for (String bet : bets) {
			if (bet.equals(kj[index1])) {
				count++;
			}
			if (bet.equals(kj[index2])) {
				count++;
			}
			if (bet.equals(kj[index2])) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @describe:11选5--不定胆-前三
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5bdwQ3(String betData, String kjData) {
		return gd11x5bdw3(betData, kjData, 0, 1, 2);
	}

	/**
	 * @describe:11选5--不定胆-中三
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5bdwZ3(String betData, String kjData) {
		return gd11x5bdw3(betData, kjData, 1, 2, 3);
	}

	/**
	 * @describe:11选5--不定胆-后三
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5bdwH3(String betData, String kjData) {
		return gd11x5bdw3(betData, kjData, 2, 3, 4);
	}

	/**
	 * 11选5--定位胆
	 * 
	 * @param betData
	 *            投注号码
	 * @param kjData
	 *            开奖号码
	 * @return
	 */
	public static int gd11x5dwd(String betData, String kjData) {
		if (betData == null || kjData == null) {
			return 0;
		}
		int count = 0;
		String[] kj = kjData.split(",");
		String[] bets = betData.split("\\|");
		for (int i = 0; i < bets.length; i++) {
			String[] bet = bets[i].split(",");
			for (int j = 0; j < bet.length; j++) {
				if (bet[j].equals(kj[i])) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * @describe:11选5--任选-一中一-复式
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rx1fs(String betData, String kjData) {
		if (betData == null || kjData == null) {
			return 0;
		}
		int count = 0;
		String[] kj = kjData.split(",");
		String[] bets = betData.split(",");
		for (String kj_str : kj) {
			for (String bet : bets) {
				if (kj_str.equals(bet)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * @describe:11选5--任选-M中M-复式
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @param num
	 *            m
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rxfs(String betData, String kjData, int num) {
		if (betData == null || kjData == null) {
			return 0;
		}
		int count = 0;
		List<List<String>> betLists = getCombination(betData, num);
		if (num > 5) {
			for (List<String> betList : betLists) {
				Object[] bet = betList.toArray();
				int isCount = 0;
				for (Object o : bet) {
					String[] kj = kjData.split(",");
					for (String k : kj) {
						if (o.equals(k)) {
							isCount++;
						}
					}
				}
				if (isCount >= 5) {
					count++;
				}
			}
		} else {
			List<List<String>> kjLists = getCombination(kjData, num);
			for (List<String> kjList : kjLists) {
				if (!kjList.get(0).equals(kjList.get(1))) {
					for (List<String> betList : betLists) {
						if (betList.containsAll(kjList) && kjList.containsAll(betList)) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	/**
	 * @describe:11选5--任选-二中二-复式
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rx2fs(String betData, String kjData) {
		return gd11x5Rxfs(betData, kjData, 2);
	}

	/**
	 * @describe:11选5--任选-三中三-复式
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rx3fs(String betData, String kjData) {
		return gd11x5Rxfs(betData, kjData, 3);
	}

	/**
	 * @describe:11选5--任选-4中4-复式
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rx4fs(String betData, String kjData) {
		return gd11x5Rxfs(betData, kjData, 4);
	}

	/**
	 * @describe:11选5--任选-5中5-复式
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rx5fs(String betData, String kjData) {
		return gd11x5Rxfs(betData, kjData, 5);
	}

	/**
	 * @describe:11选5--任选-6中5-复式
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rx6fs(String betData, String kjData) {
		return gd11x5Rxfs(betData, kjData, 6);
	}

	/**
	 * @describe:11选5--任选-7中5-复式
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rx7fs(String betData, String kjData) {
		return gd11x5Rxfs(betData, kjData, 7);
	}

	/**
	 * @describe:11选5--任选-8中5-复式
	 * @param betData
	 *            投注号码：07,08,09,10,11
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rx8fs(String betData, String kjData) {
		return gd11x5Rxfs(betData, kjData, 8);
	}

	/**
	 * @describe:11选5--任选-2中2-胆拖
	 * @param betData
	 *            投注号码：01|02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @param num
	 *            m
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rxdt2(String betData, String kjData) {
		return gd11x5Rxdt(betData, kjData, 2);
	}

	/**
	 * @describe:11选5--任选-3中3-胆拖
	 * @param betData
	 *            投注号码：01|02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @param num
	 *            m
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rxdt3(String betData, String kjData) {
		return gd11x5Rxdt(betData, kjData, 3);
	}

	/**
	 * @describe:11选5--任选-4中4-胆拖
	 * @param betData
	 *            投注号码：01|02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @param num
	 *            m
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rxdt4(String betData, String kjData) {
		return gd11x5Rxdt(betData, kjData, 4);
	}

	/**
	 * @describe:11选5--任选-5中5-胆拖
	 * @param betData
	 *            投注号码：01|02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @param num
	 *            m
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rxdt5(String betData, String kjData) {
		return gd11x5Rxdt(betData, kjData, 5);
	}

	/**
	 * @describe:11选5--任选-6中5-胆拖
	 * @param betData
	 *            投注号码：01|02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @param num
	 *            m
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rxdt6(String betData, String kjData) {
		return gd11x5Rxdt(betData, kjData, 6);
	}

	/**
	 * @describe:11选5--任选-7中5-胆拖
	 * @param betData
	 *            投注号码：01|02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @param num
	 *            m
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rxdt7(String betData, String kjData) {
		return gd11x5Rxdt(betData, kjData, 7);
	}

	/**
	 * @describe:11选5--任选-8中8-胆拖
	 * @param betData
	 *            投注号码：01|02 03 04
	 * @param kjData
	 *            开奖号码：01,02,03,05,06
	 * @param num
	 *            m
	 * @return 返回中奖注数
	 */
	public static int gd11x5Rxdt8(String betData, String kjData) {
		return gd11x5Rxdt(betData, kjData, 8);
	}

	/**
	 * 11选5 任选-m中n-胆拖
	 * 
	 * @param betData
	 *            投注号码
	 * @param kjData
	 *            开奖号码
	 * @param num
	 *            m
	 * @return
	 */
	public static int gd11x5Rxdt(String betData, String kjData, int num) {
		if (betData == null || kjData == null) {
			return 0;
		}
		if (!betData.contains("|")) {
			return 0;
		}
		int count = 0;
		String[] bets = betData.split("\\|");
		String[] dans = bets[0].split(",");
		// 拖码组合
		List<List<String>> betLists = getCombination(bets[1], num - dans.length);
		for (int i = 0; i < betLists.size(); i++) {
			List<String> betStrs = betLists.get(i);
			for (int k = 0; k < dans.length; k++) {
				betStrs.add(dans[k]);
			}
		}
		switch (num) {
		case 2:// 2中2
			for (int i = 0; i < betLists.size(); i++) {
				List<String> betStrs = betLists.get(i);
				int isCount = 0;
				for (String bet : betStrs) {
					if (kjData.indexOf(bet) != -1) {
						isCount++;
					}
				}
				if (isCount == 2) {
					count++;
				}
			}
			break;
		case 3:// 3中3
			for (int i = 0; i < betLists.size(); i++) {
				List<String> betStrs = betLists.get(i);
				int isCount = 0;
				for (String bet : betStrs) {
					if (kjData.indexOf(bet) != -1) {
						isCount++;
					}
				}
				if (isCount == 3) {
					count++;
				}
			}
			break;
		case 4:// 4中4
			for (int i = 0; i < betLists.size(); i++) {
				List<String> betStrs = betLists.get(i);
				int isCount = 0;
				for (String bet : betStrs) {
					if (kjData.indexOf(bet) != -1) {
						isCount++;
					}
				}
				if (isCount == 4) {
					count++;
				}
			}
			break;
		case 5:// 5中5
			for (int i = 0; i < betLists.size(); i++) {
				List<String> betStrs = betLists.get(i);
				int isCount = 0;
				for (String bet : betStrs) {
					if (kjData.indexOf(bet) != -1) {
						isCount++;
					}
				}
				if (isCount == 5) {
					count++;
				}
			}
			break;
		case 6:// 6中5
		case 7:// 7中5
		case 8:// 8中5
			for (int i = 0; i < betLists.size(); i++) {
				List<String> betStrs = betLists.get(i);
				int isCount = 0;
				for (String bet : betStrs) {
					if (kjData.indexOf(bet) != -1) {
						isCount++;
					}
				}
				if (isCount == 5) {
					count++;
				}
			}
			break;
		}
		return count;
	}

	/** 广东11选5 计算开奖结果方法 end */

	/** 北京PK10 计算开奖结果方法 start */
	// 前一
	public static int kjq1(String betData, String kjData) {
		if (betData != null && kjData != null) {
			return calcResult(betData.split("[|]")[0], kjData, 0);
		}
		return 0;
	}

	// 前二 01,02|02,03
	public static int kjq2(String betData, String kjData) {
		int result = 0;
		if (betData != null && kjData != null) {
			String betArr[] = betData.split("[|]");
			if (betArr.length > 1) {
				result = calcResult(betArr[0], kjData, 0);
				if (result == 1) {
					result = calcResult(betArr[1], kjData, 1);
				}
			}
		}
		return result;
	}

	// 前三
	public static int kjq3(String betData, String kjData) {
		int result = 0;

		if (betData != null && kjData != null) {
			String betArr[] = betData.split("[|]");
			result = kjq2(betData, kjData);
			if (result == 1) {
				result = calcResult(betArr[2], kjData, 2);
			}
		}
		return result;
	}

	// 定位胆
	public static int pk10dwd(String betData, String kjData) {
		int result = 0;
		if (betData != null && kjData != null) {
			String betArr[] = betData.split("[|]");
			for (int i = 0; i < betArr.length; i++) {
				if (!"_".equals(betArr[i])) {
					result += calcResult(betArr[i], kjData, i);
				}
			}
		}
		return result;

	}

	// 北京pk拾 冠亚和
	public static int pk10gyh(String betData, String kjData) {
		int result = 0;
		if (betData != null && kjData != null) {
			String betArr[] = betData.split(",");
			String kjArr[] = kjData.split(",");
			if (kjArr.length > 1) {
				Integer lottery = Integer.valueOf(kjArr[0]) + Integer.valueOf(kjArr[1]);
				for (String bet : betArr) {
					if (Integer.valueOf(bet) == lottery) {
						return 1;
					}
				}
			}
		}
		return result;
	}

	/** 龙虎和 冠军 */
	public static int pk10lh1(String betData, String kjData) {
		return getResult(betData, kjData, 0);
	}

	/** 龙虎和 亚军 */
	public static int pk10lh2(String betData, String kjData) {
		return getResult(betData, kjData, 1);
	}

	/** 龙虎和 季军 */
	public static int pk10lh3(String betData, String kjData) {
		return getResult(betData, kjData, 2);
	}

	/** 龙虎和 第四名 */
	public static int pk10lh4(String betData, String kjData) {
		return getResult(betData, kjData, 3);
	}

	/** 龙虎和 第五名 */
	public static int pk10lh5(String betData, String kjData) {
		return getResult(betData, kjData, 4);
	}

	/** 五行 冠军 */
	public static int pk10wx1(String betData, String kjData) {
		if (kjData != null) {
			return calc5(betData, kjData.split(",")[0]);
		}
		return 0;
	}

	/** 五行 亚军 */
	public static int pk10wx2(String betData, String kjData) {
		if (kjData != null) {
			return calc5(betData, kjData.split(",")[1]);
		}
		return 0;
	}

	/** 五行 季军 */
	public static int pk10wx3(String betData, String kjData) {
		if (kjData != null) {
			return calc5(betData, kjData.split(",")[2]);
		}
		return 0;
	}

	/** 大小单双 - 大小 - 冠军 */
	public static int pkdx1(String betData, String kjData) {
		if (kjData != null) {
			return calcdxds(betData, kjData.split(",")[0]);
		}
		return 0;
	}

	/** 大小单双 - 大小 - 亚军 */
	public static int pkdx2(String betData, String kjData) {
		if (kjData != null) {
			return calcdxds(betData, kjData.split(",")[1]);
		}
		return 0;
	}

	/** 大小单双 - 大小 - 季军 */
	public static int pkdx3(String betData, String kjData) {
		if (kjData != null) {
			return calcdxds(betData, kjData.split(",")[2]);
		}
		return 0;
	}

	/** 大小单双 - 单双 - 冠军 */
	public static int pkds1(String betData, String kjData) {
		if (kjData != null) {
			return calcdxds(betData, kjData.split(",")[0]);
		}
		return 0;
	}

	/** 大小单双 - 单双 - 亚军 */
	public static int pkds2(String betData, String kjData) {
		if (kjData != null) {
			return calcdxds(betData, kjData.split(",")[1]);
		}
		return 0;
	}

	/** 大小单双 - 单双 - 季军 */
	public static int pkds3(String betData, String kjData) {
		if (kjData != null) {
			return calcdxds(betData, kjData.split(",")[2]);
		}
		return 0;
	}

	/** 大小单双 - 大小单双 */
	public static int pkdxds(String betData, String kjData) {
		if (kjData != null) {
			String kjArr[] = kjData.split(",");
			return calcdxds(betData, kjArr[0] + "," + kjArr[1]);
		}
		return 0;
	}

	/**
	 * 计算 大小单双
	 */
	public static int calcdxds(String bets, String datas) {
		if (bets != null && datas != null) {
			int num = 0;
			String betArr[] = bets.split(",");
			String dataArr[] = datas.split(",");
			for (String data : dataArr) {
				num += Integer.valueOf(data);
			}
			for (String bet : betArr) {
				switch (bet) {
				case "大":
					if (num > 5) {
						return 1;
					}
					break;
				case "小":
					if (num < 6) {
						return 1;
					}
					break;
				case "单":
					if (num % 2 != 0) {
						return 1;
					}
					break;
				case "双":
					if (num % 2 == 0) {
						return 1;
					}
					break;
				default:
					break;
				}
			}
		}
		return 0;
	}

	/**
	 * 五行计算 01 02为金 03 04为木 05 06为水 07 08火 09 10土
	 */
	public static int calc5(String bets, String datas) {
		if (bets != null && datas != null) {
			int data = Integer.valueOf(datas);
			String betArr[] = bets.split(",");
			for (String bet : betArr) {
				switch (bet) {
				case "金":
					if (data == 1 || data == 2) {
						return 1;
					}
					break;
				case "木":
					if (data == 3 || data == 4) {
						return 1;
					}
					break;
				case "水":
					if (data == 5 || data == 6) {
						return 1;
					}
					break;
				case "火":
					if (data == 7 || data == 8) {
						return 1;
					}
					break;
				case "土":
					if (data == 9 || data == 10) {
						return 1;
					}
					break;
				default:
					break;
				}
			}
		}
		return 0;
	}

	/**
	 * 龙虎计算
	 */
	public static int getResult(String bets, String datas, int type) {
		if (bets != null && datas != null) {
			String betArr[] = bets.split(",");
			String dataArr[] = datas.split(",");
			for (String bet : betArr) {
				if (Integer.valueOf(dataArr[type]) > Integer.valueOf(dataArr[dataArr.length - type - 1])
						&& "龙".equals(bet)) {
					return 1;
				} else if (Integer.valueOf(dataArr[type]) < Integer.valueOf(dataArr[dataArr.length - type - 1])
						&& "虎".equals(bet)) {
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * 计算位置是否猜中
	 */
	public static int calcResult(String bets, String datas, int type) {
		String betArr[] = bets.split(",");
		String data = datas.split(",")[type];
		for (int i = 0; i < betArr.length; i++) {
			if (betArr[i].equals(data)) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 北京PK10-混合
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int bj28Hh(String betData, String kjData) {
		String[] jkString = kjData.split(",");
		int win = 0;
		int sum = 0;
		for (int i = 0; i < jkString.length; i++) {
			sum += Integer.valueOf(jkString[i]);
		}
		switch (betData) {
		case "大":
			win = sum >= 14 && sum <= 27 ? 1 : 0;
			break;
		case "小":
			win = sum >= 0 && sum <= 13 ? 1 : 0;
			break;
		case "单":
			win = sum % 2 == 1 ? 1 : 0;
			break;
		case "双":
			win = sum % 2 == 0 ? 1 : 0;
			break;
		case "大单":
			win = sum >= 15 && sum <= 27 && sum % 2 == 1 ? 1 : 0;
			break;
		case "小单":
			win = sum >= 1 && sum <= 13 && sum % 2 == 1 ? 1 : 0;
			break;
		case "大双":
			win = sum >= 14 && sum <= 26 && sum % 2 == 0 ? 1 : 0;
			break;
		case "小双":
			win = sum >= 0 && sum <= 12 && sum % 2 == 0 ? 1 : 0;
			break;
		case "极大":
			win = sum >= 22 && sum <= 27 ? 1 : 0;
			break;
		case "极小":
			win = sum >= 0 && sum <= 5 ? 1 : 0;
			break;
		default:
			win = -1;
			break;
		}
		return win;
	}

	/**
	 * 北京28-特码
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int bj28Tm(String betData, String kjData) {
		String[] jkString = kjData.split(",");
		int win = 0;
		int sum = 0;
		for (int i = 0; i < jkString.length; i++) {
			sum += Integer.valueOf(jkString[i]);
		}
		win = Integer.valueOf(betData) == sum ? 1 : 0;
		return win;
	}

	/**
	 * 北京28-特码/特码包三
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int bj28TmB3(String betData, String kjData) {
		String[] betString = betData.split("\\|");
		String[] kjString = kjData.split(",");
		int win = 0;
		int kjsum = 0, betsum = 0;
		for (int i = 0; i < kjString.length; i++) {
			kjsum += Integer.valueOf(kjString[i]);
		}
		for (int i = 0; i < betString.length; i++) {
			betsum += Integer.valueOf(betString[i]);
		}
		win = betsum == kjsum ? 1 : 0;
		return win;
	}

	/**
	 * 北京28-波色
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int bj28Bs(String betData, String kjData) {
		String[] jkString = kjData.split(",");

		int win = 0;
		int sum = 0;
		for (int i = 0; i < jkString.length; i++) {
			sum += Integer.valueOf(jkString[i]);
		}

		// 绿波1，4，7，10，16，19，22，25；
		Set<Integer> green = new HashSet<>();
		green.add(1);
		green.add(4);
		green.add(7);
		green.add(10);
		green.add(16);
		green.add(19);
		green.add(22);
		green.add(25);

		// 蓝波2，5，8，11，17，20，23，26；
		Set<Integer> blue = new HashSet<>();
		blue.add(2);
		blue.add(5);
		blue.add(8);
		blue.add(11);
		blue.add(17);
		blue.add(20);
		blue.add(23);
		blue.add(26);

		// 红波3，6，9，12，15，18，21，24；
		Set<Integer> red = new HashSet<>();
		red.add(3);
		red.add(6);
		red.add(9);
		red.add(12);
		red.add(15);
		red.add(18);
		red.add(21);
		red.add(24);

		switch (betData) {
		case "绿波":
			win = green.contains(sum) ? 1 : 0;
			break;
		case "蓝波":
			win = blue.contains(sum) ? 1 : 0;
			break;
		case "红波":
			win = red.contains(sum) ? 1 : 0;
			break;
		default:
			win = -1;
			break;
		}
		return win;
	}

	/**
	 * 北京28-豹子
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int bj28Bz(String betData, String kjData) {
		String[] jkString = kjData.split(",");
		int win = 0;
		win = jkString[0].equals(jkString[1]) && jkString[0].equals(jkString[2]) ? 1 : 0;
		return win;
	}

	/** 重庆时时彩开奖java */
	/**
	 * @describe:时时彩--定位胆--定位胆
	 * @param betData:1,8|1,6|2||
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscdwd(String betData, String kjData) {
		String[] bets = betData.split("\\|");
		String[] kj = kjData.split(",");
		int count = 0;
		for (int i = 0; i < bets.length; i++) {
			if (bets[i].contains(kj[i])) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 时时彩-五星-直选复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssc5xzxfs(String betData, String kjData) {
		String[] bets = betData.split("\\|");
		String[] kjs = kjData.split(",");
		for (int i = 0; i < bets.length; i++) {
			if (!bets[i].contains(kjs[i])) {
				return 0;
			}
		}
		return 1;
	}

	/**
	 * 时时彩-四星-直选复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssc4xzxfs(String betData, String kjData) {
		String[] bets = betData.split("\\|");
		String[] kjs = kjData.split(",");
		for (int i = 0; i < bets.length; i++) {
			if (!bets[i].contains(kjs[i + 1])) {
				return 0;
			}
		}
		return 1;
	}

	/**
	 * 时时彩-后三-直选复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssch3zxfs(String betData, String kjData) {
		String[] bets = betData.split("\\|");
		String[] kjs = kjData.split(",");
		for (int i = 0; i < bets.length; i++) {
			if (!bets[i].contains(kjs[i + 2])) {
				return 0;
			}
		}
		return 1;
	}

	/**
	 * 时时彩-后三-直选和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssch3zxhz(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String[] bts = betData.split(",");
		int count = 0;
		int result = Integer.parseInt(kjs[2]) + Integer.parseInt(kjs[3]) + Integer.parseInt(kjs[4]);
		for (String str : bts) {
			int temp = Integer.parseInt(str);
			if (temp == result) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 时时彩-后三-直选跨度
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssch3zxkd(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		Integer[] hs = { Integer.parseInt(kjs[2]), Integer.parseInt(kjs[3]), Integer.parseInt(kjs[4]) };
		Arrays.sort(hs);
		int result = hs[2] - hs[0];
		if (betData.contains(String.valueOf(result))) {
			return 1;
		}
		return 0;
	}

	/**
	 * 时时彩-后三-组三复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssch3z3fs(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String hs = kjs[2] + "," + kjs[3] + "," + kjs[4];
		return fc3dZ3(betData, hs);
	}

	/**
	 * 时时彩-后三-组六复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssch3z6fs(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String hs = kjs[2] + "," + kjs[3] + "," + kjs[4];
		return fc3dZ6(betData, hs);
	}

	/**
	 * 时时彩-后三-和值尾数
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssch3hzws(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		int result = Integer.parseInt(kjs[2]) + Integer.parseInt(kjs[3]) + Integer.parseInt(kjs[4]);
		result %= 10;
		if (betData.contains(String.valueOf(result))) {
			return 1;
		}
		return 0;
	}

	/**
	 * 时时彩-后三-特殊号
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssch3tsh(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		Integer[] hs = { Integer.parseInt(kjs[2]), Integer.parseInt(kjs[3]), Integer.parseInt(kjs[4]) };
		Arrays.sort(hs);
		if (!(hs[0] == hs[1] && hs[1] == hs[2]) && (hs[0] == hs[1] || hs[2] == hs[1])) {
			if (("对子").equals(betData)) {
				return 1;
			}
		}
		if (hs[0] == (hs[1] - 1) && hs[1] == (hs[2] - 1)) {
			if (("顺子").equals(betData)) {
				return 1;
			}
		}
		if (hs[0] == hs[1] && hs[1] == hs[2]) {
			if (("豹子").equals(betData)) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 时时彩-后三-组三和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssch3z3hz(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String hs = kjs[2] + "," + kjs[3] + "," + kjs[4];
		return fc3dZ3hz(betData, hs);
	}

	/**
	 * 时时彩-后三-组六和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int ssch3z6hz(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String hs = kjs[2] + "," + kjs[3] + "," + kjs[4];
		return fc3dZ6hz(betData, hs);
	}

	private static int sscbd(String betData, String kjData, int type) {
		if (StringUtils.isEmpty(betData)) {
			return 0;
		}
		if (type == 2) {
			String[] kjs = kjData.split(",");
			if (kjs[0].equals(kjs[1])) {
				return 0;
			}
		}

		if (kjData.contains(betData)) {
			kjData = kjData.replaceAll(",", "");
			if (type == 3) {// 组三包胆
				if (kjData.matches(".*(.).*\\1.*") && !kjData.matches("(\\w)\\1{2,}")) {// 是否有重复的值
					return 1;
				}
			}
			if (type == 6) {// 组六包胆
				if (!kjData.matches(".*(.).*\\1.*")) {// 是否有重复的值
					return 1;
				}
			}
			if (type == 2) {// 前二包胆
				return 1;
			}
		}
		return 0;
	}

	/**
	 * @describe:后三--组选包胆,组三包胆
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int ssch3z3bd(String betData, String kjData) {
		return sscbd(betData, kjData.substring(4, kjData.length()), 3);
	}

	/**
	 * @describe:后三--组选包胆,组六包胆
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int ssch3z6bd(String betData, String kjData) {
		return sscbd(betData, kjData.substring(4, kjData.length()), 6);
	}

	/**
	 * 时时彩-前三-直选复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq3zxfs(String betData, String kjData) {
		String[] bets = betData.split("\\|");
		String[] kjs = kjData.split(",");
		for (int i = 0; i < bets.length; i++) {
			if (!bets[i].contains(kjs[i])) {
				return 0;
			}
		}
		return 1;
	}

	/**
	 * 时时彩-前三-直选和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq3zxhz(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String[] bts = betData.split(",");
		int count = 0;
		int result = Integer.parseInt(kjs[0]) + Integer.parseInt(kjs[1]) + Integer.parseInt(kjs[2]);
		for (String str : bts) {
			int temp = Integer.parseInt(str);
			if (temp == result) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 时时彩-前三-直选跨度
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq3zxkd(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		Integer[] hs = { Integer.parseInt(kjs[0]), Integer.parseInt(kjs[1]), Integer.parseInt(kjs[2]) };
		Arrays.sort(hs);
		int result = hs[2] - hs[0];
		if (betData.contains(String.valueOf(result))) {
			return 1;
		}
		return 0;
	}

	/**
	 * 时时彩-前三-组三复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq3z3fs(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String hs = kjs[0] + "," + kjs[1] + "," + kjs[2];
		return fc3dZ3(betData, hs);
	}

	/**
	 * 时时彩-前三-组六复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq3z6fs(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String hs = kjs[0] + "," + kjs[1] + "," + kjs[2];
		return fc3dZ6(betData, hs);
	}

	/**
	 * 时时彩-前三-和值尾数
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq3hzws(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		int result = Integer.parseInt(kjs[0]) + Integer.parseInt(kjs[1]) + Integer.parseInt(kjs[2]);
		result %= 10;
		if (betData.contains(String.valueOf(result))) {
			return 1;
		}
		return 0;
	}

	/**
	 * 时时彩-前三-特殊号
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq3tsh(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		Integer[] hs = { Integer.parseInt(kjs[0]), Integer.parseInt(kjs[1]), Integer.parseInt(kjs[2]) };
		Arrays.sort(hs);
		if (!(hs[0] == hs[1] && hs[1] == hs[2]) && (hs[0] == hs[1] || hs[2] == hs[1])) {
			if (("对子").equals(betData)) {
				return 1;
			}
		}
		if (hs[0] == (hs[1] - 1) && hs[1] == (hs[2] - 1)) {
			if (("顺子").equals(betData)) {
				return 1;
			}
		}
		if (hs[0] == hs[1] && hs[1] == hs[2]) {
			if (("豹子").equals(betData)) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 时时彩-前三-组三和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq3z3hz(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String hs = kjs[0] + "," + kjs[1] + "," + kjs[2];
		return fc3dZ3hz(betData, hs);
	}

	/**
	 * 时时彩-前三-组六和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq3z6hz(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String hs = kjs[0] + "," + kjs[1] + "," + kjs[2];
		return fc3dZ6hz(betData, hs);
	}

	/**
	 * @describe:前三--组选包胆,组三包胆
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int sscq3z3bd(String betData, String kjData) {
		return sscbd(betData, kjData.substring(0, 5), 3);
	}

	/**
	 * @describe:前三--组选包胆,组六包胆
	 * @param betData:1,2,3,4,7,8,11
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int sscq3z6bd(String betData, String kjData) {
		return sscbd(betData, kjData.substring(0, 5), 6);
	}

	/**
	 * 时时彩-前二-直选复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq2zxfs(String betData, String kjData) {
		String[] bets = betData.split("\\|");
		String[] kjs = kjData.split(",");
		for (int i = 0; i < bets.length; i++) {
			if (!bets[i].contains(kjs[i])) {
				return 0;
			}
		}
		return 1;
	}

	/**
	 * 时时彩-前二-直选和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq2zxhz(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		String[] bts = betData.split(",");
		int count = 0;
		int result = Integer.parseInt(kjs[0]) + Integer.parseInt(kjs[1]);
		for (String str : bts) {
			int temp = Integer.parseInt(str);
			if (temp == result) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 时时彩-前二-直选跨度
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq2zxkd(String betData, String kjData) {
		String[] kjs = kjData.split(",");
		Integer[] hs = { Integer.parseInt(kjs[0]), Integer.parseInt(kjs[1]) };
		Arrays.sort(hs);
		int result = hs[1] - hs[0];
		if (betData.contains(String.valueOf(result))) {
			return 1;
		}
		return 0;
	}

	private static int fushi(String betData, String kjData) {
		String[] kj = kjData.split(",");
		if (!kj[0].equals(kj[1])) {
			Set<String> bets = ArrangeUtils.getGroup(betData, 2);
			Set<String> collect = bets.stream().filter(bet -> ArrangeUtils.isSameArray(bet.split(""), kj))
					.collect(Collectors.toSet());
			return collect.size();
		} else {
			return 0;
		}

	}

	/**
	 * 时时彩-前二-组选复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscq2Zxfs(String betData, String kjData) {
		return fushi(betData, kjData.substring(0, 3));
	}

	/**
	 * @describe:时时彩--前二--组选和值
	 * @param betData:1,2,3
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscq2Zxhz(String betData, String kjData) {
		String[] kjs = kjData.substring(0, 3).split(",");
		if (!kjs[0].equals(kjs[1])) {
			String sum = Integer.parseInt(kjs[0]) + Integer.parseInt(kjs[1]) + "";
			if (betData.contains(sum)) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * @describe:前二组选包胆
	 * @param betData:4
	 * @param kjData:5|2|4|3|1
	 * @return:
	 */
	public static int sscq2zxbd(String betData, String kjData) {
		return sscbd(betData, kjData.substring(0, 3), 2);
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-前三一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwq31m(String betData, String kjData) {
		return sscbdw1m(betData, kjData.substring(0, 5));
	}

	private static int sscbdwNm(String betData, String kjData, int n) {
		Set<String> bets = ArrangeUtils.getGroup(betData, n);
		String[] kj = kjData.split(",");
		Set<String> collect = bets.stream().filter(bet -> ArrangeUtils.getSameCount(bet.split(""), kj) >= n)
				.collect(Collectors.toSet());
		return collect.size();
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-前三二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwq32m(String betData, String kjData) {
		return sscbdwNm(betData, kjData.substring(0, 5), 2);
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-后三一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwh31m(String betData, String kjData) {
		return sscbdw1m(betData, kjData.substring(4, kjData.length()));
	}

	/**
	 * @describe:时时彩--不定位--三星不定位-后三二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwh32m(String betData, String kjData) {
		return sscbdwNm(betData, kjData.substring(4, kjData.length()), 2);
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-前四一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwq41m(String betData, String kjData) {
		return sscbdw1m(betData, kjData.substring(0, 7));
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-前四二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwq42m(String betData, String kjData) {
		return sscbdwNm(betData, kjData.substring(0, 7), 2);
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-后四一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwh41m(String betData, String kjData) {
		return sscbdw1m(betData, kjData.substring(2, kjData.length()));
	}

	/**
	 * @describe:时时彩--不定位--四星不定位-后四二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdwh42m(String betData, String kjData) {
		return sscbdwNm(betData, kjData.substring(2, kjData.length()), 2);
	}

	/**
	 * @describe:时时彩--不定位--五星不定位-五星一码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdw51m(String betData, String kjData) {
		return sscbdw1m(betData, kjData);
	}

	/**
	 * @describe:时时彩--不定位--五星不定位-五星二码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdw52m(String betData, String kjData) {
		return sscbdwNm(betData, kjData, 2);
	}

	/**
	 * @describe:时时彩--不定位--五星不定位-五星三码
	 * @param betData:6,4
	 * @param kjData:1|2|4|4|5
	 * @return:
	 */
	public static int sscbdw53m(String betData, String kjData) {
		return sscbdwNm(betData, kjData, 3);
	}

	private static List<String> getDsdx(String data) {
		List<String> result = new ArrayList<>();
		int target = Integer.parseInt(data);
		if (target >= 5 && target <= 9) {
			result.add("大");
		}
		if (target >= 0 && target <= 4) {
			result.add("小");
		}
		if (target % 2 == 1) {
			result.add("单");
		}
		if (target % 2 == 0) {
			result.add("双");
		}
		return result;
	}

	private static int getdxdsResult(String betData, String kjData, int type) {
		String[] bets = betData.split("\\|");
		String[] first = bets[0].split(",");
		String[] second = bets[1].split(",");
		String[] kjs = kjData.split(",");
		List<String> firstKj = getDsdx(kjs[0]);
		List<String> secondKj = getDsdx(kjs[1]);
		int firstCount = 0;
		for (String str : first) {
			if (firstKj.contains(str)) {
				firstCount++;
			}
		}
		int secondCount = 0;
		for (String str : second) {
			if (secondKj.contains(str)) {
				secondCount++;
			}
		}
		if (type == 3) {
			String[] third = bets[2].split(",");
			List<String> thirdKj = getDsdx(kjs[2]);
			int thirdCount = 0;
			for (String str : third) {
				if (thirdKj.contains(str)) {
					thirdCount++;
				}
			}
			return firstCount * secondCount * thirdCount;
		}

		return firstCount * secondCount;
	}

	/**
	 * @describe:时时彩--大小单双--前二
	 * @param betData:大,小,单,双|大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscq2dxds(String betData, String kjData) {
		return getdxdsResult(betData, kjData.substring(0, 3), 2);
	}

	/**
	 * @describe:时时彩--大小单双--后二
	 * @param betData:大,小,单,双|大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssch2dxds(String betData, String kjData) {
		return getdxdsResult(betData, kjData.substring(6, kjData.length()), 2);
	}

	/**
	 * @describe:时时彩--大小单双--前三
	 * @param betData:大,小,单,双|大,小,单,双|大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int sscq3dxds(String betData, String kjData) {
		return getdxdsResult(betData, kjData.substring(0, 5), 3);
	}

	/**
	 * @describe:时时彩--大小单双--后三
	 * @param betData:大,小,单,双|大,小,单,双|大,小,单,双
	 * @return:kjData:2|2|0|2|2
	 */
	public static int ssch3dxds(String betData, String kjData) {
		return getdxdsResult(betData, kjData.substring(4, kjData.length()), 3);
	}

	private static int rxnzxfs(String betData, String kjData, int num) {
		String[] kjs = kjData.split(",");
		List<Integer> indexList = new ArrayList<>();
		for (int i = -1; i <= betData.lastIndexOf("|"); ++i) {
			i = betData.indexOf("|", i);
			indexList.add(i);
		}
		String[] bet = { betData.substring(0, indexList.get(0)),
				betData.substring(indexList.get(0) + 1, indexList.get(1)),
				betData.substring(indexList.get(1) + 1, indexList.get(2)),
				betData.substring(indexList.get(2) + 1, indexList.get(3)),
				betData.substring(indexList.get(3) + 1, betData.length()) };

		List<Integer> resultList = new ArrayList<>();
		for (int i = 0; i < kjs.length; i++) {
			if (!StringUtil.isBlank(bet[i]) && bet[i].contains(kjs[i])) {
				resultList.add(1);
			}
		}
		return ArrangeUtils.C(resultList.size(), num);
	}

	/**
	 * 时时彩--任选二--直选复式
	 * 
	 * @param betData
	 *            1,2|1,2|||
	 * @param kjData
	 *            2,3,4,5,6
	 * @return
	 */
	public static int sscrx2zxfs(String betData, String kjData) {
		return rxnzxfs(betData, kjData, 2);
	}

	/**
	 * 时时彩--任选二--直选和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscrx2zxhz(String betData, String kjData) {
		return rxnzxhz(betData, kjData, 2);
	}

	/**
	 * 时时彩--任选二--组选复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscrx2Zxfs(String betData, String kjData) {
		int count = 0;
		String[] kjs = kjData.split(",");
		String[] bet = betData.split("\\|");
		List<List<String>> betPlace = getCombination(bet[0], 2);
		List<List<Integer>> indexsList = getIndexs(betPlace);
		List<List<String>> kjNums = getKjNums(kjs, indexsList);
		List<List<String>> betNums = getCombination(bet[1], 2);
		for (List<String> kjList : kjNums) {
			if (!kjList.get(0).equals(kjList.get(1))) {
				for (List<String> betList : betNums) {
					if (betList.containsAll(kjList) && kjList.containsAll(betList)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 时时彩--任选二--组选和值 (排除对子)
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscrx2Zxhz(String betData, String kjData) {
		return rxnznhz(betData, kjData, 2, 0);
	}

	/**
	 * 时时彩--任选三--直选复式
	 * 
	 * @param betData
	 *            1,2|1,2|||
	 * @param kjData
	 *            2,3,4,5,6
	 * @return
	 */
	public static int sscrx3zxfs(String betData, String kjData) {
		return rxnzxfs(betData, kjData, 3);
	}

	/**
	 * 时时彩--任选三--直选和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscrx3zxhz(String betData, String kjData) {
		return rxnzxhz(betData, kjData, 3);
	}

	/**
	 * 时时彩--任选三--组三复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscrx3z3fs(String betData, String kjData) {
		return rx3znfs(betData, kjData, 2);
	}

	/**
	 * 时时彩--任选三--组六复式
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscrx3z6fs(String betData, String kjData) {
		return rx3znfs(betData, kjData, 3);
	}

	/**
	 * 时时彩--任选三--组三和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscrx3z3hz(String betData, String kjData) {
		return rxnznhz(betData, kjData, 3, 1);
	}

	/**
	 * 时时彩--任选三--组六和值
	 * 
	 * @param betData
	 * @param kjData
	 * @return
	 */
	public static int sscrx3z6hz(String betData, String kjData) {
		return rxnznhz(betData, kjData, 3, 2);
	}

	/**
	 * 时时彩--任选四--直选复式
	 * 
	 * @param betData
	 *            1,2|1,2|||
	 * @param kjData
	 *            2,3,4,5,6
	 * @return
	 */
	public static int sscrx4zxfs(String betData, String kjData) {
		return rxnzxfs(betData, kjData, 4);
	}

	/**
	 * 时时彩--任选四--组选24 abcd
	 * 
	 * @param betData
	 *            1,2|1,2|||
	 * @param kjData
	 *            2,3,4,5,6
	 * @return
	 */
	public static int sscrx4zx24(String betData, String kjData) {
		int count = 0;
		String[] kjs = kjData.split(",");
		String[] bet = betData.split("\\|");
		List<List<String>> betPlace = getCombination(bet[0], 4);
		List<List<Integer>> indexsList = getIndexs(betPlace);
		List<List<String>> kjNums = getKjNums(kjs, indexsList);
		List<List<String>> betNums = getCombination(bet[1], 4);
		for (List<String> kjList : kjNums) {
			if (new HashSet<>(kjList).size() == 4) {
				for (List<String> betList : betNums) {
					if (kjList.containsAll(betList) && betList.containsAll(kjList)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 时时彩--任选四--组选12 aabc
	 * 
	 * @param betData
	 *            1,2|1,2|||
	 * @param kjData
	 *            2,3,4,5,6
	 * @return
	 */
	public static int sscrx4zx12(String betData, String kjData) {
		int count = 0;
		String[] kjs = kjData.split(",");
		String[] bet = betData.split("\\|");
		List<List<String>> betPlace = getCombination(bet[0], 4);
		List<List<Integer>> indexsList = getIndexs(betPlace);
		List<List<String>> kjNums = getKjNums(kjs, indexsList);
		String[] doubleNums = bet[1].split(",");
		List<List<String>> singleNums = getCombination(bet[2], 2);

		for (List<String> kjList : kjNums) {
			HashSet<String> kjSet = new HashSet<>(kjList);
			if (kjSet.size() == 3) {
				List<String> sameList = getSameNum(kjList, kjSet);
				if (sameList != null && sameList.size() == 1) {
					String sameNum = sameList.get(0);
					for (int i = 0; i < kjList.size(); i++) {
						kjList.remove(sameNum);
					}
					for (List<String> list : singleNums) {
						if (list.containsAll(kjList) && Arrays.asList(doubleNums).contains(sameNum)) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	/**
	 * 时时彩--任选四--组选6 aabb
	 * 
	 * @param betData
	 *            1,2|1,2|||
	 * @param kjData
	 *            2,3,4,5,6
	 * @return
	 */
	public static int sscrx4zx6(String betData, String kjData) {
		int result = 0;
		if (!StringUtil.isBlank(betData) && !StringUtil.isBlank(kjData)) {
			List<String> places = new ArrayList<String>();
			places.add("万位");
			places.add("千位");
			places.add("百位");
			places.add("十位");
			places.add("个位");
			String[] betArr = betData.split("\\|");
			// 组合取出所有ab
			List<List<String>> group = getCombination(betArr[1], 2);
			// 得到所有位数组合情况
			List<List<String>> allPlaceList = getCombination(betArr[0], 4);

			// 根据位数取出开奖该位上的数字
			String[] kjArr = kjData.split(",");
			int[] indexes = new int[4];
			for (List<String> list : allPlaceList) {
				List<String> target = new ArrayList<String>();
				for (int i = 0; i < list.size(); i++) {
					if (places.contains(list.get(i))) {
						indexes[i] = places.indexOf(list.get(i));
					}
				}
				for (int index : indexes) {
					target.add(kjArr[index]);
				}
				// 判断取出来的数字是不是aabb类型
				boolean flag = false;
				List<String> newList = new ArrayList<String>(new HashSet<String>(target));
				if (newList.size() == 2) {
					for (String string : newList) {
						target.remove(string);
					}
					flag = newList.containsAll(target) && target.containsAll(newList);
				}
				// group中有没有该组合 若有count++
				if (flag && group.contains(newList)) {
					result++;
				}
			}
		}
		return result;
	}

	/**
	 * 时时彩--任选四--组选4 aaab
	 * 
	 * @param betData
	 *            1,2|1,2|||
	 * @param kjData
	 *            2,3,4,5,6
	 * @return
	 */
	public static int sscrx4zx4(String betData, String kjData) {
		int count = 0;
		String[] kjs = kjData.split(",");
		String[] bet = betData.split("\\|");
		List<List<String>> betPlace = getCombination(bet[0], 4);
		List<List<Integer>> indexsList = getIndexs(betPlace);
		List<List<String>> kjNums = getKjNums(kjs, indexsList);
		String[] threeNums = bet[1].split(",");
		String[] singleNums = bet[2].split(",");

		for (List<String> kjList : kjNums) {
			HashSet<String> kjSet = new HashSet<>(kjList);
			if (kjSet.size() == 2) {
				List<String> sameList = getSameNum(kjList, kjSet);
				if (sameList != null && sameList.size() == 1) {
					kjList.removeAll(sameList);
					if (kjList.size() == 1) {
						if (Arrays.asList(threeNums).contains(sameList.get(0))
								&& Arrays.asList(singleNums).contains(kjList.get(0))) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	/**
	 * 时时彩--龙虎
	 * 
	 * @param betData
	 *            万千:万|万千:千
	 * @param kjData
	 * @return
	 */
	public static int ssclh(String betData, String kjData) {
		int count = 0;
		List<Character> places = new ArrayList<Character>();
		places.add('万');
		places.add('千');
		places.add('百');
		places.add('十');
		places.add('个');
		String[] kjs = kjData.split(",");
		String[] bets = betData.split("\\|");
		for (String bet : bets) {
			String[] betInfo = bet.split(":");
			String bigSmall = betInfo[1];
			char[] position = betInfo[0].toCharArray();

			List<Character> kjPosition = new ArrayList<>();
			List<Integer> kjNum = new ArrayList<>();
			for (char c : position) {
				int index = places.indexOf(c);
				if (index != -1) {
					kjPosition.add(c);
					kjNum.add(Integer.parseInt(kjs[index]));
				}
			}

			String result = null;
			if (kjNum.get(0) > kjNum.get(1)) {
				result = kjPosition.get(0).toString();
			} else if (kjNum.get(0) < kjNum.get(1)) {
				result = kjPosition.get(1).toString();
			}

			if (!StringUtil.isBlank(result) && result.equals(bigSmall)) {
				count++;
			}
		}
		return count;
	}

	private static List<String> getSameNum(List<String> list, HashSet<String> set) {
		List<String> result = new ArrayList<>();
		for (String str : set) {
			if (list.indexOf(str) != -1 && list.lastIndexOf(str) != -1 && list.indexOf(str) != list.lastIndexOf(str)) {
				result.add(str);
			}
		}
		return result;
	}

	private static List<List<Integer>> getIndexs(List<List<String>> betPlace) {
		List<String> places = new ArrayList<String>();
		places.add("万位");
		places.add("千位");
		places.add("百位");
		places.add("十位");
		places.add("个位");
		List<List<Integer>> indexsList = new ArrayList<>();
		for (List<String> list : betPlace) {
			List<Integer> indexs = new ArrayList<>();
			for (String postion : list) {
				int index = places.indexOf(postion);
				if (index != -1) {
					indexs.add(index);
				}
			}
			indexsList.add(indexs);
		}
		return indexsList;
	}

	private static int rxnzxhz(String betData, String kjData, int num) {
		int count = 0;
		String[] kjs = kjData.split(",");
		String[] bet = betData.split("\\|");
		List<List<String>> betPlace = getCombination(bet[0], num);
		List<List<Integer>> indexsList = getIndexs(betPlace);

		List<Integer> results = new ArrayList<>();
		for (List<Integer> list : indexsList) {
			int result = 0;
			for (Integer index : list) {
				result += Integer.parseInt(kjs[index]);
			}
			results.add(result);
		}

		String[] betNum = bet[1].split(",");
		for (Integer i : results) {
			for (String str : betNum) {
				if (i == Integer.parseInt(str)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 根据索引得到开奖号码
	 * 
	 * @param kjs
	 * @param indexsList
	 * @return
	 */
	private static List<List<String>> getKjNums(String[] kjs, List<List<Integer>> indexsList) {
		List<List<String>> kjNums = new ArrayList<>();
		for (List<Integer> list : indexsList) {
			List<String> kjNum = new ArrayList<>();
			for (Integer index : list) {
				String kj = kjs[index];
				kjNum.add(kj);
			}
			kjNums.add(kjNum);
		}
		return kjNums;
	}

	/**
	 * 任选3组N复式
	 * 
	 * @param betData
	 * @param kjData
	 * @param num
	 * @return
	 */
	private static int rx3znfs(String betData, String kjData, int num) {
		int count = 0;
		String[] kjs = kjData.split(",");
		String[] bet = betData.split("\\|");
		List<List<String>> betPlace = getCombination(bet[0], 3);
		List<List<Integer>> indexsList = getIndexs(betPlace);
		List<List<String>> kjNums = getKjNums(kjs, indexsList);
		List<List<String>> betNums = getCombination(bet[1], num);
		for (List<String> kjList : kjNums) {
			if (new HashSet<>(kjList).size() == num) {
				for (List<String> betList : betNums) {
					if (kjList.containsAll(betList) && betList.containsAll(kjList)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 幸运农场选五
	 * 
	 * @param betsStr
	 *            投注串 格式：1,2,6,7,8
	 * @return 注数
	 */
	public static int openPrizeLuckyFarm5(String betsStr, String target) {
		int num = 0;
		if (betsStr != null) {
			String betsArr[] = betsStr.split("[|]");
			String targetArr[] = target.split(",");
			StringBuilder hit = new StringBuilder();// 选中的号码hit.append(",").append(t);
			if (betsArr.length == 1) {
				for (String t : targetArr) {
					if (betsStr.contains(t)) {
						hit.append(",").append(t);
					}
				}
				if(hit.length() > 0) {
					num = getCount4Zx(hit.substring(1), 5);
				}
			} else if (betsArr.length == 2) {
				int guts = 0;// 胆是否选中
				for (String t : targetArr) {
					if (betsArr[0].contains(t)) {
						guts++;
					}
					if (betsArr[1].contains(t)) {
						hit.append(",").append(t);
					}
				}
				if(guts == betsArr[0].split(",").length) {
					if (hit.length() > 0 && guts > 0 && guts == StringUtils.split(betsArr[0], ",").length) {
						num = getCount4Zx(hit.substring(1), 5 - guts);
					}
				}
			}
		}
		return num;
	}

	/**
	 * 幸运农场选四
	 * 
	 * @param betsStr
	 *            投注串 格式：1,2,6,7
	 * @return 注数
	 */
	public static int openPrizeLuckyFarm4(String betsStr, String target) {
		int num = 0;
		if (betsStr != null) {
			String betsArr[] = betsStr.split("[|]");
			String targetArr[] = target.split(",");
			StringBuilder hit = new StringBuilder();// 选中的号码
			if (betsArr.length == 1) {
				for (String t : targetArr) {
					if (betsStr.contains(t)) {
						hit.append(",").append(t);
					}
				}
				if(hit.length() > 0) {
					num = getCount4Zx(hit.substring(1), 4);
				}
			} else if (betsArr.length == 2) {
				int guts = 0;// 胆是否选中
				for (String t : targetArr) {
					if (betsArr[0].contains(t)) {
						guts++;
					}
					if (betsArr[1].contains(t)) {
						hit.append(",").append(t);
					}
				}
				if(guts == betsArr[0].split(",").length) {
					if (hit.length() > 0 && guts > 0 && guts == StringUtils.split(betsArr[0], ",").length) {
						num = getCount4Zx(hit.substring(1), 4 - guts);
					}
				}
			}
		}
		return num;
	}

	/**
	 * 幸运农场选三 前直
	 * 
	 * @param betsStr
	 *            投注串 格式：1|2,6
	 * @return 中奖注数
	 */
	public static int openPrizeLuckyFarm3Line(String betsStr, String target) {
		int num = 0;
		if (betsStr != null && target != null) {
			String betsArr[] = betsStr.split("[|]");
			String targetArr[] = target.split(",");
			if (betsArr.length == 3 && targetArr.length > 3) {
				if (betsArr[0].contains(targetArr[0]) && betsArr[1].contains(targetArr[1])
						&& betsArr[2].contains(targetArr[2])) {
					num++;
				}
			}
		}
		return num;
	}

	/**
	 * 幸运农场选三 前组 前组胆拖
	 * 
	 * @param betsStr
	 *            投注串 格式：1,2,6
	 * @return 中奖注数
	 */
	public static int openPrizeLuckyFarm3Group(String betsStr, String target) {
		int num = 0;
		if (betsStr != null && target != null) {
			String betsArr[] = betsStr.split("[|]");
			String targetArr[] = target.split(",");
			if (targetArr.length > 3) {
				if (betsArr.length == 1) {
					if (betsStr.contains(targetArr[0]) && betsStr.contains(targetArr[1])
							&& betsStr.contains(targetArr[2])) {
						num = 1;
					}
				} else if (betsArr.length == 2) {
					int guts = 0;
					String betsArr1[] = StringUtils.split(betsArr[0], ",");
					for (int i = 0; i < 3; i++) {
						if (betsArr[0].contains(targetArr[i])) {
							guts++;
						}
					}
					if (guts == betsArr1.length) {
						for (int i = 0; i < 3; i++) {
							if (betsArr[1].contains(targetArr[i])) {
								num = 1;
							}
						}
					}
				}
			}
		}
		return num;
	}
	
	/**
	 * 幸运农场选三 任选三 任选胆拖
	 * 
	 * @param betsStr
	 *            投注串 格式：1,2,6
	 * @return 中奖注数
	 */
	public static int openPrizeLuckyFarm3(String betsStr, String target) {
		int num = 0;
		if (betsStr != null && target != null) {
			String betsArr[] = betsStr.split("[|]");
			String targetArr[] = target.split(",");
			StringBuilder hit = new StringBuilder();// 选中的号码
			if (betsArr.length == 1) {
				for (String t : targetArr) {
					if (betsStr.contains(t)) {
						hit.append(",").append(t);
					}
				}
				if(hit.length() > 0) {
					num = getCount4Zx(hit.substring(1), 3);
				}
			} else if (betsArr.length == 2) {
				int guts = 0;// 胆是否选中
				for (String t : targetArr) {
					if (betsArr[0].contains(t)) {
						guts++;
					}
					if (betsArr[1].contains(t)) {
						hit.append(",").append(t);
					}
				}
				if(guts == betsArr[0].split(",").length) {
					if (guts == 1 && betsArr[1].split(",").length > 1
							&& hit.length() > 0) {
						num = getCount4Zx(hit.substring(1), 2);
					} else if (guts == 2) {
						for (String t : targetArr) {
							if (betsArr[1].contains(t)) {
								num++;
							}
						}
					}
				}
			}
		}
		return num;
	}

	/**
	 * 幸运农场选二 连直 胆拖连组
	 * 
	 * @param betsStr
	 *            投注串 格式：1|2,6
	 * @return 中奖注数
	 */
	public static int openPrizeLuckyFarm2Line(String betsStr, String target) {
		int num = 0;
		if (betsStr != null && target != null) {
			String betsArr[] = betsStr.split("[|]");
			if (betsArr.length == 2) {
				String targetArr[] = target.split(",");
				String betsArr1[] = betsArr[0].split(",");
				for (int i = 0; i < targetArr.length; i++) {
					for (int j = 0; j < betsArr1.length; j++) {
						if (betsArr1[j].equals(targetArr[i])) {
							if ((i < targetArr.length - 1 && betsArr[1].contains(targetArr[i + 1]))) {
								num++;
							}
						}
					}
				}
			}
		}
		return num;
	}

	/**
	 * 幸运农场选二 连组
	 * 
	 * @param betsStr
	 *            投注串 格式：1,2,6
	 * @return 中奖注数
	 */
	public static int openPrizeLuckyFarm2Group(String betsStr, String target) {
		int num = 0;
		if (betsStr != null && target != null) {
			String targetArr[] = target.split(",");
			for (int i = 0; i < targetArr.length; i++) {
				if (betsStr.contains(targetArr[i])) {
					if (i < targetArr.length - 1 && betsStr.contains(targetArr[i + 1])) {
						num++;
					}
				}
			}
		}
		return num;
	}

	/**
	 * 幸运农场选二 任选二 任选胆拖
	 * 
	 * @param betsStr
	 *            投注串 格式：1,2,6
	 * @return 中奖注数
	 */
	public static int openPrizeLuckyFarm2(String betsStr, String target) {
		int num = 0;
		if (betsStr != null && target != null) {
			String betsArr[] = betsStr.split("[|]");
			String targetArr[] = target.split(",");
			StringBuilder hit = new StringBuilder();// 选中的号码
			if (betsArr.length == 1) {
				for (String t : targetArr) {
					if (betsStr.contains(t)) {
						hit.append(",").append(t);
					}
				}
				if(hit.length() > 0) {
					num = getCount4Zx(hit.substring(1), 2);
				}
			} else if (betsArr.length == 2) {
				boolean guts = false;// 胆是否选中
				for (String t : targetArr) {
					if (betsArr[0].contains(t)) {
						guts = true;
						break;
					}
				}
				if (guts) {
					for (String t : targetArr) {
						if (betsArr[1].contains(t)) {
							num++;
						}
					}
				}
			}
		}
		return num;
	}

	/**
	 * 幸运农场-选一 红投
	 * 
	 * @param bet
	 *            投注串 格式：19
	 * @param target
	 *            开奖号码 格式：06,19,15,03,08,13
	 * @return 中奖注数
	 */
	public static int openPrizeLuckyFarm1Red(String betsStr, String target) {
		int num = 0;
		if (target != null && betsStr != null) {
			String t = target.split(",")[0];
			if (("19".equals(t) || "20".equals(t))) {
				String betsArr[] = betsStr.split(",");
				for (String bet : betsArr) {
					if (bet.contains("19") || bet.contains("20")) {
						num++;
					}
				}
			}
		}
		return num;
	}

	/**
	 * 幸运农场-选一 数投
	 * 
	 * @param bet
	 *            投注串 格式：01,02,03,04
	 * @param target
	 *            开奖号码 格式：06,19,15,03,08,13
	 * @return 中奖注数
	 */
	public static int openPrizeLuckyFarm1(String bet, String target) {
		if (target != null && bet != null) {
			return bet.contains(target.split(",")[0]) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 计算组选的算法 适用于注数为组合(C)的形式 时时彩五星组选
	 * 
	 * @param param
	 *            投注串格式 1,2,3 按","分隔
	 * @param minNum
	 *            需要选择数字的最少个数
	 * @return
	 */
	public static int getCount4Zx(String param, int minNum) {
		if (param == null || "".equals(param)) {
			return 0;
		}
		int length = param.split(",").length;
		if (minNum > length)
			return 0;
		if (minNum > length / 2) {
			minNum = length - minNum;
		}
		return factorial(minNum, length) / factorial(minNum, minNum);
	}

	/**
	 * 替换阶乘的另一种方式(从n开始递减相乘，乘m个数),A(m,n)
	 */
	private static int factorial(int m, int n) {
		int sum = 1;

		while (m > 0 && n > 0) {
			sum = sum * n--;
			m--;
		}
		return sum;
	}

	/**
	 * 任选N 组N和值 任选2组选和值 num = 2 type = 0 getCombination传2 new
	 * HashSet<>(list).size()== 2 任选3组三和值 num = 3 type = 1 getCombination传3 new
	 * HashSet<>(list).size() ==2 任选3组六和值 num = 3 type = 2 getCombination传3 new
	 * HashSet<>(list).size() == 3
	 * 
	 * @param betData
	 * @param kjData
	 * @param num
	 * @param type
	 * @return
	 */
	private static int rxnznhz(String betData, String kjData, int num, int type) {
		int count = 0;
		String[] kjs = kjData.split(",");
		String[] bet = betData.split("\\|");
		List<List<String>> betPlace = getCombination(bet[0], num);
		List<List<Integer>> indexsList = getIndexs(betPlace);

		List<Integer> results = new ArrayList<>();
		for (List<Integer> list : indexsList) {
			List<String> kjList = new ArrayList<>();
			for (Integer index : list) {
				kjList.add(kjs[index]);
			}

			if (type == 1) {
				num = 2;
			}
			if (new HashSet<>(kjList).size() == num) {
				int result = 0;
				for (Integer index : list) {
					result += Integer.parseInt(kjs[index]);
				}
				results.add(result);
			}
		}

		String[] betNum = bet[1].split(",");
		for (Integer i : results) {
			for (String str : betNum) {
				if (i == Integer.parseInt(str)) {
					count++;
				}
			}
		}
		return count;
	}

	private static int qxcNd(String betData, String kjData, int type) {
		if (!StringUtil.isBlank(kjData) && !StringUtil.isBlank(betData)) {
			kjData = kjData.substring(0, 7);
			String[] kjs = kjData.split(",");
			String[] bets = betData.split("\\|");
			int count = 0;
			for (int i = 0; i < bets.length; i++) {
				if (!StringUtil.isBlank(bets[i])) {
					if (bets[i].contains(kjs[i])) {
						count++;
					}
				}
			}
			if (type == count) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	/**
	 * 七星彩 二定玩法
	 * 
	 * @describe:
	 * @param betData
	 * @param kjData
	 * @return:
	 */
	public static int qxc2d(String betData, String kjData) {
		return qxcNd(betData, kjData, 2);
	}

	/**
	 * 七星彩 三定玩法
	 * 
	 * @describe:
	 * @param betData
	 * @param kjData
	 * @return:
	 */
	public static int qxc3d(String betData, String kjData) {
		return qxcNd(betData, kjData, 3);
	}

	/**
	 * 七星彩 四定玩法
	 * 
	 * @describe:
	 * @param betData
	 * @param kjData
	 * @return:
	 */
	public static int qxc4d(String betData, String kjData) {
		return qxcNd(betData, kjData, 4);
	}

	/**
	 * 七星彩 一定玩法
	 * 
	 * @describe:
	 * @param betData
	 * @param kjData
	 * @return:
	 */
	public static int qxc1d(String betData, String kjData) {
		return qxcNd(betData, kjData, 1);
	}

	/**
	 * 七星彩二字现
	 * 
	 * @describe:
	 * @param bet
	 * @param kj
	 * @return:
	 */
	public static int qxc2zx(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			kj = kj.substring(0, 7);
			List<List<String>> betList = getCombination(bet, 2);
			for (List<String> list : betList) {
				if (getCount4Same(kj.split(","), list.toArray()) == 2) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 七星彩三字现
	 * 
	 * @describe:
	 * @param bet
	 * @param kj
	 * @return:
	 */
	public static int qxc3zx(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			kj = kj.substring(0, 7);
			String[] kjArr = kj.split(",");
			Object[] newKjArr = filterArray(kjArr);
			List<List<String>> betList = getCombination(bet, 3);
			for (List<String> list : betList) {
				if (getCount4Same(list.toArray(), newKjArr) == 3) {
					count++;
				}
			}
		}
		return count;
	}

	/** 六合彩开奖计算 */
	/**
	 * 六合彩特码B/A-选码/其他
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcTmAB(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.split("\\+");
			if (kjs.length == 2) {
				String tm = kjs[1];
				if (StringUtils.isNumeric(bet)) {
					// 特码单码：假设投注号码为开奖号码之特别号，视为中奖，其余情形视为不中奖。
					if (tm.equals(bet)) {
						return 1;
					}
				} else {
					List<String> list = getResult4TmAB(tm);
					if (list.contains(bet)) {
						return 1;
					} else if (list.contains("和")) {
						return -1;
					}
				}
			}
		}
		return 0;
	}
	
	private static List<String> getResult4TmAB(String tm) {
		List<String> result = new ArrayList<>();
		int num = Integer.parseInt(tm);
		if (num == 49) {
			result.add("和");
		} else {
			// 特码大小：开出之特码大于或等于25为特码大， 小于或等于24为特码小，开出49为和局。
			if (num >= 25 && num <= 48) {
				result.add("特大");
				if (num % 2 == 0) {
					result.add("特大双");
				} else if (num % 2 == 1) {
					result.add("特大单");
				}
			} else if (num >= 1 && num <= 24) {
				result.add("特小");
				if (num % 2 == 0) {
					result.add("特小双");
				} else if (num % 2 == 1) {
					result.add("特小单");
				}
			}
			// 特码单双：特码为双数叫特双，如8、16；特码为单数叫特单，如21、35，开出49为和局。
			if (num % 2 == 0) {
				result.add("特双");
			} else if (num % 2 == 1) {
				result.add("特单");
			}

			int sum = 0;
			if (num >= 1 && num <= 9) {
				sum = num;
			} else {
				sum = num / 10 + num % 10;
			}
			// 特码和数大小：以特码个位和十位数字之和来判断胜负，和数大于或等于7为大，小于或等于6为小，开出49号为和局。
			if (sum >= 7 && sum <= 12) {
				result.add("特合大");
			} else if (sum >= 1 && sum <= 6) {
				result.add("特合小");
			}
			// 特码和数单双：以特码个位和十位数字之和来判断单双，如01，12，32为和单，02，11，33为和双，开出49号为和局。
			if (sum % 2 == 0) {
				result.add("特合双");
			} else if (sum % 2 == 1) {
				result.add("特合单");
			}
			// 特码尾数大小：以特别号尾数若0尾~4尾为小、5尾~9尾为大；如01、32、44为特尾小；如05、18、19为特尾大，开出49号为和局
			int end = num % 10;
			if (end >= 5 && end <= 9) {
				result.add("特尾大");
			} else if (end >= 0 && end <= 4) {
				result.add("特尾小");
			}
		}

		// 根据tm得到生肖
		String sx = ZodiacRuleUtil.numToZodiac(Integer.parseInt(tm));
		String[] tx = { "兔", "马", "猴", "猪", "牛", "龙" };
		String[] dx = { "鼠", "虎", "蛇", "羊", "鸡", "狗" };
		String[] jx = { "羊", "马", "牛", "猪", "狗", "鸡" };
		String[] yx = { "猴", "蛇", "龙", "兔", "虎", "鼠" };
		String[] qx = { "鼠", "牛", "虎", "兔", "龙", "蛇" };
		String[] hx = { "马", "羊", "猴", "鸡", "狗", "猪" };

		if (Arrays.asList(tx).contains(sx)) {
			result.add("特天肖");
		}
		if (Arrays.asList(dx).contains(sx)) {
			result.add("特地肖");
		}
		if (Arrays.asList(jx).contains(sx)) {
			result.add("特家肖");
		}
		if (Arrays.asList(yx).contains(sx)) {
			result.add("特野肖");
		}
		if (Arrays.asList(qx).contains(sx)) {
			result.add("特前肖");
		}
		if (Arrays.asList(hx).contains(sx)) {
			result.add("特后肖");
		}

		return result;
	}

	/**
	 * 六合彩色波
	 * 
	 * @return
	 */
	public static int lhcTmSb(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.split("\\+");
			if (kjs.length == 2) {
				int tm = Integer.parseInt(kjs[1]);
				List<String> result = new ArrayList<>();
				// 特码波色
				Integer[] red = { 1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46 };
				Integer[] blue = { 3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48 };
				Integer[] green = { 5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49 };
				if (Arrays.asList(red).contains(tm)) {
					result.add("红波");
				} else if (Arrays.asList(blue).contains(tm)) {
					result.add("蓝波");
				} else if (Arrays.asList(green).contains(tm)) {
					result.add("绿波");
				}
				if (result.contains(bet)) {
					return 1;
				}
			}
		}
		return 0;
	}

	private static List<String> getResult4TmBb(int num) {
		List<String> result = new ArrayList<>();
		if (num == 49) {
			result.add("和");
		} else {
			Integer[] red = { 1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46 };
			Integer[] blue = { 3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48 };
			Integer[] green = { 5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49 };
			String bs = "";
			if (Arrays.asList(red).contains(num)) {
				bs = "红";
			} else if (Arrays.asList(blue).contains(num)) {
				bs = "蓝";
			} else if (Arrays.asList(green).contains(num)) {
				bs = "绿";
			}

			if (num >= 25 && num <= 48) {
				result.add(bs + "大");
				if (num % 2 == 0) {
					result.add(bs + "大双");
				} else if (num % 2 == 1) {
					result.add(bs + "大单");
				}
			} else if (num >= 1 && num <= 24) {
				result.add(bs + "小");
				if (num % 2 == 0) {
					result.add(bs + "小双");
				} else if (num % 2 == 1) {
					result.add(bs + "小单");
				}
			}

			if (num % 2 == 0) {
				result.add(bs + "双");
			} else if (num % 2 == 1) {
				result.add(bs + "单");
			}
		}

		return result;
	}

	/**
	 * 六合彩半波/半半波
	 * 
	 * @return
	 */
	public static int lhcTmBb(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.split("\\+");
			if (kjs.length == 2) {
				int tm = Integer.parseInt(kjs[1]);
				List<String> list = getResult4TmBb(tm);
				if (list.contains(bet)) {
					return 1;
				} else if (list.contains("和")) {
					return -1;
				}
			}
		}
		return 0;
	}

	/**
	 * 六合彩头尾数
	 * 
	 * @return
	 */
	public static int lhcTmTws(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.split("\\+");
			if (kjs.length == 2) {
				int tm = Integer.parseInt(kjs[1]);
				List<String> list = lhcTws(tm);
				if (list.contains(bet)) {
					return 1;
				}
			}
		}
		return 0;
	}
	
	private static List<String> lhcTws(int num) {
		List<String> result = new ArrayList<>();
		int head = num / 10;
		int tail = num % 10;
		result.add(head + "头");
		result.add(tail + "尾");
		return result;
	}

	/**
	 * 六合彩正码-选码/其他
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcZm(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.split("\\+");
			if (kjs.length == 2) {
				String[] zms = kjs[0].split(",");
				if (StringUtils.isNumeric(bet)) {
					// 正码单码：假设投注号码为开奖号码之特别号，视为中奖，其余情形视为不中奖。
					if (Arrays.asList(zms).contains(bet)) {
						return 1;
					}
				} else {
					int sum = 0;
					for (String str : zms) {
						sum += Integer.parseInt(str);
					}

					sum += Integer.parseInt(kjs[1]);
					List<String> result = new ArrayList<>();
					if (sum >= 175) {
						result.add("总大");
					} else {
						result.add("总小");
					}

					if (sum % 2 == 0) {
						result.add("总双");
					} else if (sum % 2 == 1) {
						result.add("总单");
					}

					if (result.contains(bet)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private static int lhcZnt(String bet, String betNum, String index) {
		if (StringUtils.isNumeric(bet)) {
			if (betNum.equals(bet)) {
				return 1;
			}
		} else {
			List<String> result = new ArrayList<>();
			int num = Integer.parseInt(betNum);
			if (num == 49) {
				result.add("和");
			} else {
				if (num >= 25 && num <= 48) {
					result.add(index + "大");
				} else if (num >= 1 && num <= 24) {
					result.add(index + "小");
				}
				if (num % 2 == 0) {
					result.add(index + "双");
				} else if (num % 2 == 1) {
					result.add(index + "单");
				}

				int sum = 0;
				if (num >= 1 && num <= 9) {
					sum = num;
				} else {
					sum = num / 10 + num % 10;
				}
				if (sum % 2 == 0) {
					result.add(index + "合双");
				} else if (sum % 2 == 1) {
					result.add(index + "合单");
				}
			}
			// 特码波色
			Integer[] red = { 1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46 };
			Integer[] blue = { 3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48 };
			Integer[] green = { 5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49 };
			if (Arrays.asList(red).contains(num)) {
				result.add(index + "红");
			} else if (Arrays.asList(blue).contains(num)) {
				result.add(index + "蓝");
			} else if (Arrays.asList(green).contains(num)) {
				result.add(index + "绿");
			}

			if (result.contains(bet)) {
				return 1;
			} else if (result.contains("和")) {
				return -1;
			}
		}
		return 0;
	}

	public static int lhcZ1t(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZnt(bet, kj.split("\\+")[0].split(",")[0], "正一");
		}
		return 0;
	}

	public static int lhcZ2t(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZnt(bet, kj.split("\\+")[0].split(",")[1], "正二");
		}
		return 0;
	}

	public static int lhcZ3t(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZnt(bet, kj.split("\\+")[0].split(",")[2], "正三");
		}
		return 0;
	}

	public static int lhcZ4t(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZnt(bet, kj.split("\\+")[0].split(",")[3], "正四");
		}
		return 0;
	}

	public static int lhcZ5t(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZnt(bet, kj.split("\\+")[0].split(",")[4], "正五");
		}
		return 0;
	}

	public static int lhcZ6t(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZnt(bet, kj.split("\\+")[0].split(",")[5], "正六");
		}
		return 0;
	}

	private static int lhcZmn(String bet, int num) {
		List<String> result = new ArrayList<>();
		if (num == 49) {
			result.add("和");
		} else {
			// 特码大小：开出之特码大于或等于25为特码大， 小于或等于24为特码小，开出49为和局。
			if (num >= 25 && num <= 48) {
				result.add("大码");
			} else if (num >= 1 && num <= 24) {
				result.add("小码");
			}
			// 特码单双：特码为双数叫特双，如8、16；特码为单数叫特单，如21、35，开出49为和局。
			if (num % 2 == 0) {
				result.add("双码");
			} else if (num % 2 == 1) {
				result.add("单码");
			}

			int sum = 0;
			if (num >= 1 && num <= 9) {
				sum = num;
			} else {
				sum = num / 10 + num % 10;
			}
			// 特码和数大小：以特码个位和十位数字之和来判断胜负，和数大于或等于7为大，小于或等于6为小，开出49号为和局。
			if (sum >= 7 && sum <= 12) {
				result.add("合大");
			} else if (sum >= 1 && sum <= 6) {
				result.add("合小");
			}
			// 特码和数单双：以特码个位和十位数字之和来判断单双，如01，12，32为和单，02，11，33为和双，开出49号为和局。
			if (sum % 2 == 0) {
				result.add("合双");
			} else if (sum % 2 == 1) {
				result.add("合单");
			}
			// 特码尾数大小：以特别号尾数若0尾~4尾为小、5尾~9尾为大；如01、32、44为特尾小；如05、18、19为特尾大，开出49号为和局
			int end = num % 10;
			if (end >= 5 && end <= 9) {
				result.add("尾大");
			} else if (end >= 0 && end <= 4) {
				result.add("尾小");
			}
		}

		Integer[] red = { 1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46 };
		Integer[] blue = { 3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48 };
		Integer[] green = { 5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49 };
		if (Arrays.asList(red).contains(num)) {
			result.add("红波");
		} else if (Arrays.asList(blue).contains(num)) {
			result.add("蓝波");
		} else if (Arrays.asList(green).contains(num)) {
			result.add("绿波");
		}

		if (result.contains(bet)) {
			return 1;
		} else if (result.contains("和")) {
			return -1;
		}

		return 0;
	}

	public static int lhcZm1(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZmn(bet, Integer.parseInt(kj.split("\\+")[0].split(",")[0]));
		}
		return 0;
	}

	public static int lhcZm2(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZmn(bet, Integer.parseInt(kj.split("\\+")[0].split(",")[1]));
		}
		return 0;
	}

	public static int lhcZm3(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZmn(bet, Integer.parseInt(kj.split("\\+")[0].split(",")[2]));
		}
		return 0;
	}

	public static int lhcZm4(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZmn(bet, Integer.parseInt(kj.split("\\+")[0].split(",")[3]));
		}
		return 0;
	}

	public static int lhcZm5(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZmn(bet, Integer.parseInt(kj.split("\\+")[0].split(",")[4]));
		}
		return 0;
	}

	public static int lhcZm6(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			return lhcZmn(bet, Integer.parseInt(kj.split("\\+")[0].split(",")[5]));
		}
		return 0;
	}

	public static int lhcWxType(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] metal = { "04", "05", "18", "19", "26", "27", "34", "35", "48", "49" };
			String[] wood = { "01", "08", "09", "16", "17", "30", "31", "38", "39", "46", "47" };
			String[] water = { "06", "07", "14", "15", "22", "23", "36", "37", "44", "45" };
			String[] fire = { "02", "03", "10", "11", "24", "25", "32", "33", "40", "41" };
			String[] earth = { "12", "13", "20", "21", "28", "29", "42", "43" };
			String[] kjs = kj.split("\\+");
			if (kjs.length == 2) {
				String tm = kjs[1];
				List<String> result = new ArrayList<>();
				if (Arrays.asList(metal).contains(tm)) {
					result.add("金");
				} else if (Arrays.asList(wood).contains(tm)) {
					result.add("木");
				} else if (Arrays.asList(water).contains(tm)) {
					result.add("水");
				} else if (Arrays.asList(fire).contains(tm)) {
					result.add("火");
				} else if (Arrays.asList(earth).contains(tm)) {
					result.add("土");
				}
				if (result.contains(bet)) {
					return 1;
				}
			}
		}
		return 0;
	}

	public static int lhc7sb(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.split("\\+");
			String tm = kjs[1];
			String[] zms = kjs[0].split(",");

			Map<String, Double> map = new HashMap<>();
			for (String zm : zms) {
				getBsNum(zm, map, 1.0);
			}
			getBsNum(tm, map, 1.5);

			double[] result = { StringUtil.isBlank(map.get("红波")) ? 0.0 : map.get("红波"),
					StringUtil.isBlank(map.get("蓝波")) ? 0.0 : map.get("蓝波"),
					StringUtil.isBlank(map.get("绿波")) ? 0.0 : map.get("绿波") };
			Arrays.sort(result);
			// 和局
			if (result[0] == 1.5 && result[1] == 3.0 && result[2] == 3.0) {
				if (bet.equals("和局")) {
					return 1;
				} else {
					return -1;
				}
			} else {
				List<String> keys = new ArrayList<String>();
				Set<Entry<String, Double>> set = map.entrySet();
				Iterator<Entry<String, Double>> it = set.iterator();
				while (it.hasNext()) {
					Map.Entry<String, Double> entry = (Map.Entry<String, Double>) it.next();
					if (entry.getValue().equals(result[2])) {
						keys.add(entry.getKey());
					}
				}
				if (keys.contains(bet)) {
					return 1;
				}
			}
		}
		return 0;
	}

	private static void getBsNum(String num, Map<String, Double> map, double addNum) {
		String[] red = { "01", "02", "07", "08", "12", "13", "18", "19", "23", "24", "29", "30", "34", "35", "40", "45",
				"46" };
		String[] blue = { "03", "04", "09", "10", "14", "15", "20", "25", "26", "31", "36", "37", "41", "42", "47", "48" };
		String[] green = { "05", "06", "11", "16", "17", "21", "22", "27", "28", "32", "33", "38", "39", "43", "44",
				"49" };
		if (Arrays.asList(red).contains(num)) {
			if (!StringUtil.isBlank(map.get("红波"))) {
				double redNum = map.get("红波");
				redNum += addNum;
				map.put("红波", redNum);
			} else {
				map.put("红波", addNum);
			}
		} else if (Arrays.asList(blue).contains(num)) {
			if (!StringUtil.isBlank(map.get("蓝波"))) {
				double blueNum = map.get("蓝波");
				blueNum += addNum;
				map.put("蓝波", blueNum);
			} else {
				map.put("蓝波", addNum);
			}
		} else if (Arrays.asList(green).contains(num)) {
			if (!StringUtil.isBlank(map.get("绿波"))) {
				double greenNum = map.get("绿波");
				greenNum += addNum;
				map.put("绿波", greenNum);
			} else {
				map.put("绿波", addNum);
			}
		}
	}

	public static int lhcZxbz(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.replace("+", ",").split(",");
			String[] bets = bet.split(",");
			if (getSameCount(kjs, bets) == 0) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 六合彩连码 n中m
	 * 
	 * @param bet
	 * @param kj
	 * @param n
	 * @param m
	 * @return
	 */
	private static int lmnzm(String bet, String kj, int n, int m) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			List<List<String>> betList = getCombination(bet, n);
			String zms = kj.split("\\+")[0];
			for (List<String> list : betList) {
				if (getCount4Same(list.toArray(), zms.split(",")) == m) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 六合彩连码-三全中 三全中： 所投注的每三个号码为一组合，若三个号码都是开奖号码之正码，视为中奖，其余情形视为不中奖。
	 * 如06、07、08三个都是开奖号码之正码，视为中奖，如二个正码加上一个特别号码视为不中奖。
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcLm3qz(String bet, String kj) {
		return lmnzm(bet, kj, 3, 3);
	}

	/**
	 * 六合彩连码-三中二 三中二： 所投注的每三个号码为一组合，若其中2个是开奖号码中的正码，即为三中二，视为中奖；其余视为不中奖 。
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcLm3z2(String bet, String kj) {
		return lmnzm(bet, kj, 3, 2);
	}

	/**
	 * 六合彩连码-二全中 二全中： 所投注的每二个号码为一组合，二个号码都是开奖号码之正码，视为中奖，其馀情形视为不 中奖（含一个正码加一个特别号码之情形）。
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcLm2qz(String bet, String kj) {
		return lmnzm(bet, kj, 2, 2);
	}

	/**
	 * 六合彩连码-二中特之中特 所投注的每二个号码为一组合，二个号码若其中一 个是正码，一个是特别号码，叫二中特之中特；其馀情形视为不中奖 。
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcLm2zt(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			List<List<String>> betList = getCombination(bet, 2);
			String zms = kj.split("\\+")[0];
			String tm = kj.split("\\+")[1];
			for (List<String> list : betList) {
				if (list.contains(tm)) {
					list.remove(tm);
					if (zms.contains(list.get(0))) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 六合彩连码-特串 特串： 所投注的每二个号码为一组合，其中一个是正码，一个是特别号码，视为中奖，其馀情形视为 不中奖（含二个号码都是正码之情形） 。
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcLmtc(String bet, String kj) {
		return lhcLm2zt(bet, kj);
	}

	/**
	 * 六合彩连码-四全中
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcLm4qz(String bet, String kj) {
		return lmnzm(bet, kj, 4, 4);
	}

	/**
	 * 六合彩特肖-生肖
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcTxsx(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String tm = kj.split("\\+")[1];
			// 根据单个数字得到String生肖
			String sx = ZodiacRuleUtil.numToZodiac(Integer.parseInt(tm));

			if (sx.equals(bet)) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 六合彩合肖-合肖
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcHx(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String tm = kj.split("\\+")[1];
			if (!"49".equals(tm)) {
				// 根据生肖数组得到数字的String集合
				List<String> nums = ZodiacRuleUtil.zodiacesToNums(bet.split(","));

				if (nums.contains(tm)) {
					return 1;
				}
			} else {
				return -1;
			}
		}
		return 0;
	}

	/**
	 * 六合彩平特一肖尾数-一肖
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcPt1x(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.replace("+", ",").split(",");
			// 根据bet生肖得到相对应的数字数组
			List<String> nums = ZodiacRuleUtil.zodiacToNum(null, bet);

			if (getCount4Same(nums.toArray(), kjs) >= 1) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 六合彩平特一肖尾数-尾数
	 * 
	 * @param bet
	 * @param kj
	 * @return
	 */
	public static int lhcPtws(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.replace("+", ",").split(",");

			Set<String> result = new HashSet<>();
			for (String str : kjs) {
				int num = Integer.parseInt(str);
				int tail = num % 10;
				result.add(tail + "尾");
			}

			if (result.contains(bet)) {
				return 1;
			}
		}
		return 0;
	}

	public static int lhcZxsx(String bet, String kj) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] zms = kj.split("\\+")[0].split(",");
			// 根据bet生肖得到相对应的数字数组
			List<String> nums = ZodiacRuleUtil.zodiacToNum(null, bet);
			for (String zm : zms) {
				if (nums.contains(zm)) {
					count++;
				}
			}
		}
		return count;
	}

	public static int lhcZxType(String bet, String kj) {
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			String[] kjs = kj.replace("+", ",").split(",");

			// 根据String数字数组得到生肖的Set集合
			Set<String> result = ZodiacRuleUtil.numsToZodiaces(kjs);
			if (bet.equals(result.size() + "肖")) {
				return 1;
			}
			if (result.size() % 2 == 0 && "总肖双".equals(bet)) {
				return 1;
			} else if (result.size() % 2 == 1 && "总肖单".equals(bet)) {
				return 1;
			}
		}
		return 0;
	}

	private static int lhcNlx(String bet, String kj, int n) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			List<List<String>> sxList = getCombination(bet, n);
			String[] kjs = kj.replace("+", ",").split(",");
			// 根据根据String数字数组得到生肖的Set集合
			Set<String> result = ZodiacRuleUtil.numsToZodiaces(kjs);

			for (List<String> list : sxList) {
				if (getCount4Same(list.toArray(), result.toArray()) == n) {
					count++;
				}
			}
		}
		return count;
	}

	public static int lhc2lx(String bet, String kj) {
		return lhcNlx(bet, kj, 2);
	}

	public static int lhc3lx(String bet, String kj) {
		return lhcNlx(bet, kj, 3);
	}

	public static int lhc4lx(String bet, String kj) {
		return lhcNlx(bet, kj, 4);
	}

	public static int lhc5lx(String bet, String kj) {
		return lhcNlx(bet, kj, 5);
	}

	private static int lhcNlw(String bet, String kj, int n) {
		int count = 0;
		if (!StringUtil.isBlank(bet) && !StringUtil.isBlank(kj)) {
			List<List<String>> sxList = getCombination(bet, n);
			String[] kjs = kj.replace("+", ",").split(",");
			Set<String> result = new HashSet<>();
			for (String str : kjs) {
				int num = Integer.parseInt(str);
				int tail = num % 10;
				result.add(tail + "尾");
			}

			for (List<String> list : sxList) {
				if (getCount4Same(list.toArray(), result.toArray()) == n) {
					count++;
				}
			}
		}
		return count;
	}

	public static int lhc2lw(String bet, String kj) {
		return lhcNlw(bet, kj, 2);
	}

	public static int lhc3lw(String bet, String kj) {
		return lhcNlw(bet, kj, 3);
	}

	public static int lhc4lw(String bet, String kj) {
		return lhcNlw(bet, kj, 4);
	}

	public static int lhc5lw(String bet, String kj) {
		return lhcNlw(bet, kj, 5);
	}
}