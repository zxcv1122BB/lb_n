package com.lb.sys.controller.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lb.sys.tools.GoogleAuthenticator;


@RestController
public class GooleVerifyCode {

	@RequestMapping(value = "/gooleVerifyCode", method = RequestMethod.GET)
	public Map<String,Object> imageVerifyCode(HttpServletRequest request) throws IOException {
		String secret = GoogleAuthenticator.generateSecretKey();
		String qrcode = GoogleAuthenticator.getQRBarcode(secret);
		Map<String,Object> map = new HashMap<>();
		map.put("qrcode", qrcode);
		map.put("secret", secret);
		return map;
	}
	
}
