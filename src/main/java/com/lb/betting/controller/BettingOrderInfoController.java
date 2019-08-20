/**
 * 
 */
package com.lb.betting.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.betting.model.BettingOrderDetail;
import com.lb.betting.model.BettingOrderInfo;
import com.lb.betting.model.BettingTicketDetails;
import com.lb.betting.model.GameType;
import com.lb.betting.model.UserDetail;
import com.lb.betting.service.BettingOrderInfoService;
import com.lb.sys.controller.LoginController;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.StringUtil;

/**
 * @describe 
 */
@RestController
@RequestMapping("/bets")
public class BettingOrderInfoController extends BaseController{

	@Autowired
	private BettingOrderInfoService bettingOrderInfoService;
	
	private final static Log LOGGER = LogFactory.getLog(BettingOrderInfoController.class);
	
	/**
	 * 获取投注信息列表
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/queryBettingOrderList")
	public ModelAndView queryBettingOrderList(HttpServletRequest request) {
		/**
		 * 请求参数定义
		 * pageIndex（当前页） pageNum（每一页数据条数）pageSize（显示的页数）
		 * startTime endTime orderId(訂單id) username（用户） gameType（彩种）
		 * lowerAmount(起始金额) higherAmount(最终金额) 
		 * lowerBonus(起始奖金) higherBonus(最终奖金)
		 *  status（状态  0未中奖 1中奖 2撤单 3成功）
		 * */
		Map<String, Object> map = this.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//1.后面执行的查询就是一个分页查询语句
		List<BettingOrderInfo> list=bettingOrderInfoService.queryBettingOrderInfo(map);
		//2.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<BettingOrderInfo> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	/**
	 * 开奖失败的列表
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/queryFailOrd"
			+ "erInfoList")
	public ModelAndView queryFailOrderInfo(HttpServletRequest request) {
		/**
		 * 请求参数定义
		 * pageIndex（当前页） pageNum（每一页数据条数）pageSize（显示的页数）
		 * startTime endTime orderId(訂單id) username（用户） gameType（彩种）
		 * lowerAmount(起始金额) higherAmount(最终金额) 
		 * lowerBonus(起始奖金) higherBonus(最终奖金)
		 *  status（状态  0未中奖 1中奖 2撤单 3成功）
		 * */
		Map<String, Object> map = this.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//1.后面执行的查询就是一个分页查询语句
		List<BettingOrderInfo> list=bettingOrderInfoService.queryFailOrderInfo(map);
		//2.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<BettingOrderInfo> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	/**
	 * 获取彩种列表
	 * */
	@RequestMapping(method=RequestMethod.GET,value="/queryGameType")
	public ModelAndView queryGameType(HttpServletRequest request) {
		List<GameType> queryGameType = bettingOrderInfoService.queryGameType();
		return ResponseUtils.jsonView(queryGameType);
	}
	
	/**
	 * 查看用户详情
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/queryUserDetail")
	public ModelAndView queryUserDetail(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		String username = String.valueOf(map.get("username"));
		if(StringUtils.isBlank(username)) {
			return ResponseUtils.jsonView(555, "请正确输入用户名");
		}
		UserDetail userDetail = bettingOrderInfoService.queryUserInfo(username);
		return ResponseUtils.jsonView(userDetail);
	}
	
	/**
	 * 查看订单详情
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/queryBettingOrderDetail")
	public ModelAndView queryBettingOrderDetail(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		if(map.get("betId") == null) {
			return ResponseUtils.jsonView(333, "参数异常");
		}
		BettingOrderDetail bettingOrderDetail = bettingOrderInfoService.queryBettingOrder(map);
		if(bettingOrderDetail == null) {
			return ResponseUtils.jsonView(555,"未获取到数据");
		}
		return ResponseUtils.jsonView(200,"获取成功",bettingOrderDetail);
	}
	/**
	 * 根据数字彩订单详情
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/getNumbersLotteryDetails")
	public ModelAndView getNumbersLotteryDetails(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map= BaseController.jsonToMap(request);
		
		if(map == null|| map.get("betId") == null) {
			return ResponseUtils.jsonView(333,"参数错误");
		}
		
		Map<String,Object> resultMap = bettingOrderInfoService.queryNumbersBettingDetail(map);
		
		return resultMap != null ? ResponseUtils.jsonView(200,"获取数据成功",resultMap) : ResponseUtils.jsonView(201,"无数据显示",resultMap);
	}
	/**
	 * 管理员给用户撤单操作
	 * 1.查询是否开奖 如已开奖不允许撤单
	 * 2.撤单操作需求的金额以及修改为撤单状态
	 * 3.在资金记录表中增加一条资金操作记录
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/cancelTheOrder")
	public ModelAndView cancelTheOrder(HttpServletRequest request)
	{
		Map<String, Object> map = this.jsonToMap(request);
		if(map.get("betId") == null) {
			return ResponseUtils.jsonView(333, "数据异常");
		}
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getRoleId() != 1) {
			return ResponseUtils.jsonView(333, "未登录超级管理员账号");
		}
		map.put("uid", user.getUserId());
		map.put("username", user.getUserName());
		int result = bettingOrderInfoService.cancelTheOrder(map);;
		return result>0?ResponseUtils.jsonView(200,"撤单成功"):ResponseUtils.jsonView(555,"撤单失败");
	}
	
	/**
	 * 对已开奖的投注数据进行回滚操作
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/prizeRollbackOperation")
	public ModelAndView prizeRollbackOperation(HttpServletRequest request)
	{
		Map<String, Object> map = this.jsonToMap(request);
		if(map == null || map.size() <= 0) {
			return ResponseUtils.jsonView(333, "数据异常");
		}
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getUserId() == null) {
			return ResponseUtils.jsonView(333, "未登录管理员账号");
		}
		map.put("sysId", user.getUserId());
		map.put("sysName", user.getUserName());
		int result = 0;
		try {
			result = bettingOrderInfoService.prizeRollbackOperation(map);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return result>0?ResponseUtils.jsonView(200,"操作成功"):(result == -1?ResponseUtils.jsonView(201,"无操作数据"):ResponseUtils.jsonView(555,"操作失败"));
	}
	/**
	 * 对某条投注记录进行手动开奖
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/prizeOperationByBet")
	public ModelAndView prizeOperationByBet(HttpServletRequest request)
	{
		Map<String, Object> map = this.jsonToMap(request);
		if(map == null || StringUtil.isBlank(map.get("betId"))) {
			return ResponseUtils.jsonView(333, "数据异常");
		}
		int result = 0;
		try {
			result = bettingOrderInfoService.prizeOperationByBet(map);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		int code = 200;
		String msg = "操作成功";
		if(result == 0) {
			code = 555;
			msg = "操作失败";
		}else if(result == -1) {
			code = 333;
			msg = "开奖数据异常";
		}else if(result == -2) {
			code = 333;
			msg = "该期彩票还未开奖";
		}else if(result < -2) {
			code = 333;
			msg = "系统数据异常";
		}
		return ResponseUtils.jsonView(code,msg);
	}
	/**
	 * 对整个赛事进行开奖(体彩)
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/prizeOperation")
	public ModelAndView prizeOperation(HttpServletRequest request)
	{
		Map<String, Object> map = this.jsonToMap(request);
		if(map == null || StringUtil.isBlank(map.get("matchId"))) {
			return ResponseUtils.jsonView(333, "数据异常");
		}
		int result = bettingOrderInfoService.prizeOperation(map);
		return result>0?ResponseUtils.jsonView(200,"操作成功"):ResponseUtils.jsonView(555,"操作失败");
	}
	/**
	 * 对整个期号数字彩进行开奖
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/numberPrizeOperation")
	public ModelAndView numberPrizeOperation(HttpServletRequest request)
	{
		Map<String, Object> map = this.jsonToMap(request);
		
		if(map != null && (StringUtil.isBlank(map.get("actionIssue")) || StringUtil.isBlank(map.get("typeId")))) {
			return ResponseUtils.jsonView(333, "数据异常");
		}
		int result = bettingOrderInfoService.numberPrizeOperation(map);
		return result>0?ResponseUtils.jsonView(200,"操作成功"):ResponseUtils.jsonView(555,"操作失败");
	}
	/**
	 * 获取投注记录的中奖明细
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/getActionDataResult")
	public ModelAndView getActionDataResult(HttpServletRequest request)
	{
		Map<String, Object> map = this.jsonToMap(request);
		if(map == null || map.get("betId") == null) {
			return ResponseUtils.jsonView(333, "数据异常");
		}
		/*SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getUserId() == null) {
			return ResponseUtils.jsonView(333, "未登录管理员账号");
		}
		map.put("uid", user.getUserId());*/
		List<BettingTicketDetails> list = bettingOrderInfoService.getActionDataResult(map);
		return list!=null&&list.size()>0?ResponseUtils.jsonView(200,"获取成功",list):ResponseUtils.jsonView(201,"暂无数据");
	}
	
	/**
	 * 获取投注数据统计
	 * */
	@RequestMapping(method=RequestMethod.POST,value="/getRecentlyBetCensus")
	public ModelAndView getRecentlyBetCensus(HttpServletRequest request)
	{
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getUserId() == null) {
			return ResponseUtils.jsonView(333, "未登录管理员账号");
		}
		Map<String, Object> result = bettingOrderInfoService.getRecentlyBetCensus();
		return result!=null && result.size() > 0 
				? 
				ResponseUtils.jsonView(200,"获取成功",result)
				:
				ResponseUtils.jsonView(201,"暂无数据");
	}

	/**
	 * 获取充值数据统计
	 * */
	@RequestMapping(method=RequestMethod.POST,value="/getRecentlyDepositCensus")
	public ModelAndView getRecentlyDepositCensus(HttpServletRequest request)
	{
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getUserId() == null) {
			return ResponseUtils.jsonView(333, "未登录管理员账号");
		}
		Map<String, Object> result = bettingOrderInfoService.getRecentlyDepositCensus();
		return result!=null && result.size() > 0 
				? 
				ResponseUtils.jsonView(200,"获取成功",result)
				:
				ResponseUtils.jsonView(201,"暂无数据");
	}

	/**
	 * 获取提款数据统计
	 * */
	@RequestMapping(method=RequestMethod.POST,value="/getRecentlyWithdrawCensus")
	public ModelAndView getRecentlyWithdrawCensus(HttpServletRequest request)
	{
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getUserId() == null) {
			return ResponseUtils.jsonView(333, "未登录管理员账号");
		}
		Map<String, Object> result = bettingOrderInfoService.getRecentlyWithdrawCensus();
		return result!=null && result.size() > 0 
				? 
				ResponseUtils.jsonView(200,"获取成功",result)
				:
				ResponseUtils.jsonView(201,"暂无数据");
	}

	/**
	 * 获取账变记录统计
	 * */
	@RequestMapping(method=RequestMethod.POST,value="/getRecentlyCoinUpdateCensus")
	public ModelAndView getRecentlyCoinUpdateCensus(HttpServletRequest request)
	{
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getUserId() == null) {
			return ResponseUtils.jsonView(333, "未登录管理员账号");
		}
		Map<String, Object> result = bettingOrderInfoService.getRecentlyCoinUpdateCensus();
		return result!=null && result.size() > 0 
				? 
				ResponseUtils.jsonView(200,"获取成功",result)
				:
				ResponseUtils.jsonView(201,"暂无数据");
	}
}
