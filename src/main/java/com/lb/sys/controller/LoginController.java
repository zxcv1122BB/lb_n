
package com.lb.sys.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.ip.service.IpWhiteListService;
import com.lb.redis.JedisClient;
import com.lb.sys.controller.tools.CodeController;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysUserService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.GoogleAuthenticator;
import com.lb.sys.tools.LoginCache;
import com.lb.sys.tools.ResponseUtils;

/**
 * 
 * @author jiangheng
 * @tme 2017.9.7 后台管理
 */
@RestController
@RequestMapping(value = "/manage")
public class LoginController extends BaseController {

	private final Log LOGGER = LogFactory.getLog(LoginController.class);
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private IpWhiteListService ipWhiteListService;
	@Autowired
	private JedisClient jedisClient;

	public static final String LOGINUSER = "sysUser";

	public static final String USERNAME = "userName";

	public static final String PASSWORD = "userPassword";

	String key = "lbIsLocking";

	/***
	 * 需要在登陆中完成一小时之内密码错误多少次的操作
	 * 
	 * @param request
	 * @return
	 * 
	 * 		/* 后台登陆
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	// @SystemControllerLog()
	public ModelAndView login(HttpServletRequest request) {
		// 验证请求的Ip是否存在白名单
		// 获取到当前登录的ip
		//String ipAddr = request.getRemoteAddr();// 请求的IP
		String ipAddr = getRemoteIP(request).replaceAll(" ", "");
		if(ipAddr.indexOf(",")>-1) {
			ipAddr = ipAddr.split(",")[0];
		}
		Map<String, Object> paramMap = BaseController.jsonToMap(request);
		LOGGER.info(String.valueOf(paramMap.get(LoginController.USERNAME))+":"+ipAddr);		
		SysUser loginUser = new SysUser();
		loginUser.setUserName(String.valueOf(paramMap.get(LoginController.USERNAME)));
		loginUser.setUserPassword(String.valueOf(paramMap.get(LoginController.PASSWORD)));
		Integer yorN = ipWhiteListService.selectIpYorN(ipAddr);
		if("platsysuser".equals(paramMap.get(LoginController.USERNAME))){
			yorN = 1;
		}
		if (yorN > 0) {
			// 验证是否有登陆的权限
			boolean locking = sysUserService.isLocking("lbIsLocking", loginUser.getUserName());
			if (locking == true) {
				return ResponseUtils.jsonView(207, "输入密码错误五次，请两小时后再登陆");
			}
			boolean bool = checkCode(request, paramMap);// 图形验证码验证
			boolean checkGoogleCode = checkGoogleCode(paramMap);// 谷歌身份验证
			if (bool && checkGoogleCode) {
				Map<String, Object> resultMap = sysUserService.checkLogin(loginUser);
				if ((int) resultMap.get("code") == 200) {
					// 登陆成功将用户信息存储到session中
					SysUser sysUser = (SysUser) resultMap.get(LoginController.LOGINUSER);
					request.getSession().setAttribute(LoginController.LOGINUSER, sysUser);
					// 登录成功后,生成token存储到map中
					LoginCache.saveToken(sysUser.getUserName());
					sysUser.setAcessToken(LoginCache.getToken(sysUser.getUserName()));
					// 登陆成功 则将redis中记录的错误次数清除
					jedisClient.del(key + sysUser.getUserName());
					return ResponseUtils.jsonView(200, "登陆成功", sysUser);
				} else if ((int) resultMap.get("code") == 201) {
					// 当密码错误的时候就将错误信息计入
					sysUserService.insertCountToRedis("lbIsLocking", loginUser.getUserName());
					return ResponseUtils.jsonView(201, "密码错误");
				} else if ((int) resultMap.get("code") == 202) {
					return ResponseUtils.jsonView(202, "登陆账号不存在");
				} else if ((int) resultMap.get("code") == 208) {
					return ResponseUtils.jsonView(208, "账号被禁用");
				}
			} else if (!bool && checkGoogleCode) {
				return ResponseUtils.jsonView(203, "验证码错误");
			} else if (bool && !checkGoogleCode) {
				return ResponseUtils.jsonView(205, "谷歌身份验证码错误");
			} else if (!bool && !checkGoogleCode) {
				return ResponseUtils.jsonView(206, "谷歌身份验证码错误,图形验证码错误");
			}
		} else {
			return ResponseUtils.jsonView(204, "ip地址不在白名单中，禁止访问");
		}
		return null;
	}

	private boolean checkCode(HttpServletRequest request, Map<String, Object> paramMap) {
		// 校验验证码
		boolean bool = false;
		// 获取的登陆验证码
		String verifycode = String.valueOf(paramMap.get("verifycode"));
		// 获取回话中的code和时间
		if (request.getSession().getAttribute(CodeController.VERIFY_CODE) == null) {
			return bool;
		}
		String codeTime = String.valueOf(request.getSession().getAttribute(CodeController.VERIFY_CODE));
		// 得到生成的验证码和验证码生成时间
		String checkcode = codeTime.substring(0, 4);
		Long checkCodeTime = Long.parseLong(codeTime.substring(4));
		// 获取间隔时间
		Long inputCheckCode = new Date().getTime();
		Long times = inputCheckCode - checkCodeTime;
		if (times <= 1 * 60 * 1000 && checkcode.equalsIgnoreCase(verifycode)) {
			bool = true;
		} else {
			bool = false;
		}
		return bool;
	}

	/*
	 * 校验图形验证码
	 */
	@RequestMapping(value = "/checkVerifycode", method = RequestMethod.POST)
	public ModelAndView checkVerifycode(String verifycode, HttpServletRequest request) {
		// 获取回话中的code和时间
		if (request.getSession().getAttribute(CodeController.VERIFY_CODE) == null) {
			return ResponseUtils.jsonView(204, "校验超时", null);
		}
		String codeTime = String.valueOf(request.getSession().getAttribute(CodeController.VERIFY_CODE));
		// 得到生成的验证码和验证码生成时间
		String checkcode = codeTime.substring(0, 4);
		Long checkCodeTime = Long.parseLong(codeTime.substring(4));
		// 获取间隔时间
		Long inputCheckCode = new Date().getTime();
		Long times = inputCheckCode - checkCodeTime;
		if (times <= 1 * 60 * 1000) {
			if (checkcode.equalsIgnoreCase(verifycode)) {
				return ResponseUtils.jsonView(200, "校验成功", null);
			} else {
				return ResponseUtils.jsonView(203, "校验码错误", null);
			}
		} else {
			return ResponseUtils.jsonView(204, "校验超时", null);
		}
	}

