package com.lb.log.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.log.model.LogInfo;
import com.lb.log.model.LogInfoExample;
import com.lb.log.service.LogInfoService;
import com.lb.sys.dao.LogInfoMapper;
import com.lb.sys.tools.model.Message;
/***
 * 日志业务逻辑接口实现类
 * @author tjr
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class LogInfoServiceImpl implements LogInfoService {

	@Autowired 
	private LogInfoMapper lim ; 
	
	/***
	 * 添加一条日志
	 */
	@Override
	public int insert(LogInfo record) {
		//定义标记变量  记录是否添加成功
		int flag=0;//初始值为0
		int row = lim.insert(record);
		//根据返回的影响行数 判断是否添加成功
		if(row>0) {
			flag=200;
		}else {
			flag=201;
		}
		return flag;
	}

	@Override
	public int insertSelective(LogInfo record) {
		// TODO Auto-generated method stub
		return 0;
	}
	/***
	 * 查询日志信息  查询所有的日志
	 */
	@Override
	public List<LogInfo> selectByExample(LogInfoExample example) {
		return lim.selectByExample(example);
	}

//	/***
//	 * 根据日志的编号  查询某一条日志详情
//	 */
//	@Override
//	public LogInfo selectByPrimaryKey(String logid) {
//		return lim.selectByPrimaryKey(logid);
//	}

	@Override
	public int updateByPrimaryKey(LogInfo record) {
		return lim.updateByPrimaryKey(record);
	}

	/**
	 * 添加日志
	 */
	@Override
	public Message addRiZhi(LogInfo log) {
		return lim.addRiZhi(log);
	}
	
	//查询
	@Override
	public List<LogInfo> selectAllLogs(LogInfo pam){
		return lim.selectAllLogs(pam);
    }

  
}
