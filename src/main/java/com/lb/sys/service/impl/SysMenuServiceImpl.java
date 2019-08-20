package com.lb.sys.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.SysMenuMapper;
import com.lb.sys.dao.SysRoleMapper;
import com.lb.sys.model.SysMenu;
import com.lb.sys.model.SysMenuExample;
import com.lb.sys.model.SysRole;
import com.lb.sys.model.SysRoleExample;
import com.lb.sys.model.SysRoleExample.Criteria;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysMenuService;
import com.lb.sys.tools.model.Message;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SysMenuServiceImpl implements ISysMenuService{
	private final Log LOGGER = LogFactory.getLog(SysMenuServiceImpl.class);
	@Autowired
	private SysMenuMapper sysMenuMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Override
	public Message deleteByPrimaryKey(Long menuId) {
		Message message =new Message();
		int delete = sysMenuMapper.deleteByPrimaryKey(menuId);
		if(delete>0) {
			message.setCode(200);
			message.setMsg("删除成功");
		}else {
			message.setCode(242);
			message.setMsg("后台菜单删除失败");
			LOGGER.error("后台菜单删除失败");
		}
		return message;
	}
	@Override
	public Message insertSelective(HttpServletRequest request,SysMenu record) {
		//计算当前菜单层级
		Long menuParentId = record.getMenuParentId();
		SysMenu sysMenu_parent = sysMenuMapper.selectByPrimaryKey(menuParentId);
		Short menuLevels_parent = sysMenu_parent.getMenuLevels();
		Short menuLevels=(short) (menuLevels_parent+Short.valueOf("1"));
		//计算当前排序标记
		Integer flag_parent = sysMenu_parent.getFlag();
		Integer flag = record.getFlag();
		if(flag<10) {
			//拼接如为110，父及是1，管理员添加的是1
			flag=Integer.valueOf(flag_parent+"0"+flag);
		}else {
			//拼接如为110，父及是1，管理员添加的是10
			flag=Integer.valueOf(flag_parent+""+flag);
		}
		//设置创建人
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		record.setState(Short.parseShort("1"));
		record.setCreatedDate(new Date());
		record.setCreatedUser(sysUser.getUserName());
		//设置层级
		record.setMenuLevels(menuLevels);
		//设置当前排序标记
		record.setFlag(flag);
		Message message =new Message();
		int insertSelective = sysMenuMapper.insertSelective(record);
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("添加成功");
		}else {
			message.setCode(240);
			message.setMsg("后台菜单添加失败");
			LOGGER.error("后台菜单添加失败");
		}
		return message;
	}

	@Override
	public SysMenu selectByPrimaryKey(Long menuId) {
		return sysMenuMapper.selectByPrimaryKey(menuId);
	}

	@Override
	public Message updateByPrimaryKeySelective(HttpServletRequest request,SysMenu record) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		//设置更新属性
		record.setUpdateDate(new Date());
		record.setUpdateUser(sysUser.getUserName());
		int update = sysMenuMapper.updateByPrimaryKeySelective(record);
		if(update>0) {
			message.setCode(200);
			message.setMsg("更新成功");
		}else {
			message.setCode(241);
			message.setMsg("后台菜单更新失败");
			LOGGER.error("后台菜单更新失败");
		}
		return message;
	}

	@Override
	public List<SysMenu> queryMenuListByParentId(Map<String, Object> hashMap) {
		return  sysMenuMapper.queryMenuListByParentId(hashMap);
		
	}

	@Override
	public List<Map<String, Object>> getSysMenuListByRoleId(Long roleId) {
		List<Map<String, Object>> listmap = sysMenuMapper.getSysMenuListByRoleId(roleId);
		if(listmap!=null&&!listmap.isEmpty()) {
			List<Map<String, Object>> list=new ArrayList<>();
			for (int i = 0; i < listmap.size(); i++) {
				Map<String, Object> m = listmap.get(i);
				Short spread = Short.valueOf(String.valueOf(m.get("spread")));
				if(Short.valueOf("1").equals(spread)){
					m.put("spread",true);
				}else if(Short.valueOf("2").equals(spread)){
					m.put("spread",false);
				}
				list.add(m);
			}
			return list;
		}
		return null;
		
	}

	@Override
	public List<SysMenu> queryRoleMenuList(Map<String, Object> param) {
		return sysMenuMapper.queryRoleMenuList(param);
	}

	@Override
	public int selectByMenuIdRoleId(Map<String, Object> param) {
		return sysMenuMapper.selectByMenuIdRoleId(param);
	}

	@Override
	public int insertRoleMenuRel(Map<String, Object> param) {
		return sysMenuMapper.insertRoleMenuRel(param);
	}

	@Override
	public int deleteRelById(Map<String, Object> param) {
		int flag=0;
		int deleteRelById = sysMenuMapper.deleteRelById(param);
		if(deleteRelById>0) {
			flag=200;
		}else {
			flag=252;
			LOGGER.error("后台删除授权失败");
		}
		return flag;
	}

	@Override
	public List<SysMenu> querySysMenuList() {
		SysMenuExample example = new SysMenuExample();
		List<SysMenu> list= sysMenuMapper.selectByExample(example);
		return list;
	}

	@Override
	public List<Map<String, Object>> queryAuthorizationMenu(Map<String, Object> map) {
		//判断角色是否为启用状态
		Long roleId =Long.parseLong(String.valueOf(map.get("roleId")));
		SysRoleExample example=new SysRoleExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andRoleIdEqualTo(roleId);
		createCriteria.andStateEqualTo(Short.valueOf("1"));
		List<SysRole> sysRoleList = sysRoleMapper.selectByExample(example);
		if(!sysRoleList.isEmpty()&&sysRoleList!=null&&sysRoleList.get(0)!=null) {
			//查询启用状态下，角色所对应的权限状况
			List<Map<String, Object>> listmap = sysMenuMapper.queryAuthorizationMenu(map);
			if(listmap!=null&&!listmap.isEmpty()) {
				List<Map<String, Object>> list=new ArrayList<>();
				for (int i = 0; i < listmap.size(); i++) {
					Map<String, Object> m = listmap.get(i);
					Short open = Short.valueOf(String.valueOf(m.get("open")));
					if(Short.valueOf("1").equals(open)){
						m.put("open",true);
					}else if(Short.valueOf("2").equals(open)){
						m.put("open",false);
					}
					Integer checked =Integer.valueOf(String.valueOf(m.get("checked")));
					if(Integer.valueOf("1").equals(checked)){
						m.put("checked",true);
					}else if(Integer.valueOf("0").equals(checked)){
						m.put("checked",false);
					}
					list.add(m);
				}
				return list;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Message batchAuthorization(Map<String, Object> param) {
		Message message =new Message();
		//先删除授权关系
		sysMenuMapper.batchDeleteByRoleId(param);
		List<Long> list=(List<Long>) param.get("menuIds");
		if(list.size()==0) {
			message.setCode(200);
			message.setMsg("批量授权成功");
			return message;
		}
		//批量授权
		int batchAuthorization = sysMenuMapper.batchAuthorization(param);
		if(batchAuthorization>0) {
			message.setCode(200);
			message.setMsg("批量授权成功");
		}else {
			message.setCode(250);
			message.setMsg("后台批量授权失败");
			LOGGER.error("后台批量授权失败");
		}
		return message;
	}

	@Override
	public Message batchDelete(Map<String, Object> param) {
		Message message =new Message();
		int batchDelete = sysMenuMapper.batchDelete(param);
		if(batchDelete>0) {
			message.setCode(200);
			message.setMsg("success");
		}else {
			message.setCode(252);
			message.setMsg("后台批量删除授权失败");
			LOGGER.error("后台批量删除授权失败");
		}
		return message;
	}

	@Override
	public Message isStartMenu(HttpServletRequest request, Long menuId, Short state) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		Message message =new Message();
		//设置更新属性
		SysMenu selectByPrimaryKey = sysMenuMapper.selectByPrimaryKey(menuId);
		selectByPrimaryKey.setState(state);
		selectByPrimaryKey.setUpdateDate(new Date());
		if (sysUser!=null) {
			selectByPrimaryKey.setUpdateUser(sysUser.getUserName());
		}
		//启用当前菜单
		int update= sysMenuMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
		//对子菜单递归启用操作
		recurrenceIsStart(state, sysUser, menuId);
		
		if(update>0) {
			message.setCode(200);
			message.setMsg("启用成功");
		}else {
			message.setCode(242);
			message.setMsg("后台菜单启用操作失败");
			LOGGER.error("后台菜单启用操作失败");
		}
		return message;
	}

	private void recurrenceIsStart(Short state, SysUser sysUser, Long parentId) {
		SysMenuExample example = new SysMenuExample();
		example.createCriteria().andMenuParentIdEqualTo(parentId);
		List<SysMenu> select= sysMenuMapper.selectByExample(example);
		if(select!=null&&!select.isEmpty()) {
			for (SysMenu sysMenu : select) {
				sysMenu.setState(state);
				sysMenu.setUpdateDate(new Date());
				if (sysUser!=null) {
					sysMenu.setUpdateUser(sysUser.getUserName());
				}
				//递归更新
				sysMenuMapper.updateByPrimaryKeySelective(sysMenu);
				//递归调用
				parentId = sysMenu.getMenuId();
				recurrenceIsStart(state, sysUser, parentId);
			}
		}
	}

	@Override
	public List<Map<String, Object>> queryLike(Map<String, Object> map) {
		return sysMenuMapper.queryLike(map);
	}

	@Override
	public List<Map<String, Object>> queryAll() {
		List<Map<String, Object>> listmap = sysMenuMapper.queryAll();
		if(listmap!=null&&!listmap.isEmpty()) {
			List<Map<String, Object>> list=new ArrayList<>();
			for (int i = 0; i < listmap.size(); i++) {
				Map<String, Object> m = listmap.get(i);
				Short isParent = Short.valueOf(String.valueOf(m.get("isParent")));
				if(Short.valueOf("1").equals(isParent)){
					m.put("isParent",true);
				}else if(Short.valueOf("2").equals(isParent)){
					m.put("isParent",false);
				}
				list.add(m);
			}
			return list;
		}
		return null;
	}

	@Override
	public String queryMenuPath(HttpServletRequest request,Long roleId) {
		String menuPath = sysMenuMapper.queryMenuPath(roleId);
		if(!StringUtils.isEmpty(menuPath)) {
			menuPath=menuPath.replace(" ",""); 
			String[] splits = menuPath.split(",");
			menuPath ="";
			for (String split : splits) {
				//给路劲加上项目名
				menuPath+=request.getContextPath()+"/"+split;
			}
		}
		return menuPath;
	}
	@Override
	public Message queryByMenuName(Map<String, Object> map) {
		int selectByExample = sysMenuMapper.queryByMenuName(map);
		
		Message message =new Message();
		if(selectByExample>0) {
			message.setCode(243);
			message.setMsg("存在此菜单");
		}else {
			message.setCode(200);
			message.setMsg("此菜单可使用");
			LOGGER.error("此菜单可使用");
		}
		return message;
	}
	
	@Override
	public List<Map<String, Object>> getRealyRoleList(Map<String, Object> map) {
		List<Map<String,Object>> resultList = sysMenuMapper.getRealyRoleList(map);
		if(resultList != null) {
			for (Map<String,Object> m : resultList) {
				if("1".equals(m.get("open") + "")) {
					m.put("open",true);
				}else if("2".equals(m.get("open") + "")) {
					m.put("open",false);
				}
				if("1".equals(m.get("checked") + "")) {
					m.put("checked",true);
				}else {
					m.put("checked",false);
				}
			}
		}
		return resultList;
	}
}
