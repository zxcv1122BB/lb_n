package com.lb.onlineSchedule.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.lb.onlineSchedule.ScheduleTaskService;
import com.lb.onlineSchedule.cache.OnlineDetailCache;
import com.lb.redis.JedisClient;
import com.lb.sys.controller.LoginController;
import com.lb.sys.service.ILogindetailService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.CommonTools;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.IPSeeker;
import com.lb.sys.tools.JSONUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.SocketUtils;
import com.lb.sys.tools.StringUtil;

import net.sf.json.JSONObject;

@RestController
public class OnlineController extends BaseController {
	private final static Log LOGGER = LogFactory.getLog(ScheduleTaskService.class);

	@Autowired
	private ILogindetailService logindetailService;

	@Autowired
	private JedisClient jedisClient;

	// private static final String onlineKey = "onlineHeartbeat";
	// private static final String onlineKey = "onlineUsers";

	/**
	 * 
	 * @describe:心跳接口,统计在线会员人数
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/onlineHeartbeat")
	public Map<String, Object> onlineHeartbeat(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		Map<String, Object> resultMap = new HashMap<>();
		if (paramMap.get(LoginController.USERNAME) == null) {// 无用户名,表示未登录
			resultMap.put("code", 292);
			resultMap.put("msg", "心跳无参数失败");
			return resultMap;
		}
		String username = String.valueOf(paramMap.get(LoginController.USERNAME)[0]);
		// 访问时间的字符串
		String dateString = DateUtils.getDateString(new Date());
		// 表示APP登陆成功存储的值
		String loginState = jedisClient.get("signalLogin" + username);
		if (!OnlineDetailCache.getRequestDetail().containsKey(username) && loginState != null) {// 表示已经登录成功,但是未记录
			Map<String, Object> map = new HashMap<>();
			map.put("username", username);
			map.put("loginTime", dateString);
			map.put("ip", paramMap.get("ip")[0]);
			map.put("channel", String.valueOf(paramMap.get("channel")[0]));
			map.put("device", String.valueOf(paramMap.get("device")[0]));
			logindetailService.insertSelective(map);
			OnlineDetailCache.addOnlineDetailCache(username, dateString, request.getRemoteAddr());
			//LOGGER.info("在线人数-->" + OnlineDetailCache.getRequestDetail());
			resultMap.put("code", 293);
			resultMap.put("msg", "登录记录添加成功");
			return resultMap;
		} else if (username != null && !"".equals(username)) {
			if (loginState == null) {// 表示redis中没有登录成功的token
				resultMap.put("code", 291);
				resultMap.put("msg", "强制下线");
				return resultMap;
			}
			// 更新最后登录时间,和ip地址.避免中途退出后,ip没有进行更改
			OnlineDetailCache.updateBreakDetailCache(username, dateString, paramMap.get("ip")[0]);
			resultMap.put("code", 200);
			resultMap.put("msg", "心跳成功");
			return resultMap;
		} else {
			resultMap.put("code", 292);
			resultMap.put("msg", "心跳失败");
			return resultMap;
		}
	}

	/**
	 * 
	 * @describe:心跳接口,统计在线代理人数
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/agencyOnlineHeartbeat")
	public ModelAndView agencyOnlineHeartbeat(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap.get(LoginController.USERNAME) == null) {// 无用户名,表示未登录
			return ResponseUtils.jsonView(292, "心跳无参数失败");
		}
		String username = String.valueOf(paramMap.get(LoginController.USERNAME)[0]);
		// 访问时间的字符串
		String dateString = DateUtils.getDateString(new Date());
		// 表示登陆成功存储的值
		String loginState = jedisClient.hget("agencySignalLogin", username);
		if (!OnlineDetailCache.getAgencyDetail().containsKey(username) && loginState != null) {// 表示已经登录成功,但是未记录
			Map<String, Object> map = new HashMap<>();
			map.put("username", username);
			map.put("loginTime", dateString);
			map.put("ip", paramMap.get("ip")[0]);
			logindetailService.insertAgencySelective(map);
			OnlineDetailCache.addAgencyOnlineDetailCache(username, dateString, request.getRemoteAddr());
			//LOGGER.info("在线人数-->" + OnlineDetailCache.getAgencyDetail());
			return ResponseUtils.jsonView(293, "登录记录添加成功");
		} else if (OnlineDetailCache.getAgencyDetail().containsKey(username)) {
			if (loginState == null) {
				return ResponseUtils.jsonView(291, "强制下线");
			}
			OnlineDetailCache.updateAgencyBreakDetailCache(username, dateString);
			return ResponseUtils.jsonView(200, "心跳成功");
		} else {
			return ResponseUtils.jsonView(292, "心跳失败");
		}
	}

	/**
	 * 获取在人员线列表
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getOnlineUserList")
	public ModelAndView getOnlineUserList(HttpServletRequest request) {
		try {
			Map<String, Object> paramMap = BaseController.jsonToMap(request);
			// 当前页
			Integer pageIndex = Integer.valueOf(paramMap.get("pageIndex").toString());
			// 每一页数据条数
			Integer pageNum = Integer.valueOf(paramMap.get("pageNum").toString());
			// 查询在线人员姓名
			String userName = String.valueOf(paramMap.get(LoginController.USERNAME));
			// 查询类型
			String queryType =paramMap.get("type")!=null?paramMap.get("type").toString():"";
			// 在线人员map集合
			String info = SocketUtils.getUserInfo("API.GetOnlineUserList", "showout1");
			if (StringUtil.isBlank(info) || "[]".equals(info)) {
				return ResponseUtils.jsonView(291, "查询无数据");
			}
			info = info.substring(1, info.length() - 1).replace(" ", "");
			List<String> onlineNameList = Arrays.asList(info.split(","));
			List<Map<String, Object>> result = new ArrayList<>();
			Map<String, Object> userInfoPage = null;// 返回前端的map集合
			if (onlineNameList != null && onlineNameList.size() > 0) {

				userInfoPage = new HashMap<>();
				// 在线用户名list集合
				if (StringUtils.isNotEmpty(userName)) {
					if ("name".equals(queryType)) {// 按名字来查询
						onlineNameList = CommonTools.search(userName, onlineNameList);
						if (onlineNameList == null || onlineNameList.isEmpty()) {
							return ResponseUtils.jsonView(291, "查询无数据");
						}
					}
					if ("proxy".equals(queryType)) {// 按代理账号来查询
						List<String> onlineUsers = jedisClient.hmget("user_info",
								onlineNameList.toArray(new String[onlineNameList.size()]));
						userName = "AGENT_COUNT:".concat(userName);
						onlineUsers = CommonTools.search(userName, onlineUsers);// 包含该代理ip的在线用户的用户信息
						if (onlineUsers == null || onlineUsers.isEmpty()) {
							return ResponseUtils.jsonView(291, "查询无数据");
						}
						List<String> nameList = new ArrayList<>();
						for (int i = 0; i < onlineUsers.size(); i++) {
							nameList.add(String.valueOf((JSONUtils.toMap(onlineUsers.get(i))).get("USER_NAME")));
						}
						onlineNameList = nameList;
					}
				}
				/**
				 * 新增逻辑 获取用户 2018年7月11日15:56:03
				 * 
				 */
				String userStr = onlineNameList.toString().substring(1, onlineNameList.toString().length() - 1)
						.replace(" ", "");
				String	reply = SocketUtils.getUserInfo("API.GetUserLastLoginDetail", userStr);
				//LOGGER.info("在线用户字符串:"+reply);
				JSONObject replyJSON = new JSONObject();
				if(StringUtils.isNotEmpty(reply))
					replyJSON =	JSONObject.fromObject(reply);
				//LOGGER.info("在线用户JSON:"+replyJSON.entrySet());

