package com.lb.sys.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.download.model.UserDownload;
import com.lb.redis.JedisClient;
import com.lb.sys.dao.SysConfigureMapper;
import com.lb.sys.dao.UserModelMapper;
import com.lb.sys.dao.UserVipInfoMapper;
import com.lb.sys.model.MemeberJsonResult;
import com.lb.sys.model.UserModel;
import com.lb.sys.model.UserVipModel;
import com.lb.sys.service.UserModelService;
import com.lb.sys.tools.CommonTools;
import com.lb.sys.tools.IPSeeker;
import com.lb.sys.tools.MyMD5Util;
import com.lb.sys.tools.ShortenUrl;
import com.lb.sys.tools.SocketUtils;
import com.lb.sys.tools.StringUtil;

import net.sf.json.JSONObject;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class UserModelServiceImpl implements UserModelService {
	@Autowired
	private UserModelMapper userMapper;

	@Autowired
	private UserVipInfoMapper userVipInfoMapper;
	@Autowired SysConfigureMapper sysMapper;
	@Autowired
	private JedisClient jedisClient;
	private static final Logger logger = LoggerFactory.getLogger(UserModelServiceImpl.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final String key = "lsIsLocking";

	@Override
	public List<UserModel> selectAllUser() {
		// TODO Auto-generated method stub
		return userMapper.selectAllUser();
	}

	@Override
	// public List<UserModel> exportUser_Info(String arrValue) {
	// // TODO Auto-generated method stub
	// return userMapper.exportUser_Info(arrValue);
	// }

	public List<UserModel> exportUser_Info(Integer parm) {
		// TODO Auto-generated method stub
		return userMapper.exportUser_Info(parm);
	}

	@Override
	public List<UserModel> selectLike(UserModel um) {

		return userMapper.selectLike(um);
	}

	// 修改状态
	@Override
	public int updateStatc(UserModel user) {
		int flag = 0;
		int update = userMapper.updateStatc(user);
		if (update > 0) {
			flag = 200;
		} else {
			flag = 241;
		}
		return flag;
	}

	@Override
	public UserModel queryUserByUserName(String userName) {
		List<UserModel> selectByExample = userMapper.queryUserByUserName(userName);
		if (!selectByExample.isEmpty() && null != selectByExample.get(0)) {
			return selectByExample.get(0);
		}
		return null;
	}

	@Override
	public List<UserModel> selectVipByUser(Map<String, Object> map) {
		return userMapper.selectVipByUser(map);
	}
	
	@Override
	public List<Map<String,Object>> selectBankType() {
		return userMapper.selectBankType();
	}

	// 站内信查询用户名
	public List<UserModel> queryUsername() {
		Map<String, Object> map = new HashMap<>();
		map.put("status", 1);
		return userMapper.queryUsername(map);
	}

	// 修改会员基本信息
	@Override
	public int updateByPrimaryKeySelective(UserModel record) {
		// 判断是否修改了密码，如果修改了，则将密码加密
		if (record.getPssword() != null && !(record.getPssword().equals(""))) {
			record.setPssword(MyMD5Util.encodePassword(record.getPssword(), MyMD5Util.salt(record.getPssword())));
		}
		// 调用修改方法 返回影响行数
		return userMapper.updateByPrimaryKeySelective(record);
	}

	// 站内信获取分组id和名
	public List<Map<String, Object>> queryAllUsername() {
		Map<String, Object> param = new HashMap<>();
		param.put("status", 1);
		return userVipInfoMapper.queryAllVipNames(param);
	}

	@Override
	public UserModel selectCiDbYuID(Integer uid) {
		// TODO Auto-generated method stub
		return userMapper.selectCiDbYuID(uid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lb.sys.service.UserModelService#queryUserList(java.util.Map)
	 */
	@Override
	public List<UserVipModel> queryUserList(Map<String, Object> map) {
		try {

			String option = String.valueOf(map.get("keywordOption"));
			if (option != null && (option.contains("=") || option.contains(";"))) {
				map.put("keywordOption", null);
			}
			List<UserVipModel> queryUserList = userMapper.queryUserList(map);

			// 用户集合
			List<String> onlineNameList = new ArrayList<>();
			queryUserList.forEach(userPOJO -> {
				onlineNameList.add(userPOJO.getUsername());
			});
			//logger.info("[会员管理] 全部用户集合:" + onlineNameList.toString());
			// 获取在线用户最后登录时间
			JSONObject replyJSON = new JSONObject();
			if (onlineNameList.size() > 0) {
				String userStr = onlineNameList.toString().substring(1, onlineNameList.toString().length() - 1)
						.replace(" ", "");
				String	reply = SocketUtils.getUserInfo("API.GetUserLastLoginDetail", userStr);
				//logger.info("[会员管理] Go程序返回 在线用户字符串:" + reply);
				if (StringUtils.isNotEmpty(reply))
					replyJSON = JSONObject.fromObject(reply);
			}

			// 赋值
			List<UserVipModel> resList = new ArrayList<UserVipModel>();
			for (UserVipModel itemPOJO : queryUserList) {
				String itemUserName = itemPOJO.getUsername();
				// 更新用户状态
				String lockCount = jedisClient.get(key + itemUserName);
				if(!StringUtil.isBlank(lockCount)) {
					int count = Integer.parseInt(lockCount);
					if(count == 5) {
						itemPOJO.setStatus((byte)2);
					}
				}
				// 最后登录时间赋值
				JSONObject itemJSON = replyJSON.getJSONObject(itemUserName);
				String loginTime = itemJSON.containsKey("loginTime") ? itemJSON.getString("loginTime") : "";
				if (StringUtils.isNotEmpty(loginTime) && replyJSON.containsKey(itemUserName)) {
					itemPOJO.setLoginTime(sdf.parse(loginTime));
				} else {
					itemPOJO.setLoginTime(null);
				}

				// 最后登录地址赋值
				String LAST_LOGIN_IP = itemJSON.containsKey("ip") ? itemJSON.getString("ip") : null;
				String json_result = "";
				// 将IP转成物理地址
				if (StringUtils.isNotEmpty(LAST_LOGIN_IP)) {
					try {
						IPSeeker is = IPSeeker.getInstance();
						json_result = is.getAddress(LAST_LOGIN_IP);
					} catch (Exception e) {
						//logger.error("[会员管理] IP转换地址异常：" + e.getMessage());
						e.printStackTrace();
					}
				}
				itemPOJO.setLoginIp(LAST_LOGIN_IP);
				itemPOJO.setLoginAddress(json_result);

				resList.add(itemPOJO);
			}
			return resList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public List<UserDownload> queryUserListDownl(Map<String, Object> map) {
		List<UserDownload> queryUserList = userMapper.exportUserList(map);
		//// List<UserDownload> downUserList = new ArrayList<UserDownload>();
		// // 判断是否查询到值
		// if (queryUserList.size() > 0) {
		// for (UserVipModel userVipModel : queryUserList) {
		// //判断时间登陆时间是否为空
		//// if() {
		////
		//// }
		// SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
		// String loginTime = sdf.format(userVipModel.getLoginTime());
		// String regTime = sdf.format(userVipModel.getRegTime());
		// UserDownload userDownload = new UserDownload(userVipModel.getUsername(),
		//// userVipModel.getFullName(),
		// userVipModel.getCOIN(), userVipModel.getVipName(), regTime, loginTime,
		// userVipModel.getLoginIp(), userVipModel.getPhoneNumber(),
		//// userVipModel.getQq(),
		// userVipModel.getWeixin(), userVipModel.getEmail(),
		//// userVipModel.getBankAccount(),
		// userVipModel.getBankName(),userVipModel.getBankAddress(),userVipModel.getRegIp());
		// downUserList.add(userDownload);
		// }
		// }
		return queryUserList;
	}

	// 修改会员基本信息
	@Override
	public int updateVipUser(Map<String, Object> bean) {
		// 判断是否修改了密码，如果修改了，则将密码加密
		if (bean != null && bean.get("password") != null && bean.get("rePassword") != null) {
			if (!bean.get("password").equals(bean.get("rePassword"))) {
				return -1;
			}
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			bean.put("password", encoder.encode(bean.get("password").toString()));
		}
		if (bean != null && bean.get("coinPssword") != null && bean.get("reCoinPssword") != null) {
			if (!bean.get("coinPssword").equals(bean.get("reCoinPssword"))) {
				return -1;
			}
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			bean.put("coinPssword", encoder.encode(bean.get("coinPssword").toString()));
		}
		// 调用修改方法 返回影响行数
		return userMapper.updateVipUser(bean);
	}
	
	// 修改会员基本信息
	@Override
	public int updateVipUser1(Map<String, Object> bean) {
		// 判断是否修改了密码，如果修改了，则将密码加密
		if (bean != null && bean.get("password") != null && bean.get("rePassword") != null) {
			if (!bean.get("password").equals(bean.get("rePassword"))) {
				return -1;
			}
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			bean.put("password", encoder.encode(bean.get("password").toString()));
		}
		if (bean != null && bean.get("coinPssword") != null && bean.get("reCoinPssword") != null) {
			if (!bean.get("coinPssword").equals(bean.get("reCoinPssword"))) {
				return -1;
			}
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			bean.put("coinPssword", encoder.encode(bean.get("coinPssword").toString()));
		}
		// 调用修改方法 返回影响行数
		return userMapper.updateVipUser1(bean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lb.sys.service.UserModelService#getMemberList()
	 */
	@Override
	public List<MemeberJsonResult> getMemberList() {
		return userMapper.getMemberList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lb.sys.service.UserModelService#queryKeyList()
	 */
	@Override
	public List<MemeberJsonResult> getKeyList() {
		return userMapper.getKeyList();
	}

	@Override
	public UserVipModel getUserByUserName(Map<String, Object> map) {
		return userMapper.getUserByUserName(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lb.sys.service.UserModelService#getProxyList()
	 */
	public List<MemeberJsonResult> getProxyList(String proxyName) {
		return userMapper.getProxyList(proxyName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lb.sys.service.UserModelService#insertVipUser(com.lb.sys.model.
	 * UserVipModel)
	 */
	@Override
	public int insertVipUser(HttpServletRequest request, UserVipModel model) {
		if (model == null || StringUtils.isBlank(model.getPassword()) || StringUtils.isBlank(model.getRePassword())
				|| StringUtils.isBlank(model.getPhoneNumber()) || StringUtils.isBlank(model.getUsername())
				|| StringUtils.isBlank(model.getFullName())) {
			return -2;
		}
		if(model.getUserType() == null) {
			return -3;
		}
		if(StringUtils.isBlank(model.getData())) {
			return -4;
		}
		List<Map<String, Object>> rebateConfigList = userMapper.getRebateConfigList();
		if(rebateConfigList == null || rebateConfigList.size() <=0) {
			logger.error("无系统彩种返点配置");
			return -5;
		}
		
		Map<String,Object> param = new HashMap<>();
		if(!StringUtils.isBlank(model.getProxyName())) {
			param.put("proxyName", model.getProxyName());
		}
		Map<String,Object> rebateConfigMap = userMapper.getProxyRebate(param);
		if(rebateConfigMap != null && !StringUtil.isBlank(rebateConfigMap.get("data"))) {
			if(rebateConfigMap.get("agentGrade") != null) {
				model.setAgentGrade(Integer.valueOf(rebateConfigMap.get("agentGrade") + "") + 1);
			}
			model.setProxyId(rebateConfigMap.get("proxyId") + "");
			String autoSortUid = rebateConfigMap.get("autoSort")+"";
			rebateConfigMap = strToMap(rebateConfigMap.get("data") + "");
			for(Map<String,Object> config : rebateConfigList) {
				config.put("rebate", rebateConfigMap.get(config.get("code")));
			}
			//查询当前代理下属-最大代理编号
			String curMaxNum = userMapper.qryMaxAutoSortUidByAgentId(model.getProxyId());
			if(StringUtil.isBlank(curMaxNum)) {
				autoSortUid = autoSortUid+"000";
			}else {
				autoSortUid	= CommonTools.autoProxyHierarchyUid(curMaxNum);
			}
			model.setAutoSortUid(autoSortUid);
		}else {
			String autoSortUid = "000";
			String curMaxNum = userMapper.qryMaxAutoSortUidByAgentId("0");
			if(!StringUtil.isBlank(curMaxNum)) {
				autoSortUid	= CommonTools.autoProxyHierarchyUid(curMaxNum);
			}
			model.setAutoSortUid(autoSortUid);
		}
		String datas[] = model.getData().split("@");
		boolean flag = false;
		String rebateArr2[] = null;
		double rebate = 0.0;
		try {
			for(Map<String, Object> rebateMap : rebateConfigList) {
				boolean check = true;
				if(!StringUtil.isBlank(rebateMap.get("code"))) {
					String code = rebateMap.get("code") + "";
					for(String data : datas) {
						rebateArr2 = StringUtils.split(data, "#");
						if(code.equals(rebateArr2[0])) {
							check = false;
							rebate = Double.valueOf(rebateArr2[1]);
							if(rebate < 0 || rebate > Double.valueOf(rebateMap.get("rebate") + "")) {
								flag = true;
							}
							break;
						}
					}
				}
				if(check) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -6;
		}
		if(flag) {
			logger.info("返点信息异常--> proxyId("+ model.getProxyId() +")  rebateConfigList("+ rebateConfigList +")  datas("+ model.getData() +")");
			return -6;
		}
		/**1级代理的情况*/
		if(model.getAgentGrade() == null) {
			model.setAgentGrade(1);
		}
		String password = model.getPassword();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		password = encoder.encode(password);
		model.setPassword(password);
		model.setRegIp(request.getRemoteAddr());
		int result = userMapper.insertVipUser(model);
		/*if (result > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("USER_NAME", model.getUsername());
			map = userMapper.getRegisterId(map);
			if (map != null) {
				Long uid = (Long) map.get("UID");
				String code = ShareCodeUtil.toSerialCode(uid);
				logger.info("开始生成推广码---newPromotionCode:" + code);
				map.put("newPromotionCode", code);
				map.put("uid", uid);
				result = userMapper.registerUserAfter(map);
			} else {
				logger.info("通过会员账号获取会员id失败---model:" + model);
			}
		} else {
			logger.info("增加会员失败---model:" + model);
		}*/
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lb.sys.service.UserModelService#checkUsername(java.lang.String)
	 */
	@Override
	public int checkUsername(String username) {
		return userMapper.checkUsername(username);
	}

	// 系统风险评估_运营分析
	@Override
	public List<Map<String, Object>> getOperationUserList(Map<String, Object> param) {
		return userMapper.getOperationUserList(param);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lb.sys.service.UserModelService#queryProxyList()
	 */
	@Override
	public List<MemeberJsonResult> getAllProxyList() {
		return userMapper.getAllProxyList();
	}

	@Override
	public Map<String, Object> queryUserBetsum(String userName) {
		return userMapper.queryUserBetsum(userName);
	}

	@Override
	public List<Map<String, Object>> getRebateConfigList() {
		List<Map<String, Object>> rebateConfigList = userMapper.getRebateConfigList();
		return rebateConfigList;
	}

	private Map<String,Object> strToMap(String datas){
		Map<String,Object> map = null;
		if(!StringUtil.isBlank(datas)) {
			 map = new HashMap<String,Object>();
			for(String data : datas.split("@")) {
				if(!StringUtil.isBlank(data)) {
					String arr[] = data.split("#");
					map.put(arr[0] , arr[1]);
				}
			}
		}
		return map;
	}
	
	@Override
	public int addInvitateInfo(Map<String, Object> param) {
		
		Map<String,Object> rebateConfigMap = userMapper.getProxyRebate(param);
		if(rebateConfigMap == null || rebateConfigMap.size() <= 0) {
			logger.info("根据传入代理查询返点信息异常param:" + param);
			return -1;
		}
		if(!StringUtil.isBlank(rebateConfigMap.get("agentGrade")) && !"1".equals(rebateConfigMap.get("agentGrade").toString())) {
			logger.info("只能给一级代理添加邀请码:" + rebateConfigMap);
			return -2;
		}
		if(StringUtil.isBlank(rebateConfigMap.get("data"))) {
			logger.info("根据代理查询的返点信息为null:" + rebateConfigMap);
			return -3;
		}
		
		if(!StringUtil.isBlank(rebateConfigMap.get("userType")) && !"1".equals(rebateConfigMap.get("userType").toString())) {
			logger.info("该用户不是代理类型:" + rebateConfigMap);
			return -5;
		}
		
		String myselfRebates[] = rebateConfigMap.get("data").toString().split("@");
		String datas[] = param.get("data").toString().split("@");
		boolean flag = false;
		String rebateArr1[] = null;
		String rebateArr2[] = null;
		double rebate = 0.0;
		try {
			for(String myselfRebate : myselfRebates) {
				rebateArr1 = StringUtils.split(myselfRebate, "#");
				boolean check = true;
				if(!StringUtil.isBlank(rebateArr1[0])) {
					for(String data : datas) {
						rebateArr2 = StringUtils.split(data, "#");
						if(rebateArr1[0].equals(rebateArr2[0])) {
							check = false;
							rebate = Double.valueOf(rebateArr2[1]);
							if(rebate < 0 || rebate > Double.valueOf(rebateArr1[1])) {
								flag = true;
							}
							break;
						}
					}
				}
				if(check) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -4;
		}
		if(flag) {
			logger.info("返点信息异常-->myselfRebates("+ rebateConfigMap.get("data") +")  datas("+ param.get("data") +")");
			return -4;
		}
		param.put("uid", rebateConfigMap.get("proxyId"));
		param.put("username", rebateConfigMap.get("proxyName"));
		param.put("agentGrade", Integer.valueOf(rebateConfigMap.get("agentGrade") + "") + 1);
		
		String proxyExtensionUrl = String.valueOf(rebateConfigMap.get("proxyExtensionUrl"));
		String appKeySource = String.valueOf(rebateConfigMap.get("appKeySource"));
		int result = 0;
		if(proxyExtensionUrl != null && appKeySource != null) {
			Random r = new Random();
			for(int i = 0 ; i < 3 ; i ++) {
				int num = r.nextInt(100000000 - 1);
				param.put("invitateCode", (num + 1) * (int)(Math.pow(10, 8 - String.valueOf(num).length())));
				proxyExtensionUrl = proxyExtensionUrl + "?" + param.get("invitateCode");
				param.put("invitateUrl", proxyExtensionUrl);
				param.put("shortUrl", ShortenUrl.getSortenURl(proxyExtensionUrl, appKeySource));
				try {
					result = userMapper.addInvitateInfo(param);
				} catch (Exception e) {
					logger.info("已有该邀请码，重新生成" + e.getMessage());
					continue;
				}
				break;
			}
			
		}
		return result;
	}
	
	@Override
	public int removeInvitateInfo(Map<String, Object> param) {
		return userMapper.removeInvitateInfo(param);
	}

	@Override
	public List<Map<String, Object>> queryInvitateInfoList(Map<String, Object> param) {
		return userMapper.queryInvitateInfoList(param);
	}
	
}
