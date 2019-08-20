package com.lb.sys.service;

import java.util.List;
import java.util.Map;

import com.lb.sys.model.SysMessageText;

/**
 * 站内信service
 * @author jiangheng
 *
 */
public interface ISysMessageService {

	int insertSelective(SysMessageText sysMessageText);

	int updateSysMessageText(SysMessageText sysMessageText);

	int deleteSysMessageTextByid(long id);
	
	SysMessageText selectSysMessageTextByid(Long id);

	List<SysMessageText> selectSysBulletinList();

	int batchSysMessageTextByid(List<String> ids);

	List<SysMessageText> selectByTitle(String title);
	/**
	 * 查询所有需要推送的消息
	 * @param map 参数集合
	 * @return 消息集合
	 */
    List<SysMessageText> selectAllContexts(Map<String,Object> map);
}
