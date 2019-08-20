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
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysMenuService;
import com.lb.sys.tools.SpringUtil;


@WebFilter(filterName = "permission", urlPatterns = "*.html")
public class Permission implements Filter {
	
	 /**  
     * 配置允许的html
     */  
    private String allowHtml = null;   
    /**  
     * 重定向的URL  
     */  
    private String redirectLogin = null;
    
    private String redirectAd=null;
  
    @Autowired
	private ISysMenuService sysMenuService;
    
    public void init(FilterConfig filterConfig) throws ServletException {   
       
    }   
  
    /**  
     * 在过滤器中实现权限控制  
     */  
    public void doFilter(ServletRequest sRequest, ServletResponse sResponse,   
            FilterChain filterChain) throws IOException, ServletException {   
        HttpServletRequest request = (HttpServletRequest) sRequest;   
        HttpServletResponse response = (HttpServletResponse) sResponse; 
        
        // 放行的URL   
    	allowHtml=getHTML(request,"/download.html")+getHTML(request,"/login.html")+getHTML(request,"/index.html")+getHTML(request,"/navbar.html")+getHTML(request,"/welcome.html")+getHTML(request,"/errorPage.html");
    	// 指定要重定向的登录页面   
        redirectLogin =getHTML(request,"/login.html");
        // 指定要重定向的没权限页面页面   
        redirectAd = getHTML(request,"/errorPage.html");
        
        HttpSession session = request.getSession();   
        SysUser sysUser = (SysUser) session.getAttribute("sysUser");
        //获取访问路径
        String url = request.getRequestURI();
        //设置响应编码格式
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // 会话中存在用户，则验证用户是否存在当前页面的权限   
        if(sysUser!=null) {
        	if (sysMenuService == null) {// 解决service为null无法注入问题
        		sysMenuService=SpringUtil.getBean(ISysMenuService.class);
    		}
        	//拦截HTML页面
    		Long roleId = sysUser.getRoleId();
    		String menuPaths=sysMenuService.queryMenuPath(request,roleId);
    		if(!(allowHtml+menuPaths).contains(url)) {
    			response.sendRedirect(redirectAd);   
    		}   
		}else {
			//若果不是登录页面，则重定向到登录页面
			if(!redirectLogin.equals(url)) {
				response.sendRedirect(redirectLogin);   
			}
		}
        filterChain.doFilter(sRequest, sResponse);
    }

    public void destroy() { 
    }  
    
    public String getHTML(HttpServletRequest request,String html) {
    	html=request.getContextPath()+html;
		return html;   
    }  
}  
