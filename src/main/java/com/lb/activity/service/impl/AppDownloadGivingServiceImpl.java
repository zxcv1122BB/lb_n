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

import com.lb.activity.model.AppDownloadGiving;
import com.lb.activity.service.IAppDownloadGivingService;
import com.lb.sys.dao.AppDownloadGivingMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.model.Message;
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class AppDownloadGivingServiceImpl implements IAppDownloadGivingService{
	
	private final Log LOGGER = LogFactory.getLog(AppDownloadGivingServiceImpl.class);
	
	@Autowired
	private AppDownloadGivingMapper appDownloadGivingMapper;

	@Override
	public List<Map<String, Object>> queryAppDownloadGivingList(Map<String, Object> map) {
		List<Map<String, Object>> list = appDownloadGivingMapper.queryAppDownloadGivingList(map);
		return list;
	}

	@Override
	public Message addAppDownloadGiving(HttpServletRequest request, AppDownloadGiving appDownloadGiving) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		if(sysUser!=null) {
			appDownloadGiving.setCreatedUser(sysUser.getUserName());
		}
		appDownloadGiving.setState(Byte.parseByte("0"));
		appDownloadGiving.setCreatedDate(new Date());
		int insertSelective = appDownloadGivingMapper.insertSelective(appDownloadGiving);
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("添加成功");
		}else {
			message.setCode(340);
			message.setMsg("app下载赠送添加失败");
			LOGGER.info("app下载赠送添加失败");
		}
		return message;
	}

	@Override
	public Map<String, Object> queryAppDownloadGivingById(Integer id) {
		return appDownloadGivingMapper.queryAppDownloadGivingById(id);
	}

	@Override
	public Message updateAppDownloadGiving(HttpServletRequest request, AppDownloadGiving appDownloadGiving) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		if(sysUser!=null) {
			appDownloadGiving.setUpdateUser(sysUser.getUserName());
		}
		//设置更新属性
		appDownloadGiving.setUpdateDate(new Date());
		int update= appDownloadGivingMapper.updateByPrimaryKeySelective(appDownloadGiving);
		if(update>0) {
			message.setCode(200);
			message.setMsg("更新成功");
		}else {
			message.setCode(341);
			message.setMsg("app下载赠送更新失败");
			LOGGER.info("app下载赠送更新失败");
		}
		return message;
	}

	@Override
	public Message deleteAppDownloadGiving(HttpServletRequest request, Integer id) {
		Message message =new Message();
		int delete = appDownloadGivingMapper.deleteByPrimaryKey(id);
		if(delete>0) {
			message.setCode(200);
			message.setMsg("删除成功");
		}else {
			message.setCode(342);
			message.setMsg("app下载赠送删除失败");
			LOGGER.info("app下载赠送删除失败");
		}
		return message;
	}

	@Override
	public Message isStartAppDownloadGiving(HttpServletRequest request, Integer id, Byte state) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		Message message =new Message();
		//设置更新属性
		AppDownloadGiving selectByPrimaryKey =appDownloadGivingMapper.selectByPrimaryKey(id);
		selectByPrimaryKey.setState(state);
		selectByPrimaryKey.setUpdateDate(new Date());
		if(sysUser!=null) {
			selectByPrimaryKey.setUpdateUser(sysUser.getUserName());
		}
		int update=appDownloadGivingMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
		if(update>0) {
			message.setCode(200);
			message.setMsg("启用成功");
		}else {
			message.setCode(343);
			message.setMsg("app下载赠送启用失败");
			LOGGER.info("app下载赠送启用失败");
		}
		return message;
	}

}
