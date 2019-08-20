package com.lb.ip.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.ip.model.IpWhiteList;
import com.lb.ip.service.IpWhiteListService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

@RestController
@RequestMapping("/ip")
public class IpController extends BaseController {
	@Autowired
	private IpWhiteListService ipWhiteListService;

	// 查询所有的白名单Ip
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/selectAllIps")
	//@SystemControllerLog()
	public ModelAndView selectAllIps(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		Integer status = Integer.valueOf(map.get("status").toString());
		List<IpWhiteList> list = ipWhiteListService.selectByExample(status);
		PageUtils<IpWhiteList> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}

	// 添加一个白名单ip
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/addIp")
	public ModelAndView addIp(HttpServletRequest request) {
		// 获取参数
		IpWhiteList ipWhiteList = this.jsonToBean(request, IpWhiteList.class);
		ipWhiteList.setStatus(0);
		ipWhiteList.setInputtime(new Date());
		ipWhiteList.setUpdatetime(new Date());
		// 判断是否为空
		int resultInt = ipWhiteListService.insert(ipWhiteList);
		if (resultInt == 103) {
			return ResponseUtils.jsonView(103, "该ip已经存在于白名单中");
		}
		if (resultInt == 101) {
			return ResponseUtils.jsonView(101, "ip地址格式错误");
		}

		return resultInt == 100 ? ResponseUtils.jsonView(100, "添加成功") : ResponseUtils.jsonView(102, "添加失败");
	}

	// 查询某一个ip是否已经存在
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/selectIp")
	//@SystemControllerLog()
	public ModelAndView selectIp(HttpServletRequest request) {
		// 获取参数
		Map<String, Object> map = this.jsonToMap(request);
		String ip = map.get("ip").toString();
		// 调用
		Integer resultInt = ipWhiteListService.selectIpYorN(ip);
		return resultInt > 0 ? ResponseUtils.jsonView(101, "该ip已经存在") : ResponseUtils.jsonView(100, "不存在");
	}

	// 删除 禁用一个ip
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/deleteIpOrOpen")
	//@SystemControllerLog()
	public ModelAndView deleteIpOrOpen(HttpServletRequest request) {
		// 获取参数
		IpWhiteList ipWhiteList = this.jsonToBean(request, IpWhiteList.class);
		// 判断是否为空
		if (ipWhiteList != null) {
			// 调用删除方法
			int resultInt = ipWhiteListService.deleteIp(ipWhiteList);
			return resultInt == 100 ? ResponseUtils.jsonView(100, "更改状态成功") : ResponseUtils.jsonView(102, "更改状态失败");
		} else {
			return ResponseUtils.jsonView(101, "要更改的数据有空值，更改失败");
		}
	}

	// 修改ip信息
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/updateIp")
	//@SystemControllerLog()
	public ModelAndView updateIp(HttpServletRequest request) {
		// 获取参数
		IpWhiteList ipWhiteList = this.jsonToBean(request, IpWhiteList.class);
		ipWhiteList.setUpdatetime(new Date());
		// 调用删除方法
		int resultInt = ipWhiteListService.updateByPrimaryKeySelective(ipWhiteList);
		if (resultInt == 103) {
			return ResponseUtils.jsonView(103, "该ip已经存在于白名单中");
		}
		if (resultInt == 101) {
			return ResponseUtils.jsonView(101, "ip地址格式错误");
		}
		return resultInt == 100 ? ResponseUtils.jsonView(100, "修改ip信息成功") : ResponseUtils.jsonView(102, "修改ip信息失败");

	}
}
