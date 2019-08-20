package com.lb.activity.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.download.model.PreferentialCardRecord;
import com.lb.sys.tools.model.Message;

public interface IPreferentialCardService {

	List<Map<String, Object>> queryPreferentialCard(Map<String, Object> map);

	Message addPreferentialCard(HttpServletRequest request, Map<String, Object> map);

	Message updatePreferentialCard(HttpServletRequest request, Map<String, Object> redPacket);

	Message deletePreferentialCard(HttpServletRequest request, Integer redPacketId);

	List<PreferentialCardRecord> exportPreferentialCard(Map<String, Object> map);

}
