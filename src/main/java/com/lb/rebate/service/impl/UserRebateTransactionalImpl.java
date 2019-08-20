package com.lb.rebate.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.lb.rebate.service.IUserRebateTransactionalService;
import com.lb.sys.dao.UserRebateMapper;


@Service
public class  UserRebateTransactionalImpl implements IUserRebateTransactionalService {
	private final static Logger log = LoggerFactory.getLogger(UserRebateTransactionalImpl.class);
	@Autowired UserRebateMapper userRebateMapper;	
	@Override
	 
	@Transactional(isolation =Isolation.REPEATABLE_READ,propagation = Propagation.NESTED, readOnly = false, rollbackFor = Exception.class)
	public boolean userRebateTransactional(Map<String, Object> paramMap) {
		
		int qryGameBetNum=0,gameBetNum=0,userCoinNum=0,coinLogNum=0;
		try {
			
			boolean moreThan0 = (boolean) paramMap.get("moreThan0");
			boolean moreThanMinBet = (boolean) paramMap.get("moreThanMinBet");
			qryGameBetNum = Integer.valueOf(paramMap.get("num").toString());
			gameBetNum = userRebateMapper.updateGameBetStatus(paramMap);
			if(qryGameBetNum != gameBetNum) {
				throw new RuntimeException("查询有效投注订单数与实际修改订单数不一致,有效:实际=="+qryGameBetNum+":"+gameBetNum);				
			}
			if(moreThan0 && moreThanMinBet) {				
				userCoinNum = userRebateMapper.updateUserCoin(paramMap);			
				coinLogNum = userRebateMapper.insertCoinLog(paramMap);
				if(1!=userCoinNum || 1!=coinLogNum) {
					throw new RuntimeException("CoinLog与UserInfo操作条数不一致,CoinLog:UserInfo=="+coinLogNum+":"+userCoinNum);
				}
			}
			log.info((moreThan0 && moreThanMinBet) ? "【返利】":"【不符返利条件】"+"。UID:"+paramMap.get("UID")+",返利金额:"+paramMap.get("addCoin")+",总投注金额"+paramMap.get("sum")+",返利点%:"+paramMap.get("percent"));			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("[用户返利]异常，事务回滚。报错信息:"+e.getMessage());
			/*
			 * log.error("[用户返利],出现异常:"+e.getMessage()+"。请检查SQL生效条数或SQL语句:"+"qryGameBetNum:"+qryGameBetNum+",gameBetNum:"+gameBetNum
					+",userCoinNum:"+userCoinNum+",coinLogNum:"+coinLogNum+"。SQL参数:"+paramMap.entrySet());
					*/			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return false;
	}

	
}
