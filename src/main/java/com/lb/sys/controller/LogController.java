package com.lb.sys.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.log.model.LogInfo;
import com.lb.log.service.LogInfoService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

/***
 * 日志信息操作（查询类）
 * 
 * @author ASUS
 *
 */
@RestController
@RequestMapping("/log")
public class LogController extends BaseController {

	@Autowired
	private LogInfoService logInfoService;

	/***
	 * 查询所有的日志信息，要求能够按照日志的生成时间来查询，
	 * 查询某一个时间段
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/selectAllLog")
	public ModelAndView theLottery(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		// 取出筛选的时间参数 并且去除开始和结尾的两个空格
		String operateDate = (map.get("startTime").toString()).replaceAll(" ", "");
		String endTime = (map.get("endTime").toString()).replaceAll(" ", "");
		LogInfo lg = new LogInfo();
		lg.setStartDate(operateDate);
		lg.setEndTime(endTime);
		// 做模糊搜索
		List<LogInfo> logs = logInfoService.selectAllLogs(lg);
		PageUtils<LogInfo> pageInfo =  new PageUtils<>(pageIndex,pageNum,logs);
		return ResponseUtils.jsonView(pageInfo);
	}
}
