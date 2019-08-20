package com.lb.sys.tools;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class DateScopeUtils {
	public static String DATE_PATTERN = "yyyy-MM-dd";
	public static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static String TIME_PATTERN = "HH:mm:ss";
	public static String TIME_START = "00:00:00";
	public static String TIME_END = "23:59:59";
	
	/**
	 * 
	 * @param dt
	 * @param type 1今天2昨天3本周4上周5本月6上月
	 * @return
	 */
	public static Map<String,Object> getDateByType(DateTime dt,int type){
		if(dt == null) dt = DateTime.now();
		Map<String,Object> map = new HashMap<String, Object>();		
		switch (type) {
		case 1:
			map.put("start", getDay(dt, 0, 1));
			map.put("end", getDay(dt, 0, 2));
			break;
		case 2:
			map.put("start", getDay(dt, -1, 1));
			map.put("end", getDay(dt, -1, 2));
			break;
		case 3:
			map.put("start", getMonday(dt, 0, 1));
			map.put("end", getSunday(dt, 0, 2));
			break;
		case 4:
			map.put("start", getMonday(dt, -1, 1));
			map.put("end", getSunday(dt, -1, 2));
			break;
		case 5:
			map.put("start", getMonthFirstDay(dt, 0, 1));
			map.put("end", getMonthLastDay(dt, 0, 2));
			break;
		case 6:
			map.put("start", getMonthFirstDay(dt, -1, 1));
			map.put("end", getMonthLastDay(dt, -1, 2));
			break;
		default:
			map.put("start", getDay(dt, 0, 1));
			map.put("end", getDay(dt, 0, 2));
			break;
		}
		return map;
	}
	/**
	 * @param dt
	 * @param intervalDay 
	 * @param timeType 0:HH:mm:ss,1:00:00:00,2:23:59:59
	 * @return
	 */
	public static String getDay(DateTime dt,int intervalDay,int timeType){
		if(dt == null) dt = DateTime.now();
		DateTime handleDT = dt.plusDays(intervalDay);
		String endTime = getSpecialTime(handleDT, timeType);
		return handleDT.toString(DATE_PATTERN)+" "+endTime;
	}
	/**
	 * 获取N周前或N周后的周一
	 * @param dt 时间，不传则按当前时间计算
	 * @param intervalWeek //正数:N周前的周一，复数:N周后的周一
	 * @return
	 */
	public static String getMonday(DateTime dt,int intervalWeek,int timeType){
		if(dt == null) dt = DateTime.now();
		int dayOfWeek = dt.getDayOfWeek();
		DateTime handleDT = dt.plusWeeks(intervalWeek).minusDays(dayOfWeek-1);
		String endTime = getSpecialTime(handleDT, timeType);
		return dt.plusWeeks(intervalWeek).minusDays(dayOfWeek-1).toString(DATE_PATTERN)+" "+endTime;
	}
	/**
	 * 获取N周前或N周后的周日
	 * @param dt 时间，不传则按当前时间计算
	 * @param intervalWeek //正数:N周前的周日，复数:N周后的周日
	 * @return
	 */
	public static String getSunday(DateTime dt,int intervalWeek,int timeType){
		if(dt == null) dt = DateTime.now();
		int dayOfWeek = dt.getDayOfWeek();
		DateTime handleDT = dt.plusWeeks(intervalWeek).minusDays(dayOfWeek-1);
		String endTime = getSpecialTime(handleDT, timeType);
		return dt.plusWeeks(intervalWeek).plusDays(7-dayOfWeek).toString(DATE_PATTERN)+" "+endTime;
	}
	
	public static String getMonthFirstDay(DateTime dt,int intervalMonth,int timeType){
		if(dt == null) dt = DateTime.now();
		DateTime dateTime = dt.plusMonths(intervalMonth).dayOfMonth().withMinimumValue();
		String endTime = getSpecialTime(dateTime, timeType);		
		return dateTime.toString(DATE_PATTERN)+" "+endTime;
	}
	
	public static String getMonthLastDay(DateTime dt,int intervalMonth,int timeType){
		if(dt == null) dt = DateTime.now();
		DateTime dateTime = dt.plusMonths(intervalMonth).dayOfMonth().withMaximumValue();
		String endTime = getSpecialTime(dateTime, timeType);		
		return dateTime.toString(DATE_PATTERN)+" "+endTime;
	}
	/**
	 * @param dt
	 * @param timeType 0:HH:mm:ss,1:00:00:00,2:23:59:59
	 * @return
	 */
	public static String getSpecialTime(DateTime dt,int timeType) {
		String time = "";
		switch (timeType) {
		case 0:
			if(dt == null) dt = DateTime.now();
			time = dt.toString(TIME_PATTERN);
			break;
		case 1:
			time = TIME_START;
			break;
		case 2:
			time = TIME_END;
			break;
		default:
			if(dt == null) dt = DateTime.now();
			time = dt.toString(TIME_PATTERN);
			break;
		}
		return time;
	}
	public static void main(String[] args) {
		for (int i = 1; i < 7; i++) {
			System.err.println(getDateByType(null, i));
		}
	}
}
