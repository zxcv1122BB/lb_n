package com.lb.sys.service.impl;
import java.util.Date;
import java.util.HashMap;
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

import com.lb.redis.JedisClient;
import com.lb.sys.controller.LoginController;
import com.lb.sys.dao.SysUserMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.model.SysUserExample;
import com.lb.sys.model.SysUserExample.Criteria;
import com.lb.sys.model.SysUserR;
import com.lb.sys.service.ISysUserService;
import com.lb.sys.tools.MyMD5Util;
import com.lb.sys.tools.SpringUtil;
import com.lb.sys.tools.model.Message;
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SysUserServiceImpl implements ISysUserService{
	private final Log LOGGER = LogFactory.getLog(SysUserServiceImpl.class);
	@Autowired
	private SysUserMapper sysUserMapper;

	@Override
	public Message deleteByPrimaryKey(HttpServletRequest request,Long userId) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		SysUser selectByPrimaryKey = sysUserMapper.selectByPrimaryKey(userId);
		selectByPrimaryKey.setState(Short.valueOf("0"));
		selectByPrimaryKey.setUpdateDate(new Date());
		selectByPrimaryKey.setUpdateUser(sysUser.getUserName());
		int update= sysUserMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
		
		Message message =new Message();
		if(update>0) {
			message.setCode(200);
			message.setMsg("删除成功");
		}else {
			message.setCode(222);
			message.setMsg("删除后台用户失败");
			LOGGER.error("删除后台用户失败");
		}
		return message;
	}

	@Override
	public Message insertSelective(HttpServletRequest request,SysUser record) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		//对密码进行MD5加密
		LOGGER.info("原密码"+record.getUserPassword());
		String encodePassword = MyMD5Util.encodePassword(record.getUserPassword(), MyMD5Util.salt(record.getUserPassword()));
		LOGGER.info("修改后密码"+encodePassword);
		record.setUserPassword(encodePassword);
		//设置更新属性
		record.setCreatedDate(new Date());
		if(sysUser!=null) {
			record.setCreatedUser(sysUser.getUserName());
		}
		record.setState(Short.valueOf("1"));
		int insertSelective = sysUserMapper.insertSelective(record);
		
		Message message =new Message();
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("添加成功");
		}else {
			message.setCode(220);
			message.setMsg("添加后台用户失败");
			LOGGER.error("添加后台用户失败");
		}
		return message;
	}

	@Override
	public List<SysUser> selectByExample() {
		SysUserExample example=new SysUserExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andStateEqualTo(Short.valueOf("1"));
		return sysUserMapper.selectByExample(example);
	}

	@Override
	public SysUser selectByPrimaryKey(Long userId) {
		return sysUserMapper.selectByPrimaryKey(userId);
	}

	@Override
	public Message updateByPrimaryKeySelective(HttpServletRequest request,SysUser record) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		//对密码进行MD5加密
		if(null!=record.getUserPassword()&&!"".equals(record.getUserPassword())) {
			String encodePassword = MyMD5Util.encodePassword(record.getUserPassword(), MyMD5Util.salt(record.getUserPassword()));
			record.setUserPassword(encodePassword);
		}
		//设置更新属性
		record.setUpdateDate(new Date());
		record.setUpdateUser(sysUser.getUserName());
		int update=sysUserMapper.updateByPrimaryKeySelective(record);
		
		Message message =new Message();
		if(update>0) {
			message.setCode(200);
			message.setMsg("更新成功");
		}else {
			message.setCode(221);
			message.setMsg("后台用户更新失败");
			LOGGER.error("后台用户更新失败");
		}
		return message;
	}

	//后台登陆验证
	public Map<String,Object> checkLogin(SysUser loginUser) {
		SysUserExample example = new SysUserExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andUserNameEqualTo(loginUser.getUserName());
		Map<String,Object> map = new HashMap<>();
		//查询到符合改名字的用户信息
		List<SysUser> sysUser = sysUserMapper.selectByExample(example);
		//存在该用户名
		if(sysUser!=null && sysUser.size()>0&&"1".equals(sysUser.get(0).getState().toString())) {
			//从数据库中查询到的该用户
			SysUser user = sysUser.get(0);
			//登陆时的用户密码
			String userPassword = loginUser.getUserPassword();
			//核对登陆密码和数据库存储密码
			boolean bool = MyMD5Util.isPasswordInvalid(user.getUserPassword(),userPassword, MyMD5Util.salt(userPassword));
			if(bool) {
				map.put("code",200);
				map.put(LoginController.LOGINUSER, user);
			}else {
				map.put("code",201);
			}
		}else if(sysUser!=null && sysUser.size()>0&&"0".equals(sysUser.get(0).getState().toString())) {
			map.put("code",208);
		}else if(sysUser==null||sysUser.size()==0){
			map.put("code",202);
		}
		return map;
	}
	
	

	@Override
	public Message isStartUsing(HttpServletRequest request, Long userId, Short state) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		//设置更新属性
		SysUser selectByPrimaryKey = sysUserMapper.selectByPrimaryKey(userId);
		selectByPrimaryKey.setState(state);
		selectByPrimaryKey.setUpdateDate(new Date());
		selectByPrimaryKey.setUpdateUser(sysUser.getUserName());
		int update= sysUserMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
		
		Message message =new Message();
		if(update>0) {
			message.setCode(200);
			message.setMsg("操作成功");
		}else {
			message.setCode(222);
			message.setMsg("操作失败");
			LOGGER.error("操作失败");
		}
		return message;
	}

	@Override
	public Message queryUserByUserName(String userName) {
		SysUserExample example=new SysUserExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andUserNameEqualTo(userName);
		List<SysUser> selectByExample = sysUserMapper.selectByExample(example);
		Message message =new Message();
		if(!selectByExample.isEmpty()&&null!=selectByExample.get(0)) {
			message.setCode(223);
			message.setMsg("存在此管理员");
		}else {
			message.setCode(200);
			message.setMsg("此管理员账号可使用");
			LOGGER.error("此管理员账号可使用");
		}
		return message;
	}

	@Override
	public List<SysUserR> isExist(Map<String, Object> map) {
		return sysUserMapper.isExist(map);
	}

	public Map<String, Object> queryGoogleCodeStateByUsername(String username) {
		return sysUserMapper.queryGoogleCodeStateByUsername(username);
	}

	
	public int updateGoogleCodeStateByUsername(Map<String, Object> map) {
		return sysUserMapper.updateGoogleCodeStateByUsername(map);
	}

	
	/***
	 * 将登陆的时候密码错误的次数记录下来
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean insertCountToRedis(String key,String UID) {
		String newKey=key+UID;
		JedisClient jedisClientPool= (JedisClient)SpringUtil.getBean("jedisClient");
		// 首先 从redis中查询出该管理员是否有过密码错误的次数
		String string = jedisClientPool.get(newKey);
		try {
			if (string == null || string.equals(" ")) {
				jedisClientPool.set(newKey, "1");
				// 如果是第一个计数 则将该条记录的过期时间设置为一小时  expire
				jedisClientPool.expire(newKey, 7200);
			} else {
				Integer count = Integer.parseInt(string) + 1;
				// 否则 说明该管理员之前已经有过密码错误的情况 则将该记录次数累计+1 覆盖之前的登陆次数
				boolean update = jedisClientPool.update(newKey, count.toString());
			    // 如果为第五次 则将该条记录的国企时间设置为两小时
			    jedisClientPool.expire(newKey, 7200);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

	/**
	 * 获取某一个用户的登陆错误次数
	 */
	@Override
	public boolean isLocking(String key,String uid) {
		 String newKey=key+uid;
		 JedisClient jedisClientPool= (JedisClient) SpringUtil.getBean("jedisClient");
		  String string = jedisClientPool.get(newKey);
		  int count = 0;
		  if(string!=null && !(string.equals(""))) {
			  count=Integer.parseInt(string);
			  boolean flag=true;
			  //判断是是否为锁定的状态
			  if(count<5) {
				  flag =false;  
			  } 
			  return flag;
		  }else {
			  return false;   
		  }
	}

	@Override
	public boolean isPassword(Map<String, Object> map) {
		String userName= String.valueOf(map.get("userName"));
		//原密码
		String oldPassword= String.valueOf(map.get("oldPassword"));
		//通过账号查询信息
		SysUserExample example = new SysUserExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andUserNameEqualTo(userName);
		createCriteria.andStateEqualTo(Short.valueOf("1"));
		//查询到符合改名字的用户信息
		List<SysUser> sysUser = sysUserMapper.selectByExample(example);
		if(sysUser!=null&&sysUser.size()>0) {
			//校验账号密码
			if(!StringUtils.isBlank(oldPassword)) {
				String password = sysUser.get(0).getUserPassword();
				//验证会员原密码是否正确
				if(MyMD5Util.isPasswordInvalid(password,oldPassword, MyMD5Util.salt(oldPassword))) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean updatePassword(Map<String, Object> map) {
		boolean flag = isPassword(map);
		if(flag) {
			String password= String.valueOf(map.get("password"));
			//md5加密
			password= MyMD5Util.encodePassword(password, MyMD5Util.salt(password));
			map.put("password", password);
			//更新密码
			int update=sysUserMapper.updatePassword(map);
			if(update>0) {
				return true;
			}
		}
		return false;
	}
}
