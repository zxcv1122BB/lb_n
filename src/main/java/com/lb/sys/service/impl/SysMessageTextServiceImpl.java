package com.lb.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lb.sys.dao.SysMessageTextMapper;
import com.lb.sys.dao.UserModelMapper;
import com.lb.sys.dao.UserVipInfoMapper;
import com.lb.sys.model.SysMessageText;
import com.lb.sys.model.SysMessageTextExample;
import com.lb.sys.service.ISysMessageService;

/**
 * 站内信得发送
 * 
 * @author jiangheng
 *
 */
@Service
public class SysMessageTextServiceImpl implements ISysMessageService {

	private final Log LOGGER = LogFactory.getLog(SysMessageTextServiceImpl.class);

	// 站内信内容表
	@Autowired
	private SysMessageTextMapper sysMessageTextMapper;

	@Autowired
	private UserModelMapper userModelMapper;
	
	@Autowired
	private UserVipInfoMapper userVipInfoMapper;

	@Override
	public int insertSelective(SysMessageText sysMessageText) {
		sysMessageText.setCreateDate(new Date());
		sysMessageText.setSendDate(new Date());
		return sysMessageTextMapper.insertSelective(sysMessageText);
	}

	// 更新站内消息
	@Override
	public int updateSysMessageText(SysMessageText sysMessageText) {
		sysMessageText.setUpdateDate(new Date());
		return sysMessageTextMapper.updateByPrimaryKeySelective(sysMessageText);
	}

	//删除站内信,若是单发需要删除表记录表中的内容
	@Override
	public int deleteSysMessageTextByid(long id) {
		SysMessageText sysMessageText = new SysMessageText();
		sysMessageText.setId(id);
		sysMessageText.setDelStatus(Byte.valueOf("0"));
		sysMessageText.setUpdateDate(new Date());
		return sysMessageTextMapper.updateByPrimaryKeySelective(sysMessageText);
	}

	//查询站内信
	@Override
	public SysMessageText selectSysMessageTextByid(Long id) {
		SysMessageText sysMessageText = sysMessageTextMapper.selectByPrimaryKey(id);
		if(sysMessageText!=null && "1".equals(String.valueOf(sysMessageText.getDelStatus()))) {
			if(sysMessageText.getSendType()==1) {//单发
				List<Long> userids = subIds(sysMessageText.getReceiver());
				List<String> usernames = userModelMapper.queryUserNameByids(userids);
				sysMessageText.setReceiverName(usernames);
			}else if(2==sysMessageText.getSendType()) {//组发
				List<Long> userVIPids = subIds(sysMessageText.getReceiver());
				List<Integer> vipids = uservipids(userVIPids);
				List<String> uservipnames = userVipInfoMapper.queryVIpNameByids(vipids);
				sysMessageText.setReceiverName(uservipnames);
			}
			return sysMessageText;
		}
		return null;
	}


	@Override
	public List<SysMessageText> selectSysBulletinList() {
		SysMessageTextExample example = new SysMessageTextExample();
		example.setOrderByClause("send_date DESC");
		example.createCriteria().andDelStatusEqualTo(Byte.valueOf("1"));
		return sysMessageTextMapper.selectByExample(example);
	}

	@Override
	public int batchSysMessageTextByid(List<String> ids) {
		List<Long> listIds = new ArrayList<>();
		if(ids!=null && ids.size()>0) {
			for (String id : ids) {
				if(id!=null) {
					listIds.add(Long.parseLong(id));
				}
			}
			SysMessageText record = new SysMessageText();
			record.setDelStatus(Byte.valueOf("0"));
			SysMessageTextExample example = new SysMessageTextExample();
			example.createCriteria().andIdIn(listIds);
			return sysMessageTextMapper.updateByExampleSelective(record, example);
		}else {
			LOGGER.error("批量删除失败");
			return -1;
		}
	}

	@Override
	public List<SysMessageText> selectByTitle(String title) {
		Map<String,Object> map = new HashMap<>();
		map.put("title", title);
		map.put("delStatus", Byte.parseByte("1"));
		List<SysMessageText> sysBulletList = sysMessageTextMapper.querySysMessageTextByName(map);
		return sysBulletList;
	}
		
	private  List<Long> subIds(String receiptids) {
		String[] userids = receiptids.split(",");
		List<Long> useridlist= new ArrayList<>();
		for (String id : userids) {
			if(id!=null && !"".equals(id)) {
				useridlist.add(Long.parseLong(id.trim()));
			}
		}
		return useridlist;
	}
	
	private List<Integer> uservipids(List<Long> userVIPids) {
		List<Integer> vipids = null;
		if(userVIPids!=null) {
			vipids = new ArrayList<>();
			for (Long vipid : userVIPids) {
				vipids.add(vipid.intValue());
			}
		}
		return vipids;
	}

	//查询所有站内信
	@Override
	public List<SysMessageText> selectAllContexts(Map<String, Object> map) {
		return sysMessageTextMapper.selectAllContexts(map);
	}
	
	
	
}
