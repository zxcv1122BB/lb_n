package com.lb.member.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.member.model.DepositGivingStrategy;
import com.lb.sys.tools.model.Message;

public interface IDepositGivingStrategyService {

	List<Map<String, Object>> queryAllDepositGivingStrategy();

	Message addDepositGivingStrategy(HttpServletRequest request, Map<String, Object> map);

	DepositGivingStrategy queryDepositGivingStrategyById(Integer strategyId);

	Message updateDepositGivingStrategy(HttpServletRequest request, Map<String, Object> map);

	Message deleteDepositGivingStrategy(HttpServletRequest request, Integer strategyId);

	Message isStartDepositGivingStrategy(HttpServletRequest request, Integer strategyId, Byte state);

	List<Map<String, Object>> codeInformation(Map<String, Object> map);

}
