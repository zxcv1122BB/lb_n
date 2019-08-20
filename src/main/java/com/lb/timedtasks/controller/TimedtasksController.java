package com.lb.timedtasks.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.GetPropertiesValue;
import com.lb.sys.tools.HttpClientUtil;
import com.lb.sys.tools.ResponseUtils;

import net.sf.json.JSONObject;

/***
 * 定时任务管理控制器层
 * @author ASUS
 *
 */
@Controller
@RequestMapping("/job")
public class TimedtasksController extends BaseController {
	
    //将请求转发
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/getAllJobList")
	public ModelAndView getAllJobList(HttpServletRequest request,HttpServletResponse response) {
		//获取参数
		Map<String, Object> pramMap = this.jsonToMap(request);
		Map<String,String> newParm=new HashMap<String,String>();
		//首先判断获取到的值是否为空
		if(pramMap.get("pageIndex") !=null && !(pramMap.get("pageIndex").equals(""))) {
			newParm.put("pageIndex", pramMap.get("pageIndex").toString());
		}
		
		if(pramMap.get("pageNum") !=null && !(pramMap.get("pageNum").equals(""))) {
			newParm.put("pageNum", pramMap.get("pageNum").toString());
		}
		
		if(pramMap.get("pageSize") !=null && !(pramMap.get("pageSize").equals(""))) {
			newParm.put("pageSize", pramMap.get("pageSize").toString());
		}
		String url = GetPropertiesValue.getValue("URL","lsschedule_project_url");
		String doPost = HttpClientUtil.doPost(url+"/Timer/selectAllTimer",newParm);
		if(doPost.equals("")) {
			return ResponseUtils.jsonView(200,"获取数据失败",null);
		}else {
			JSONObject fromObject = JSONObject.fromObject(doPost);
			return ResponseUtils.jsonView(200,"获取数据成功",fromObject);
		}
	}
	
	    // 删除一个定时任务
		@SuppressWarnings({ "static-access", "unchecked" })
		@RequestMapping(method = RequestMethod.POST, value = "/deleteOneTimer")
		public ModelAndView deleteOneTimer(HttpServletRequest request, HttpServletResponse response) {
			Map<String, Object> pramMap = this.jsonToMap(request);
			Map<String,String> newParm=new HashMap<String,String>();
			newParm.put("jobId", pramMap.get("jobId").toString());
			String doPost = HttpClientUtil.doPost(GetPropertiesValue.getValue("URL","lsschedule_project_url")+"/Timer/deleteOneTimer",newParm);
			if(doPost.equals("")) {
				return ResponseUtils.jsonView(200,"获取数据失败",null);
			}else {
				//返回调用接口的返回值
				JSONObject fromObject = JSONObject.fromObject(doPost);
				return ResponseUtils.jsonView(fromObject);
			}
		}

		/***
		 * 新增一个定时任务
		 * 
		 * @param request
		 * @param response
		 * @return
		 */
		@SuppressWarnings({ "static-access", "unchecked" })
		// 添加一个定时任务
		@RequestMapping(method = RequestMethod.POST, value = "/addTimer")
		public ModelAndView addTimer(HttpServletRequest request, HttpServletResponse response) {
			// 获取参数，并且添加到数据库中
			Map<String, Object> pramMap = this.jsonToMap(request);
			Map<String,String> newParm=new HashMap<String,String>();
			newParm.put("jobName", pramMap.get("jobName").toString());
			newParm.put("jobGroup", pramMap.get("jobGroup").toString());
			newParm.put("cronExpression", pramMap.get("cronExpression").toString());
			newParm.put("beanClass", pramMap.get("beanClass").toString());
			newParm.put("executeMethod", pramMap.get("executeMethod").toString());
			newParm.put("jobStatus", pramMap.get("jobStatus").toString());
			newParm.put("jobDesc", pramMap.get("jobDesc").toString());
			String doPost = HttpClientUtil.doPost(GetPropertiesValue.getValue("URL","lsschedule_project_url")+"/Timer/addTimer",newParm);
			if(doPost.equals("")) {
				return ResponseUtils.jsonView(200,"获取数据失败",null);
			}else {
				//返回调用接口的返回值
				JSONObject fromObject = JSONObject.fromObject(doPost);
				return ResponseUtils.jsonView(fromObject);
			}
		}

