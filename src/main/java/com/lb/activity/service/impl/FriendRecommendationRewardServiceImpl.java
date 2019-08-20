package com.lb.activity.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.activity.service.IFriendRecommendationRewardService;
import com.lb.sys.dao.FriendRecommendationRewardMapper;
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class FriendRecommendationRewardServiceImpl implements IFriendRecommendationRewardService{
	
	@Autowired
	private FriendRecommendationRewardMapper friendRecommendationRewardMapper;

	@Override
	public List<Map<String, Object>> queryFriendRecommendationRewardList(Map<String, Object> map) {
		return friendRecommendationRewardMapper.queryFriendRecommendationRewardList(map);
	}



}
