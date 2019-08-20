package com.lb.activity.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.activity.model.AppDownloadGiving;
import com.lb.sys.tools.model.Message;

public interface IAppDownloadGivingService {

	List<Map<String, Object>> queryAppDownloadGivingList(Map<String, Object> map);

	Message addAppDownloadGiving(HttpServletRequest request, AppDownloadGiving appDownloadGiving);

	Map<String, Object> queryAppDownloadGivingById(Integer id);

	Message updateAppDownloadGiving(HttpServletRequest request, AppDownloadGiving appDownloadGiving);

	Message deleteAppDownloadGiving(HttpServletRequest request, Integer id);

	Message isStartAppDownloadGiving(HttpServletRequest request, Integer id, Byte state);

}
