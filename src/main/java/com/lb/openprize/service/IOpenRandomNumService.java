package com.lb.openprize.service;

import java.util.List;
import java.util.Map;

public interface IOpenRandomNumService {

	void openLuckNum(String oneTypeId, String issue);

	String openSetLuckNum(String issue, String oneTypeId, String luckNumber);

	List<Map<String, String>> getSystemColorType();

	List<Map<String, Object>> querySystemColorNum(Map<String, Object> map);

	String updateSetLuckNum(Map<String, Object> map);

	String preinstallLuckNum(Map<String, Object> map);
	String generateIssue(int suffixLength,int intervalMinute,String oneTypeId);
}
