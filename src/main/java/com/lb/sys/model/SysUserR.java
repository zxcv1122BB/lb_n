package com.lb.sys.model;

import java.util.Date;

import com.lb.sys.service.ISysUserService;
import com.lb.sys.tools.SpringUtil;

public class SysUserR {
    
    private Long userId;

   
    private Long roleId;

   
    private String userName;
    
    private String roleName;

  
    private String userPassword;

   
    private String userChName;

    
    private Short userSex;

    
    private Short state;

   
    private String endIp;

   
    private Date createdDate;

   
    private Date updateDate;

   
    private String updateUser;

   
    private String createdUser;
    
    private String acessToken;
    
    private Short googlecodeState;
    
    private String googlecodeSecret;
    
    private int  isLocking;
    
    public int getIsLocking() {
    	 //获取redis操作工具类
		ISysUserService iSysUserService= (ISysUserService)SpringUtil.getBean("sysUserServiceImpl");
		//判断该用户是否为锁定的状态
		boolean locking = iSysUserService.isLocking("lbIsLocking", this.userName);
		if(locking==true) {
			return 1;
		}else {
			return 0;
		}
	}


	public void setIsLocking(int isLocking) {
		this.isLocking = isLocking;
	}


	public String getGooglecodeSecret() {
		return googlecodeSecret;
	}


	public void setGooglecodeSecret(String googlecodeSecret) {
		this.googlecodeSecret = googlecodeSecret;
	}


	public Short getGooglecodeState() {
		return googlecodeState;
	}


	public void setGooglecodeState(Short googlecodeState) {
		this.googlecodeState = googlecodeState;
	}


	public String getAcessToken() {
		return acessToken;
	}


	public void setAcessToken(String acessToken) {
		this.acessToken = acessToken;
	}


	public Long getUserId() {
        return userId;
    }

  
    public void setUserId(Long userId) {
        this.userId = userId;
    }

   
    public Long getRoleId() {
        return roleId;
    }

  
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

  
    public String getUserName() {
        return userName;
    }

  
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

  
    public String getUserPassword() {
        return userPassword;
    }

   
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

   
    public String getUserChName() {
        return userChName;
    }

   
    public void setUserChName(String userChName) {
        this.userChName = userChName == null ? null : userChName.trim();
    }

   
    public Short getUserSex() {
        return userSex;
    }

   
    public void setUserSex(Short userSex) {
        this.userSex = userSex;
    }

   
    public Short getState() {
        return state;
    }

   
    public void setState(Short state) {
        this.state = state;
    }

   
    public String getEndIp() {
        return endIp;
    }

   
    public void setEndIp(String endIp) {
        this.endIp = endIp == null ? null : endIp.trim();
    }

  
    public Date getCreatedDate() {
        return createdDate;
    }

   
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

   
    public Date getUpdateDate() {
        return updateDate;
    }

   
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

   
    public String getUpdateUser() {
        return updateUser;
    }

   
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

   
    public String getCreatedUser() {
        return createdUser;
    }

  
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser == null ? null : createdUser.trim();
    }


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}