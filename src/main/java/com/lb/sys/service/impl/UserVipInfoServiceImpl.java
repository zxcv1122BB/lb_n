package com.lb.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.UserVipInfoMapper;
import com.lb.sys.model.UserVipInfo;
import com.lb.sys.service.UserVipInfoService;
import com.lb.sys.tools.model.Message;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class UserVipInfoServiceImpl implements UserVipInfoService {

	@Autowired
	private UserVipInfoMapper userVipInfoMapper;


	/***
	 * 查询所有vip分类信息
	 */
	@Override
	public List<Map<String,Object>> selectVips() {
		return userVipInfoMapper.selectVips();
	}

	/***
	 * 删除
	 */
	@Override
	public Message deleteVip(Integer vipId) {
		return userVipInfoMapper.deleteVip(vipId);
	}

	/****
	 * 禁用或者开启
	 */
	@Override
	public Message upVip(Map<String, Integer> map) {
		int upVip = userVipInfoMapper.upVip(map); // 获取影响行数
		if (upVip > 0) {
			return new Message(200, "操作成功");
		} else {
			return new Message(201, "操作失败");
		}
	}

	/***
	 * 修改基本信息
	 */
	@Override
	public Message updateVIP(UserVipInfo record) {
		int restInt = userVipInfoMapper.updateByPrimaryKeySelective(record);
		// 判断是否操作成功
		if (restInt > 0) {
			// 大于0则表示操作成功
			return new Message(200, "操作成功");
		} else {
			return new Message(201, "操作失败");
		}
	}

	/***
	 * 查询某一个状态下的所有vip信息
	 */
	@Override
	public List<UserVipInfo> selectAllVipByS0(Integer statu) {
		return userVipInfoMapper.selectAllVipByS0(statu);
	}

	// 添加一个会员信息
	@Override
	public Message addUserVip(UserVipInfo map) {

		// 首先查询是否有重名 vipname
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("vipname", map.getVipname());
		// 查询会员grout是否已经存在
		int selectCountForGroup = userVipInfoMapper.selectCountForGroup(map.getGroup());
		if (selectCountForGroup > 0) {
			return new Message(202, "操作失败,会员等级唯一标识不能重复");
		}
		int selectCount = userVipInfoMapper.selectCount(parm);
		if (selectCount <= 0) {
			int restInt = userVipInfoMapper.addUserVip(map);
			// 判断是否添加成功
			if (restInt > 0) {
				return new Message(200, "操作成功");
			} else {
				return new Message(201, "操作失败");
			}
		} else {
			return new Message(202, "操作失败,已存在该会员等级名称");
		}
	}

	/***
	 * 修改会员的等级信息
	 */
	@Override
	public Message updateUserVip(Map<String, Object> map) {
		// 查询会员grout是否已经存在
		int selectCount = userVipInfoMapper.selectCount(map);
		if (selectCount <= 0) {
			int restCoun = userVipInfoMapper.updateUserVip(map);
			// 判断是否修改成功
			if (restCoun > 0) {
				return new Message(200, "操作成功");
			} else {
				return new Message(201, "操作失败");
			}
		} else {
			return new Message(202, "操作失败,已存在该会员等级名称");
		}
	}

	/***
	 * 根据编号查询
	 */
	@Override
	public UserVipInfo selectbyId(Integer vipId) {
		return userVipInfoMapper.selectbyId(vipId);
	}
}
