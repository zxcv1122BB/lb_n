package com.lb.pay;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.service.IPayInfoService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.GetPropertiesValue;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

/**
 * @author jh
 * @date:2018年1月9日下午3:32:27
 * @describe:第三方支付平台信息
 */
@RestController
@RequestMapping("/pay")
public class PayInfoController extends BaseController {

	@Autowired
	private IPayInfoService payInfoService;

	// 查询第三方支付方式
	@RequestMapping(method = RequestMethod.POST, value = "/queryPayList")
	public ModelAndView queryPayList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<Map<String, Object>> payInfoList = payInfoService.queryPayList();
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,payInfoList);
		return ResponseUtils.jsonView(pageInfo);
	}

	// 添加支付方式
	@RequestMapping(method = RequestMethod.POST, value = "/addPayInfo")
	public ModelAndView addPayInfo(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = -1;
		if (map != null && !map.isEmpty()) {
			result = payInfoService.addPayInfo(map);
		}
		return result > 0 ? ResponseUtils.jsonView(200, "添加成功") : ResponseUtils.jsonView(803, "添加失败");
	}

	// 查询该支付方式下的所有网关信息
	@RequestMapping(method = RequestMethod.POST, value = "/queryPaytypeListByMethid")
	public ModelAndView queryPaytypeListByMethid(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		List<Map<String, Object>> paytypeList = null;
		if (map != null && !"".equals(map.get("methodid").toString())) {
			paytypeList = payInfoService.queryPaytypeListByMethid(Integer.valueOf(map.get("methodid").toString()));
		}
		return (paytypeList != null && !paytypeList.isEmpty()) ? ResponseUtils.jsonView(paytypeList)
				: ResponseUtils.jsonView(804, "暂无数据");
	}

	// 删除该支付方式
	@RequestMapping(method = RequestMethod.POST, value = "/delPayInfoByid")
	public ModelAndView delPayInfoByid(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = -1;
		if (map != null && !"".equals(map.get("methodid").toString())) {
			result = payInfoService.delPayInfoByid(Integer.valueOf(map.get("methodid").toString()));
		}
		return (result >= 0) ? ResponseUtils.jsonView(200, "删除成功") : ResponseUtils.jsonView(805, "删除失败");
	}

	// 查询第三方支付名称
	@RequestMapping(method = RequestMethod.POST, value = "/queryPayOnlineNames")
	public ModelAndView queryPayOnlineNames(HttpServletRequest request) {
//		List<Map<String, Object>> list = payInfoService.queryPayOnlineNames();
		Map<String, String> json2Map = GetPropertiesValue.getJson2Map("payCompany","payCompanyMapper");
		return (json2Map != null && !json2Map.isEmpty()) ? ResponseUtils.jsonView(json2Map) : ResponseUtils.jsonView(806, "暂无数据");
	}

	// 查询在线支付类型
	@RequestMapping(method = RequestMethod.POST, value = "/queryPayTypeList")
	public ModelAndView queryPayTypeList(HttpServletRequest request) {
		List<Map<String, Object>> list = payInfoService.queryPayTypeList();
		return (list != null && !list.isEmpty()) ? ResponseUtils.jsonView(list) : ResponseUtils.jsonView(807, "暂无数据");
	}

	// 查询在线支付类型
	@RequestMapping(method = RequestMethod.POST, value = "/updatePayInfo")
	public ModelAndView updatePayInfo(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = -1;
		if (map != null && !map.isEmpty()) {
			result = payInfoService.updatePayInfo(map);
		}
		return (result>=0) ? ResponseUtils.jsonView(200, "修改成功") : ResponseUtils.jsonView(808, "修改失败");
	}
}
