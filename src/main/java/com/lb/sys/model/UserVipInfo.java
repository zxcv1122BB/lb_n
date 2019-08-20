package com.lb.sys.model;

import java.math.BigDecimal;
import java.util.Date;

public class UserVipInfo {
    private Integer vipid;  //会员等级记录编号

    private String vipname;  //会员等级名称

    private BigDecimal depositamount;  //充值最低金额

    private String vipicourl;  //图标地址

    private String remark;  //备注信息

    private Date inputtime;  //录入时间

    private Date updatetime;  //最后修改时间

    private Integer status;  //状态  标识是否禁用状态  0：启用   1：禁用
    
    private Integer group;//vip等级
    
    private BigDecimal percent;
    
    
    
    public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	//组中的人数
    private Integer userCount;  //该vip等级下游多少个会员

    public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public Integer getVipid() {
        return vipid;
    }

    public void setVipid(Integer vipid) {
        this.vipid = vipid;
    }

    public String getVipname() {
        return vipname;
    }

    public void setVipname(String vipname) {
        this.vipname = vipname == null ? null : vipname.trim();
    }

    public BigDecimal getDepositamount() {
        return depositamount;
    }

    public void setDepositamount(BigDecimal depositamount) {
        this.depositamount = depositamount;
    }

    public String getVipicourl() {
        return vipicourl;
    }

    public void setVipicourl(String vipicourl) {
        this.vipicourl = vipicourl == null ? null : vipicourl.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getInputtime() {
        return inputtime;
    }

    public void setInputtime(Date inputtime) {
        this.inputtime = inputtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "UserVipInfo [vipid=" + vipid + ", vipname=" + vipname + ", depositamount=" + depositamount
				+ ", vipicourl=" + vipicourl + ", remark=" + remark + ", inputtime=" + inputtime + ", updatetime="
				+ updatetime + ", status=" + status + ", group=" + group + ", percent=" + percent + ", userCount="
				+ userCount + "]";
	}
    
    
}