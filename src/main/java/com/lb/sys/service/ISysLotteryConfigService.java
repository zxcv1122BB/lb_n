package com.lb.sys.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.sys.model.SysLotteryConfig;
import com.lb.sys.tools.model.Message;

public interface ISysLotteryConfigService {

	Message add(HttpServletRequest request, SysLotteryConfig sysFbConfigure);

	Message update(HttpServletRequest request, SysLotteryConfig sysFbConfigure);

	Message delete(Integer id);

	List<SysLotteryConfig> querySysLotteryConfigList(Map<String, Object> map);

}
