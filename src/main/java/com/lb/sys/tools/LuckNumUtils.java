package com.lb.sys.tools;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
/**
 * 用于计划生成预设开奖号码
 */
public class LuckNumUtils {

	/**
	 * 时时彩
	 */
	// 计算万位定位胆的计划开奖号码
	public static String getWanLuckNum() {
		return getLuckNum(5);
	}

	// 计算后二直选的计划开奖号码
	public static String getH2LuckNum() {
		return getLuckNum(7) + "-" + getLuckNum(7);
	}

	// 计算后三直选的计划开奖号码
	public static String getH3ZLuckNum() {
		return getLuckNum(8) + "-" + getLuckNum(8) + "-" + getLuckNum(8);
	}

	// 计算后三组六的计划开奖号码
	public static String getH3Z6LuckNum() {
		return getLuckNum(9);
	}

	/**
	 * pk10 
	 */
	// 冠军定位胆
	public static String getBjGjLuckNum() {
		return getLuckNum(5, 10, 1);
	}

	/**
	 * 快三
	 */
	// 和值
	public static String getJsksLuckNum() {
		return getLuckNumNoZero(8, 16, 3);
	}
	
	/**
	 * 快乐10分 
	 */
	//数投
	public static String getKlsfLuckNum() {
		return getLuckNum(9,18,1);
	}
	
	/**
	 * 11选5
	 */
	// 定位胆 第一位
	public static String get11x5LuckNum() {
		return getLuckNum(6, 11, 1);
	}
	
	/**
	 * 3d 
	 */
	//定位胆
	public static String get3DLuckNum() {
		return getLuckNum(8) + "-" + getLuckNum(8) + "-" + getLuckNum(8);
	}
	
	/**
	 * 北京28 
	 */
	//特码
	public static String getBj28LuckNum(){
		return getLuckNumNoZero(14, 28, 0);
	}
	
	/**
	 * 七星彩
	 */
	// 二字现
	public static String getQxcLuckNum() {
		return getLuckNum(7);
	}
	
	private static String getLuckNum(int n) {
		Random ran = new Random();
		Integer[] arr = new Integer[n];
		int i = 0;
		while (i < n) {
			int t = ran.nextInt(10);
			if (!Arrays.asList(arr).contains(t)) {
				arr[i] = t;
				i++;
			}
		}
		Arrays.sort(arr);
		return StringUtils.join(arr);
	}

	private static String getLuckNum(int num, int a, int b) {
		Random ran = new Random();
		Integer[] arr = new Integer[num];
		int i = 0;
		while (i < num) {
			int t = ran.nextInt(a) + b;
			if (!Arrays.asList(arr).contains(t)) {
				arr[i] = t;
				i++;
			}
		}
		Arrays.sort(arr);
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < arr.length; j++) {
			sb.append(String.format("%02d", arr[j]));
			if (j != arr.length - 1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	private static String getLuckNumNoZero(int num, int a, int b) {
		Random ran = new Random();
		Integer[] arr = new Integer[num];
		int i = 0;
		while (i < num) {
			int t = ran.nextInt(a) + b;
			if (!Arrays.asList(arr).contains(t)) {
				arr[i] = t;
				i++;
			}
		}
		Arrays.sort(arr);
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < arr.length; j++) {
			sb.append(arr[j]);
			if (j != arr.length - 1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
}
