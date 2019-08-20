package com.lb.activity.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.activity.model.TurntablePrize;
import com.lb.activity.service.ITurntablePrizeService;
import com.lb.sys.dao.TurntablePrizeMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.model.Message;
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class TurntablePrizeServiceImpl implements ITurntablePrizeService{
	
	private final Log LOGGER = LogFactory.getLog(TurntablePrizeServiceImpl.class);
	
	@Autowired
	private TurntablePrizeMapper turntablePrizeMapper;
	@Override
	public List<TurntablePrize> queryTurntablePrizeList(Map<String, Object> map) {
		List<TurntablePrize> list = turntablePrizeMapper.queryTurntablePrizeList(map);
		return list;
	}

	@Override
	public Message addTurntablePrize(HttpServletRequest request, TurntablePrize turntablePrize) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		if(sysUser!=null) {
			turntablePrize.setCreatedUser(sysUser.getUserName());
		}
		turntablePrize.setState(Byte.parseByte("1"));
		turntablePrize.setCreatedDate(new Date());
		int insertSelective = turntablePrizeMapper.insertSelective(turntablePrize);
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("添加成功");
		}else {
			message.setCode(325);
			message.setMsg("转盘奖励添加失败");
			LOGGER.info("转盘奖励添加失败");
		}
		return message;
	}

	@Override
	public TurntablePrize queryTurntablePrizeById(Integer id) {
		return turntablePrizeMapper.selectByPrimaryKey(id);
	}

	@Override
	public Message updateTurntablePrize(HttpServletRequest request, TurntablePrize turntablePrize) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		if(sysUser!=null) {
			turntablePrize.setUpdateUser(sysUser.getUserName());
		}
		//设置更新属性
		turntablePrize.setUpdateDate(new Date());
		int update= turntablePrizeMapper.updateByPrimaryKeySelective(turntablePrize);
		if(update>0) {
			message.setCode(200);
			message.setMsg("更新成功");
		}else {
			message.setCode(326);
			message.setMsg("转盘奖励编辑失败");
			LOGGER.info("转盘奖励编辑失败");
		}
		return message;
	}

	@Override
	public Message deleteTurntablePrize(HttpServletRequest request, Integer id) {
		Message message =new Message();
		int delete = turntablePrizeMapper.deleteByPrimaryKey(id);
		if(delete>0) {
			message.setCode(200);
			message.setMsg("删除成功");
		}else {
			message.setCode(327);
			message.setMsg("转盘奖励删除失败");
			LOGGER.info("转盘奖励删除失败");
		}
		return message;
	}

	@Override
	public Message isStartTurntablePrize(HttpServletRequest request, Integer id, Byte state) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		Message message =new Message();
		//设置更新属性
		TurntablePrize selectByPrimaryKey =turntablePrizeMapper.selectByPrimaryKey(id);
		selectByPrimaryKey.setState(state);
		selectByPrimaryKey.setUpdateDate(new Date());
		if(sysUser!=null) {
			selectByPrimaryKey.setUpdateUser(sysUser.getUserName());
		}
		int update=turntablePrizeMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
		if(update>0) {
			message.setCode(200);
			message.setMsg("启用成功");
		}else {
			message.setCode(327);
			message.setMsg("转盘奖励启用失败");
			LOGGER.info("转盘奖励启用失败");
		}
		return message;
	}

}
