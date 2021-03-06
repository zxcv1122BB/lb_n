package com.lb.sys.model;

import java.util.Date;

public class SysMenu {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.MENU_ID
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private Long menuId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.MENU_PARENT_ID
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private Long menuParentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.MENU_NAME
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private String menuName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.MENU_LEVELS
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private Short menuLevels;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.MENU_TYPE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private Short menuType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.MENU_PATH
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private String menuPath;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.DESC_CONTENT
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private String descContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.STATE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private Short state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.IS_PUBLISH
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private Short isPublish;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.FLAG
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private Integer flag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.CREATED_DATE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private Date createdDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.UPDATE_DATE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private Date updateDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.UPDATE_USER
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private String updateUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.CREATED_USER
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    private String createdUser;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.MENU_ID
     *
     * @return the value of sys_menu.MENU_ID
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.MENU_ID
     *
     * @param menuId the value for sys_menu.MENU_ID
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.MENU_PARENT_ID
     *
     * @return the value of sys_menu.MENU_PARENT_ID
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public Long getMenuParentId() {
        return menuParentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.MENU_PARENT_ID
     *
     * @param menuParentId the value for sys_menu.MENU_PARENT_ID
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setMenuParentId(Long menuParentId) {
        this.menuParentId = menuParentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.MENU_NAME
     *
     * @return the value of sys_menu.MENU_NAME
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.MENU_NAME
     *
     * @param menuName the value for sys_menu.MENU_NAME
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.MENU_LEVELS
     *
     * @return the value of sys_menu.MENU_LEVELS
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public Short getMenuLevels() {
        return menuLevels;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.MENU_LEVELS
     *
     * @param menuLevels the value for sys_menu.MENU_LEVELS
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setMenuLevels(Short menuLevels) {
        this.menuLevels = menuLevels;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.MENU_TYPE
     *
     * @return the value of sys_menu.MENU_TYPE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public Short getMenuType() {
        return menuType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.MENU_TYPE
     *
     * @param menuType the value for sys_menu.MENU_TYPE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setMenuType(Short menuType) {
        this.menuType = menuType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.MENU_PATH
     *
     * @return the value of sys_menu.MENU_PATH
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public String getMenuPath() {
        return menuPath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.MENU_PATH
     *
     * @param menuPath the value for sys_menu.MENU_PATH
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath == null ? null : menuPath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.DESC_CONTENT
     *
     * @return the value of sys_menu.DESC_CONTENT
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public String getDescContent() {
        return descContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.DESC_CONTENT
     *
     * @param descContent the value for sys_menu.DESC_CONTENT
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setDescContent(String descContent) {
        this.descContent = descContent == null ? null : descContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.STATE
     *
     * @return the value of sys_menu.STATE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public Short getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.STATE
     *
     * @param state the value for sys_menu.STATE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setState(Short state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.IS_PUBLISH
     *
     * @return the value of sys_menu.IS_PUBLISH
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public Short getIsPublish() {
        return isPublish;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.IS_PUBLISH
     *
     * @param isPublish the value for sys_menu.IS_PUBLISH
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setIsPublish(Short isPublish) {
        this.isPublish = isPublish;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.FLAG
     *
     * @return the value of sys_menu.FLAG
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.FLAG
     *
     * @param flag the value for sys_menu.FLAG
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.CREATED_DATE
     *
     * @return the value of sys_menu.CREATED_DATE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.CREATED_DATE
     *
     * @param createdDate the value for sys_menu.CREATED_DATE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.UPDATE_DATE
     *
     * @return the value of sys_menu.UPDATE_DATE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.UPDATE_DATE
     *
     * @param updateDate the value for sys_menu.UPDATE_DATE
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.UPDATE_USER
     *
     * @return the value of sys_menu.UPDATE_USER
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.UPDATE_USER
     *
     * @param updateUser the value for sys_menu.UPDATE_USER
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.CREATED_USER
     *
     * @return the value of sys_menu.CREATED_USER
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public String getCreatedUser() {
        return createdUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.CREATED_USER
     *
     * @param createdUser the value for sys_menu.CREATED_USER
     *
     * @mbg.generated Sat Oct 14 14:11:51 CST 2017
     */
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser == null ? null : createdUser.trim();
    }
}