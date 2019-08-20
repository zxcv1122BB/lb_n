package com.lb.report.model;

import java.math.BigDecimal;

import com.alibaba.druid.util.StringUtils;

public class OperationAnalysis {
	
	private String user_name;
	private String user_type;
	private String mDeposit;
	private int depositCount;
	private BigDecimal depositAccount ;
	
	private String mWithdraw;
	private int withdrawalCount;
	private BigDecimal withdrawalAcount ;
	
	private BigDecimal balance ;

	public String getmDeposit() {
		return mDeposit;
	}

	public void setmDeposit(String mDeposit) {
		if(StringUtils.isEmpty(mDeposit)) {
			return ;
		}
		String[] split = mDeposit.split(",");
		depositCount=Integer.valueOf(split[1]);
		depositAccount=new BigDecimal(split[0]);
		this.mDeposit = null;
	}

	public int getDepositCount() {
		return depositCount;
	}

	public void setDepositCount(int depositCount) {
		this.depositCount = depositCount;
	}

	public BigDecimal getDepositAccount() {
		return depositAccount;
	}

	public void setDepositAccount(BigDecimal depositAccount) {
		this.depositAccount = depositAccount;
	}

	public String getmWithdraw() {
		return mWithdraw;
	}

	public void setmWithdraw(String mWithdraw) {
		if(StringUtils.isEmpty(mWithdraw)) {
			return ;
		}
		String[] split = mWithdraw.split(",");
		withdrawalCount=Integer.valueOf(split[1]);
		withdrawalAcount=new BigDecimal(split[0]);
		this.mWithdraw = null;
	}

	public int getWithdrawalCount() {
		return withdrawalCount;
	}

	public void setWithdrawalCount(int withdrawalCount) {
		this.withdrawalCount = withdrawalCount;
	}

	public BigDecimal getWithdrawalAcount() {
		return withdrawalAcount;
	}

	public void setWithdrawalAcount(BigDecimal withdrawalAcount) {
		this.withdrawalAcount = withdrawalAcount;
	}

	public BigDecimal getBalance() {
		if(getDepositAccount()==null) {
			depositAccount=new BigDecimal("0") ;
		}
		if(getWithdrawalAcount()==null) {
			withdrawalAcount=new BigDecimal("0") ;
		}
		return depositAccount.subtract(withdrawalAcount);
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


}
