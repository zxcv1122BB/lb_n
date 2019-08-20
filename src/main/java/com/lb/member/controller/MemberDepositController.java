package com.lb.member.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.member.service.IMemberDepositService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.StringUtil;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/memberDeposit")
public class MemberDepositController extends BaseController{
	@Autowired
	private IMemberDepositService memberDepositService;
	/*充值列表*/
	@RequestMapping(method=RequestMethod.GET,value="/queryMemberDepositList")
	public ModelAndView queryMemberDepositList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		map.put("payType", StringUtil.isBlank(map.get("payType")) ? "":  String.valueOf(map.get("payType")));
		List<Map<String, Object>> list=memberDepositService.queryMemberDepositList(map);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	@RequestMapping(method=RequestMethod.POST,value="/depositIsLock")
	public ModelAndView depositIsLock(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message=memberDepositService.depositIsLock(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg());
	}
	/*充值信息查询*/
	@RequestMapping(method=RequestMethod.GET,value="/depositQuery")
	public ModelAndView depositQuery(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Map<String, Object> message=memberDepositService.depositQuery(map);
		return ResponseUtils.jsonView(message);
	}
	/*充值处理*/
	@RequestMapping(method=RequestMethod.POST,value="/depositHandle")
	public ModelAndView depositHandle(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message = null;
		try {
			message = memberDepositService.depositHandle(request,map);
		} catch (Exception e) {
			e.printStackTrace();
			message = new Message(555, "处理失败");
		}
		return ResponseUtils.jsonView(message.getCode(),message.getMsg());
	}
	/*充值用户信息查询*/
	@RequestMapping(method=RequestMethod.POST,value="/depositUserInfo")
	public ModelAndView depositUserInfo(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(map.get("userName")!=null) {
			Map<String, Object> userInfoMap=memberDepositService.depositUserInfo(map);
			return ResponseUtils.jsonView(200,"",userInfoMap);
		}
		return ResponseUtils.jsonView(233,"参数错误");
	}
}
