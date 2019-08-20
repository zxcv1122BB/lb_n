package com.lb.betting.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.controller.LoginController;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.ResponseUtils;
import com.lsopen.open.OpenTools;

/**
 * 
 * @author ASUS 手工开奖信息处理 * 远程访问lsschedule项目调用/OpenManage/Open接口
 */
@RestController
@RequestMapping("/call")
public class CallOpenManageController extends BaseController {

	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.GET, value = "/openmanage")
	public ModelAndView openManage(HttpServletRequest request) {
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getRoleId()!= 1) {
			return ResponseUtils.jsonView(333, "您的账号不是超级管理员，不能进行此操作");
		}
		// 将ajax传来的参数进行转换
		Map<String, Object> map = this.jsonToMap(request);
		// 从上面的map中取得订单号
		String bet_id = map.get("id").toString();
		// 这是要转去别的项目的接口是传过去的参数
		//HashMap<String, String> passOpenParam = new HashMap<>();
		// xml里面配置的url
		//String path = GetPropertiesValue.getValue("URL","openManagerUrl");
		//HttpClientUtil httpClientUtil = new HttpClientUtil();
		boolean judge = (boolean) map.get("judge");
		String result = null;
		// 做判断,如果是手动开奖就是true,手动添加开奖任务就是false
		if (judge) {
			result = OpenTools.dealOpen("BetRecord",bet_id);
		} else {
			result = OpenTools.dealOpen("","");
		}
		if(result == null) {
			return ResponseUtils.jsonView(202, "处理失败");
		}else if(result.indexOf("success")>-1)
		{
			return ResponseUtils.jsonView(200, "处理成功");
		}
		else if("HasNotEndMatch".equals(result)) 
		{
			return ResponseUtils.jsonView(200, "当前投注记录存在未结束的比赛，不能进行开奖处理");
		}
		else if("NoBetRecord".equals(result)) 
		{
			return ResponseUtils.jsonView(200, "不存在未开奖或可开奖处理的投注记录");
		}else if(result.indexOf("error")>-1) 
		{
			return ResponseUtils.jsonView(202, "处理异常");
		}
		return ResponseUtils.jsonView(200, "处理成功");
	}
}
