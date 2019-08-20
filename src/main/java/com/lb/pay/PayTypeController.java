package com.lb.pay;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.service.IPayTypeService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.StringUtil;

@RestController
@RequestMapping("/payType")
public class PayTypeController extends BaseController {

	@Autowired
	private IPayTypeService payTypeService;

	// 查询第三方支付方式
	@RequestMapping(method = RequestMethod.POST, value = "/queryPayList")
	public ModelAndView queryPayList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		String payType = map.get("payType").toString();
		if (map == null || map.isEmpty() || StringUtil.isBlank(payType)) {
			return ResponseUtils.jsonView(202, "参数错误");
		}
		List<Map<String, Object>> payInfoList = payTypeService.queryPayList(payType);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,payInfoList);
		return ResponseUtils.jsonView(200, "查询成功", pageInfo);
	}

	// 添加支付方式
	@RequestMapping(method = RequestMethod.POST, value = "/addPayType")
	public ModelAndView addPayType(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = -1;
		if (map != null && !map.isEmpty()) {
			result = payTypeService.addPayType(map);
		}
		return result > 0 ? ResponseUtils.jsonView(200, "添加成功") : ResponseUtils.jsonView(202, "添加失败");
	}

	// 删除该支付方式
	@RequestMapping(method = RequestMethod.POST, value = "/delPayTypeByid")
	public ModelAndView delPayTypeByid(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = -1;
		if (map != null && !StringUtil.isBlank(map.get("id"))) {
			result = payTypeService.delPayTypeByid(map.get("id").toString());
		}
		return (result >= 0) ? ResponseUtils.jsonView(200, "删除成功") : ResponseUtils.jsonView(202, "删除失败");
	}

	// 更新在线支付类型
	@RequestMapping(method = RequestMethod.POST, value = "/updatePayType")
	public ModelAndView updatePayType(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = -1;
		if (map != null && !map.isEmpty()) {
			result = payTypeService.updatePayType(map);
		}
		return (result > 0) ? ResponseUtils.jsonView(200, "修改成功") : ResponseUtils.jsonView(202, "修改失败");
	}
}
