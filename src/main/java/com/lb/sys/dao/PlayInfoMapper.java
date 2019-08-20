package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface PlayInfoMapper {

	//查询所有的支付平台信息
	List<Map<String, Object>> queryPayList();
	
	//查询该支付方式下的支付类型
	List<Map<String, Object>> queryPaytypeByMethodid(Integer methodid);
	
	//添加支付信息,添加信息后,主键回显
	Integer addPayInfo(Map<String,Object> map);
	
	//逻辑删除第三方支付方式
	Integer deletePayInfo(Integer id);
	
	//更新第三方支付信息
	Integer updatePayInfo(Map<String,Object> map);

	Integer addPaytype(Map<String, Object> map);

	Integer deletePayTypeList(Integer id);

	List<Map<String, Object>> queryPayOnlineNames();

	List<Map<String, Object>> queryPayTypeList();
	
	Integer updatePaytypeDetailInfo(Map<String, Object> map);
	
}
