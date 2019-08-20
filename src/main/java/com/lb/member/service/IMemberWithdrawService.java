package com.lb.member.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.download.model.WithdrawRecord;
import com.lb.sys.tools.model.Message;

public interface IMemberWithdrawService {

	List<Map<String, Object>> queryMemberWithdrawList(Map<String, Object> map);

	Message withdrawIsLock(HttpServletRequest request, Map<String, Object> map);

	Map<String, Object> withdrawQuery(Map<String, Object> map);

	Message withdrawHandle(HttpServletRequest request, Map<String, Object> map);

	List<WithdrawRecord> exportWithdrawRecord(Map<String, Object> map);

	Map<String, Object> withdrawUserInfo(Map<String, Object> map);
	
	Message memberDrawingManage(Map<String, Object> paramMap);

}
