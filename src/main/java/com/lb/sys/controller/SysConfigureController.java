/**
 * 
 */
package com.lb.sys.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.model.MemeberJsonResult;
import com.lb.sys.model.RangeModel;
import com.lb.sys.model.SysConfigure;
import com.lb.sys.model.SysConfigureModule;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysConfigureService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.ServerStateUtils;
import com.lb.sys.tools.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @describe 系统信息配置类
 */
@RestController
@RequestMapping("/sysConfigure")
public class SysConfigureController extends BaseController{
	
	@Autowired
	private ISysConfigureService sysConfigureService;
	/**
	 * 查询系统基本开关配置信息
	 * */
	@RequestMapping(method=RequestMethod.GET,value="/querySysConfigureModule")
	public ModelAndView getSysConfigureModule()
	{
		List<SysConfigureModule> module = sysConfigureService.querySysConfigureModule();
		return module!=null&&module.size()>0?ResponseUtils.jsonView(module):ResponseUtils.jsonView(201,"暂无数据");
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/querySysConfigure")
	public ModelAndView querySysConfigure(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		//模块id
		if(StringUtil.isBlank(map.get("moduleId"))) {
			return ResponseUtils.jsonView(555,"参数异常");
		}
		
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		map.put("roleId", sysUser.getRoleId());
		List<SysConfigure> sysSwitch = sysConfigureService.querySysConfigure(map);
		return ResponseUtils.jsonView(sysSwitch);
	}
	/**
	 * 修改系统基本开关配置信息
	 * */
	@RequestMapping(method=RequestMethod.POST,value="/updateSysConfigure")
	public ModelAndView updateSysConfigure(HttpServletRequest request)
	{

		String jsonString = request.getParameter("configureList");
		Collection<SysConfigure> coll = getConfigureList(jsonString);
		
		ModelAndView model = null;
		try {
			sysConfigureService.updateSysConfigure(coll);
			model =ResponseUtils.jsonView(200, "修改成功");
		}catch(Exception e) {
			e.printStackTrace();
			model =ResponseUtils.jsonView(555, "修改失败");
		}
		
		return model;
	}
	@SuppressWarnings("unchecked")
	private List<SysConfigure> getConfigureList(String jsonString)
	{
		List<SysConfigure> list = null;
		if(jsonString != null) {
			JSONArray jsonarray = JSONArray.fromObject(jsonString);
			if(jsonarray != null && jsonarray.size()>0) {
				list = new ArrayList<SysConfigure>();
				for(int i = 0;i<jsonarray.size() ; i++) {
					JSONObject job = jsonarray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
					SysConfigure config = (SysConfigure) JSONObject.toBean(job, SysConfigure.class);
					if(config.getDataType() != null && config.getDataType() == 3) {
						JSONArray jsonarray1 = job.getJSONArray("list");
						if(jsonarray1 != null && jsonarray1.size()>0) {
							Collection<RangeModel> models = JSONArray.toCollection(jsonarray1,RangeModel.class);
							if(models != null) {
								config.setList(Arrays.asList(models.toArray(new RangeModel[0])));
							}
						}
					}else if(config.getDataType() != null && config.getDataType() == 4) {
						JSONArray jsonarray1 = job.getJSONArray("list2");
						if(jsonarray1 != null && jsonarray1.size()>0) {
							Collection<MemeberJsonResult> models = JSONArray.toCollection(jsonarray1,MemeberJsonResult.class);
							if(models != null) {
								config.setList2(Arrays.asList(models.toArray(new MemeberJsonResult[0])));
							}
						}
					}
					list.add(config);
				}
			}
		}
		return list;
	}
	/**
	 * 获取一个单独的系统配置
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/getOnlyConfigure")
	public ModelAndView getOnlyConfigure(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		//模块id
		if(StringUtil.isBlank(map.get("configure"))) {
			return ResponseUtils.jsonView(555,"参数异常");
		}
		
		Map<String,Object> configureMap = sysConfigureService.getOnlyConfigure(map);
		return configureMap!=null?ResponseUtils.jsonView(200,"获取成功",configureMap):ResponseUtils.jsonView(201,"该配置不存在或开关关闭");
	}

	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/qryByConfigure")
	public ModelAndView qryByConfigure(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		//模块id
		if(StringUtil.isBlank(map.get("configure"))) {
			return ResponseUtils.jsonView(133,"参数异常");
		}
		Map<String,Object> configureMap = sysConfigureService.qryByConfigure(map.get("configure").toString());
		return configureMap!=null?ResponseUtils.jsonView(200,"获取成功",configureMap):ResponseUtils.jsonView(201,"该配置不存在");
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/resetPrivateKey")
	public ModelAndView resetPrivateKey(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		//模块id
		if(StringUtil.isBlank(map.get("privateKey"))) {
			return ResponseUtils.jsonView(133,"参数异常");
		}
		int result = sysConfigureService.resetPrivateKey(map);
		return result > 0 ? ResponseUtils.jsonView(200,"重置成功"):ResponseUtils.jsonView(201,"重置失败");
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/restServer")
	public ModelAndView restServer(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		if(StringUtil.isBlank(map.get("status"))) {
			return ResponseUtils.jsonView(133,"参数异常");
		}
		String status  = map.get("status").toString();
		int result = 0;
		if("1".equals(status)) {
			result = ServerStateUtils.executeShellCommandStart();
		}else if("0".equals(status)) {
			result = ServerStateUtils.executeShellCommandShutdown();	
		}
		return result > 0 ? ResponseUtils.jsonView(200,"操作成功"):ResponseUtils.jsonView(201,"操作失败");
	}
}
