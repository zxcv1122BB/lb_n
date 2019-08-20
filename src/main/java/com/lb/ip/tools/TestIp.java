package com.lb.ip.tools;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.ip.service.IpWhiteListService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.ResponseUtils;

@RestController
@RequestMapping("/testIp")
public class TestIp extends BaseController{
	@Autowired
	private IpWhiteListService ipWhiteListService;
	
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/testIp")
	public ModelAndView testIp(HttpServletRequest request) {
		//获取到当前登录的ip
		String ipAddr = request.getRemoteAddr();// 请求的IP
		//首先判断是否为格式合法的Ip地址
		IpWhiteListTools ipWhiteListTools = new IpWhiteListTools();
		Integer yorN = ipWhiteListService.selectIpYorN(ipAddr);
		//判断数据库白名单中是否有该Ip的存在
		if(ipWhiteListTools.red(ipAddr) && (yorN>0)) {
			return ResponseUtils.jsonView(101, "true");
		}else {
			return ResponseUtils.jsonView(101, "false");
		}
	}
}
