package com.lb.sys.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class ArrangeUtils {

	public static Set<String> getGroup(String str, int num) {// 组合，从字符串str中取m个字符
		str = str.replaceAll(",", "");
		int n = 1;// 存放n的阶乘
		int m = 1;// 存放m的阶乘
		int n_m = 1;// 存放n-m的阶乘
		for (int i = 0; i < str.length(); i++) {
			n *= (i + 1);
		}
		for (int i = 0; i < num; i++) {
			m *= (i + 1);
		}
		for (int i = 0; i < str.length() - num; i++) {
			n_m *= (i + 1);
		}
		Set<String> sets = new HashSet<String>();
		while (true) {
			String s = "";
			for (int i = 0; i < num; i++) {// 产生可能的组合字符串
				s += str.charAt((int) (Math.random() * str.length()));
			}
			boolean flage1 = true;
			for (int i = 0; i < s.length(); i++) {// 过滤产生的组合中相同的字符串
				int num4 = s.length() - (s.replace(String.valueOf(s.charAt(i)), "")).length();
				if (num4 > 1) {
					flage1 = false;
				}
			}
			boolean flage2 = true;
			for (String string : sets) {// 过滤排列的字符串
				String temp = string;
				for (int i = 0; i < s.length(); i++) {
					String str1 = s.substring(i, i + 1);
					temp = temp.replace(str1, "");
				}
				if (temp.length() == 0) {
					flage2 = false;
				}
			}
			if (!sets.contains(s) && flage1 && flage2) {
				sets.add(s);
			}
			if (sets.size() == n / (n_m * m)) {
				break;
			}
		}
		return sets;
	}

	public static Set<String> getArrangement(String str, int num) {// 排列组合，从字符串str中取m个字符
		str = str.replaceAll(",", "");
		int n = 1;// 存放n的阶乘
		int n_m = 1;// 存放n-m的阶乘
		for (int i = 0; i < str.length(); i++) {
			n *= (i + 1);
		}
		for (int i = 0; i < str.length() - num; i++) {
			n_m *= (i + 1);
		}
		Set<String> sets = new HashSet<String>();
		while (true) {
			String s = "";
			for (int i = 0; i < num; i++) {
				s += str.charAt((int) (Math.random() * str.length()));
			}
			boolean flage1 = true;
			for (int i = 0; i < s.length(); i++) {// 产生的组合中不能有相同的字符
				int num4 = s.length() - (s.replace(String.valueOf(s.charAt(i)), "")).length();
				if (num4 > 1) {
					flage1 = false;
				}
			}
			if (!sets.contains(s) && flage1) {
				sets.add(s);
			}
			if (sets.size() == n / n_m) {
				break;
			}
		}
		return sets;
	}

	/**
	 * 数学中的统计方法，用于整数，A(3,2)即3*2,A(5,3)即5*4*3
	 * 
	 * @param first
	 *            开始的数
	 * @param second
	 *            个数
	 * @return
	 */
	public static int A(int first, int second) {
		int tmp = first;
		int result = first;
		int count = 0;
		while (count < second - 1) {
			if (second == 1) {
				return first;
			} else {
				count++;
				tmp--;
				result = result * tmp;
			}
		}
		return result;
	}

	/**
	 * 实现了数学中阶乘的方法 factorialA(5)即5!
	 * 
	 * @param number
	 * @return
	 */
	public static int factorialA(int number) {
		return A(number, number);
	}

	/**
	 * 实现了数学中的组合方法C(n,r)即 n!/(n-r)!r!
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static int C(int n, int r) {
		if (n == r) {
			return 1;
		}
		return factorialA(n) / (factorialA(n - r) * factorialA(r));
	}

	/**
	 * 笛卡尔计算数组的组合数
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList Dikaerji0(String[]... arr) {
		if (arr == null||arr.length==0) {
			return null;
		}
		ArrayList<List<String>> al0 = new ArrayList<>();
		for (String[] str : arr) {
			ArrayList<String> str1 = new ArrayList<>(Arrays.asList(str));
			al0.add(str1);
		}
		ArrayList a0 = (ArrayList) al0.get(0);// l1
		ArrayList result = new ArrayList();// 组合的结果
		for (int i = 1; i < al0.size(); i++) {
			ArrayList a1 = (ArrayList) al0.get(i);
			ArrayList temp = new ArrayList();
			// 每次先计算两个集合的笛卡尔积，然后用其结果再与下一个计算
			for (int j = 0; j < a0.size(); j++) {
				for (int k = 0; k < a1.size(); k++) {
					ArrayList cut = new ArrayList();

					if (a0.get(j) instanceof ArrayList) {
						cut.addAll((ArrayList) a0.get(j));
					} else {
						cut.add(a0.get(j));
					}
					if (a1.get(k) instanceof ArrayList) {
						cut.addAll((ArrayList) a1.get(k));
					} else {
						cut.add(a1.get(k));
					}
					temp.add(cut);
				}
			}
			a0 = temp;
			if (i == al0.size() - 1) {
				result = temp;
			}
		}
		return result;
	}
	
/**
 *@describe:判断2个数组元素是否相同
 *@param args:
 */
	public static boolean isSameArray(String[] arr,String[] brr) {
		if (arr==null ||brr==null ||arr.length!=brr.length) {
			return false;
		}
		Arrays.sort(arr);
		Arrays.sort(brr);
		String arrStr = StringUtils.join(arr);
		String brrStr = StringUtils.join(brr);
		if (arrStr.equals(brrStr)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 *@describe:比较2个数组中相同元素的个数
	 *@param args:
	 */
	public static int getSameCount(String[] arr,String[] brr) {
		if (arr==null ||brr==null) {
			return 0;
		}
		List<String> list = new ArrayList<>();
		for (String a : arr) {
			for (String b : brr) {
				if (a.equals(b) && !list.contains(a)) {
					list.add(a);
				}
			}
		}
		return list.size();
	}
	
	/**
	 *@describe:过滤掉数组中重复的元素
	 *@param args:
	 */
	public static String[] filterSameArray(String[] arr) {
		if (arr==null ||arr.length==0) {
			return arr;
		}
		List<String> list = new ArrayList<>();
		for (String a : arr) {
			if (!list.contains(a)) {
				list.add(a);
			}
		}
		return list.toArray(new String[0]);
	}
	
	//字符串排序
	public static String sortStr(String str) {
		String[] split = str.split("");
		Arrays.sort(split);
		return StringUtils.join(split,"");
	}
}
