package com.lb.interceptor;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lb.log.model.LogInfo;
import com.lb.log.service.LogInfoService;
import com.lb.sys.controller.LoginController;
import com.lb.sys.model.SysUser;

//
//HandlerInterceptorAdapter
public class LoginInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	@SuppressWarnings("unused")
	private static final ThreadLocal<LogInfo> logThreadLocal = new NamedThreadLocal<LogInfo>("ThreadLocal log");
//	@Autowired
//	private ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

	@Autowired
	private LogInfoService logService;

	Date date;

	/**
	 * 预处理回调方法，实现处理器的预处理（如登录检查）。 第三个参数为响应的处理器，即controller。
	 * 返回true，表示继续流程，调用下一个拦截器或者处理器。 返回false，表示流程中断，通过response产生响应。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		date = new Date();
		return true;
	}

	/**
	 * 当前请求进行处理之后，也就是Controller 方法调用之后执行， 但是它会在DispatcherServlet 进行视图返回渲染之前被调用。
	 * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理。
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	/**
	 * 方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。 这个方法的主要作用是用于进行资源清理工作的。
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
//		HttpSession session = request.getSession(false);
		// 判断用户是否登陆  
		SysUser user = (SysUser) request.getSession(false).getAttribute(LoginController.LOGINUSER);

		if (logService == null) {// 解决service为null无法注入问题
			BeanFactory factory = WebApplicationContextUtils
					.getRequiredWebApplicationContext(request.getServletContext());
			logService = (LogInfoService) factory.getBean("logInfoServiceImpl");
		}

		if (user != null) {
			String remoteAddr = request.getRemoteAddr();// 请求的IP
			String requestUri = request.getRequestURI();// 请求的Uri
			String method = request.getMethod(); // 请求的方法类型(post/get)
			Map<String, String[]> params = request.getParameterMap(); // 请求提交的参数
			LogInfo log = new LogInfo();
			// LogInfo log= logThreadLocal.get();
			if (ex != null) {
				log.setException(ex.toString());
			}
			log.setLogid(UUID.randomUUID().toString());
			log.setRemoteaddr(remoteAddr);
			log.setRequesturl(requestUri);
			log.setMethod(method);
			log.setMapToParams(params);
			log.setUserid(Integer.valueOf(user.getUserId().toString()));
//			log.setUserid(15);
			log.setOperatedate(date);
			log.setTimeout(new Date());
			//Message message = logService.addRiZhi(log);
//			logger.info(message.getMsg());
		} else {
			// 报错日志
			logger.info("用户没有登陆");
		}
	}

	/**
	 * 保存日志线程
	 */
	@SuppressWarnings("unused")
	private static class SaveLogThread implements Runnable {
		private LogInfo log;
		private LogInfoService logService;

		public SaveLogThread(LogInfo log, LogInfoService logService) {
			this.log = log;
			this.logService = logService;
		}

		@Override
		public void run() {
			logService.insert(log);
		}
	}

	/**
	 * 日志更新线程
	 */
	@SuppressWarnings("unused")
	private static class UpdateLogThread extends Thread {
		private LogInfo log;
		private LogInfoService logService;

		public UpdateLogThread(LogInfo log, LogInfoService logService) {
			super(UpdateLogThread.class.getSimpleName());
			this.log = log;
			this.logService = logService;
		}

		@Override
		public void run() {
			this.logService.updateByPrimaryKey(log);
		}
	}

	// @Bean
	// public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
	// return new ThreadPoolTaskExecutor();
	// }

}
