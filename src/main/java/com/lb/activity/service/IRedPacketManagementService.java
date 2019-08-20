package com.lb.activity.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.activity.model.RedPacketManagement;
import com.lb.sys.pojo.RedPacketPojo;
import com.lb.sys.tools.model.Message;

public interface IRedPacketManagementService {

	List<Map<String, Object>> queryAllRedPacketManagement();

	Message addRedPacketManagement(HttpServletRequest request, Map<String, Object> map);

	RedPacketManagement queryRedPacketManagementById(Integer redPacketId);

	Message updateRedPacketManagement(HttpServletRequest request, RedPacketManagement redPacket);

	Message deleteRedPacketManagement(HttpServletRequest request, Integer redPacketId);

	Message isStartRedPacketManagement(HttpServletRequest request, Integer redPacketId, Byte state);

	List<RedPacketPojo> isExistRedPacket(Map<String, Object> map);

	Message fetchRedPacket(HttpServletRequest request, Map<String, Object> map);

}
