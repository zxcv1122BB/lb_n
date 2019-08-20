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

import com.lb.sys.model.SysMessage;
import com.lb.sys.model.SysMessageText;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysMessageService;
import com.lb.sys.service.SysMessageService;
import com.lb.sys.service.UserModelService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.AppPush;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * 站内信api
 * 判断网络状态
 * @author jiangheng
 *
 */
@RestController
@RequestMapping(value = "/sysMessage")
public class SysMessageController extends BaseController {

	private static final Log LOGGER = LogFactory.getLog(SysMessageController.class);

	@Autowired
	private ISysMessageService sysMessageService;
	@Autowired
	private UserModelService userModelService;
	@Autowired
	private SysMessageService sysMessagService;

	
	// 添加站内信
	@RequestMapping(method = RequestMethod.POST, value = "/addSysMessageText")
	public ModelAndView addSysActivity(HttpServletRequest request) {
		SysMessageText sysMessageText = BaseController.jsonToBean(request, SysMessageText.class);
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		sysMessageText.setCreateUser(user.getUserName());
		int flag = sysMessageService.insertSelective(sysMessageText);
		return flag > 0 ? ResponseUtils.jsonView(200, "添加成功") : ResponseUtils.jsonView(290, "站内信添加失败");
	}

	// 更改站内信
	@RequestMapping(method = RequestMethod.POST, value = "/updateSysMessageText")
	public ModelAndView updateSysMessageText(HttpServletRequest request) {
		SysMessageText sysMessageText = BaseController.jsonToBean(request, SysMessageText.class);
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		sysMessageText.setUpdateUser(user.getUserName());
		int flag = sysMessageService.updateSysMessageText(sysMessageText);
		return flag > 0 ? ResponseUtils.jsonView(200, "更新成功") : ResponseUtils.jsonView(291, "站内信更新失败");
	}

	// 根据id删除站内信
	@RequestMapping(method = RequestMethod.POST, value = "/deleteSysMessageTextById")
	public ModelAndView deleteSysMessageText(HttpServletRequest request) {
		Map<String, Object> jsonToMap = BaseController.jsonToMap(request);
		String id = String.valueOf(jsonToMap.get("id"));
		if (id != null && !"".equals(id)) {
			int flag = sysMessageService.deleteSysMessageTextByid(Long.parseLong(id));
			return flag > 0 ? ResponseUtils.jsonView(200, "删除成功") : ResponseUtils.jsonView(292, "站内信删除失败");
		} else {
			LOGGER.error("无删除主键");
			return ResponseUtils.jsonView(292, "站内信删除失败");
		}
	}

	// 根据id查询站内信内容
	@RequestMapping(method = RequestMethod.GET, value = "/querySysMessageTextById")
	public ModelAndView querySysMessageTextById(HttpServletRequest request) {
		Map<String, Object> jsonToMap = BaseController.jsonToMap(request);
		String id = String.valueOf(jsonToMap.get("id"));
		if (id != null && !"".equals(id)) {
			Long messageId = Long.parseLong(id);
			SysMessageText sysMessageText = sysMessageService.selectSysMessageTextByid(messageId);
			return (sysMessageText == null) ? ResponseUtils.jsonView(293, "查无此数据")
					: ResponseUtils.jsonView(sysMessageText);
		} else {
			// 前端需要传过2个参数,id和sendtype区别单发和群发,若是单发需要userid
			return ResponseUtils.jsonView(293, "查无此数据");
		}
	}

