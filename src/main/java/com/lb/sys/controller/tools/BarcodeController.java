package com.lb.sys.controller.tools;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.tools.BarcodeFactory;
import com.lb.sys.tools.ResponseUtils;

/**
 * 
 * @author Administrator
 *获取二维码：base64字符串
 */
@Controller
public class BarcodeController{


	@RequestMapping(value = "barcode", method = RequestMethod.GET)
	public ModelAndView imageVerifyCode(HttpServletRequest request, HttpServletResponse response){
		//二维码logo图片位置
		String codeIconPath =request.getSession().getServletContext().getRealPath("/images/timg.gif");
		String Path=BarcodeFactory.encode("http://154.85.9.5:2080"+request.getContextPath()+"/download/downloadApp",300, 300, codeIconPath,false);
		return ResponseUtils.jsonView(200,"成功",Path);
	}
}
