package com.lb.OpenLog.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.OpenLog.service.OpenLogService;
import com.lb.betting.model.BettingOrderInfo;
import com.lb.sys.dao.BettingOrderInfoMapper;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

/***
 * 开奖日志控制器层
 * 
 * @author ASUS
 */
@RestController
@RequestMapping("/openLog")
public class OpenLogController extends BaseController {
	@Autowired
	private OpenLogService openLogService;
	@Autowired
	private BettingOrderInfoMapper bettingOrderInfoMapper;

	// 查询所有开奖日志
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/getAllOpenLog")
	public ModelAndView getAllOpenLog(HttpServletRequest request) {
		// 获取参数
		Map<String, Object> map = this.jsonToMap(request);
		
		try {
			// 当前页
			Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
			// 每一页数据条数
			Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
			// 显示的页数
			if(map.get("openno")!=null&&!"".equals(map.get("openno"))) {
				map.put("openno", map.get("openno").toString().trim().replace("-", ""));
			}
			// 1.引入分页插件
			//PageHelper.startPage(pageIndex, pageNum);
			// 调用查询方法
			List<Map<String, Object>> all = openLogService.getAll(map);
			// 3.创建PageInfo封装分页信息,最多显示pageSize页
			PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,all);
			return   ResponseUtils.jsonView(200, "获取数据成功",pageInfo);
		} catch (Exception e) {
			return ResponseUtils.jsonView(201, "访问出现错误，请稍后再试",null) ;
		}
		
	}
	
	
	//查询详情
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/selectByOpenno")
	public ModelAndView selectByOpenno(HttpServletRequest request) {
		// 获取参数
		Map<String, Object> map = this.jsonToMap(request);
		try {
			Map<String, Object> selectByOpenno = openLogService.selectByOpenno(map);
			return ResponseUtils.jsonView(200, "获取数据成功",selectByOpenno) ;
		} catch (Exception e) {
			return ResponseUtils.jsonView(201, "访问出现错误，请稍后再试",null) ;
		}
	}
	
	//查询投注详情
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/selectBygameBet")
	public ModelAndView selectBygameBet(HttpServletRequest request) {
		// 获取参数
		Map<String, Object> map = this.jsonToMap(request);
		try {
			List<BettingOrderInfo> queryBettingOrderInfo = bettingOrderInfoMapper.queryBettingOrderInfoByCal_no(map);
			return ResponseUtils.jsonView(200, "获取数据成功",queryBettingOrderInfo) ;
		} catch (Exception e) {
			return ResponseUtils.jsonView(201, "访问出现错误，请稍后再试",null) ;
		}
	}
	
	
	
	
	
	

}