		// 暂停/停用一个定时任务
		@SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.POST, value = "/stopOneTimer")
		public ModelAndView stopOneTimer(HttpServletRequest request, HttpServletResponse response) {
			// 获取到前端传递过来的参数
			@SuppressWarnings("static-access")
			Map<String, Object> pramMap = this.jsonToMap(request);
			Map<String,String> newParm=new HashMap<String,String>();
			newParm.put("jobId",pramMap.get("jobId").toString());
			String doPost = HttpClientUtil.doPost(GetPropertiesValue.getValue("URL","lsschedule_project_url")+"/Timer/stopOneTimer",newParm);
			if(doPost.equals("")) {
				return ResponseUtils.jsonView(200,"获取数据失败",null);
			}else {
				//返回调用接口的返回值
				JSONObject fromObject = JSONObject.fromObject(doPost);
				return ResponseUtils.jsonView(fromObject);
			}
		}

		// 启用一个定时任务
		@SuppressWarnings({ "static-access", "unchecked" })
		@RequestMapping(method = RequestMethod.POST, value = "/startOneTimer")
		public ModelAndView startOneTimer(HttpServletRequest request, HttpServletResponse response) {
			// 获取到前端传递过来的参数
			Map<String, Object> pramMap = this.jsonToMap(request);
			Map<String,String> newParm=new HashMap<String,String>();
			newParm.put("jobId",pramMap.get("jobId").toString());
			String doPost = HttpClientUtil.doPost(GetPropertiesValue.getValue("URL","lsschedule_project_url")+"/Timer/startOneTimer",newParm);
			if(doPost.equals("")) {
				return ResponseUtils.jsonView(200,"获取数据失败",null);
			}else {
				//返回调用接口的返回值
				JSONObject fromObject = JSONObject.fromObject(doPost);
				return ResponseUtils.jsonView(fromObject);
			}
		}

		// 修改定时任务
		@SuppressWarnings({ "static-access", "unchecked" })
		@RequestMapping(method = RequestMethod.POST, value = "/updateOneTimer")
		public ModelAndView updateOneTimer(HttpServletRequest request, HttpServletResponse response) {
			Map<String, Object> pramMap = this.jsonToMap(request);
			Map<String,String> newParm=new HashMap<String,String>();
			newParm.put("jobId", pramMap.get("jobId").toString());
			newParm.put("jobName", pramMap.get("jobName").toString());
			newParm.put("jobGroup", pramMap.get("jobGroup").toString());
			newParm.put("cronExpression", pramMap.get("cronExpression").toString());
			newParm.put("beanClass", pramMap.get("beanClass").toString());
			newParm.put("executeMethod", pramMap.get("executeMethod").toString());
			if(pramMap.get("jobDesc")!=null && !(pramMap.get("jobDesc").equals(""))) {
				newParm.put("jobDesc", pramMap.get("jobDesc").toString());
			}
			String doPost = HttpClientUtil.doPost(GetPropertiesValue.getValue("URL","lsschedule_project_url")+"/Timer/updateOneTimer",newParm);
			if(doPost.equals("")) {
				return ResponseUtils.jsonView(200,"获取数据失败",null);
			}else {
				//返回调用接口的返回值
				JSONObject fromObject = JSONObject.fromObject(doPost);
				return ResponseUtils.jsonView(fromObject);
			}
		}

		/***
		 * 根据编号查询一个定时任务的详情
		 * @param request
		 * @param response
		 * @return
		 */
		@SuppressWarnings("static-access")
		@RequestMapping(method = RequestMethod.POST, value = "/getByJobDetil")
		public ModelAndView getByJobDetil(HttpServletRequest request, HttpServletResponse response) {
			Map<String, Object> pramMap = this.jsonToMap(request);
			Map<String,String> newParm=new HashMap<String,String>();
			newParm.put("jobId", pramMap.get("jobId").toString());
			String doPost = HttpClientUtil.doPost(GetPropertiesValue.getValue("URL","lsschedule_project_url")+"/Timer/getByJobDetil",newParm);
			if(doPost.equals("")) {
				return ResponseUtils.jsonView(201,"获取数据失败",null);
			}else {
				//返回调用接口的返回值
				JSONObject fromObject = JSONObject.fromObject(doPost);
				return ResponseUtils.jsonView(200,"获取成功",fromObject);
			}
		}
	
	
		//查看日志
		@SuppressWarnings("static-access")
		@RequestMapping(method = RequestMethod.POST, value = "/getAllJobLogList")
		public ModelAndView getAllJobLogList(HttpServletRequest request,HttpServletResponse response) {
			//获取参数
			Map<String, Object> pramMap = this.jsonToMap(request);
			Map<String,String> newParm=new HashMap<String,String>();
			//首先判断获取到的值是否为空
			if(pramMap.get("pageIndex") !=null && !(pramMap.get("pageIndex").equals(""))) {
				newParm.put("pageIndex", pramMap.get("pageIndex").toString());
			}
			
			if(pramMap.get("pageNum") !=null && !(pramMap.get("pageNum").equals(""))) {
				newParm.put("pageNum", pramMap.get("pageNum").toString());
			}
			
			if(pramMap.get("pageSize") !=null && !(pramMap.get("pageSize").equals(""))) {
				newParm.put("pageSize", pramMap.get("pageSize").toString());
			}
			
			if(pramMap.get("inputDate") !=null && !(pramMap.get("inputDate").equals(""))) {
				newParm.put("inputDate", pramMap.get("inputDate").toString());
			}
			String doPost = HttpClientUtil.doPost(GetPropertiesValue.getValue("URL","lsschedule_project_url")+"/QrtzLog/selectQrtzLog",newParm);
			if(doPost.equals("")) {
				return ResponseUtils.jsonView(200,"获取数据失败",null);
			}else {
				JSONObject fromObject = JSONObject.fromObject(doPost);
				return ResponseUtils.jsonView(200,"获取数据成功",fromObject);
			}
		}
		
		//执行失败的定时任务
		@SuppressWarnings({ "static-access", "unchecked" })
		@RequestMapping(method = RequestMethod.POST, value = "/implementFailTimer")
		public ModelAndView implementFailTimer(HttpServletRequest request,HttpServletResponse response) {
			//获取参数
			Map<String, Object> pramMap = this.jsonToMap(request);
			Map<String,String> newMap=new HashMap<String,String>();
			newMap.put("logId", pramMap.get("logId").toString());
			//获取参数
			String doPost = HttpClientUtil.doPost(GetPropertiesValue.getValue("URL","lsschedule_project_url")+pramMap.get("url").toString(),newMap);
			if(doPost.equals("")) {
				return ResponseUtils.jsonView(200,"获取数据失败",null);
			}else {
				JSONObject fromObject = JSONObject.fromObject(doPost);
				return ResponseUtils.jsonView(fromObject);
			}
		}
}
