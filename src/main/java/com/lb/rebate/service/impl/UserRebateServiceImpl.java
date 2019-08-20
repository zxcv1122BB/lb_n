package com.lb.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.lb.rebate.service.UserRebateService;
import com.lb.sys.dao.CoinLogMapper;
import com.lb.sys.dao.GameBetsPOJOMapper;
import com.lb.sys.dao.RebateLogMapper;
import com.lb.sys.dao.SysConfigureMapper;
import com.lb.sys.dao.UserModelMapper;
import com.lb.sys.dao.UserRebateMapper;
import com.lb.sys.tools.StringUtil;

@Service
public class UserRebateServiceImpl implements UserRebateService {
	private static Logger log = LoggerFactory.getLogger(UserRebateServiceImpl.class);
	@Autowired
	CoinLogMapper coinLogMapper;
	@Autowired
	UserRebateTransactionalImpl transactionalImpl;
	@Autowired
	UserRebateMapper userRebateMapper;
	@Autowired
	RebateLogMapper rebateLogMapper;
	@Autowired
	GameBetsPOJOMapper gameBetsMapper;
	@Autowired
	UserModelMapper userModelMapper;
	@Autowired
	SysConfigureMapper sysConfigureMapper;
	private static final String prefixStr = "[批量投注返利回滚]- ";
	private final static String USER_REBATE_CONFIGURE_NAME = "userRebate";

	@Override
	public List<Map<String, Object>> qryRebateLog() {
		// TODO Auto-generated method stub
		return rebateLogMapper.qryRebateLog();
	}

