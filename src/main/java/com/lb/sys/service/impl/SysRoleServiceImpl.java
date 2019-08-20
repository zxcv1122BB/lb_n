package com.lb.sys.service.impl;
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

import com.lb.sys.dao.SysRoleMapper;
import com.lb.sys.model.SysRole;
import com.lb.sys.model.SysRoleExample;
import com.lb.sys.model.SysRoleExample.Criteria;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysRoleService;
import com.lb.sys.tools.model.Message;
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SysRoleServiceImpl implements ISysRoleService{
	private final Log LOGGER = LogFactory.getLog(SysRoleServiceImpl.class);
	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Override
	public Message deleteByPrimaryKey(Long roleId) {
		SysRole selectByPrimaryKey = sysRoleMapper.selectByPrimaryKey(roleId);
		selectByPrimaryKey.setState(Short.valueOf("0"));
		int update = sysRoleMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
	
		Message message =new Message();
		if(update>0) {
			message.setCode(200);
			message.setMsg("删除成功");
		}else {
			message.setCode(232);
			message.setMsg("后台角色删除失败");
			LOGGER.error("后台角色删除失败");
		}
		return message;
	}

	@Override
	public Message insertSelective(HttpServletRequest request,SysRole record) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		record.setState(Short.parseShort("1"));
		record.setCreatedDate(new Date());
		record.setCreatedUser(sysUser.getUserName());
		int insertSelective = sysRoleMapper.insertSelective(record);
		
		Message message =new Message();
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("添加成功");
		}else {
			message.setCode(230);
			message.setMsg("后台角色添加失败");
			LOGGER.error("后台角色添加失败");
		}
		return message;
	}

	@Override
	public List<SysRole> selectByExample() {
		return sysRoleMapper.selectList();
	}

	@Override
	public SysRole selectByPrimaryKey(Long roleId) {
		return sysRoleMapper.selectByPrimaryKey(roleId);
	}

	@Override
	public Message updateByPrimaryKeySelective(HttpServletRequest request,SysRole record) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		//设置更新属性
		record.setUpdateDate(new Date());
		record.setUpdateUser(sysUser.getUserName());
		int update= sysRoleMapper.updateByPrimaryKeySelective(record);
		
		Message message =new Message();
		if(update>0) {
			message.setCode(200);
			message.setMsg("更新成功");
		}else {
			message.setCode(231);
			message.setMsg("后台角色更新失败");
			LOGGER.error("后台角色更新失败");
		}
		return message;
	}

	@Override
	public Message isStartRole(HttpServletRequest request,Map<String, Object> map) {
		Long roleId =Long.parseLong(String.valueOf(map.get("roleId")));
		Short state =Short.parseShort(String.valueOf(map.get("state")));
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		//设置更新属性
		SysRole selectByPrimaryKey = sysRoleMapper.selectByPrimaryKey(roleId);
		selectByPrimaryKey.setState(state);
		selectByPrimaryKey.setUpdateDate(new Date());
		selectByPrimaryKey.setUpdateUser(sysUser.getUserName());
		//对角色进行启用
		int update= sysRoleMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
		//对角色菜单依赖表进行启用
		sysRoleMapper.updateRel(map);
		
		Message message =new Message();
		if(update>0) {
			message.setCode(200);
			message.setMsg("启用成功");
		}else {
			message.setCode(232);
			message.setMsg("后台角色启用操作失败");
			LOGGER.error("后台角色启用操作失败");
		}
		return message;
	}

	@Override
	public Message queryRoleByRoleName(String roleName) {
		SysRoleExample example=new SysRoleExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andRoleNameEqualTo(roleName);
		List<SysRole> selectByExample = sysRoleMapper.selectByExample(example);
		
		Message message =new Message();
		if(!selectByExample.isEmpty()&&null!=selectByExample.get(0)) {
			message.setCode(233);
			message.setMsg("已存在此角色");
		}else {
			message.setCode(200);
			message.setMsg("此角色可使用");
			LOGGER.error("此角色可使用");
		}
		return message;
	}

	@Override
	public List<SysRole> queryRealyRole(Map<String,Object> param) {
		return sysRoleMapper.queryRealyRole(param);
	}
}
