package com.lb.member.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.member.model.DepositGivingStrategy;
import com.lb.member.service.IDepositGivingStrategyService;
import com.lb.sys.dao.DepositGivingStrategyMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.StringUtil;
import com.lb.sys.tools.model.Message;
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class DepositGivingStrategyServiceImpl implements IDepositGivingStrategyService{
	
	private final Log LOGGER = LogFactory.getLog(MemberDepositServiceImpl.class);
	
	@Autowired
	private DepositGivingStrategyMapper depositGivingStrategyMapper;
	
	@Override
	public List<Map<String, Object>> queryAllDepositGivingStrategy() {
		return depositGivingStrategyMapper.queryAllDepositGivingStrategy();
	}

	@Override
	public Message updateDepositGivingStrategy(HttpServletRequest request, Map<String, Object> map) {
		Message message =new Message();
		if(map == null || (StringUtil.isBlank(map.get("givingProportion"))
				&& StringUtil.isBlank(map.get("givingProportion")) 
				&& StringUtil.isBlank(map.get("givingCredits")))) {
			LOGGER.error("充值赠送策略更新失败,类型异常：" + map);
			message.setCode(301);
			message.setMsg("充值赠送策略更新失败");
			return message;
		}
		
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		if(sysUser!=null){
			map.put("updateUser", sysUser.getUserName());
		}
		//设置更新属性
		int update= depositGivingStrategyMapper.updateByPrimaryKeySelective(map);
		if(update>0) {
			message.setCode(200);
			message.setMsg("success");
		}else {
			message.setCode(301);
			message.setMsg("充值赠送策略更新失败");
			LOGGER.error("充值赠送策略更新失败");
		}
		return message;
	}

	@Override
	public Message deleteDepositGivingStrategy(HttpServletRequest request, Integer strategyId) {
		Message message =new Message();
		
		int update = depositGivingStrategyMapper.deleteByPrimaryKey(strategyId);
		if(update>0) {
			message.setCode(200);
			message.setMsg("success");
		}else {
			message.setCode(302);
			message.setMsg("充值赠送策略删除失败");
			LOGGER.error("充值赠送策略删除失败");
		}
		return message;
	}

	@Override
	public Message isStartDepositGivingStrategy(HttpServletRequest request, Integer strategyId, Byte state) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		Message message =new Message();
		//设置更新属性
		Map<String, Object> map=new HashMap<>();
		map.put("updateUser", sysUser.getUserName());
		map.put("state",state);
		map.put("id",strategyId);
		int update= depositGivingStrategyMapper.updateByPrimaryKeySelective(map);
		if(update>0) {
			message.setCode(200);
			message.setMsg("success");
		}else {
			message.setCode(302);
			message.setMsg("充值赠送策略启用操作失败");
			LOGGER.error("充值赠送策略启用操作失败");
		}
		return message;
	}


	@Override
	public Message addDepositGivingStrategy(HttpServletRequest request,Map<String, Object> map) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		map.put("createdUser", sysUser.getUserName());
		int insertSelective = depositGivingStrategyMapper.insertSelective(map);
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("success");
		}else {
			message.setCode(300);
			message.setMsg("充值赠送策略添加失败");
			LOGGER.error("充值赠送策略添加失败");
		}
		return message;
	}

	@Override
	public DepositGivingStrategy queryDepositGivingStrategyById(Integer strategyId) {
		return depositGivingStrategyMapper.selectByPrimaryKey(strategyId);
	}

	@Override
	public List<Map<String, Object>> codeInformation(Map<String, Object> map) {
		
		return depositGivingStrategyMapper.codeInformation(map);
	}
}
