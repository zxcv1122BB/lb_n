package com.lb.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.SysActivityMapper;
import com.lb.sys.model.SysActivity;
import com.lb.sys.model.SysActivityExample;
import com.lb.sys.service.ISysActivityService;
/**
 * 优惠活动serviceImpl
 * @author Administrator
 *
 */
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SysActivityServiceImpl implements ISysActivityService {

	private final Log LOGGER = LogFactory.getLog(SysActivityServiceImpl.class);
	
	@Autowired
	private SysActivityMapper sysActivityMapper;
	
	//逻辑删除
	@Override
	public int deleteByPrimaryKey(Long id) {
		SysActivity sysActivity = new SysActivity();
		sysActivity.setDelStatus(Byte.parseByte("0"));
		sysActivity.setId(id);
		sysActivity.setUpdateDate(new Date());
		return sysActivityMapper.updateByPrimaryKeySelective(sysActivity);
	}

	@Override
	public int insert(SysActivity record) {
		record.setCreateDate(new Date());
		return sysActivityMapper.insert(record);
	}

	//选择性添加,206优惠活动添加失败
	@Override
	public int insertSelective(SysActivity record) {
		record.setCreateDate(new Date());
		return sysActivityMapper.insertSelective(record);
	}

	//根据主键查询
	@Override
	public SysActivity selectByPrimaryKey(Long id) {
		SysActivity sysActivity = sysActivityMapper.selectByPrimaryKey(id);
		if( sysActivity!=null && "1".equals(String.valueOf(sysActivity.getDelStatus()))) {
			return sysActivity;
		}else {
			return null;
		}
	}

	//根据主键选择性更新
	@Override
	public int updateByPrimaryKeySelective(SysActivity record) {
		record.setUpdateDate(new Date());
		return sysActivityMapper.updateByPrimaryKeySelective(record);
	}

	//根据主键所有的更新
	@Override
	public int updateByPrimaryKey(SysActivity record) {
		record.setUpdateDate(new Date());
		return sysActivityMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<SysActivity> selectSysActivityList(Map<String,Object> map) {
		map.put("delStatus", Byte.parseByte("1"));
		List<SysActivity> sysActivtys = sysActivityMapper.querSysActivity(map);
		if(sysActivtys!=null && sysActivtys.size()>0) {
			return sysActivtys;
		}else {
			return null;
		}
	}

	//批量删除
	@Override
	public int batchSysActivityByid(List<String> ids) {
		List<Long> listIds = new ArrayList<>();
		if(ids!=null && ids.size()>0) {
			for (String id : ids) {
				if(id!=null) {
					listIds.add(Long.parseLong(id));
				}
			}
		
			SysActivity record = new SysActivity();
			record.setDelStatus(Byte.valueOf("0"));
			SysActivityExample example = new SysActivityExample();
			example.createCriteria().andIdIn(listIds);
			return sysActivityMapper.updateByExampleSelective(record, example);
		}else {
			LOGGER.error("批量删除失败");
			return -1;
		}
	}
	
	

}
