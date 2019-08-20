package com.lb.sys.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.redis.JedisClient;
import com.lb.sys.model.UserVipInfo;
import com.lb.sys.service.UserVipInfoService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

/****
 * 会员等级管理
 * 
 * @author ASUS
 *
 */
@RestController
@RequestMapping("/userVIP")
public class UserVipInfoController extends BaseController {
	@Autowired
	private UserVipInfoService userVipInfoService;
	@Autowired 
    private JedisClient jedisClient;
	// 显示所有的会员等级列表
	@RequestMapping(method = RequestMethod.GET, value = "/selectVips")
	//@SystemControllerLog()
	public ModelAndView selectVips(HttpServletRequest request) {
        List<Map<String,Object>> list = userVipInfoService.selectVips();
		return ResponseUtils.jsonView(list);
	}
	
	//删除某一个会员等级
	@RequestMapping(method = RequestMethod.GET, value = "/deleteVip")
	//@SystemControllerLog()
	public ModelAndView deleteVip(HttpServletRequest request) {
		//接受参数
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer vipId= Integer.valueOf(map.get("vipId").toString());
		Message message = userVipInfoService.deleteVip(vipId);
		return ResponseUtils.jsonView(message);
	}
	
	
	
	//解除一个用户的冻结状态
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/Unlock")
//	    String 
		//@SystemControllerLog()
		public ModelAndView Unlock(HttpServletRequest request,HttpServletResponse response) {
			//接受参数
			Map<String, Object> map = BaseController.jsonToMap(request);
			//根据key来删除redis中对应的数据
			Long del = jedisClient.del(map.get("username").toString());
			//判断是否操作成功
			return ResponseUtils.jsonView(null);
		}
	
	//禁用或者开启
	@RequestMapping(method = RequestMethod.GET, value = "/upVip")
	//@SystemControllerLog()
	public ModelAndView upVip(HttpServletRequest request) {
		//接受参数
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(null==map || null==map.get("vip_Id") || null==map.get("statu"))
			return ResponseUtils.jsonView(201, "参数异常");
				
		Integer vip_Id= Integer.valueOf(map.get("vip_Id").toString());
		Integer statu= Integer.valueOf(map.get("statu").toString());
		Map<String, Integer> mapPa=new HashMap<>();
		mapPa.put("vip_Id", vip_Id);
		mapPa.put("statu", statu);
		Message message = userVipInfoService.upVip(mapPa);
		return ResponseUtils.jsonView(message);
	}
	
	//修改会员等级基本信息  updateVIP
	@RequestMapping(method = RequestMethod.POST, value = "/updateVIP")
	//@SystemControllerLog()
	public ModelAndView updateVIP(HttpServletRequest request) {
		UserVipInfo bean = BaseController.jsonToBean(request, UserVipInfo.class);
		//更新时间
		bean.setUpdatetime(new Date());
		Message message = userVipInfoService.updateVIP(bean);
		return ResponseUtils.jsonView(message);
	}
	
	//查询某一个状态下的所有信息
	@RequestMapping(method = RequestMethod.GET, value = "/selectOption")
	//@SystemControllerLog()
	public ModelAndView selectOption(HttpServletRequest request) {
		//接受参数
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer stutas = Integer.valueOf(map.get("stutas").toString());
		//调用查询方法
		List<UserVipInfo> list = userVipInfoService.selectAllVipByS0(stutas);
		return ResponseUtils.jsonView(list);
	}
	
	//添加一个会员信息
	@RequestMapping(method = RequestMethod.POST, value = "/addVip")
	public ModelAndView addVip(HttpServletRequest request) {
		//接受参数
		UserVipInfo bean = BaseController.jsonToBean(request, UserVipInfo.class);
		bean.setInputtime(new Date());
		bean.setUpdatetime(new Date());
		//判断是否有传入状态参数
		if(bean.getStatus()==null || !(bean.getStatus().equals(""))) {
			bean.setStatus(0);
		}
		//调用添加方法
		Message message = userVipInfoService.addUserVip(bean);
		return ResponseUtils.jsonView(message);
	}
	
	//修改会员的vip等级  可以批量
	@RequestMapping(method = RequestMethod.POST, value = "/updateUserVip")
	//@SystemControllerLog()
	public ModelAndView updateUserVip(HttpServletRequest request) {
		//接受参数
		Map<String, Object> map = BaseController.jsonToMap(request);
		//调用修改方法
		Message message = userVipInfoService.updateUserVip(map);
		return ResponseUtils.jsonView(message);
	}
	
	
	
	//根据编号查询某一个会员等级的详细信息
	@RequestMapping(method = RequestMethod.GET, value = "/selectById")
	//@SystemControllerLog()
	public ModelAndView selectById(HttpServletRequest request) {
		//接受参数
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer vipid = Integer.valueOf(map.get("vipId").toString());
		//调用修改方法
		UserVipInfo selectbyId = userVipInfoService.selectbyId(vipid);
		return ResponseUtils.jsonView(selectbyId);
	}
	
	
	
}
