package com.lb.sys.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.model.SysAdvPicture;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysAdvPictureService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * 轮播图api
 * @author jiangheng
 *
 */

@RestController
@RequestMapping(value="/sysAdvPicture")
public class SysAdvPictureController extends BaseController{

	private final Log LOGGER = LogFactory.getLog(SysAdvPictureController.class);
	
	@Autowired
	private ISysAdvPictureService sysAdvPictureService;
	
	//添加轮播图片
	@RequestMapping(method=RequestMethod.POST,value="/addSysAdvPicture")
	public ModelAndView addSysAdvPicture(HttpServletRequest request) {
		SysAdvPicture sysAdvPicture = BaseController.jsonToBean(request, SysAdvPicture.class);
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		sysAdvPicture.setCreateUser(user.getUserName());
		sysAdvPicture.setPictureUrl(sysAdvPicture.getPictureUrl().trim());
		int flag = sysAdvPictureService.insertSelective(sysAdvPicture);
		return flag>0?ResponseUtils.jsonView(200,"添加成功  "):ResponseUtils.jsonView(270,"添加失败");
	}
		
	//根据主键id查询
	@RequestMapping(method=RequestMethod.GET,value="/querySysAdvPictureById")
	public ModelAndView querySysAdvPictureById(HttpServletRequest request) {
		Map<String, Object> jsonToMap = BaseController.jsonToMap(request);
		String sysAdvPictureId=String.valueOf(jsonToMap.get("id"));
		if(sysAdvPictureId!=null&&!"".equals(sysAdvPictureId)) {
			SysAdvPicture sysAdvPicture = sysAdvPictureService.selectByPrimaryKey(Long.parseLong(sysAdvPictureId));
			return (sysAdvPicture==null)?ResponseUtils.jsonView(273,"查无此数据"):ResponseUtils.jsonView(sysAdvPicture);
		}else {
			LOGGER.error("没有查询的主键");
			return ResponseUtils.jsonView(273,"无查询主键");
		}
	}	
		
	//更新轮播图片
	@RequestMapping(method=RequestMethod.POST,value="/updateSysAdvPicture")
	public ModelAndView updateSysAdvPicture(HttpServletRequest request) {
		SysAdvPicture sysAdvPicture = BaseController.jsonToBean(request, SysAdvPicture.class);
		if(sysAdvPicture!=null) {
			SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
			sysAdvPicture.setUpdateUser(user.getUserName());
			sysAdvPicture.setPictureUrl(sysAdvPicture.getPictureUrl().trim());
			int resultInt = sysAdvPictureService.updateByPrimaryKeySelective(sysAdvPicture);
			return resultInt>0?ResponseUtils.jsonView(200,"更新成功"):ResponseUtils.jsonView(271,"轮播图更新失败");
		}else {
			LOGGER.error("没有可更新数据");
			return ResponseUtils.jsonView(271,"轮播图更新失败");
		}
	}	
	
	//根据id逻辑删除
	@RequestMapping(method=RequestMethod.POST,value="/deleteSysAdvPictureById")
	public ModelAndView deleteSysAdvPictureById(HttpServletRequest request) {
		Map<String, Object> jsonToMap = BaseController.jsonToMap(request);
		String sysAdvPictureId=String.valueOf(jsonToMap.get("id"));
		if(sysAdvPictureId!=null&&!"".equals(sysAdvPictureId)) {
			int resultInt = sysAdvPictureService.deleteByPrimaryKey(Long.parseLong(sysAdvPictureId));
			return resultInt>0?ResponseUtils.jsonView(200,"删除成功"):ResponseUtils.jsonView(272,"删除失败");
		}else {
			LOGGER.error("没有删除的主键");
			return ResponseUtils.jsonView(272,"删除失败");
		}
	}
	
	//分页查询
	@RequestMapping(method=RequestMethod.GET,value="/querySysAdvPictureList")
	public ModelAndView querySysAdvPictureList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<SysAdvPicture> sysAdvPictures = sysAdvPictureService.selectSysAdvPictureList();
		PageUtils<SysAdvPicture> pageInfo =  new PageUtils<>(pageIndex,pageNum,sysAdvPictures);
		return ResponseUtils.jsonView(pageInfo);
	}
	
	//批量删除batchSysMessageByid
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "batchSysAdvPictureByid", method = RequestMethod.POST)
	public ModelAndView batchSysAdvPictureByid(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(map!=null && map.size()>0) {
			 List<String> ids = JSONArray.toList((JSONArray) map.get("sysAdvPictureids"),String.class, new JsonConfig());
			int flag = sysAdvPictureService.batchSysAdvPictureByid(ids);
			return flag>0?ResponseUtils.jsonView(200,"批量删除成功"):ResponseUtils.jsonView(273,"批量删除失败");
		}else {
			LOGGER.error("无公告信息批量删除id");
			return ResponseUtils.jsonView(273,"批量删除失败",null);
		}
	}
	
}