				List<String> nameList = new ArrayList<>();// 每页取值时的name集合
				int currIdx = (pageIndex > 1 ? (pageIndex - 1) * pageNum : 0);
				for (int i = 0; i < pageNum && i < onlineNameList.size() - currIdx; i++) {
					nameList.add(onlineNameList.get(currIdx + i));
				}

				List<String> userInfoList = jedisClient.hmget("user_info",
						nameList.toArray(new String[nameList.size()]));
				for (String string : userInfoList) {
					if (!StringUtils.isEmpty(string)) {
						Map<String, Object> map = JSONUtils.toMap(string);
						String username = map.get("USER_NAME").toString();
						JSONObject itemJSON = replyJSON.getJSONObject(username);
						if(itemJSON != null) {
							String loginTime = itemJSON.containsKey("loginTime") ? itemJSON.getString("loginTime") : "";
							map.put("loginTime", loginTime);
							// 最后登录地址赋值
							String LAST_LOGIN_IP = itemJSON.containsKey("ip") ? itemJSON.getString("ip") : null;
							String json_result = "";
							// 将IP转成物理地址
							if (StringUtils.isNotEmpty(LAST_LOGIN_IP)) {
								try {
									IPSeeker is = IPSeeker.getInstance();
									json_result = is.getAddress(LAST_LOGIN_IP);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							map.put("LAST_LOGIN_IP", LAST_LOGIN_IP);
							map.put("loginAddress", json_result);
						}
						result.add(map);
					}
				}
				Integer page = (onlineNameList.size() % pageNum == 0) ? (onlineNameList.size() / pageNum)
						: (onlineNameList.size() / pageNum + 1);
				userInfoPage.put("pageSize", onlineNameList.size());
				userInfoPage.put("pages", page);
				userInfoPage.put("userInfoList", result);
			}
			return userInfoPage != null ? ResponseUtils.jsonView(200, "在线消息列表", userInfoPage)
					: ResponseUtils.jsonView(291, "查询无数据");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;

	}

