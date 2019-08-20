package com.lb.sys.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.model.SysBulletin;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysBulletinService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.GetPropertiesValue;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
/*
 * 公告信息API
 */
@RestController
@RequestMapping(value="/sysBulletin")
public class SysBulletinController extends BaseController{

	private final Log LOGGER = LogFactory.getLog(SysBulletinController.class);
	
	@Autowired
	private ISysBulletinService sysBulletinService;
	
	//添加公告信息
	@RequestMapping(method=RequestMethod.POST,value="/addSysBulletin")
	public ModelAndView addSysBulletin(HttpServletRequest request) {
		SysBulletin sysBulletin = BaseController.jsonToBean(request, SysBulletin.class);
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		sysBulletin.setCreateUser(user.getUserName());
		sysBulletin.setTitle(sysBulletin.getTitle().trim());
		sysBulletin.setContent(sysBulletin.getContent().trim());
		int flag = sysBulletinService.insertSelective(sysBulletin);
		return flag>0?ResponseUtils.jsonView(200,"添加成功  "):ResponseUtils.jsonView(280,"添加失败");
	}
	
	//根据主键id查询
	@RequestMapping(method=RequestMethod.GET,value="/querySysBulletinById")
	public ModelAndView querySysBulletinById(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> jsonToMap = BaseController.jsonToMap(request);
		String sysBulletinId=String.valueOf(jsonToMap.get("id"));
		if(sysBulletinId!=null&&!"".equals(sysBulletinId)) {
			SysBulletin sysBulletin = sysBulletinService.selectByPrimaryKey(Long.parseLong(sysBulletinId));
			return (sysBulletin==null)?ResponseUtils.jsonView(283,"查无此数据"):ResponseUtils.jsonView(sysBulletin);
		}else {
			LOGGER.error("没有查询的主键");
			return ResponseUtils.jsonView(283,"无查询主键");
		}
	}	
	
	//更新公告信息
	@RequestMapping(method=RequestMethod.POST,value="/updateSysBulletin")
	public ModelAndView updateSysBulletin(HttpServletRequest request) {
		SysBulletin sysBulletin = BaseController.jsonToBean(request, SysBulletin.class); 
		if(sysBulletin!=null) {
			SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
			sysBulletin.setUpdateUser(user.getUserName());
			sysBulletin.setTitle(sysBulletin.getTitle().trim());
			sysBulletin.setContent(sysBulletin.getContent().trim());
			int resultInt = sysBulletinService.updateByPrimaryKeySelective(sysBulletin);
			return resultInt>0?ResponseUtils.jsonView(200,"更新成功"):ResponseUtils.jsonView(281,"公告更新失败");
		}else {
			LOGGER.error("没有可更新数据");
			return ResponseUtils.jsonView(281,"公告更新失败");
		}
	}
	
	//根据id逻辑删除
	@RequestMapping(method=RequestMethod.POST,value="/deleteSysBulletinById")
	public ModelAndView deleteSysBulletinById(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> jsonToMap = BaseController.jsonToMap(request);
		String sysBulletinId=String.valueOf(jsonToMap.get("id"));
		if(sysBulletinId!=null&&!"".equals(sysBulletinId)) {
			int resultInt = sysBulletinService.deleteByPrimaryKey(Long.parseLong(sysBulletinId));
			return resultInt>0?ResponseUtils.jsonView(200,"删除成功"):ResponseUtils.jsonView(282,"删除失败");
		}else {
			LOGGER.error("没有删除的主键");
			return ResponseUtils.jsonView(282,"删除失败");
		}
	}
	
	//分页查询
	@RequestMapping(method=RequestMethod.GET,value="/querySysBulletinList")
	public ModelAndView querySysBulletinList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<SysBulletin> sysBulletins = sysBulletinService.selectSysBulletinList();
		PageUtils<SysBulletin> pageInfo =  new PageUtils<>(pageIndex,pageNum,sysBulletins);
		return ResponseUtils.jsonView(pageInfo);
	}
	
	//批量删除batchSysMessageByid
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "batchSysBulletinByid", method = RequestMethod.POST)
	public ModelAndView batchSysBulletinByid(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(map!=null && map.size()>0) {
			 List<String> ids = JSONArray.toList((JSONArray) map.get("sysMessageids"),String.class, new JsonConfig());
			int flag = sysBulletinService.batchSysMessageByid(ids);
			return flag>0?ResponseUtils.jsonView(200,"批量删除成功"):ResponseUtils.jsonView(283,"批量删除失败");
		}else {
			LOGGER.error("无公告信息批量删除id");
			return ResponseUtils.jsonView(283,"批量删除失败",null);
		}
	}
	
	//根据标题查询
	@RequestMapping(method=RequestMethod.GET,value="/querySysBulletinByTitle")
	public ModelAndView querySysBulletinByTitle(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//搜索的字段内容
		String title=String.valueOf(map.get("title")).trim();
		map.put("title", title);
		List<SysBulletin> sysBulletins = sysBulletinService.selectByTitle(map);
		PageUtils<SysBulletin> pageInfo =  new PageUtils<>(pageIndex,pageNum,sysBulletins);
		return ResponseUtils.jsonView(pageInfo);
	}
	
	/**
	 * 公告图片上传
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	public ModelAndView chatImgUpload(MultipartFile file) {
		try {
			LOGGER.info("文件名：" + file);
			if(file.isEmpty() || file==null) {
				return ResponseUtils.jsonView(201, "图片不能为空", null);
			}
			// 获取文件名
			String fileName = file.getOriginalFilename();
			if(fileName.equals("undefined")) {
				return ResponseUtils.jsonView(201, "图片格式不正确", null);
			}
			//获取文件大小
			long size = file.getSize();
			LOGGER.info("上传的文件大小为：" + size);
			long maxSize = Long.valueOf(GetPropertiesValue.getValue("fileupload","file_chatimages_size").toString());
			if(size>1024*1024*maxSize) {
				return ResponseUtils.jsonView(201, "图片大小不能超过"+maxSize+"M", null);
			}
			LOGGER.info("上传的文件名为：" + fileName);
			// 获取文件的后缀名
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			LOGGER.info("上传的后缀名为：" + suffixName);
			//文件名称重命名
			fileName = DateUtils.getDateString(new Date(),"yyyyMMddHHmmss")+suffixName;
			String mkdir = DateUtils.getDateString(new Date(),"yyyyMMdd");
			String filePath = GetPropertiesValue.getValue("fileupload","file_images_path")+mkdir;
			LOGGER.info("文件目录为："+filePath);
			File dest = new File(filePath+"//"+ fileName);
			//判断目录是否存在
			if(!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();	
			}
			file.transferTo(dest);
			return ResponseUtils.jsonView(200, "请求成功", fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.jsonView(201, "服务器异常", null);
	}
	
}
