package com.lb.sys.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lb.redis.JedisClient;

import net.sf.json.JSONObject;

/**
 * @describe 香港六合彩生肖规则工具类
 */
public class ZodiacRuleUtil {

	private static final String ZODIAC_ORDER = "鼠|牛|虎|兔|龙|蛇|马|羊|猴|鸡|狗|猪";
	
	private static String currentZodiac = "狗";//当年生肖

	private static JedisClient jedisClient = null;
	
	private static JdbcTemplate jdbcTemplate = null;
	
	static {
		jedisClient = SpringUtil.getBean(JedisClient.class);
		jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);
	}
	
	/**
	 * @param zodiac 生肖：鼠,牛,虎,兔,龙,蛇,马,羊,猴,鸡,狗,猪
	 * @return List<String> 返回生肖所对应的数字的拼接字符串
	 * */
	public static String zodiacToNum(String zodiac) {
		reset();
		String zodiaces[] = ZODIAC_ORDER.split("[|]");
		int index = -1;
		int inIndex = -1;
		String zodiacNums = null;
		for(int i = 0 ; i < zodiaces.length ; i ++) {
			if(zodiaces[i].equals(currentZodiac)) {
				index = i + 1;
			}
			if(zodiaces[i].equals(zodiac)) {
				inIndex = i + 1;
			}
		}
		if(index != -1 && inIndex != -1) {
			int initNum = 1;
			if(inIndex > index) {
				initNum += 12 - inIndex + index;
			}else {
				initNum += (index - inIndex);
			}
			StringBuilder sbr = new StringBuilder();
			for(int i = 0 ; i < 5 ; i++) {
				if(i != 4 || inIndex == index) {
					sbr.append((initNum < 10?",0":",") + initNum);
					initNum += 12;
				}
			}
			zodiacNums = sbr.toString().substring(1);
		}
		return zodiacNums;
	}
	/**
	 * @param zodiac 生肖：鼠,牛,虎,兔,龙,蛇,马,羊,猴,鸡,狗,猪
	 * @return List<String> 返回生肖所对应的数字集合
	 * */
	public static List<String> zodiacToNum(List<String> inList,String zodiac) {
		reset();
		String zodiaces[] = ZODIAC_ORDER.split("[|]");
		int index = -1;
		int inIndex = -1;
		List<String> zodiacNum = null;
		if(inList != null) {
			zodiacNum = inList;
		}else {
			zodiacNum = new ArrayList<String>();
		}
		for(int i = 0 ; i < zodiaces.length ; i ++) {
			if(zodiaces[i].equals(currentZodiac)) {
				index = i + 1;
			}
			if(zodiaces[i].equals(zodiac)) {
				inIndex = i + 1;
			}
		}
		if(index != -1 && inIndex != -1) {
			int initNum = 1;
			if(inIndex > index) {
				initNum += 12 - inIndex + index;
			}else {
				initNum += (index - inIndex);
			}
			
			for(int i = 0 ; i < 5 ; i++) {
				if(i != 4 || inIndex == index) {
					zodiacNum.add((initNum < 10 ? "0" : "" ) + initNum);
					initNum += 12;
				}
			}
		}
		return zodiacNum;
	}
	/**
	 * 生肖数组转所对应的数字
	 * */
	public static List<String> zodiacesToNums(String zodiaces[]){
		List<String> zodiacNum =  null;
		if(zodiaces != null) {
			zodiacNum =  new ArrayList<String>();
			for(String zodiac : zodiaces) {
				zodiacToNum(zodiacNum,zodiac);
			}
		}
		return zodiacNum;
	}
	
	/**
	 * @param num 某个生肖下的数字
	 * @return String 返回数字对应生肖：鼠,牛,虎,兔,龙,蛇,马,羊,猴,鸡,狗,猪
	 * */
	public static String numToZodiac(int num) {
		String zodiac = null;
		if(num > 0 && num < 50) {
			String zodiaces[] = ZODIAC_ORDER.split("[|]");
			num %= 12;
			num -= 1;
			for(int i = 0 ; i < zodiaces.length ; i ++) {
				if(zodiaces[i].equals(currentZodiac)) {
					if(i < num) {
						zodiac = zodiaces[i + 12 - num];
					}else {
						zodiac = zodiaces[i - num];
					}
					break;
				}
			}
		}
		return zodiac;
	}
	
	/**
	 * 多个生肖数字转成对应的生肖集合
	 * */
	public static Set<String> numsToZodiaces(String nums[]){
		Set<String> zodiacNums = null;
		if(nums != null) {
			zodiacNums = new HashSet<String>();
			for(String num : nums) {
				zodiacNums.add(numToZodiac(Integer.valueOf(num)));
			}
		}
		return zodiacNums;
	}
	/**
	 * 获取生肖数组
	 * */
	public static String[] getZodiacList() {
		return ZODIAC_ORDER.split("[|]");
	}
	/**
	 * 获取对应生肖所对应的map字符串
	 * */
	public static Map<String,Object> getZodiacNumsMap(String currentZodiac){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		for(String zodiac : ZODIAC_ORDER.split("[|]")) {
			resultMap.put(zodiac, zodiacToNum(zodiac));
		}
		return resultMap;
	}
	/**设置当年生肖*/
	@SuppressWarnings({ "unchecked", "unused" })
	private static void reset() {
		if(jedisClient == null) {
			jedisClient = SpringUtil.getBean(JedisClient.class);
		}
		Map<String,Object> configMap = null;
		if(jedisClient != null && "1".equals(jedisClient.get("isUseCacheData") + "")) {
			String configJson = jedisClient.hget("sys_configure", "currentZodiacConfig");
			if(configJson != null) {
				configMap = JSONObject.fromObject(configJson);
			}
		}else {
			if(jdbcTemplate == null) {
				jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);
			}
			Map<String, Object> queryForMap = null;
			try {
				configMap = jdbcTemplate.queryForMap("SELECT sys_config1,`status`,`on_off` FROM ls.`sys_configure` WHERE sys_configure.`configure`='currentZodiacConfig' AND sys_configure.`status`=1 AND sys_configure.`on_off`=1 LIMIT 1");
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
		if(configMap != null && "1".equals(configMap.get("status") + "") && "1".equals(configMap.get("on_off") + "")) {
			currentZodiac = String.valueOf(configMap.get("sys_config1"));
		}
	}
}
