package com.lb.onlineSchedule.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineDetailCache {
	//在线会员map
	private static ConcurrentHashMap<String, Map<String,String>> requestDetail = new ConcurrentHashMap<>();
	//在线代理map
	private static ConcurrentHashMap<String, Map<String,String>> agencyDetail = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String,  Map<String,String>> getRequestDetail() {
		
		return requestDetail;
	}
	public static ConcurrentHashMap<String,  Map<String,String>> getAgencyDetail() {
		
		return agencyDetail;
	}

	public static void setRequestDetail(ConcurrentHashMap<String,  Map<String,String>> requestDetail) {
		OnlineDetailCache.requestDetail = requestDetail;
	}
	
	public static void setAgencyDetail(ConcurrentHashMap<String,  Map<String,String>> agencyDetail) {
		OnlineDetailCache.agencyDetail = agencyDetail;
	}
	//第一次登陆时的存储
	public static void addOnlineDetailCache(String username,String date, String ip) {
		Map<String,String> map = new HashMap<>();
		map.put("loginTime", date);
		map.put("lastTime", date);
		map.put("ip", ip);
		requestDetail.put(username, map);
	}
	//第一次登陆时的存储
	public static void addAgencyOnlineDetailCache(String username,String date, String ip) {
		Map<String,String> map = new HashMap<>();
		map.put("loginTime", date);
		map.put("lastTime", date);
		map.put("ip", ip);
		agencyDetail.put(username, map);
	}
	//心跳时存储,最近的访问时间
	public static void updateBreakDetailCache(String username,String date,String ip) {
		Map<String, String> map = requestDetail.get(username);
		map.put("lastTime", date);
		map.put("ip", ip);
	}
	//心跳时存储,最近的访问时间
	public static void updateAgencyBreakDetailCache(String username,String date) {
		agencyDetail.get(username).put("lastTime", date);
	}
}
