package com.lb.sys.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.excel.ExportExcel;
import com.lb.redis.JedisClient;
import com.lb.sys.model.MemeberJsonResult;
import com.lb.sys.model.SysUser;
import com.lb.sys.model.UserModel;
import com.lb.sys.model.UserVipModel;
import com.lb.sys.service.IProxyInfoService;
import com.lb.sys.service.UserModelService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.SocketUtils;
import com.lb.sys.tools.StringUtil;

@RestController
@RequestMapping("/user")
public class UserModelConntroller extends BaseController {
	private final Log LOGGER = LogFactory.getLog(SysBulletinController.class);
	private final String key = "lsIsLocking";
	@Autowired
	private UserModelService userService;

	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private IProxyInfoService proxyInfoService;

//	private static final String onlineKey = "onlineUsers";

	/**
	 * 查询 带模糊
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/selectAllUser")
	public ModelAndView selectAllUser(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		UserModel um = new UserModel();
		if (map.get("userName") != null && !(map.get("userName").equals(""))) {
			um.setUserName("%" + map.get("userName").toString() + "%");
		}

		if (map.get("weixin") != null && !(map.get("weixin").equals(""))) {
			um.setWeixin("%" + map.get("weixin").toString() + "%");
		}

		if (map.get("qq") != null && !(map.get("qq").equals(""))) {
			um.setQq("%" + map.get("qq").toString() + "%");
		}
		List<UserModel> selectAllUser = userService.selectLike(um);
		PageUtils<UserModel> pageInfo =  new PageUtils<>(pageIndex,pageNum,selectAllUser);
		return ResponseUtils.jsonView(pageInfo);
	}

	// 修改状态 禁用或者启用
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.POST, value = "/updateStatc")
	public ModelAndView updateStatc(HttpServletRequest request) {
		UserModel um = BaseController.jsonToBean(request, UserModel.class);
		if (um != null) {
			Byte status = um.getStatus();
			if(!StringUtil.isBlank(status) && "1".equals(status+"")) {
				String userName = um.getUserName();
				Long del = jedisClient.del(key + userName);
			}
			int resultInt = userService.updateStatc(um);
			return resultInt == 200 ? ResponseUtils.jsonView(200, "修改状态成功") : ResponseUtils.jsonView(241, "修改状态失败");
		} else {
			LOGGER.error("没有可更新数据");
			return ResponseUtils.jsonView(281, "修改状态失败");
		}
	}

	// 查询某一个vip组别下的所有会员信息
	@RequestMapping(method = RequestMethod.POST, value = "/selectUsersByVip")
	// @SystemControllerLog()
	public ModelAndView selectUsersByVip(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		// 调用查询
		List<UserModel> list = userService.selectVipByUser(map);
		PageUtils<UserModel> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	
	// 查询某一个vip组别下的所有会员信息
	@RequestMapping(method = RequestMethod.POST, value = "/selectBankType")
	public ModelAndView selectBankType(HttpServletRequest request) {
		List<Map<String,Object>> list = userService.selectBankType();
		return ResponseUtils.jsonView(list);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/export/{VIP_ID}")
	public void export(@PathVariable("VIP_ID") Integer VIP_ID, HttpServletRequest request, HttpServletResponse response,
			String queryJson) {
		List<UserModel> userlList = userService.exportUser_Info(VIP_ID);
		ExportExcel<UserModel> ee = new ExportExcel<UserModel>();
		// 头信息
		String[] headers = { "会员编号", "会员名称", "手机号码", "QQ号码", "微信号码", "用户真实姓名", "注册ip", "注册时间", "会员类型", "积分", "累计积分",
				"个人财产", "冻结资产", "投注流水", "邮箱", "修改时间", "录入时间", "会员等级" };
		// 默认的表名字
		String fileName = "用户信息表";
		ee.exportExcel(headers, userlList, fileName, response);
	}

	// 修改会员基本信息（包括修改密码）
	@RequestMapping(method = RequestMethod.POST, value = "/updateUser")
	public ModelAndView updateUser(HttpServletRequest request) {
		UserModel bean = BaseController.jsonToBean(request, UserModel.class);
		int i = userService.updateByPrimaryKeySelective(bean);
		// 判断是否修改成功
		return i > 0 ? ResponseUtils.jsonView(200, "操作成功") : ResponseUtils.jsonView(201, "操作失败");
	}

	// 站内信查询用户名
	@RequestMapping(method = RequestMethod.GET, value = "/queryUsername")
	public ModelAndView queryUsername(HttpServletRequest request) {
		List<UserModel> usernameList = userService.queryUsername();
		return (usernameList == null) ? ResponseUtils.jsonView(251, "查询用户名失败") : ResponseUtils.jsonView(usernameList);
	}

	// 站内信查询用户组信息
	@RequestMapping(method = RequestMethod.GET, value = "/queryAllUsername")
	public ModelAndView queryAllUsername(HttpServletRequest request) {
		List<Map<String, Object>> allUsername = userService.queryAllUsername();
		return (allUsername == null) ? ResponseUtils.jsonView(251, "查询用户名失败") : ResponseUtils.jsonView(allUsername);
	}

	/**
	 * 获取会员列表及条件筛选排序查询
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/queryUserList")
	public ModelAndView queryUserList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		if (pageIndex == null || pageIndex <= 0) {
			pageIndex = 1;
		}
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		if (pageNum == null || pageNum <= 0) {
			pageNum = 5;
		}
		// 显示的页数
		Integer pageSize = Integer.valueOf(map.get("pageSize").toString());
		if (pageSize == null || pageSize <= 0) {
			pageSize = 10;
		}

		List<UserVipModel> allUser = userService.queryUserList(map);
		if (allUser == null || allUser.size() <= 0) {
			return ResponseUtils.jsonView(555, "暂无数据");
		}
		Object obj = map.get("onlineSort");

		checkOnlineUser(allUser, obj);

		Map<String, Object> pageInfo = new HashMap<String, Object>();
		int currIdx = (pageIndex >= 1 ? (pageIndex - 1) * pageNum : 0);// 当前行
		Integer page = (pageNum > allUser.size() - currIdx) ? (allUser.size() - currIdx) : pageNum;
		pageInfo.put("total", allUser.size());// 总行数
		pageInfo.put("pages",
				(allUser.size() % pageNum == 0 ? (allUser.size() / pageNum) : (allUser.size() / pageNum) + 1));// 总页数
		pageInfo.put("list", allUser.subList(currIdx, currIdx + page));
		return ResponseUtils.jsonView(200, "获取成功", pageInfo);
	}

	/***
	 * 解除锁定
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/unlockMember")
	public ModelAndView unlockMember(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 取出会员名称
		String username = map.get("username").toString();
		Long del = jedisClient.del(key + username);
		// 判断是否解除绑定成功
		return del >= 1 ? ResponseUtils.jsonView(200, "解除锁定成功") : ResponseUtils.jsonView(201, "解除锁定失败");
	}

	// 修改会员基本信息（包括修改密码）
	@RequestMapping(method = RequestMethod.POST, value = "/updateVipUser")
	// @SystemControllerLog()
	public ModelAndView updateVipUser(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		int i = userService.updateVipUser(map);
		// 判断是否修改成功
		return i > 0 ? ResponseUtils.jsonView(200, "操作成功")
				: (i == -1 ? ResponseUtils.jsonView(333, "两次输入密码不一致") : ResponseUtils.jsonView(555, "操作失败"));
	}
	
	// 修改会员基本信息（包括修改密码）
	@RequestMapping(method = RequestMethod.POST, value = "/updateVipUser1")
	// @SystemControllerLog()
	public ModelAndView updateVipUser1(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		int i = userService.updateVipUser1(map);
		// 判断是否修改成功
		return i > 0 ? ResponseUtils.jsonView(200, "操作成功")
				: (i == -1 ? ResponseUtils.jsonView(333, "两次输入密码不一致") : ResponseUtils.jsonView(555, "操作失败"));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getUserByUserName")
	public ModelAndView getUserByUserName(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		UserVipModel bean = userService.getUserByUserName(map);
		return ResponseUtils.jsonView(bean);
	}

	/**
	 * 管理员增加会员
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/insertVipUser")
	public ModelAndView insertVipUser(HttpServletRequest request) {
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		UserVipModel bean = BaseController.jsonToBean(request, UserVipModel.class);
		int i = -1;
		if (user != null && user.getUserId() != null && user.getUserId() >= 0) {
			bean.setCreateBy(user.getUserId() + "");
			i = userService.insertVipUser(request, bean);
			if (i == -2) {
				LOGGER.info("管理员增加用户 -- 输入信息不完整");
				return ResponseUtils.jsonView(555, "输入信息不完整");
			}else if (i == -3) {
				LOGGER.info("管理员增加用户 -- 未输入用户类型");
				return ResponseUtils.jsonView(555, "未录入用户类型");
			}else if (i == -4) {
				LOGGER.info("管理员增加用户 -- 未设置彩种返点");
				return ResponseUtils.jsonView(555, "未设置彩种返点");
			}else if (i == -5) {
				LOGGER.info("管理员增加用户 -- 未查询到系统彩种返点");
				return ResponseUtils.jsonView(555, "系统彩种配置异常");
			}else if (i == -6) {
				LOGGER.info("管理员增加用户 -- 彩种返点未设置在系统需求范围");
				return ResponseUtils.jsonView(555, "彩种返点超出范围");
			}
		} else {
			LOGGER.info("未获取到管理员");
		}
		// 判断是否增加成功
		return i > 0 ? ResponseUtils.jsonView(200, "操作成功") : ResponseUtils.jsonView(555, "操作失败");
	}

	/**
	 * 获取会员等级名称列表
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getMemberList")
	public ModelAndView getMemberList() {
		List<MemeberJsonResult> list = userService.getMemberList();
		return list.size() > 0 ? ResponseUtils.jsonView(200, "获取数据成功", list)
				: ResponseUtils.jsonView(555, "暂无数据", list);
	}

	/**
	 * 获取筛选条件列表
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getKeyList")
	public ModelAndView getKeyList() {
		List<MemeberJsonResult> list = userService.getKeyList();
		return list!= null && list.size() > 0 ? ResponseUtils.jsonView(200, "获取数据成功", list)
				: ResponseUtils.jsonView(555, "暂无数据", list);
	}

	/**
	 * 模糊查询获取代理名称列表
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getProxyList")
	public ModelAndView getProxyList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		String proxyName = String.valueOf(map.get("proxyName"));
		if (StringUtils.isBlank(proxyName)) {
			return ResponseUtils.jsonView(333, "未传入代理名称");
		}
		List<MemeberJsonResult> list = userService.getProxyList(proxyName);
		return list.size() > 0 ? ResponseUtils.jsonView(200, "获取数据成功", list)
				: ResponseUtils.jsonView(555, "暂无数据", list);
	}

	/**
	 * 获取所有代理名称列表
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getAllProxyList")
	public ModelAndView getAllProxyList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		List<MemeberJsonResult> list = userService.getAllProxyList();
		return list.size() > 0 ? ResponseUtils.jsonView(200, "获取数据成功", list) : ResponseUtils.jsonView(555, "暂无数据");
	}

	/**
	 * 校验用户账号是否已存在
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/checkUsername")
	public ModelAndView checkUsername(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		String username = String.valueOf(map.get("username"));
		if (StringUtils.isBlank(username)) {
			return ResponseUtils.jsonView(333, "请正确传入用户账号");
		}
		int check = userService.checkUsername(username);
		return check <= 0 ? ResponseUtils.jsonView(200, "可以使用该账号") : ResponseUtils.jsonView(555, "该账号已存在");
	}

	/**
	 * 获取在线状态以及是否根据在线排序
	 */
	private void checkOnlineUser(List<UserVipModel> list, Object obj) {
		String info = SocketUtils.getUserInfo("API.GetOnlineUserList", "showout1");
		if (!StringUtil.isBlank(info) && !"[]".equals(info)) {
			info = info.substring(1, info.length() - 1).replace(" ", "");
			List<String> onlineNameList = Arrays.asList(info.split(","));
			if (list != null && list.size() > 0 && onlineNameList != null) {
				byte a = 1;
				for (UserVipModel user : list) {
					if (user != null && onlineNameList.contains(user.getUsername())) {
						user.setOnlineStatus(a);
					}
				}

				if (obj != null && !"".equals(obj.toString().trim())) {
					Collections.sort(list, new Comparator<UserVipModel>() {// 将在线条件排序

						@Override
						public int compare(UserVipModel model1, UserVipModel model2) {
							if (Integer.valueOf(obj.toString()) == 1) {
								return model1.getOnlineStatus() - model2.getOnlineStatus();
							} else {
								return model2.getOnlineStatus() - model1.getOnlineStatus();
							}

						}

					});
				}
			}
		}
	}

