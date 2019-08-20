package com.lb.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.HiddenHttpMethodFilter;  
  
  
/** 
 * 处理form表单头的过滤器， 
 * 如果表单有_header字段，可以自动将该字段转为request的header头信息（增加一条头） 
 */  
@WebFilter(filterName = "headerAuthorityFilter", urlPatterns = "/download/*")
public class HeaderAuthorityFilter extends HiddenHttpMethodFilter{  
  
      
    @Override  
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  
            throws ServletException, IOException {  
    	//从表单里面获取的
        String header=request.getParameter("X-Authorization");
        if (header!=null && !header.trim().equals("")) {  
            HttpServletRequest wrapper = new HttpHeaderRequestWrapper(request,header);  
            super.doFilterInternal(wrapper, response, filterChain);  
        }else {  
            super.doFilterInternal(request, response, filterChain);  
        }  
    }  
      
    private static class HttpHeaderRequestWrapper extends HttpServletRequestWrapper{  
  
        private final String header;  
          
        public HttpHeaderRequestWrapper(HttpServletRequest request,String header) {  
            super(request);  
            this.header=header;  
        }  
  
        @Override  
        public String getHeader(String name) { 
            if (name!=null && name.equals("X-Authorization")&&super.getHeader("X-Authorization")==null) {
                return header;
            }else {  
                return super.getHeader(name);
            }  
        }  
          
    }  
      
      
}