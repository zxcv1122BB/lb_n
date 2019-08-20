package com.lb.sys.service;

import java.util.List;
import java.util.Map;

public interface IPayInfoService {

	List<Map<String, Object>> queryPayList();

	Integer addPayInfo(Map<String, Object> map);

	List<Map<String, Object>> queryPaytypeListByMethid(Integer integer);

	Integer delPayInfoByid(Integer integer);

	List<Map<String, Object>> queryPayOnlineNames();

	List<Map<String, Object>> queryPayTypeList();

	Integer updatePayInfo(Map<String, Object> map);

}