	@SuppressWarnings("unused")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public boolean batchRollBack(String batch_no) {
		// TODO Auto-generated method stub
		try {
			/***
			 * U:update,I:insert,Num:影响条数 【整体事务控制】 1 查询rebate_log批量日志 2
			 * 先查询coin_log账变、计算投注返利回滚的前后金额
			 * 
			 * 3 批量修改game_bets状态,UNum == 日志.总订单数 4 批量插入coin_log,INum == 日志.用户数 5
			 * 修改user_info,INum
			 */

			// 1 查询rebate_log批量日志
			Map<String, Object> rebateLog = rebateLogMapper.qryRebateLogByBatchNo(batch_no);

			boolean continueFlag = rebateLog != null && rebateLog.get("rebate_type") != null
					&& 1 == Integer.valueOf(rebateLog.get("rebate_type").toString());// 返利类型，1：投注返利,2:投注返利回滚
			// 校验是否需要继续执行
			if (!continueFlag) {
				log.error(prefixStr + batch_no + ":" + rebateLog.entrySet());
				return false;
			}

			// 2 先查询coin_log账变、计算投注返利回滚的前后金额
			List<Map<String, Object>> coinLogList = coinLogMapper.qryUserRebateCoinLog(batch_no);
			log.info(prefixStr + "批次信息:" + rebateLog.entrySet() + ",coinLogList0:" + coinLogList.get(0).toString());

			// 3 批量修改game_bets状态,UNum == 日志.总订单数
			int gUNum = gameBetsMapper.updateOrderRebateStatus(batch_no);

			// 4 批量插入coin_log,INum == 日志.用户数
			int cINum = coinLogMapper.insertOfURebateRollback(coinLogList);
			int userCoinNum = 0;

			// 5 修改user_info,INum
			if (coinLogList != null && coinLogList.size() > 0)
				userCoinNum = userModelMapper.updateRollBackCoin(coinLogList);

			Map<String, Object> paramMap = new HashMap<>();

			paramMap.put("batch_no", batch_no);
			paramMap.put("rebate_type", 2);

			int totalOrder = Integer.valueOf(rebateLog.get("total_order").toString());
			int totalUser = Integer.valueOf(rebateLog.get("total_user").toString());

			// 【更新批量日志信息】
			int rebateLogNum = rebateLogMapper.updateStatus(paramMap);
			boolean transFlag = totalOrder == gUNum && totalUser == userCoinNum && 1 == rebateLogNum;
			if (!transFlag) {
				String errorInfo = "事务异常 —— Log记录操作用户数/订单数:" + totalUser + "/" + totalOrder + ",回滚操作数:" + userCoinNum
						+ "/" + gUNum;
				throw new RuntimeException(errorInfo);
			}
			String info = prefixStr + "日志记录-涉及总用户数:" + totalUser + ",总订单数:" + totalOrder + ",查询账变数:"
					+ coinLogList.size() + ",更新订单状态数:" + gUNum + ",修改用户余条额数:" + userCoinNum + ",修改批量日志数:"
					+ rebateLogNum;
			log.info(info);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public boolean batchBetRebate(String batch_no) {
		// TODO Auto-generated method stub
		try {

			List<Map<String, Object>> betSumList = gameBetsMapper.qryBetSumByBatchNo(batch_no);
			Map<String, Object> sysConfig = getSysConfig(USER_REBATE_CONFIGURE_NAME);
			boolean isUserRebate = null != sysConfig && !StringUtil.isBlank(sysConfig.get("on_off"))
					&& "1".equals(sysConfig.get("on_off").toString())
					&& !StringUtil.isBlank(sysConfig.get("sys_config1"))
					&& !StringUtil.isBlank(sysConfig.get("sys_config2"));
			if (!isUserRebate) {
				log.info("[批量用户返利],系统配置开关:" + sysConfig + ",暂不执行用户返利");
				return false;
			}
			Calendar sC = Calendar.getInstance();
			// 日志参数
			Set<Object> ignoreSet = new TreeSet();
			Set<Object> errorSet = new TreeSet();
			Set<Object> successSet = new TreeSet();
			int igoreNum = 0, errorNum = 0, successNum = 0;
			BigDecimal totalEffectBet = new BigDecimal(0);
			BigDecimal totalRebate = new BigDecimal(0);
			// 接受实际需要返利的数据
			List<Map<String, Object>> paramMapList = new ArrayList<>();

			/**
			 * 用户返利所需最低投注金额 后续可能配置在系统配置表中
			 */
			BigDecimal minBetSum = new BigDecimal(100);
			int minInt = 100;

			/**
			 * 投注返利操作
			 */
			BigDecimal bigDecimal0 = new BigDecimal(0);
			BigDecimal bigDecimal100 = new BigDecimal(100);

			/**
			 * 获取[用户返利所需投注金额]的系统配置
			 */
			// Map<String, Object> sysConfig = getSysConfig(MIN_REBATE_BET_SUM);
			try {
				minBetSum = new BigDecimal(sysConfig.get("sys_config1").toString());
			} catch (Exception e) {
				// TODO: handle exception
				log.error("[批量用户返利],查询 <用户返利所需最低投注金额> 报错。使用默认值:100,错误信息:" + e.getMessage());
				minBetSum = new BigDecimal(minInt);
			}

			/**
			 * 批量处理用户返利
			 */
			for (int i = 0; i < betSumList.size(); i++) {
				Map<String, Object> indexMap = betSumList.get(i);
				String UID = indexMap.get("uid") + "";
				BigDecimal sum = new BigDecimal(indexMap.get("sum").toString());
				// log.info("[批量用户返利],"+indexMap.entrySet());
				BigDecimal itemPercent = (BigDecimal) indexMap.get("percent");
				boolean moreThan0 = itemPercent.compareTo(bigDecimal0) > 0;
				boolean moreThanMinBet = sum.compareTo(minBetSum) >= 0;
				Integer num = Integer.valueOf(String.valueOf(indexMap.get("num")));
				BigDecimal multiply = sum.multiply(itemPercent.divide(bigDecimal100));
				BigDecimal addCoin = multiply.setScale(3, BigDecimal.ROUND_HALF_UP);
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("UID", UID);
				paramMap.put("user_name", indexMap.get("USER_NAME"));
				paramMap.put("user_type", indexMap.get("USERTYPE"));
				paramMap.put("num", num);
				paramMap.put("sum", sum);
				paramMap.put("initCoin", indexMap.get("COIN"));
				paramMap.put("initFCoin", indexMap.get("FCION"));
				paramMap.put("percent", itemPercent);
				paramMap.put("addCoin", addCoin);
				paramMap.put("moreThan0", moreThan0);
				paramMap.put("moreThanMinBet", moreThanMinBet);
				paramMap.put("user_rebate_id", batch_no);
				/**
				 * 区分订单投注返利类型 0未返利,1已返利，2不满足返利条件，3不支持返利的彩种
				 * 
				 */
				String sysConfig2 = sysConfig.get("sys_config2").toString();
				List<String> config2 = new ArrayList<>(Arrays.asList(sysConfig2.split(",")));

				paramMap.put("rebateStatus", moreThan0 && moreThanMinBet ? 1 : 2);// ==为true。用户返利状态:0未返利,1已返利，2不满足返利条件

				paramMapList.add(paramMap);
				if (!moreThan0 || !moreThanMinBet) {
					boolean flag = transactionalImpl.userRebateTransactional(paramMap);// 事务
					if (!flag) {
						errorSet.add(UID);
						errorNum += num;
					} else {
						ignoreSet.add(UID);
						igoreNum += num;
						totalEffectBet = totalEffectBet.add(sum);
						totalRebate = totalRebate.add(addCoin);
					}
				} else {
					boolean flag = transactionalImpl.userRebateTransactional(paramMap);// 事务
					if (!flag) {
						errorSet.add(UID);
						errorNum += num;
					} else {
						successSet.add(UID);
						successNum += num;
						totalEffectBet = totalEffectBet.add(sum);
						totalRebate = totalRebate.add(addCoin);
					}
				}

			}
			Calendar eC = Calendar.getInstance();
			int total_order = successNum + igoreNum + errorNum;
			int total_user = successSet.size() + ignoreSet.size() + errorSet.size();
			String info = "[批量用户返利]," + "已/待处理用户:" + total_user + "/" + betSumList.size() + "。耗时:"
					+ (eC.getTimeInMillis() - sC.getTimeInMillis()) + "毫秒." + "成功用户ID:" + successSet + ",处理成功订单数:"
					+ successNum + ".忽略用户ID:" + ignoreSet + ",忽略处理订单数:" + igoreNum + ".失败用户ID:" + errorSet + ",处理失败订单数:"
					+ errorNum + "。非返利订单数:";
			log.info(info);

			/**
			 * 记录执行批量投注返利的log
			 */

			Map<String, Object> rebateMap = new HashMap<>();

			rebateMap.put("info", info);

			rebateMap.put("batch_no", batch_no);
			rebateMap.put("rebate_type", 1);// 用户投注返利
			rebateMap.put("total_user", total_user);
			rebateMap.put("total_order", total_order);// 总处理订单数--包含失败
			rebateMap.put("effect_bet", totalEffectBet);// --实际处理的总投注金额
			rebateMap.put("total_rebate", totalRebate);// 总返利金额

			rebateMap.put("success_num", successNum);
			rebateMap.put("ignore_num", igoreNum);
			rebateMap.put("error_num", errorNum);

			log.info("" + rebateMap.entrySet());
			
			int insertCoinLog = rebateLogMapper.updateStatus(rebateMap);
			if (insertCoinLog != 1 || total_user != betSumList.size()) {
				throw new RuntimeException(
						"事务异常,记录日志数：" + insertCoinLog + "，已/待处理用户:" + total_user + "/" + betSumList.size());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return true;
	}

	public Map<String, Object> getSysConfig(String configureName) {
		Map<String, Object> qryByConfigure = new HashMap<>();
		qryByConfigure = sysConfigureMapper.qryByConfigure(configureName);
		return qryByConfigure;
	}
}
