/**
 * 
 */
package com.lb.sys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lb.sys.model.SysConfigure;
import com.lb.sys.model.SysConfigureModule;

/**
 * @author wz
 * @describe 
 * @date 2017年9月20日
 */
public interface ISysConfigureService {
	
	List<SysConfigure> querySysConfigure(Map<String, Object> map);

	int updateSysConfigure(Collection<SysConfigure> configure);

	List<SysConfigureModule> querySysConfigureModule();
	
	Map<String,Object> getOnlyConfigure(Map<String, Object> map);
	
	Map<String,Object> qryByConfigure(String configureName);

	int resetPrivateKey(Map<String, Object> map);
}
