/**
 * 
 */
package com.lb.sys.service;

import java.util.List;

import com.lb.sys.model.SysRegisterConfigure;

/**
 * @author wz
 * @describe 
 * @date 2017年9月22日
 */
public interface ISysRegisterConfigureService {
	List<SysRegisterConfigure> queryRegisterConfigure(Integer type);

	int updateRegisterConfigure(SysRegisterConfigure configure);
}
