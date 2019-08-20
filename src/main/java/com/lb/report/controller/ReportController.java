package com.lb.report.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.member.service.ICoinLogService;
import com.lb.report.model.GameBetsPOJO;
import com.lb.report.model.GlobalCount;
import com.lb.report.model.TeamCount;
import com.lb.report.model.UserCount;
import com.lb.report.service.GameBetsService;
import com.lb.report.service.ProxyDistributionService;
import com.lb.report.service.ReportMonthSummaryService;
import com.lb.sys.controller.LoginController;
import com.lb.sys.dao.UserModelMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.StringUtil;
import com.lb.sys.tools.model.Message;

/**
 * 报表管理
 * 
 * @author ASUS
 *
 */
@RestController
@RequestMapping("/reportManage")
public class ReportController extends BaseController {

	@Autowired
	private GameBetsService gameBetsService;

	@Autowired
	private ICoinLogService coinLogService;

	@Autowired
	private ReportMonthSummaryService reportMonthSummaryService;

	@Autowired
	private ProxyDistributionService proxyDistributionService;

	
	@Autowired
	private UserModelMapper userModelMapper;
	
	/***
	 * 查询会员概况信息
	 * @param request
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/queryUserDataSurvey")
	public ModelAndView queryUserDataSurvey(HttpServletRequest request,HttpServletResponse response) {
		try {
			//获取请求参数
			Map<String, Object> jsonToMap = this.jsonToMap(request);
			//调用查询方法
			UserCount ls = userModelMapper.selectUserDate(jsonToMap);
			UserCount his = userModelMapper.selectLsHistoryUserDate(jsonToMap);
			if(his!=null) {
				ls.setDepositSum(ls.getDepositSum().add(his.getDepositSum()));
				ls.setDepositRgSum(ls.getDepositRgSum().add(his.getDepositRgSum()));
				ls.setfCSumCoin(ls.getfCSumCoin().add(his.getfCSumCoin()));
				ls.setHisFSumCoin(ls.getHisFSumCoin().add(his.getHisFSumCoin()));
				ls.setfBSumCoin(ls.getfBSumCoin().add(his.getfBSumCoin()));
				ls.setbCSumCoin(ls.getbCSumCoin().add(his.getbCSumCoin()));
				ls.setnSumCoin(ls.getnSumCoin().add(his.getnSumCoin()));
				ls.setqSumCoin(ls.getqSumCoin().add(his.getqSumCoin()));
				ls.setgSumCoin(ls.getgSumCoin().add(his.getgSumCoin()));
				ls.setWithdrawSum(ls.getWithdrawSum().add(his.getWithdrawSum()));
				ls.setRegGivingSum(ls.getRegGivingSum().add(his.getRegGivingSum()));
				ls.setDepositGivingSum(ls.getDepositGivingSum().add(his.getDepositGivingSum()));
				ls.setAddMoneySum(ls.getAddMoneySum().add(his.getAddMoneySum()));
				ls.setSubtractMoneySum(ls.getSubtractMoneySum().add(his.getSubtractMoneySum()));
				ls.setRgGivingMoneySumCoin(ls.getRgGivingMoneySumCoin().add(his.getRgGivingMoneySumCoin()));
				ls.setActivityBonusCoin(ls.getActivityBonusCoin().add(his.getActivityBonusCoin()));
				ls.setProxyAddMoneySumCoin(ls.getProxyAddMoneySumCoin().add(his.getProxyAddMoneySumCoin()));
				ls.setProxySubtractMoneySumCoin(ls.getProxySubtractMoneySumCoin().add(his.getProxySubtractMoneySumCoin()));
				ls.setBetRebate(ls.getBetRebate().add(his.getBetRebate()));
				//最后充值记录
				if(StringUtil.isBlank(ls.getLastDepositAwamount()) || StringUtil.isBlank(ls.getLastDepositTime())
						|| StringUtil.isBlank(ls.getLastDepositInfo())) {
					ls.setLastDepositAwamount(his.getLastDepositAwamount());
					ls.setLastDepositTime(his.getLastDepositTime());
					ls.setLastDepositInfo(his.getLastDepositInfo());
				}
				//最后提现记录
				if(StringUtil.isBlank(ls.getLastWithdrwAwamount()) || StringUtil.isBlank(ls.getLastWithdrawTime())
						|| StringUtil.isBlank(ls.getLastWithdrawInfo())) {
					ls.setLastWithdrwAwamount(his.getLastWithdrwAwamount());
					ls.setLastWithdrawTime(his.getLastWithdrawTime());
					ls.setLastWithdrawInfo(his.getLastWithdrawInfo());
				}
				
			}
			return ResponseUtils.jsonView(200,"请求成功",ls);
		} catch (Exception e) {
			return ResponseUtils.jsonView(201,"请求失败",null);
		}
	}
	
	//根据会员名称搜索会员是否存在
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/selectUserCountByUserName")
	public ModelAndView selectUserCountByUserName(HttpServletRequest request,HttpServletResponse response) {
		//获取请求参数
		Map<String, Object> jsonToMap = this.jsonToMap(request);
		//调用查询方法
	     int selectUserCountByUserName = userModelMapper.selectUserCountByUserName(jsonToMap);
		return ResponseUtils.jsonView(200,"请求成功",selectUserCountByUserName);
	}

	/**
	 * 统计概况-查询今日、昨日中奖金额、投注金额以及盈亏(投注金额-中奖金额)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/queryTodayAndYesterDayMoney")
	public ModelAndView queryTodayAndYesterDayMoney(HttpServletRequest request) {
		GameBetsPOJO gameBets = gameBetsService.queryTodayAndYesterDayMoney();
		if (gameBets == null) {
			return ResponseUtils.jsonView(201, "暂无今昨日盈亏数据");
		}
		// 今日中奖
		BigDecimal toDayBouns = new BigDecimal(gameBets.getToDayBouns()!=null?gameBets.getToDayBouns():0.00);
		gameBets.setToDayBouns(toDayBouns.doubleValue());
		// 今日盈亏===取绝对值
		BigDecimal toDayLoss = new BigDecimal(gameBets.getToDayLoss()!=null?gameBets.getToDayLoss():0.00).abs();
		gameBets.setToDayLoss(toDayLoss.doubleValue());
		// 今日中奖+盈亏
		BigDecimal toDaySumamount = new BigDecimal(0);
		toDaySumamount = toDayLoss.add(toDayBouns);
		// 昨日中奖
		BigDecimal yesterDayBouns = new BigDecimal(gameBets.getYesterDayBouns()!=null?gameBets.getYesterDayBouns():0.00);
		gameBets.setYesterDayBouns(yesterDayBouns.doubleValue());
		// 昨日盈亏==取绝对值
		BigDecimal yesterDayLoss = new BigDecimal(gameBets.getYesterDayLoss()!=null?gameBets.getYesterDayLoss():0.00).abs();
		gameBets.setYesterDayLoss(yesterDayLoss.doubleValue());
		// 昨日中奖+盈亏
		BigDecimal yesterDaySumamount = new BigDecimal(0);
		yesterDaySumamount = yesterDayLoss.add(yesterDayBouns);
		// 今日中奖百分比、盈亏百分比
		if (toDaySumamount == null || toDaySumamount.compareTo(new BigDecimal(0)) != 0) {
			gameBets.setToDayBounsPercent(toDayBouns.divide(toDaySumamount, 2, BigDecimal.ROUND_HALF_UP)
					.multiply(new BigDecimal(100)).doubleValue());
			gameBets.setToDayLossPercent(toDayLoss.divide(toDaySumamount, 2, BigDecimal.ROUND_HALF_UP)
					.multiply(new BigDecimal(100)).doubleValue());
		} else {
			gameBets.setToDayBounsPercent(0.00);
			gameBets.setToDayBounsPercent(0.00);
		}
		// 昨日中奖百分比、盈亏百分比
		if (yesterDaySumamount == null || yesterDaySumamount.compareTo(new BigDecimal(0)) != 0) {
			gameBets.setYesterDayBounsPercent(yesterDayBouns.divide(yesterDaySumamount, 2, BigDecimal.ROUND_HALF_UP)
					.multiply(new BigDecimal(100)).doubleValue());
			gameBets.setYesterDayLossPercent(yesterDayLoss.divide(yesterDaySumamount, 2, BigDecimal.ROUND_HALF_UP)
					.multiply(new BigDecimal(100)).doubleValue());
		} else {
			gameBets.setYesterDayBounsPercent(0.00);
			gameBets.setYesterDayLossPercent(0.00);
		}
		return gameBets==null ?
				ResponseUtils.jsonView(201,"暂无数据")
				:ResponseUtils.jsonView(200,"请求成功",gameBets);
	}

	/**
	 * 统计概况-分页查询每月各项统计记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/queryReportMonthSummaryPage")
	public ModelAndView queryReportMonthSummaryPage(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.parseInt(map.get("pageIndex").toString()) ;
		// 每一页数据条数
		Integer pageNum = Integer.parseInt(map.get("pageNum").toString());
		// 2.后面执行的查询就是一个分页查询语句
		List<Map<String, Object>> list = reportMonthSummaryService.queryReportMonthSummaryList();
		// 3.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return pageInfo == null ?
				ResponseUtils.jsonView(201,"暂无数据成功")
				:ResponseUtils.jsonView(200,"请求成功",pageInfo);
	}

	/**
	 * 财务报表-分页查询各用户各项数据的统计信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/queryFinanceListPage")
	public ModelAndView queryFinanceListPage(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = map.get("pageIndex")!=null?Integer.valueOf(map.get("pageIndex").toString()):1;
		// 每一页数据条数
		Integer pageNum = map.get("pageNum")!=null?Integer.valueOf(map.get("pageNum").toString()):50;
		List<TeamCount> list =coinLogService.queryFinanceList(map);
		// 3.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<TeamCount> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return pageInfo == null ?
				ResponseUtils.jsonView(201,"暂无数据成功")
				:ResponseUtils.jsonView(200,"请求成功",pageInfo);
	}

	/**
	 * 代理商分销
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveProxyDistrition")
	public ModelAndView save(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message msg = proxyDistributionService.insertProxyDistribution(map);
		return ResponseUtils.jsonView(msg.getCode(), msg.getMsg(), null);
	}

	/**
	 * 根据相应的条件查询代理商返点记录信息 进行代理商返点或返利
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/editProxyDistributionCoin")
	public ModelAndView editProxyDistributionCoin(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		map.put("status", "0");
		Message msg = proxyDistributionService.rebateProxyDistribution(map);
		return ResponseUtils.jsonView(msg.getCode(), msg.getMsg(), null);
	}

	/**
	 * 彩票分析
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getLotteryList")
	public ModelAndView getLotteryList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.parseInt(map.get("pageIndex").toString()) ;
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString()) ; 
		if (map.get("agentCount") != null&&!"".equals(map.get("agentCount"))) {
			int count = proxyDistributionService.isExsitAgency(map.get("agentCount").toString());
			if (count <= 0) {
				return ResponseUtils.jsonView(201, "不存在此代理");
			}
		}
		// 2.后面执行的查询就是一个分页查询语句
		List<Map<String, Object>> list = gameBetsService.getLotteryList(map);
		// 3.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		Map<String, Object> gameBetMap=gameBetsService.getLotteryListTotal(map);
		map.clear();
		map.put("pageInfo", pageInfo);
		map.put("gameBetMap", gameBetMap);
		return ResponseUtils.jsonView(map);
	}

	/**
	 * 多级代理返点记录表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/agentRebateListList")
	public ModelAndView agentRebateListList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString()) ;
		// 每一页数据条数
		Integer pageNum = Integer.valueOf( map.get("pageNum").toString()) ;
		// 1.引入分页插件
		// 2.后面执行的查询就是一个分页查询语句
		List<Map<String, Object>> list = proxyDistributionService.getAgentRebateList(map);
		// 3.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return pageInfo == null ?
				ResponseUtils.jsonView(201,"暂无数据成功")
				:ResponseUtils.jsonView(200,"请求成功",pageInfo);
	}

	/**
	 * 多级代理回滚
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rollBack")
	public ModelAndView rollBack(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		map.put("userName", user.getUserName());// 操作人
		map.put("operaterIp", request.getRemoteAddr());// 操作ip
		map.put("operateUid", user.getUserId());
		Integer result = proxyDistributionService.rollBack(map);
		// 3.创建PageInfo封装分页信息,最多显示pageSize页
		return result >= 0 ? ResponseUtils.jsonView(200, "回滚成功") : ResponseUtils.jsonView(501, "回滚失败");
	}

	/**
	 * 查询统计概况
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/queryCountGK")
	public ModelAndView queryCountGK(HttpServletRequest request) {
		Map<String, Object> map = proxyDistributionService.queryCountGK();
		return ResponseUtils.jsonView(200, "操作成功", map);
	}

	/**
	 * 团队帐变管理
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/teamCount")
	public ModelAndView teamCount(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = map.get("pageIndex")!=null?Integer.valueOf(map.get("pageIndex")+""):1;
		// 每一页数据条数
		Integer pageNum = map.get("pageNum")!=null?Integer.valueOf(map.get("pageNum")+""):10;
		List<Map<String, Object>> list =  proxyDistributionService.teamCount(map);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		map.clear();
		map.put("pageInfo", pageInfo);
		return ResponseUtils.jsonView(200, "操作成功", map);
	}
	
	@RequestMapping(value = "/globalCount", method = RequestMethod.GET)
	public ModelAndView globalCount(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		GlobalCount message=proxyDistributionService.globalCount(map);		
		return ResponseUtils.jsonView(200,"查询成功",message);
	}
	//首页今日
	@RequestMapping(value = "/getHomeTodayReports", method = RequestMethod.GET)
	public ModelAndView getHomeTodayReports(HttpServletRequest request) {
		Map<String,Object> resultMap = proxyDistributionService.queryHomeTodayReports();
		return ResponseUtils.jsonView(200,"操作成功",resultMap);
	}
	
	@RequestMapping(value = "/qryGamebetsBetSum", method = RequestMethod.GET)
	public ModelAndView qryGamebetsBetSum(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(!map.containsKey("uid")) {
			return ResponseUtils.jsonView(201, "参数错误");
		}
		// 当前页
		Integer pageIndex = map.get("pageIndex")!=null?Integer.valueOf(map.get("pageIndex")+""):1;
		// 每一页数据条数
		Integer pageNum = map.get("pageNum")!=null?Integer.valueOf(map.get("pageNum")+""):10;
		List<Map<String, Object>> list =  gameBetsService.qryGamebetsBetSum(Integer.valueOf(map.get("uid")+""));
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);	
		return ResponseUtils.jsonView(200,"查询成功",pageInfo);
	}
}
