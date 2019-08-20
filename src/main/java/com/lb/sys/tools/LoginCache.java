package com.lb.sys.tools;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @date:2017年12月16日下午12:01:32
 * @describe:登录成功后,将生产的token存储到本地的缓存中,下次登录时,进行验证.
 */
public class LoginCache {

	private static final ConcurrentHashMap<String,String> CURRENT_LOGINTOKEN = new ConcurrentHashMap<String,String>();
	
	public static final String HEARD_TOKEN = "X-Authorization";
	/**
	 * 生成token
	 * @return
	 */
	private static String tokenFactory() {
		return UUID.randomUUID().toString();
	}
	
	
	/**
	 * 校验token
	 * @param tokenFromMap:服务器端的token
	 * @param tokenFromClient:来自客户端的token
	 * @return
	 */
	public static boolean checkToken(String username,HttpServletRequest request) {
		String tokenFromMap = getToken(username);
		String tokenFromClient = getTokenFromHeard(request);
		if(tokenFromMap!=null && tokenFromClient !=null && tokenFromMap!="" && tokenFromClient !="") {
			return tokenFromMap.equals(tokenFromClient);
		}else {
			return false;
		}
	}
	
	/**
	 * 以用户名为key,将token存储在map集合中
	 * @param usrname:用户名
	 */
	public static void saveToken(String usrname) {
		LoginCache.CURRENT_LOGINTOKEN.put(usrname, tokenFactory());
	}
	
	/**
	 * 通过用户名,取出token
	 * @param usrname
	 * @return
	 */
	public static String getToken(String usrname) {
		return LoginCache.CURRENT_LOGINTOKEN.get(usrname);
	}
	
	private static String getTokenFromHeard(HttpServletRequest request) {
		return request.getHeader(HEARD_TOKEN);
	}
	
	public static void deleteToken(String username) {
		LoginCache.CURRENT_LOGINTOKEN.remove(username);
	}
	
}
