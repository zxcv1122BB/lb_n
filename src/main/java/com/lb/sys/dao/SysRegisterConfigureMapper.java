/**
 * 
 */
package com.lb.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.SysRegisterConfigure;

/**
 * @author wz
 * @describe 系统注册方式设置dao层接口
 * @date 2017年9月20日
 */
public interface SysRegisterConfigureMapper {
	
	List<SysRegisterConfigure> queryRegisterConfigure(@Param("type") Integer type);

	int updateRegisterConfigure(SysRegisterConfigure configure);
	
}
