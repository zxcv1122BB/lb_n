package com.lb.sys.model;

import java.math.BigDecimal;
import java.util.Date;

public class ProxyInfo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.id
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.pid
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private Integer pid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.login_account
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String loginAccount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.status
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private Byte status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.password
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.proxy_name
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String proxyName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.coin_pssword
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String coinPssword;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.tell
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String tell;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.email
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String email;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.qq
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String qq;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.bank_name
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String bankName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.bank_account
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String bankAccount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.bank_address
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String bankAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.rebate_ratio
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private BigDecimal rebateRatio;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.coin
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private BigDecimal coin;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.reg_ip
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String regIp;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.reg_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private Date regTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.del_status
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private Byte delStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.proxy_level
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private Byte proxyLevel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.created_user
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String createdUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.updata_user
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private String updataUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.create_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.updata_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private Date updataTime;

    private String lastLoginIp;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column proxy_info.last_login_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    private Date lastLoginTime;

    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.id
     *
     * @param id the value for proxy_info.id
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.pid
     *
     * @return the value of proxy_info.pid
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.pid
     *
     * @param pid the value for proxy_info.pid
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.login_account
     *
     * @return the value of proxy_info.login_account
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getLoginAccount() {
        return loginAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.login_account
     *
     * @param loginAccount the value for proxy_info.login_account
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount == null ? null : loginAccount.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.status
     *
     * @return the value of proxy_info.status
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.status
     *
     * @param status the value for proxy_info.status
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.password
     *
     * @return the value of proxy_info.password
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.password
     *
     * @param password the value for proxy_info.password
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.proxy_name
     *
     * @return the value of proxy_info.proxy_name
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getProxyName() {
        return proxyName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.proxy_name
     *
     * @param proxyName the value for proxy_info.proxy_name
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setProxyName(String proxyName) {
        this.proxyName = proxyName == null ? null : proxyName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.coin_pssword
     *
     * @return the value of proxy_info.coin_pssword
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getCoinPssword() {
        return coinPssword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.coin_pssword
     *
     * @param coinPssword the value for proxy_info.coin_pssword
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setCoinPssword(String coinPssword) {
        this.coinPssword = coinPssword == null ? null : coinPssword.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.tell
     *
     * @return the value of proxy_info.tell
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getTell() {
        return tell;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.tell
     *
     * @param tell the value for proxy_info.tell
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setTell(String tell) {
        this.tell = tell == null ? null : tell.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.email
     *
     * @return the value of proxy_info.email
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.email
     *
     * @param email the value for proxy_info.email
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.qq
     *
     * @return the value of proxy_info.qq
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getQq() {
        return qq;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.qq
     *
     * @param qq the value for proxy_info.qq
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.bank_name
     *
     * @return the value of proxy_info.bank_name
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.bank_name
     *
     * @param bankName the value for proxy_info.bank_name
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.bank_account
     *
     * @return the value of proxy_info.bank_account
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.bank_account
     *
     * @param bankAccount the value for proxy_info.bank_account
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.bank_address
     *
     * @return the value of proxy_info.bank_address
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getBankAddress() {
        return bankAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.bank_address
     *
     * @param bankAddress the value for proxy_info.bank_address
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress == null ? null : bankAddress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.rebate_ratio
     *
     * @return the value of proxy_info.rebate_ratio
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public BigDecimal getRebateRatio() {
        return rebateRatio;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.rebate_ratio
     *
     * @param rebateRatio the value for proxy_info.rebate_ratio
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setRebateRatio(BigDecimal rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.coin
     *
     * @return the value of proxy_info.coin
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public BigDecimal getCoin() {
        return coin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.coin
     *
     * @param coin the value for proxy_info.coin
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setCoin(BigDecimal coin) {
        this.coin = coin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.reg_ip
     *
     * @return the value of proxy_info.reg_ip
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getRegIp() {
        return regIp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.reg_ip
     *
     * @param regIp the value for proxy_info.reg_ip
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setRegIp(String regIp) {
        this.regIp = regIp == null ? null : regIp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.reg_time
     *
     * @return the value of proxy_info.reg_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public Date getRegTime() {
        return regTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.reg_time
     *
     * @param regTime the value for proxy_info.reg_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.del_status
     *
     * @return the value of proxy_info.del_status
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public Byte getDelStatus() {
        return delStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.del_status
     *
     * @param delStatus the value for proxy_info.del_status
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setDelStatus(Byte delStatus) {
        this.delStatus = delStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.proxy_level
     *
     * @return the value of proxy_info.proxy_level
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public Byte getProxyLevel() {
        return proxyLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.proxy_level
     *
     * @param proxyLevel the value for proxy_info.proxy_level
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setProxyLevel(Byte proxyLevel) {
        this.proxyLevel = proxyLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.created_user
     *
     * @return the value of proxy_info.created_user
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getCreatedUser() {
        return createdUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.created_user
     *
     * @param createdUser the value for proxy_info.created_user
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser == null ? null : createdUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.updata_user
     *
     * @return the value of proxy_info.updata_user
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public String getUpdataUser() {
        return updataUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.updata_user
     *
     * @param updataUser the value for proxy_info.updata_user
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setUpdataUser(String updataUser) {
        this.updataUser = updataUser == null ? null : updataUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.create_time
     *
     * @return the value of proxy_info.create_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.create_time
     *
     * @param createTime the value for proxy_info.create_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.updata_time
     *
     * @return the value of proxy_info.updata_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public Date getUpdataTime() {
        return updataTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.updata_time
     *
     * @param updataTime the value for proxy_info.updata_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setUpdataTime(Date updataTime) {
        this.updataTime = updataTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.last_login_ip
     *
     * @param lastLoginIp the value for proxy_info.last_login_ip
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp == null ? null : lastLoginIp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column proxy_info.last_login_time
     *
     * @return the value of proxy_info.last_login_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column proxy_info.last_login_time
     *
     * @param lastLoginTime the value for proxy_info.last_login_time
     *
     * @mbg.generated Thu Oct 26 14:30:34 CST 2017
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

}