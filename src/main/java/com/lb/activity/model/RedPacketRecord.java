package com.lb.activity.model;

import java.util.Date;

public class RedPacketRecord {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.id
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.red_packet_id
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private Integer redPacketId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.uid
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private Long uid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.user_name
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.red_packet_title
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private String redPacketTitle;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.red_packet_money
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private Float redPacketMoney;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.ip
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private String ip;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.state
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private Byte state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.flag
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private Byte flag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column red_packet_record.create_time
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.id
     *
     * @return the value of red_packet_record.id
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.id
     *
     * @param id the value for red_packet_record.id
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.red_packet_id
     *
     * @return the value of red_packet_record.red_packet_id
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public Integer getRedPacketId() {
        return redPacketId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.red_packet_id
     *
     * @param redPacketId the value for red_packet_record.red_packet_id
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setRedPacketId(Integer redPacketId) {
        this.redPacketId = redPacketId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.uid
     *
     * @return the value of red_packet_record.uid
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public Long getUid() {
        return uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.uid
     *
     * @param uid the value for red_packet_record.uid
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.user_name
     *
     * @return the value of red_packet_record.user_name
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.user_name
     *
     * @param userName the value for red_packet_record.user_name
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.red_packet_title
     *
     * @return the value of red_packet_record.red_packet_title
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public String getRedPacketTitle() {
        return redPacketTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.red_packet_title
     *
     * @param redPacketTitle the value for red_packet_record.red_packet_title
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setRedPacketTitle(String redPacketTitle) {
        this.redPacketTitle = redPacketTitle == null ? null : redPacketTitle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.red_packet_money
     *
     * @return the value of red_packet_record.red_packet_money
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public Float getRedPacketMoney() {
        return redPacketMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.red_packet_money
     *
     * @param redPacketMoney the value for red_packet_record.red_packet_money
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setRedPacketMoney(Float redPacketMoney) {
        this.redPacketMoney = redPacketMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.ip
     *
     * @return the value of red_packet_record.ip
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public String getIp() {
        return ip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.ip
     *
     * @param ip the value for red_packet_record.ip
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.state
     *
     * @return the value of red_packet_record.state
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public Byte getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.state
     *
     * @param state the value for red_packet_record.state
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setState(Byte state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.flag
     *
     * @return the value of red_packet_record.flag
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.flag
     *
     * @param flag the value for red_packet_record.flag
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column red_packet_record.create_time
     *
     * @return the value of red_packet_record.create_time
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column red_packet_record.create_time
     *
     * @param createTime the value for red_packet_record.create_time
     *
     * @mbg.generated Sat Sep 30 16:38:20 CST 2017
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}