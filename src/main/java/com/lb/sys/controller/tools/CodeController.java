package com.lb.sys.controller.tools;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.lb.sys.tools.MobileAuthCodeGenerator;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.VerifyCodeUtils;

/**
 * 
 * @author Administrator
 *获取图片验证码
 */
@Controller
@RequestMapping("code")
public class CodeController {

	public static final String VERIFY_CODE = "verifycode";

	@RequestMapping(value = "image", method = RequestMethod.GET)
	public void imageVerifyCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "150") int w,
			@RequestParam(required = false, defaultValue = "60") int h) throws IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
		// 存入会话session,验证码和时间一起被存储在session中
		String checkCode = verifyCode+new Date().getTime();
		WebUtils.setSessionAttribute(request, VERIFY_CODE, checkCode);
		// 生成图片
		// int w = 200, h = 80;
		VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
	}
	
	@RequestMapping(value = "imageForAdmin", method = RequestMethod.GET)
	public void imageForAdmin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "150") int w,
			@RequestParam(required = false, defaultValue = "60") int h) throws IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
		// 存入会话session
		WebUtils.setSessionAttribute(request, "ADMIN_VERIFY_CODE", verifyCode.toLowerCase());
		// 生成图片
		// int w = 200, h = 80;
		VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
	}
	@RequestMapping(value = "imageCode", method = RequestMethod.GET)
	public ModelAndView image(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String code = MobileAuthCodeGenerator.generateVerifyCode(4);
        String data = "";
        try {
             data = MobileAuthCodeGenerator.outputImage(150,60,code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("imageString", data);
        hashMap.put("uuid", data);
        return ResponseUtils.jsonView(200, "",hashMap);    
	}
	
}
