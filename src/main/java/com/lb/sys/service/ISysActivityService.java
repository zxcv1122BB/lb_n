package com.lb.sys.service;


import java.util.List;
import java.util.Map;

import com.lb.sys.model.SysActivity;

/**
 * 优惠活动service
 * @author Administrator
 *
 */
public interface ISysActivityService {
	
	int deleteByPrimaryKey(Long id);

	int insert(SysActivity record);

    int insertSelective(SysActivity record);

    SysActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysActivity record);

    int updateByPrimaryKey(SysActivity record);

	List<SysActivity> selectSysActivityList(Map<String, Object> map);

	int batchSysActivityByid(List<String> ids);

}
