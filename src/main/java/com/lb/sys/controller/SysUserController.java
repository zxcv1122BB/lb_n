package com.lb.sys.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.redis.JedisClient;
import com.lb.sys.model.SysUser;
import com.lb.sys.model.SysUserR;
import com.lb.sys.service.ISysUserService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.GoogleAuthenticator;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.ServerStateUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/sysUser")
public class SysUserController extends BaseController{
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private JedisClient jedisClient;

	@RequestMapping(method=RequestMethod.POST,value="/addUser")
	public ModelAndView addUser(HttpServletRequest request){
		SysUser user = BaseController.jsonToBean(request, SysUser.class);
		Message message= sysUserService.insertSelective(request,user);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/getServerState")
	public ModelAndView getServerState(HttpServletRequest request){
		return ResponseUtils.jsonView(200,"获取成功",ServerStateUtils.getServerStateInfo());
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/unlockUser")
	public ModelAndView unlockUser(HttpServletRequest request){
		SysUser user = BaseController.jsonToBean(request, SysUser.class);
		//接触锁定
		Long del = jedisClient.del("lbIsLocking"+user.getUserName());
		return del>=1 ? ResponseUtils.jsonView(200,"解除成功"):ResponseUtils.jsonView(200,"解除失败");
	}
	
	
	@RequestMapping(method=RequestMethod.GET,value="/queryUserById")
	public ModelAndView queryUserById(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Long userId =(Long) map.get("userId");
		SysUser user=sysUserService.selectByPrimaryKey(userId);
		return ResponseUtils.jsonView(user);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/queryUserByUserName")
	public ModelAndView queryUserByUserName(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		String userName = String.valueOf(map.get("userName"));
		Message message=sysUserService.queryUserByUserName(userName);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	//模糊搜索和用户列表
	@RequestMapping(method=RequestMethod.GET,value="/isExist")
	public ModelAndView isExist(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getRoleId() == null) {
			return ResponseUtils.jsonView(333,"权限异常");
		}
		map.put("adminRoleId", user.getRoleId());
		List<SysUserR> userList=sysUserService.isExist(map);
		PageUtils<SysUserR> pageInfo =  new PageUtils<>(pageIndex,pageNum,userList);
		return ResponseUtils.jsonView(pageInfo);
	}
	@RequestMapping(method=RequestMethod.POST,value="/updateUser")
	public ModelAndView updateUser(HttpServletRequest request) {
		SysUser user = BaseController.jsonToBean(request, SysUser.class);
		Message message= sysUserService.updateByPrimaryKeySelective(request,user);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
		
	}
	@RequestMapping(method=RequestMethod.POST,value="/deleteUser")
	public ModelAndView deleteUser(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Long userId =(Long) map.get("userId");
		Message message= sysUserService.deleteByPrimaryKey(request,userId);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	//校验密码
	@RequestMapping(method=RequestMethod.POST,value="/isPassword")
	public ModelAndView isPassword(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		boolean flag= sysUserService.isPassword(map);
		return flag == true ? ResponseUtils.jsonView(200, "原密码正确") : ResponseUtils.jsonView(338, "原密码错误");
	}
	//更新密码
	@RequestMapping(method=RequestMethod.POST,value="/updatePassword")
	public ModelAndView updatePassword(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		boolean flag= sysUserService.updatePassword(map);
		return flag == true ? ResponseUtils.jsonView(200, "密码更改成功") : ResponseUtils.jsonView(339, "密码更改失败");
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/isStartUsing")
	public ModelAndView isStartUsing(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Long userId =Long.parseLong(String.valueOf(map.get("userId")));
		Short state =Short.parseShort(String.valueOf(map.get("state")));
		Message message= sysUserService.isStartUsing(request,userId,state);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	
	//获取谷歌身份是否开启的状态
	@RequestMapping(method=RequestMethod.GET,value="/getGoogleCodeStatus")
	public ModelAndView getGoogleCodeStatus(HttpServletRequest request) {
		String username = request.getParameter(LoginController.USERNAME);
		Map<String, Object> resultMap = sysUserService.queryGoogleCodeStateByUsername(username);
		if(resultMap!=null && !resultMap.isEmpty()) {
			resultMap.put("qrcode", GoogleAuthenticator.getQRBarcode(String.valueOf(resultMap.get("GOOGLECODE_SECRET"))));
			return ResponseUtils.jsonView(200, "获取谷歌验证码状态成功", resultMap);
		}else {
			return ResponseUtils.jsonView(225, "账号名不存在", resultMap);
		}
	}
	
	//修改谷歌身份是否开启的状态和秘钥
	@RequestMapping(method=RequestMethod.POST,value="/updateGoogleCodeStatus")
	public ModelAndView updateGoogleCodeStatus(HttpServletRequest request) {
		Map<String, Object> paramMap = BaseController.jsonToMap(request);
		boolean bool = checkGoogleCode(paramMap);
		if(bool) {
			Integer resultInt = sysUserService.updateGoogleCodeStateByUsername(paramMap);
			return resultInt>0?ResponseUtils.jsonView(200, "修改谷歌验证码状态成功"):ResponseUtils.jsonView(226, "修改谷歌验证码状态失败");
		}else {
			return ResponseUtils.jsonView(227, "谷歌验证码校验失败");
		}
	}
	
	// 校验谷歌身份验证码
	private boolean checkGoogleCode(Map<String, Object> param) {
		//param是前台传入的参数
		Long googleVerifycode = Long.parseLong(String.valueOf(param.get("googleVerifycode")));
		String googleSecret = String.valueOf(param.get("googleSecret"));
		long t = System.currentTimeMillis();
		GoogleAuthenticator ga = new GoogleAuthenticator();
		// 设置验证码偏移时间,1个单位是偏移30秒,共60秒有效期
		ga.setWindowSize(1);
		return ga.check_code(googleSecret, googleVerifycode, t);
	}

	
}
	

	

