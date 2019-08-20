package com.lb.sys.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lb.sys.dao.SysLotteryConfigMapper;
import com.lb.sys.model.SysLotteryConfig;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysLotteryConfigService;
import com.lb.sys.tools.model.Message;


@Service
public class SysLotteryConfigServiceImpl implements ISysLotteryConfigService {
	private final Log LOGGER = LogFactory.getLog(SysRoleServiceImpl.class);
	@Autowired
	private SysLotteryConfigMapper sysLotteryConfigMapper;

	@Override
	public List<SysLotteryConfig> querySysLotteryConfigList(Map<String, Object> map) {
		return sysLotteryConfigMapper.querySysLotteryConfigList(map);
	}

	@Override
	public Message add(HttpServletRequest request, SysLotteryConfig sysFbConfigure) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		sysFbConfigure.setCreateDate(new Date());
		sysFbConfigure.setCreateUser(sysUser.getUserName());
		int insertSelective = sysLotteryConfigMapper.insertSelective(sysFbConfigure);
		Message message =new Message();
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("添加成功");
		}else {
			message.setCode(230);
			message.setMsg("彩种配置添加失败");
			LOGGER.error("彩种配置添加失败");
		}
		return message;
	}

	@Override
	public Message update(HttpServletRequest request, SysLotteryConfig sysFbConfigure) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		sysFbConfigure.setUpdateDate(new Date());
		sysFbConfigure.setUpdateUser(sysUser.getUserName());
		int update= sysLotteryConfigMapper.updateByPrimaryKeySelective(sysFbConfigure);
		
		Message message =new Message();
		if(update>0) {
			message.setCode(200);
			message.setMsg("更新成功");
		}else {
			message.setCode(231);
			message.setMsg("彩种配置更新失败");
			LOGGER.error("彩种配置更新失败");
		}
		return message;
	}

	@Override
	public Message delete(Integer id) {
		int delete =sysLotteryConfigMapper.deleteByPrimaryKey(id);
		Message message =new Message();
		if(delete>0) {
			message.setCode(200);
			message.setMsg("删除成功");
		}else {
			message.setCode(232);
			message.setMsg("彩种配置删除失败");
			LOGGER.error("彩种配置删除失败");
		}
		return message;
	}
}