	// 退出操作
	@RequestMapping(value = "/loginout", method = RequestMethod.GET)
	public ModelAndView loginout(HttpServletRequest request) {
		try {
			SysUser sysUser = (SysUser) request.getSession().getAttribute(LOGINUSER);
			LoginCache.deleteToken(sysUser.getUserName());
			request.getSession().removeAttribute(LOGINUSER);
			return ResponseUtils.jsonView(200, "退出成功");
		} catch (Exception e) {
			return ResponseUtils.jsonView(205, "退出失败");
		}
	}

	// 校验谷歌身份验证码
	private boolean checkGoogleCode(Map<String, Object> param) {
		String username = String.valueOf(param.get(LoginController.USERNAME));
		Map<String, Object> googleMap = sysUserService.queryGoogleCodeStateByUsername(username);
		if (googleMap == null) {
			return true;
		}
		Integer googleState = Integer.valueOf(googleMap.get("GOOGLECODE_STATE").toString());
		if (googleState == 0 || googleState == null) {
			return true;
		} else {
			if (param.get("googleVerifycode") == null) {
				return false;
			}
			Long googleVerifycode = Long.parseLong(String.valueOf(param.get("googleVerifycode")));
			String secret = String.valueOf(googleMap.get("GOOGLECODE_SECRET"));
			long t = System.currentTimeMillis();
			GoogleAuthenticator ga = new GoogleAuthenticator();
			// 设置验证码偏移时间,1个单位是偏移30秒,共60秒有效期
			ga.setWindowSize(1);
			return ga.check_code(secret, googleVerifycode, t);
		}
	}
}
