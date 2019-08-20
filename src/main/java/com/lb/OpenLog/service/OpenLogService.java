package com.lb.OpenLog.service;

import java.util.List;
import java.util.Map;

/**
 *开奖日志业务逻辑接口类
 * @author ASUS
 *
 */
public interface OpenLogService {
	/***
	 * 查询所有的开奖日志
	 * @return
	 */
	 public List<Map<String, Object>> getAll(Map<String,Object> parmMap);
	 /***
	  * 根据主键查询详情
	  * @param parmMap 参数map
	  * @return
	  */
	 public Map<String, Object> selectByOpenno(Map<String,Object> parmMap);
}