	// 分页查询
	@RequestMapping(method = RequestMethod.GET, value = "/querySysMessageTextList")
	public ModelAndView querySysBulletinList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<SysMessageText> sysMessageText = sysMessageService.selectSysBulletinList();
		PageUtils<SysMessageText> pageInfo =  new PageUtils<>(pageIndex,pageNum,sysMessageText);
		return ResponseUtils.jsonView(pageInfo);
	}

	// 批量删除batchSysMessageByid
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "batchSysMessageTextByid", method = RequestMethod.POST)
	public ModelAndView batchSysBulletinByid(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if (map != null && map.size() > 0) {
			List<String> ids = JSONArray.toList((JSONArray) map.get("ids"), String.class, new JsonConfig());
			int flag = sysMessageService.batchSysMessageTextByid(ids);
			return flag > 0 ? ResponseUtils.jsonView(200, "批量删除成功") : ResponseUtils.jsonView(293, "批量删除失败");
		} else {
			LOGGER.error("无公告信息批量删除id");
			return ResponseUtils.jsonView(293, "批量删除失败", null);
		}
	}

	// 根据标题查询
	@RequestMapping(method = RequestMethod.GET, value = "/querySysMessageTextByTitle")
	public ModelAndView querySysBulletinByTitle(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		// 搜索的字段内容
		String title =null;
		if(map.get("title")!=null && !(map.get("title").equals(""))) {
			title=String.valueOf(map.get("title"));
		}
		List<SysMessageText> sysMessageText = sysMessageService.selectByTitle(title);
		PageUtils<SysMessageText> pageInfo =  new PageUtils<>(pageIndex,pageNum,sysMessageText);
		return ResponseUtils.jsonView(pageInfo);
	}

	// 测试：查询所有需要推送出去的消息
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.POST, value = "/testMessages")
	public ModelAndView testMessages(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		map.put("sendType", 1);
		List<SysMessageText> sysMessageText1 = sysMessageService.selectAllContexts(map);
		map.put("sendType", 2);
		List<SysMessageText> sysMessageText2 = sysMessageService.selectAllContexts(map);
		// 根据uid查询到cid
		String cid = userModelService.selectCiDbYuID(Integer.valueOf(map.get("uid").toString())).getCid();
		// 3.创建PageInfo封装分页信息,最多显示pageSize页
		return ResponseUtils.jsonView(null);
	}

	// 推送消息：根据会员等级推送/根据会员推送
	@RequestMapping(method = RequestMethod.POST, value = "/submitMsg")
	public ModelAndView submitMsg(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		map.put("sendType", 1);
		List<SysMessageText> sysMessageText1 = sysMessageService.selectAllContexts(map);
		map.put("sendType", 2);
		List<SysMessageText> sysMessageText2 = sysMessageService.selectAllContexts(map);
		// 根据uid查询到cid
		String cid = userModelService.selectCiDbYuID(Integer.valueOf(map.get("uid").toString())).getCid();
		//System.out.println(sysMessageText1.size() + "," + sysMessageText2.size());
		int i=0;//计数器
		int count=sysMessageText1.size() + sysMessageText2.size();
		// 判断是否推送
		//首先判断是否有给他的vip等级推送
		if (sysMessageText1.size() > 0 && cid!=null) {
			for (SysMessageText mst1 : sysMessageText1) {
				if(AppPush.sendAlias(mst1.getTitle(), mst1.getContents(),cid).equals("ok")) {
					sysMessagService.tuiOverAdd(new SysMessage(Long.valueOf(mst1.getId()),Long.valueOf(map.get("uid").toString())));
					i++;
				}
			}
		}

		
		//其次判断是否有给他个人推送的
		if (sysMessageText2.size() > 0 && cid!=null) {
			for (SysMessageText mst2 : sysMessageText2) {
				if(AppPush.sendAlias(mst2.getTitle(), mst2.getContents(),cid).equals("ok")) {
					//添加一条推送记录
					sysMessagService.tuiOverAdd(new SysMessage(Long.valueOf(mst2.getId()),Long.valueOf(map.get("uid").toString())));
					i++;
				}
			}
		}
		// 3.创建PageInfo封装分页信息,最多显示pageSize页
		return i==count ? ResponseUtils.jsonView(200,"发送成功"):ResponseUtils.jsonView(201,"发送失败");
	}
	
	/***
	 * 给所有的app推送消息 次接口是给所有的app推送的所以不能再前端页面上调用
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/submitMsgAll")
	public ModelAndView submitMsgAll(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		List<SysMessageText> sysMessageText = sysMessageService.selectAllContexts(map);
		if(sysMessageText.size()>0) {
			try {
				for (SysMessageText mst2 : sysMessageText) {
					if(AppPush.sendAll(mst2.getTitle(), mst2.getContents()).equals("ok")) {
						//添加一条推送记录
						sysMessagService.tuiOverAdd(new SysMessage(Long.valueOf(mst2.getId()),Long.valueOf(map.get("uid").toString())));
					}
				}
				return ResponseUtils.jsonView(200,"推送成功");
			} catch (Exception e) {
				return ResponseUtils.jsonView(201,"推送失败");
			}
		}else {
			return ResponseUtils.jsonView(202,"无推送记录");
		}
	}
}
