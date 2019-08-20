package com.lb.sys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lb.interceptor.LoginInterceptor;
import com.lb.interceptor.SignValidateInterceptor;
import com.lb.interceptor.SignalLoginInterceptor;
import com.lb.interceptor.TimestampInterceptor;
import com.lb.sys.tools.GetPropertiesValue;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 单点登录拦截器
		InterceptorRegistration signalLoginInterceptor = registry.addInterceptor(new SignalLoginInterceptor());
		// 配置拦截的路径
		signalLoginInterceptor.addPathPatterns("/**");
		// 配置不拦截的路径
		signalLoginInterceptor.excludePathPatterns(GetPropertiesValue.getValueArray("URL","signalLoginInterceptorExcludeUrl"));
		//时间戳拦截
		InterceptorRegistration timestampInterceptor = registry.addInterceptor(new TimestampInterceptor());
		// 配置拦截的路径
		timestampInterceptor.addPathPatterns("/**");
		// 配置不拦截的路径
		timestampInterceptor.excludePathPatterns(GetPropertiesValue.getValueArray("URL","timestampInterceptorExcludeUrl"));
		//签名拦截
		InterceptorRegistration signValidateInterceptor = registry.addInterceptor(new SignValidateInterceptor());
		// 配置拦截的路径
		signValidateInterceptor.addPathPatterns("/**");
		// 配置不拦截的路径
		signValidateInterceptor.excludePathPatterns(GetPropertiesValue.getValueArray("URL","loginInterceptorExcludeUrl"));
		// 登录日志拦截器
		InterceptorRegistration ir = registry.addInterceptor(new LoginInterceptor());
		// 配置拦截的路径
		ir.addPathPatterns("/**");
		// 配置不拦截的路径
		ir.excludePathPatterns(GetPropertiesValue.getValueArray("URL","loginInterceptorExcludeUrl"));
		
		
	}

}
