package com.lb.member.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.sys.tools.model.Message;

public interface IBetsumLogService {

	List<Map<String, Object>> queryBetsumLogList(Map<String, Object> map);

	Message addAndSubtractCode(HttpServletRequest request, Map<String, Object> map);

}