	/**
	 * 强制会员下线
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/forcedOffline")
	public ModelAndView forcedOffline(HttpServletRequest request) {
		Map<String, Object> paramMap = BaseController.jsonToMap(request);
		String userName = String.valueOf(paramMap.get("userName"));
		// 删除登陆成功后的标记,单点登录也用该标记来核对
		Long del = jedisClient.del("signalLogin" + userName);
		String msg = SocketUtils.getUserInfo("API.ForceOffline", userName);
		// logindetailService.offlineHandle(userName);
		return del > 0 && "ok".equals(msg) ? ResponseUtils.jsonView(200, "强制下线成功")
				: ResponseUtils.jsonView(290, "强制下线失败");
	}

	/**
	 * 强制代理下线
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/forcedAgencyOffline")
	public ModelAndView forcedAgencyOffline(HttpServletRequest request) {
		Map<String, Object> paramMap = BaseController.jsonToMap(request);
		String userName = String.valueOf(paramMap.get("userName"));
		// 删除登陆成功后的标记,单点登录也用该标记来核对
		Long del = jedisClient.hdel("agencySignalLogin", userName);
		logindetailService.agencyofflineHandle(userName);
		return del > 0 ? ResponseUtils.jsonView(200, "强制下线成功") : ResponseUtils.jsonView(290, "强制下线失败");
	}

	/**
	 * 获取在线人数,提款人数,充值人数
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getDepositAndWithdrawCount")
	public ModelAndView getDepositAndWithdrawCount(HttpServletRequest request) {
		Map<String, Object> map = logindetailService.getDepositAndWithdrawCount();
		// map.put("onlineCount", jedisClient.hgetAll(onlineKey).size());
		//map.put("onlineCount", SocketUtils.getUserInfo("API.GetOnlineUserCount", "showout1"));
		//在线,
		return ResponseUtils.jsonView(200, "提款,充值", map);
	}

	/**
	 * app端人员自动下线了,ls项目退出时httpClient调用
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/APPClientOffline")
	public void aPPClientOffline(HttpServletRequest request) {
		String userName = request.getParameter(LoginController.USERNAME);
		logindetailService.offlineHandle(userName);
	}

	/**
	 * 获取会员在线人数
	 */
	/*
	 * @RequestMapping(method = RequestMethod.GET, value = "/getOnlineMap") public
	 * String getOnlineMap(HttpServletRequest request) { //
	 * ConcurrentHashMap<String, Map<String, String>> nameMap = //
	 * OnlineDetailCache.getRequestDetail(); //Map<String, String> nameMap =
	 * jedisClient.hgetAll(onlineKey);
	 * 
	 * Gson(); return gson.toJson(nameMap.keySet()); }
	 */

	/**
	 * 获取代理在线人数
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getAgencyOnlineMap")
	public String getAgencyOnlineMap(HttpServletRequest request) {
		ConcurrentHashMap<String, Map<String, String>> nameMap = OnlineDetailCache.getAgencyDetail();
		Gson gson = new Gson();
		return gson.toJson(nameMap.keySet());
	}

}
