package com.lb.sys.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/***
 * 字符串切割工具对象
 * 
 * @author ASUS
 *
 */
public class StringSlip {

	// 切割字符串
	public static int[] spliString(String parm) {
		String sp = "：";
		if (parm.contains(":")) {
			sp = ":";
		}
		String temp[] = parm.split(sp);
		int arr[] = { Integer.valueOf(temp[0].replaceAll(" ", "")), Integer.valueOf(temp[1].replaceAll(" ", "")) };
		// 将切割后的数据进行相加
		return arr;
	}
	
	//去除字符串中间的字符
	public static String spliStringString(String parm) {
		String sp = "：";
		if (parm.contains(":")) {
			sp = ":";
		}
		String temp[] = parm.split(sp);
		StringBuffer sb = new StringBuffer(temp[0].replaceAll(" ", ""));
		sb.append(temp[1].replaceAll(" ", ""));
		return sb.toString();
	}

	// 相加
	public static int addCount(String parm) {
		int temp[] = spliString(parm);
		// 将切割后的数据进行相加
		int sum = 0;
		sum = temp[0] + temp[1];
		return sum;
	}

	// 计算得出胜负
	public static String succOrBai(String parm) {
		int temp[] = spliString(parm);
		String returString = null;
		if (temp[0] > temp[1]) {
			returString = "胜";
		} else if (temp[0] < temp[1]) {
			returString = "负";
		} else {
			returString = "平";
		}

		return returString;
	}

	/**
	 * 计算出让球胜平负
	 * 
	 * @param quanchang
	 *            全场比分
	 * @param shourang
	 *            受让球
	 * @return 胜平负
	 */
	public static String getRqspf(String quanchang, int shourang) {
		int temp[] = spliString(quanchang);
		temp[0] = temp[0] + shourang;
		String result = null;
		// 判断胜平负
		if (temp[0] > temp[1]) {
			result = "胜";
		} else if (temp[0] < temp[1]) {
			result = "负";
		} else {
			result = "平";
		}
		return result;
	}

	// 时间推算
	@SuppressWarnings("static-access")
	public static String getTwoDate(String dateStr,int num) {
        String redult="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(dateStr);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE,num);// 把日期往后增加一天.整数往后推,负数往前移动
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			redult = sdf.format(date); // 增加一天后的日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return redult;
	}

}
