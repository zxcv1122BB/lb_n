package com.lb.sys.controller;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.model.ProxyInfo;
import com.lb.sys.model.SysUser;
import com.lb.sys.model.UserVipModel;
import com.lb.sys.pojo.SysRegisterOptionPojo;
import com.lb.sys.service.IProxyInfoService;
import com.lb.sys.service.ISysRegisterOptionService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

@RestController
@RequestMapping("/proxyinfo")
public class ProxyInfoController extends BaseController{

	@Autowired
	private IProxyInfoService service;
	@Autowired	
	ISysRegisterOptionService sysRegisterOptionService;
	
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/deleteByProxyInfo")
	public ModelAndView deleteByProxyInfo(HttpServletRequest request) {
		ProxyInfo proxy = this.jsonToBean(request, ProxyInfo.class);
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		proxy.setUpdataUser(user.getUserName());
		proxy.setUpdataTime(new Date());
		int result= service.deleteByProxyInfo(proxy);
		return result>0?ResponseUtils.jsonView(200,"删除代理成功"):ResponseUtils.jsonView(261,"删除代理失败");
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/updateByProxyInfo")
	public ModelAndView updateByProxyInfo(HttpServletRequest request) {
		ProxyInfo proxy = this.jsonToBean(request, ProxyInfo.class);
		//如果修改系统默认代理，防止设置为禁用状态
		if(proxy.getId()==1) {
			proxy.setStatus(Byte.valueOf("1"));
		}
		if(proxy.getPid()!=null) {
			ProxyInfo proxy_parent=service.getProxyById(proxy.getPid());
			if(proxy.getRebateRatio()!=null&&(proxy_parent.getRebateRatio().compareTo(proxy.getRebateRatio())<=0||proxy.getRebateRatio().compareTo(new BigDecimal(0))<0)) {
				return ResponseUtils.jsonView(263,"返点数设置错误,范围为0~"+proxy_parent.getRebateRatio());
			}
		}else {
			if(proxy.getRebateRatio()!=null&&(proxy.getRebateRatio().compareTo(new BigDecimal("12"))>0||proxy.getRebateRatio().compareTo(new BigDecimal(0))<0)) {
				return ResponseUtils.jsonView(263,"返点数设置错误,范围为0~12");
			}
		}
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		proxy.setUpdataUser(user.getUserName());
		proxy.setUpdataTime(new Date());
		//对密码进行加密
		if(!StringUtils.isEmpty(proxy.getPassword())) {
			BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
			String encodePassword = encoder.encode(proxy.getPassword());
			proxy.setPassword(encodePassword);
		}
		if(!StringUtils.isEmpty(proxy.getCoinPssword())) {
			BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
			String encodePassword = encoder.encode(proxy.getCoinPssword());
			proxy.setCoinPssword(encodePassword);
		}
		int result= service.updateByProxyInfo(proxy);
		return result>0?ResponseUtils.jsonView(200,"更新代理成功"):ResponseUtils.jsonView(261,"更新代理失败");
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/updateByStatus")
	public ModelAndView updateByStatus(HttpServletRequest request) {
		ProxyInfo proxy = this.jsonToBean(request, ProxyInfo.class);
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		proxy.setUpdataUser(user.getUserName());
		proxy.setUpdataTime(new Date());
		int result= service.updateByStatus(proxy);
		return result>0?ResponseUtils.jsonView(200,"启用代理成功"):ResponseUtils.jsonView(261,"启用代理失败");
	}
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/checkAccount")
	public ModelAndView checkAccount(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		int result= service.checkAccount(map);
		return result>0?ResponseUtils.jsonView(211,"代理账号存在"):ResponseUtils.jsonView(200,"代理账号可使用");
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/insertProxyInfo")
	public ModelAndView insertProxyInfo(HttpServletRequest request) {
		ProxyInfo proxy = this.jsonToBean(request, ProxyInfo.class);
		if(proxy.getPid()!=null) {
			ProxyInfo proxy_parent=service.getProxyById(proxy.getPid());
			if(proxy.getRebateRatio()!=null&&proxy_parent.getRebateRatio().compareTo(proxy.getRebateRatio())<=0) {
				return ResponseUtils.jsonView(263,"返点数设置错误,其值不能高于父级代理"+proxy_parent.getRebateRatio());
			}
		}else {
			if(proxy.getRebateRatio()!=null&&proxy.getRebateRatio().compareTo(new BigDecimal("12"))>0) {
				return ResponseUtils.jsonView(263,"返点数设置错误,其值不能高于12");
			}
		}
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		proxy.setCreatedUser(user.getUserName());
		proxy.setCreateTime(new Date());
		proxy.setRegIp(request.getRemoteAddr());
		//对密码进行加密
		if(!StringUtils.isEmpty(proxy.getPassword())) {
			BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
			String encodePassword = encoder.encode(proxy.getPassword());
			proxy.setPassword(encodePassword);
		}
		if(!StringUtils.isEmpty(proxy.getCoinPssword())) {
			BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
			String encodePassword = encoder.encode(proxy.getCoinPssword());
			proxy.setCoinPssword(encodePassword);
		}
		int result= service.insertProxyInfo(proxy);
		return result>0?ResponseUtils.jsonView(200,"新增代理成功"):ResponseUtils.jsonView(261,"新增代理失败");
	}
	
	//根据 account：登陆账号，proxyName：代理姓名查询代理数据信息
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/selectByProxyInfo")
	public ModelAndView selectByProxyInfo(HttpServletRequest request) {
		
		Map<String, Object> paramMap = this.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(paramMap.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(paramMap.get("pageNum").toString());
		List<UserVipModel> proxyInfoList=service.selectByProxyInfo(paramMap);
		//3.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<UserVipModel> pageInfo =  new PageUtils<>(pageIndex,pageNum,proxyInfoList);
		return ResponseUtils.jsonView(pageInfo);
	}
	//获取所有代理信息
	@RequestMapping(method=RequestMethod.GET,value="/selectProxyInfoAll")
	public ModelAndView selectProxyInfoAll(HttpServletRequest request) {
		List<Map<String,Object>> proxyInfoList=service.selectProxyInfoAll();
		return ResponseUtils.jsonView(proxyInfoList);
	}
	@RequestMapping(value = "/updateProxyPassword", method = RequestMethod.POST)
	public ModelAndView updatePassword(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		map.put("updataUser", sysUser.getUserName());
		boolean message=service.updatePassword(map);
		return (message==true)?ResponseUtils.jsonView(200,"密码更新成功"):ResponseUtils.jsonView(210,"密码更新失败");
		
	}
	/*
	 * 【完善个人信息】-查询需要完善的信息列
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value="/querySysRegisterOptionByTypeId", method=RequestMethod.GET)
	public  ModelAndView querySysRegisterOptionByTypeId(HttpServletRequest request,HttpServletResponse response) {	
		Map<String, Object> map = this.jsonToMap(request);
		Integer register_type = Integer.valueOf(map.get("register_type").toString());
		List<SysRegisterOptionPojo> pojoList = sysRegisterOptionService.querySysRegisterOptionByTypeId(register_type);
		return pojoList != null ? ResponseUtils.jsonView(200, "成功", pojoList):ResponseUtils.jsonView(133, "查询结果为空", pojoList);
	}
	/*
	 * 【完善个人信息】唯一性校验
	 */
	@RequestMapping(value="/isOnlyValidate", method=RequestMethod.GET)
	public ModelAndView isOnlyValidate(HttpServletRequest request,HttpServletResponse response) {		
		if(validateString(request.getParameter("type")) && validateString(request.getParameter("value"))) {
			String  type = request.getParameter("type");
			String  value = request.getParameter("value");				
			return ResponseUtils.jsonView(200, "校验成功", sysRegisterOptionService.isOnlyValidate(type, value));
		}else {
			return ResponseUtils.jsonView(200, "校验成功", null);
		}		
	}
	
	/*
	 * String判空
	 */	
	public static boolean validateString(String param) {
		if(!param.isEmpty() && param != null){
			return true;
		}else {
			return false;
		}
	}
}
	

	

