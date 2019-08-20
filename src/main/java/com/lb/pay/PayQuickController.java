package com.lb.pay;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.service.IPayQuickService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

/**
 * @author jh
 * @date:2018年1月17日上午10:23:43
 * @describe:快速入账的后台管理系统
 */
@RestController
@RequestMapping("/payQuick")
public class PayQuickController extends BaseController {

	@Autowired
	private IPayQuickService payQuickService;

	// 查询第三方支付方式
	@RequestMapping(method = RequestMethod.POST, value = "/queryPayList")
	public ModelAndView queryPayList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<Map<String, Object>> payInfoList = payQuickService.queryPayList();
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,payInfoList);
		return ResponseUtils.jsonView(pageInfo);
	}

	// 添加支付方式
	@RequestMapping(method = RequestMethod.POST, value = "/addPayInfo")
	public ModelAndView addPayInfo(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = -1;
		if (map != null && !map.isEmpty()) {
			result = payQuickService.addPayInfo(map);
		}
		return result > 0 ? ResponseUtils.jsonView(200, "添加成功") : ResponseUtils.jsonView(809, "添加失败");
	}

	// 删除该支付方式
	@RequestMapping(method = RequestMethod.POST, value = "/delPayInfoByid")
	public ModelAndView delPayInfoByid(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = -1;
		if (map != null && !"".equals(map.get("id").toString())) {
			result = payQuickService.delPayInfoByid(Integer.valueOf(map.get("id").toString()));
		}
		return (result >= 0) ? ResponseUtils.jsonView(200, "删除成功") : ResponseUtils.jsonView(810, "删除失败");
	}

	// 查询在线支付类型
	@RequestMapping(method = RequestMethod.POST, value = "/updatePayInfo")
	public ModelAndView updatePayInfo(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = -1;
		if (map != null && !map.isEmpty()) {
			result = payQuickService.updatePayInfo(map);
		}
		return (result > 0) ? ResponseUtils.jsonView(200, "修改成功") : ResponseUtils.jsonView(811, "修改失败");
	}
}
