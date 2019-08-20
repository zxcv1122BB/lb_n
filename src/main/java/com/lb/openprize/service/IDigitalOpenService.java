package com.lb.openprize.service;

import java.util.Map;

/**
 * 数字彩开奖结果相关信息接口
 * @author ASUS
 *
 */
public interface IDigitalOpenService {

	/**
	 * 添加数字彩开奖结果信息
	 * @param map
	 * @return
	 */
	int insertDigitalOpenData(Map<String, Object> paramterMap);
	 
}
