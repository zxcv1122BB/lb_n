package com.lb.sys.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.ProxyInfoMapper;
import com.lb.sys.model.ProxyInfo;
import com.lb.sys.model.UserVipModel;
import com.lb.sys.service.IProxyInfoService;
import com.lb.sys.tools.IPSeeker;
import com.lb.sys.tools.SocketUtils;

import net.sf.json.JSONObject;

/**
 * 代理管理serviceImpl
 * 
 * @author ZPJ
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class ProxyInfoServiceImpl implements IProxyInfoService {

	@Autowired
	private ProxyInfoMapper mapper;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public List<UserVipModel> selectByProxyInfo(Map<String, Object> map) {
		List<UserVipModel> queryUserList = mapper.selectByProxyInfo(map);
		
		for (UserVipModel userVipModel : queryUserList) {
			String username = userVipModel.getUsername();
			String reply = SocketUtils.getUserInfo("API.GetUserLastLoginDetail", username);
			if (StringUtils.isNotEmpty(reply)) {
				JSONObject replyJSON = JSONObject.fromObject(reply);
				// 最后登录时间赋值
				JSONObject itemJSON = replyJSON.getJSONObject(username);
				String loginTime = itemJSON.containsKey("loginTime") ? itemJSON.getString("loginTime") : "";
				if (StringUtils.isNotEmpty(loginTime) && replyJSON.containsKey(username)) {
					try {
						userVipModel.setLoginTime(sdf.parse(loginTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					userVipModel.setLoginTime(null);
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
						e.printStackTrace();
					}
				}
				userVipModel.setLoginIp(LAST_LOGIN_IP);
				userVipModel.setLoginAddress(json_result);
			}
		}
		return queryUserList;
	}

	@Override
	public int deleteByProxyInfo(ProxyInfo proxy) {
		return mapper.deleteByPrimaryKey(proxy.getId());
	}

	@Override
	public int insertProxyInfo(ProxyInfo proxy) {
		int result = mapper.insertSelective(proxy);
		return result;
	}

	@Override
	public int updateByProxyInfo(ProxyInfo proxy) {
		return mapper.updateByPrimaryKeySelective(proxy);
	}

	@Override
	public int updateByStatus(ProxyInfo proxy) {
		proxy.setUpdataTime(new Date());
		return mapper.updateByStatus(proxy);
	}

	@Override
	public List<Map<String, Object>> getProxyUserList(Map<String, Object> param) {

		return mapper.getProxyUserList(param);
	}

	@Override
	public List<Map<String, Object>> selectProxyInfoAll() {
		return mapper.selectProxyInfoAll();
	}

	@Override
	public int checkAccount(Map<String, Object> map) {
		return mapper.checkAccount(map);
	}

	@Override
	public boolean updatePassword(Map<String, Object> map) {
		// 密码类型
		String passwordType = String.valueOf(map.get("passwordType"));
		// 处理账号密码
		if ("1".equals(passwordType)) {
			// 此时map包含userName与password
			// 对新密码进行加密然后再次存入map
			String password = String.valueOf(map.get("password"));
			// 创建一个加密对象
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			password = encoder.encode(password);
			map.put("password", password);
			// 更新密码
			int update = mapper.updatePassword(map);
			if (update > 0) {
				return true;
			}
		}
		// 处理提款密码
		if ("2".equals(passwordType)) {
			// 此时map包含userName与password
			// 对新密码进行加密然后再次存入map
			String coinPssword = String.valueOf(map.get("password"));
			// 创建一个加密对象
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			coinPssword = encoder.encode(coinPssword);
			map.put("coinPssword", coinPssword);
			// 更新密码
			int update = mapper.updatePassword(map);
			if (update > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ProxyInfo getProxyById(Integer pid) {
		return mapper.selectByPrimaryKey(pid);
	}

	@Override
	public String queryProxyIds(Integer type,Integer uid) {
		return mapper.queryProxyIds(type,uid);
	}
}
