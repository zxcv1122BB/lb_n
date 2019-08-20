package com.lb.sys.config;

/*import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.lb.filter.DecodeFilter;
import com.lb.filter.Permission;*/

/*@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@ComponentScan
public class FilterConfig{
	@Bean
    public FilterRegistrationBean decodeFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        DecodeFilter decodeFilter = new DecodeFilter();
        registrationBean.setFilter(decodeFilter);
        registrationBean.setName("decodeFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("exclusions", "*.png,*.html,*.css,*.js,/code/**,/keyPair,/manage/checkVerifycode,/error");
        registrationBean.setOrder(Integer.MAX_VALUE);
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean permissionFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        Permission permission = new Permission();
        registrationBean.setFilter(permission);
        registrationBean.setName("permission");
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("exclusions","login.html,*.png,*.html,*.css,*.js,/code/**,/keyPair,/manage/checkVerifycode,/error"); 
        registrationBean.setOrder(Integer.MAX_VALUE-1);
        return registrationBean;
    }
    
}*/