package com.lb.member.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.download.model.DepositRecord;
import com.lb.sys.tools.model.Message;

public interface IMemberDepositService {

	List<Map<String, Object>> queryMemberDepositList(Map<String, Object> map);

	Message depositHandle(HttpServletRequest request,Map<String, Object> map);

	Map<String, Object> depositQuery(Map<String, Object> map);

	Message depositIsLock(HttpServletRequest request, Map<String, Object> map);

	List<DepositRecord> exportDepositRecord(Map<String, Object> map);

	Map<String, Object> depositUserInfo(Map<String, Object> map);

}
