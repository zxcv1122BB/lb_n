package com.lb.sys.controller.tools;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lb.sys.tools.PublicKeyUtil;
import com.lb.sys.tools.RSAUtil;

/*
 * 登陆时获取RSA加密的模数和基数
 */

@RestController
public class KeyPairController {

	@RequestMapping(value = "keyPair")
    public PublicKeyUtil keyPair(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	String username = request.getParameter("username");
    	PublicKeyUtil publicKeyMap = null;
    	if (StringUtils.isNotEmpty(username)) {
    		//用户名存储到session中,解密时从中获取
    		request.getSession().setAttribute("username", username);
    		publicKeyMap = RSAUtil.getInstance(username,request).getPublicKeyUtil(true);
		}
    	return publicKeyMap;
    }
	
}