	/**
	 * 系统风险评估_运营分析
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getOperationUserList")
	public ModelAndView getOperationUserList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		// 查询类型
		String userType = String.valueOf(map.get("userType"));
		List<Map<String, Object>> allUser = null;
		if ("2".equals(userType)) {// 会员
			Map<String, Object> param = new HashMap<>();
			param.put("startDate", map.get("startDate"));
			param.put("endDate", map.get("endDate"));
			allUser = userService.getOperationUserList(param);
		} else if ("1".equals(userType)) {// 代理
			Map<String, Object> param = new HashMap<>();
			param.put("startDate", map.get("startDate"));
			param.put("endDate", map.get("endDate"));
			allUser = proxyInfoService.getProxyUserList(param);
		}
		PageUtils<Map<String,Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,allUser);
		return ResponseUtils.jsonView(pageInfo);
	}
	
	/**
	 * 获取返点列表
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getRebateConfigList")
	public ModelAndView getRebateConfigList(HttpServletRequest request) {
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if (user == null || user.getUserId() == null || user.getUserId() <= 0) {
			LOGGER.info("未获取到管理员");
			return ResponseUtils.jsonView(333, "账号异常，请重新登录");
		}
		List<Map<String, Object>> configList = userService.getRebateConfigList();
		// 判断是否增加成功
		return configList != null && configList.size() > 0 ? ResponseUtils.jsonView(200, "获取成功" , configList) : ResponseUtils.jsonView(201, "暂无数据");
	}
	
	/**
	 * 增加邀请码
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addInvitateInfo")
	public ModelAndView addInvitateInfo(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if (user == null || user.getUserId() == null || user.getUserId() <= 0) {
			LOGGER.info("未获取到管理员");
			return ResponseUtils.jsonView(333, "账号异常，请重新登录");
		}
		if(map == null || StringUtil.isBlank(map.get("userType")) || StringUtil.isBlank(map.get("data"))
				|| StringUtil.isBlank(map.get("proxyId"))) {
			return ResponseUtils.jsonView(200, "传入参数错误" );
		}
		int result = userService.addInvitateInfo(map);
		if(result == -1) {
			return ResponseUtils.jsonView(333, "传入参数不完整");
		}else if(result == -2) {
			return ResponseUtils.jsonView(333, "请用一级代理");
		}else if(result == -3) {
			return ResponseUtils.jsonView(333, "该代理返点信息异常");
		}else if(result == -4) {
			return ResponseUtils.jsonView(333, "返点信息范围不正确");
		}else if(result == -5) {
			return ResponseUtils.jsonView(333, "该用户非代理类型");
		}
		// 判断是否增加成功
		return result > 0 ? ResponseUtils.jsonView(200, "添加成功" ) : ResponseUtils.jsonView(555, "添加失败");
	}
	
	/**
	 * 删除邀请码
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/removeInvitateInfo")
	public ModelAndView removeInvitateInfo(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if (user == null || user.getUserId() == null || user.getUserId() <= 0) {
			LOGGER.info("未获取到管理员");
			return ResponseUtils.jsonView(333, "账号异常，请重新登录");
		}
		if(map == null || StringUtil.isBlank(map.get("invitateId"))
				|| StringUtil.isBlank(map.get("proxyId"))) {
			return ResponseUtils.jsonView(200, "传入参数错误" );
		}
		int result = userService.removeInvitateInfo(map);
		return result > 0 ? ResponseUtils.jsonView(200, "删除成功" ) : ResponseUtils.jsonView(555, "删除失败");
	}
	/**
	 * 查询邀请码列表
	 * 
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "/queryInvitateInfoList")
	public ModelAndView queryInvitateInfoList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(map == null || map.get("pageIndex") == null
				|| map.get("pageNum") == null || map.get("pageSize") == null
				|| StringUtil.isBlank(map.get("proxyId")) 
				|| StringUtil.isBlank(map.get("userType"))) {
			LOGGER.info(map);
			return ResponseUtils.jsonView(333,"传入参数不完整");
		}
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<Map<String, Object>> list = userService.queryInvitateInfoList(map);
		PageUtils<Map<String,Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return pageInfo != null && pageInfo.getTotal() > 0 ? ResponseUtils.jsonView(200,"获取成功",pageInfo) : ResponseUtils.jsonView(201,"暂无数据");
	}
	
	
	
}
