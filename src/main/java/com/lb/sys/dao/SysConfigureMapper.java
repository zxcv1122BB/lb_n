/**
 * 
 */
package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.SysConfigure;
import com.lb.sys.model.SysConfigureModule;

/**
 * @describe 系统开关dao层接口
 */
public interface SysConfigureMapper {
	
	List<SysConfigure> querySysConfigure(Map<String, Object> map);

	int updateSysConfigure(SysConfigure configure);

	List<SysConfigureModule> querySysConfigureModule();
	
	Byte websiteSwitchStatus();
	
	Map<String,Object> getOnlyConfigure(Map<String, Object> map);
	
	Map<String,Object> getSysByConfigure(@Param("configure") String configure);
	
	List<Map<String,Object>> getFfcConfigure();
	
	Map<String,Object> qryByConfigure(String configureName);

	int resetPrivateKey(Map<String, Object> map);
}
