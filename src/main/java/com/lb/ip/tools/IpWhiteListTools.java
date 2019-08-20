package com.lb.ip.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpWhiteListTools {

	// 验证Ip的格式是否为正确格式
	public static boolean red(String ipAddr) {
		// 首先判断是否为合法的Ip
		Pattern pattern = Pattern.compile(
				"\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ipAddr);
		return matcher.matches();
	}

}
