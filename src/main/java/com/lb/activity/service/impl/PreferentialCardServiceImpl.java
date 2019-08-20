package com.lb.activity.service.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.lb.activity.service.IPreferentialCardService;
import com.lb.download.model.PreferentialCardRecord;
import com.lb.sys.dao.PreferentialCardMapper;
import com.lb.sys.dao.RedPacketManagementMapper;
import com.lb.sys.tools.RandomNum;
import com.lb.sys.tools.model.Message;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class PreferentialCardServiceImpl implements IPreferentialCardService{
	
	private final Log LOGGER = LogFactory.getLog(RedPacketManagementServiceImpl.class);
	
	@Autowired
	private RedPacketManagementMapper redPacketManagementMapper;
	@Autowired
	private PreferentialCardMapper preferentialCardMapper;

	@Override
	public List<Map<String, Object>> queryPreferentialCard(Map<String, Object> map1) {
		List<Map<String, Object>> list =preferentialCardMapper.queryPreferentialCard(map1);
		for (Map<String, Object> map : list) {
			List<Integer> vipIdList = new ArrayList<>();	
			String vipIds = String.valueOf(map.get("vips"));
			if(!StringUtils.isEmpty(vipIds)) {
				String[] splits = vipIds.split(",");
				for (String split : splits) {
					try {
						vipIdList.add(Integer.valueOf(split));
					} catch (Exception e) {
						
					}
				}
				String vipName=redPacketManagementMapper.getVipName(vipIdList);
				map.put("vips", vipName);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Message addPreferentialCard(HttpServletRequest request, Map<String, Object> map) {
		Message message =new Message();
		//优惠卡张数
		int count=Integer.valueOf(map.get("count").toString());
		//序号orderNumber及优惠卡密码password的集合
		List<Map<String, Object>> list=new ArrayList<>();
		long currentTimeMillis = System.currentTimeMillis();
		map.put("batch", currentTimeMillis);
		for(int i=1;i<=count;i++) {
			Map<String, Object> xuMap=new HashedMap();
			//生成优惠卡密码
			xuMap.put("account",RandomNum.toSerialCode(currentTimeMillis+i));
			xuMap.put("password", RandomNum.genRandomPassWord(6));
			xuMap.put("orderNumber", i);
			list.add(xuMap);
		}
		map.put("passwordList", list);
		int insertSelective = preferentialCardMapper.addPreferentialCard(map);
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("添加成功");
		}else {
			message.setCode(310);
			message.setMsg("添加失败");
			LOGGER.error("添加失败");
		}
		return message;
	}
	@Override
	public Message updatePreferentialCard(HttpServletRequest request, Map<String, Object> map) {
		Message message =new Message();
		int update= preferentialCardMapper.updatePreferentialCard(map);
		if(update>0) {
			message.setCode(200);
			message.setMsg("更新成功");
		}else {
			message.setCode(311);
			message.setMsg("编辑失败");
			LOGGER.error("编辑失败");
		}
		return message;
	}

	@Override
	public Message deletePreferentialCard(HttpServletRequest request, Integer id) {
		Message message =new Message();
		int update= preferentialCardMapper.deletePreferentialCard(id);
		if(update>0) {
			message.setCode(200);
			message.setMsg("删除成功");
		}else {
			message.setCode(311);
			message.setMsg("删除失败");
			LOGGER.error("删除失败");
		}
		return message;
	}

	@Override
	public List<PreferentialCardRecord> exportPreferentialCard(Map<String, Object> map1) {
		List<PreferentialCardRecord> list=preferentialCardMapper.exportPreferentialCard(map1);
		for (PreferentialCardRecord map : list) {
			List<Integer> vipIdList = new ArrayList<>();	
			String vipIds = map.getVips();
			if(!StringUtils.isEmpty(vipIds)) {
				String[] splits = vipIds.split(",");
				for (String split : splits) {
					try {
						vipIdList.add(Integer.valueOf(split));
					} catch (Exception e) {
						
					}
				}
				String vipName=redPacketManagementMapper.getVipName(vipIdList);
				map.setVips(vipName);
			}
		}
		return list;
	}
}
