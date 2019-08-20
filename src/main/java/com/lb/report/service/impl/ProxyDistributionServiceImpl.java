package com.lb.report.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.member.model.CoinLog;
import com.lb.redis.JedisClient;
import com.lb.report.model.GlobalCount;
import com.lb.report.model.TeamCount;
import com.lb.report.service.ProxyDistributionService;
import com.lb.sys.dao.CoinLogMapper;
import com.lb.sys.dao.ProxyDistributionMapper;
import com.lb.sys.dao.ProxyInfoMapper;
import com.lb.sys.dao.SysConfigureMapper;
import com.lb.sys.model.SysConfigure;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.StringUtil;
import com.lb.sys.tools.model.Message;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class ProxyDistributionServiceImpl implements ProxyDistributionService {

	private final Log LOGGER = LogFactory.getLog(ProxyDistributionServiceImpl.class);
	
	@Autowired
	private ProxyDistributionMapper proxyDistributionMapper;
	@Autowired
	private ProxyInfoMapper proxyInfoMapper;
	@Autowired
	private SysConfigureMapper sysConfigureMapper;
	@Autowired
	private CoinLogMapper coinLogMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public Message insertProxyDistribution(Map<String, Object> paramMap) {
		Message msg = new Message();
		try {
			paramMap.put("moduleId", 9);
			//判断是否开启代理商返利模式
			List<SysConfigure> configures = this.sysConfigureMapper.querySysConfigure(paramMap);
			Byte onOff = 0;//默认未开启
			String sysConfig = "1"; //默认按投注流水模式
			for(SysConfigure configure:configures) {
				//代理返利模式
				if(configure.getConfigure().equals("rebackType")) {
					onOff = configure.getOnOff();
					sysConfig = configure.getSysConfig1();
					break;
				}
			}
			if(onOff == 0) {//未开启返利模式
				msg.setCode(201);
				msg.setMsg("还未开启代理商返利模式");
				return msg;
			}
			
			//根据时间区间查询代理商所发展的会员信息
			List<Map<String,Object>> distributions = proxyInfoMapper.queryProxyInfoUserList(paramMap);
			if(distributions!=null && distributions.size()>0) {
				for(int i =0;i<distributions.size();i++) {
					//会员信息
					Map<String,Object> distribution = distributions.get(i);
					Map<String, Object> disMap = new HashMap<>();
					disMap.put("uid", distribution.get("uid"));
					disMap.put("startDate", paramMap.get("startDate"));
					disMap.put("endDate", paramMap.get("endDate"));
					BigDecimal rebateRatio = new BigDecimal(distribution.get("rebateRatio")+"");
					BigDecimal amount  = new BigDecimal(sysConfig.equals("1")?distribution.get("userBettingAmount")+"":
						distribution.get("userCaseOutAmount")+"");
					Integer agentId = Integer.valueOf(distribution.get("agentId")+"");
					//调用递归计算代理商分销
					String distributionInfo = getDistributionInfo(agentId,rebateRatio, sysConfig, amount,new BigDecimal("0.00"));
					//截掉最后一位#
					distributionInfo = distributionInfo!=null?
							distributionInfo.substring(0,distributionInfo.length()-1):"";
						disMap.put("amount", amount);
					//格式：1-1-27.002#2-1-28.002 含义：代理商id-返点类型type-返点金额money
					if(distributionInfo!=null && !distributionInfo.equals("")) {
						String[] infos = distributionInfo.split("#");
						for(String info :infos) {
							String[] infos_$ = info.split("-");
							String agentId_$ = infos_$[0];
						    String rebateType = infos_$[1];
							BigDecimal coin = new BigDecimal(infos_$[2]);
							BigDecimal rebateNum = new BigDecimal(infos_$[3]);
							disMap.put("rebatesAmount", coin);
							disMap.put("rebatesType", rebateType);
							disMap.put("rebatesNum", rebateNum);
							disMap.put("agentId", agentId_$);
							this.proxyDistributionMapper.insert(disMap);
						}
					}
				}
				msg.setCode(200);
				msg.setMsg("成功");
			}
			else {
				msg.setCode(201);
				msg.setMsg("未查询到相关信息");
			}
		} catch (NumberFormatException e) {
			LOGGER.error("新增代理商分销信息出现异常："+e.getMessage());
			msg.setCode(201);
			msg.setMsg("失败");
		}
		return msg;
	}

	
	/**
	 * 递归计算代理商分销
	 * @param agentId 代理商id
	 * @param upRebateRatio 上一级代理商返点比例
	 * @param sysConfig 返利模式 1=按投注流水 2=按存款
	 * @param amount 投注流水总金额 或存款总金额
	 * @param rebateRatio 当前代理商返点比例
	 * @return
	 */
	private String getDistributionInfo(Integer agentId,BigDecimal upRebateRatio,
			String sysConfig,BigDecimal amount,BigDecimal rebateRatio) {
		//查询当前代理商上级代理 如果当前代理商id为0 则无需再次查询
		Map<String,Object> proxyInfo = agentId>0?proxyInfoMapper.queryProxyInfoById(agentId):null;
		//上一级代理商返点比例-当前代理商返点比例 由于单位为% 所以应除以100 .divide(new BigDecimal("100"))
		//保留两位小数点
		BigDecimal mRebateRation = (upRebateRatio.subtract(rebateRatio));
		BigDecimal mRebateRation_$=mRebateRation.divide(new BigDecimal("100")).multiply(amount);
		String distributionInfo =proxyInfo!=null?agentId+"-"+sysConfig+"-"+
					mRebateRation_$.setScale(2,BigDecimal.ROUND_HALF_UP)+"-"+mRebateRation+"#":"";
		if(proxyInfo!=null && !proxyInfo.isEmpty()) {
			Integer agentId_$ = Integer.valueOf(proxyInfo.get("pid")+"");
			String prebateRatio = proxyInfo.get("prebateRatio")!=null?proxyInfo.get("prebateRatio")+"":"0.00";
			BigDecimal upRebateRation_$ = new BigDecimal(prebateRatio);
			distributionInfo+=getDistributionInfo(agentId_$,upRebateRation_$, sysConfig, amount, upRebateRatio);
		}
		return distributionInfo;
	}

	@Override
	public Message rebateProxyDistribution(Map<String, Object> paramMap) {
		Message msg = new Message();
		try {
			//查询还未返点的代理商分销信息
			List<Map<String,Object>> rebateProxys = this.proxyDistributionMapper.findProxyDistributionInfo(paramMap);
			if(rebateProxys!=null && rebateProxys.size()>0) {
				for(int i=0;i<rebateProxys.size();i++) {
					Map<String,Object> rebateProxy = rebateProxys.get(i);
					//代理商id
					Integer agentId = Integer.valueOf(rebateProxy.get("agentId")+"");
					//返利金额
					BigDecimal rebate_coin = new BigDecimal(rebateProxy.get("rebatesAmount")!=null?rebateProxy.get("rebatesAmount")+"":"0.00");
					//返利时间
					String rebatesTime = DateUtils.getDateString(new Date());
					//查询代理商详情
					Map<String,Object> proxyInfo = this.proxyInfoMapper.queryProxyInfoById(agentId);
					//更新代理商个人财产金额
					this.proxyInfoMapper.updateProxyInfoCoin(agentId, rebate_coin);
					//新增代理商返点记录信息
					CoinLog coinLog = new CoinLog();
					coinLog.setOrderId(DateUtils.getDateString(new Date(), "yyyyMMddHHmmss"));
					coinLog.setUserType(Byte.valueOf("1"));
					coinLog.setUid(Long.valueOf(agentId));
					coinLog.setCoin(rebate_coin);
					coinLog.setUsercoin(new BigDecimal(proxyInfo.get("userCoin")+"").add(rebate_coin));
					coinLog.setCoinBefore(new BigDecimal(proxyInfo.get("userCoin")+""));
					coinLog.setCoinOperateType(Byte.valueOf("11"));
					coinLog.setInputtime(new Date());
					this.coinLogMapper.insertSelective(coinLog);
					//更新代理商分销状态是否返点
					Integer id = Integer.valueOf(rebateProxy.get("id")+"");
					this.proxyDistributionMapper.updateProxyStatus(id,"1",rebatesTime);
				}
				msg.setCode(200);
				msg.setMsg("成功");
			}else {
				msg.setCode(201);
				msg.setMsg("未查询到相关信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
		    LOGGER.error("代理商返点金额异常："+e.getMessage());
		    msg.setCode(201);
		    msg.setMsg("失败");
		}
		return msg;
	}


	@Override
	public List<Map<String, Object>> getAgentRebateList(Map<String, Object> map) {
		return proxyDistributionMapper.getAgentRebateList(map);
	}


	@SuppressWarnings("unused")
	@Override
	public Integer rollBack(Map<String, Object> map) {
		try {
			Map<String,Object> proxyDistributionParam = new HashMap<>();
			//更新proxyDistribution表
			proxyDistributionParam.put("rebatesAmount", 0);//回滚金额设为0
			proxyDistributionParam.put("status", 2);//状态改为2,表示已回滚
			proxyDistributionParam.put("id", map.get("id"));//表id
			Integer resultpd =  proxyDistributionMapper.rollBackProxyDistribution(proxyDistributionParam);
			//更新代理用户信息表agentId
			proxyDistributionParam.put("rebatesAmount",map.get("rebatesAmount"));//将代理标的账号余额减去反水余额
			proxyDistributionParam.put("agentId",map.get("agentId"));//代理的id
			Integer resultPf = proxyInfoMapper.rollbackProxyInfo(proxyDistributionParam);
			//在coin_log表插入记录
			Map<String, Object> param = new HashMap<>();
			Map<String, Object> proxyInfo = proxyInfoMapper.queryProxyInfoCoinkByid((Integer)map.get("agentId"));//变动后的账号余额
			param.put("userType", 1);//用户类型
			param.put("uid", map.get("agentId"));//代理用户id
			param.put("userName", proxyInfo.get("userName"));//代理姓名
			param.put("coin", "-"+map.get("rebatesAmount"));//变动金额
			param.put("userCoin", proxyInfo.get("coin"));//变动后余额
			param.put("coinOperateType", 8);//操作类型反水
			param.put("operateUser", map.get("userName"));//操作人
			param.put("operateIP", map.get("operaterIp"));//操作IP
			param.put("operateDate", new Date());//操作时间
			param.put("operateUid", map.get("operateUid"));
			Integer coinResult = coinLogMapper.addCoinLogRollBack(param);
			return 1;
		}catch (Exception e) {
			LOGGER.info(e);
			return -1;
		}
	}

	@Override
	public Map<String, Object> queryCountGK() {
		return proxyDistributionMapper.queryCountGK();
	}

	@Override
	public List<Map<String, Object>> teamCount(Map<String, Object> map) {
		Long startSearchTime = System.currentTimeMillis();
		if(jedisClient.exists("TEAM_REPORT_INFO_SEARCH_TIME_KEY")) {
			Long endSearchTime = Long.valueOf(jedisClient.get("TEAM_REPORT_INFO_SEARCH_TIME_KEY"));
			Long time = startSearchTime - endSearchTime;
			if(time>10*60*1000) {
				proxyDistributionMapper.queryTodayTeamReports();
			}
		}else {
			proxyDistributionMapper.queryTodayTeamReports();
			jedisClient.set("TEAM_REPORT_INFO_SEARCH_TIME_KEY", startSearchTime+"");
		}
		List<Map<String,Object>> teamListMap = proxyDistributionMapper.queryTeamCount(map);
		return teamListMap;
	}

	@Override
	public int isExsitAgency(String agentCount) {
		return proxyDistributionMapper.isExsitAgency(agentCount);
	}


	@Override
	public GlobalCount globalCount(Map<String, Object> map) {
		GlobalCount result = new GlobalCount();
		GlobalCount resultMap = proxyDistributionMapper.queryGlobalCount(map);
		BigDecimal clogSumCoin = resultMap.getRebateSumCoin().add(resultMap.getBonusSumCoin());
		BigDecimal gbetSumCoin = resultMap.getfCPJWin().add(resultMap.getHisFPJWin().add(resultMap.getfBPJWin()))
				.add(resultMap.getbCPJWin()).add(resultMap.getNumPJWin()).add(resultMap.getNumQpPJWin());
		resultMap.setSumWin(gbetSumCoin.subtract(clogSumCoin));
		GlobalCount hisResultMap = proxyDistributionMapper.queryLsHistoryGlobalCount(map);
		BigDecimal hisclogSumCoin = hisResultMap.getRebateSumCoin().add(hisResultMap.getBonusSumCoin());
		BigDecimal hisgbetSumCoin = hisResultMap.getfCPJWin().add(hisResultMap.getHisFPJWin().add(hisResultMap.getfBPJWin()))
				.add(hisResultMap.getbCPJWin()).add(hisResultMap.getNumPJWin()).add(hisResultMap.getNumQpPJWin());
		hisResultMap.setSumWin(hisgbetSumCoin.subtract(hisclogSumCoin));
		result.setBetSum(resultMap.getBetSum()+hisResultMap.getBetSum());
		result.setSumWin(resultMap.getSumWin().add(hisResultMap.getSumWin()));
		result.setRebateSumCoin(resultMap.getRebateSumCoin().add(hisResultMap.getRebateSumCoin()));
		result.setWithdrawSumCoin(resultMap.getWithdrawSumCoin().add(hisResultMap.getWithdrawSumCoin()));
		result.setRegGivingSumCoin(resultMap.getRegGivingSumCoin().add(hisResultMap.getRegGivingSumCoin()));
		result.setDepositGivingSumCoin(resultMap.getDepositGivingSumCoin().add(hisResultMap.getDepositGivingSumCoin()));
		result.setAddMoneySumCoin(resultMap.getAddMoneySumCoin().add(hisResultMap.getAddMoneySumCoin()));
		result.setSubtractMoneySumCoin(resultMap.getSubtractMoneySumCoin().add(hisResultMap.getSubtractMoneySumCoin()));
		result.setProxyAddMoneySumCoin(resultMap.getProxyAddMoneySumCoin().add(hisResultMap.getProxyAddMoneySumCoin()));
		result.setProxySubtractMoneySumCoin(resultMap.getProxySubtractMoneySumCoin().add(hisResultMap.getProxySubtractMoneySumCoin()));
		result.setBetRebate(resultMap.getBetRebate().add(hisResultMap.getBetRebate()));
		result.setBonusSumCoin(resultMap.getBonusSumCoin().add(hisResultMap.getBonusSumCoin()));
		result.setRgGivingMoneySumCoin(resultMap.getRgGivingMoneySumCoin().add(hisResultMap.getRgGivingMoneySumCoin()));
		result.setDepositSumCoin(resultMap.getDepositSumCoin().add(hisResultMap.getDepositSumCoin()));
		result.setDepositRgSumCoin(resultMap.getDepositRgSumCoin().add(hisResultMap.getDepositRgSumCoin()));
		result.setfCSumCoin(resultMap.getfCSumCoin().add(hisResultMap.getfCSumCoin()));
		result.setfCPJSumCoin(resultMap.getfCPJSumCoin().add(hisResultMap.getfCPJSumCoin()));
		result.setfCPJWin(resultMap.getfCPJWin().add(hisResultMap.getfCPJWin()));
		result.setbCPJSumCoin(resultMap.getbCPJSumCoin().add(hisResultMap.getbCPJSumCoin()));
		result.setbCSumCoin(resultMap.getbCSumCoin().add(hisResultMap.getbCSumCoin()));
		result.setbCPJWin(resultMap.getbCPJWin().add(hisResultMap.getbCPJWin()));
		result.setNumSumCoin(resultMap.getNumSumCoin().add(hisResultMap.getNumSumCoin()));
		result.setNumPJSumCoin(resultMap.getNumPJSumCoin().add(hisResultMap.getNumPJSumCoin()));
		result.setNumPJWin(resultMap.getNumPJWin().add(hisResultMap.getNumPJWin()));
		result.setNumQpSumCoin(resultMap.getNumQpSumCoin().add(hisResultMap.getNumQpSumCoin()));
		result.setNumQpPJSumCoin(resultMap.getNumQpPJSumCoin().add(hisResultMap.getNumQpPJSumCoin()));
		result.setNumQpPJWin(resultMap.getNumQpPJWin().add(hisResultMap.getNumQpPJWin()));
		return result;
	}


	@Override
	public TeamCount queryTotalTeamCount(Map<String, Object> map) {
		return proxyDistributionMapper.queryTotalTeamCount(map);
	}


	@Override
	public Map<String, Object> queryHomeTodayReports() {
		return proxyDistributionMapper.queryHomeTodayReports();
	}

	
}
