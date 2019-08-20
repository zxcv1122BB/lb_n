package com.lb.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 对编码进行解密
 * 
 * @author Administrator
 *
 */
@WebFilter(filterName = "decodeFilter", urlPatterns = "/*")
public class DecodeFilter implements Filter {
	
	
	@Override
	public void destroy() {
//		LOGGER.info("过滤器销毁了");
	}

	// 重写解密的方法
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		//MyReqesut myReqesut = new MyReqesut(req); // 装饰者模式
		//设置响应吗的编码格式
		response.setCharacterEncoding("UTF-8");
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
//		LOGGER.info("过滤器初始化");
	}
}
