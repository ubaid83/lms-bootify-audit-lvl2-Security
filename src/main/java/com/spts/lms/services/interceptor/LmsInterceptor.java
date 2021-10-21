package com.spts.lms.services.interceptor;

import java.net.InetAddress;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.spts.lms.beans.UserLog;
import com.spts.lms.daos.MongoDAO;
import com.spts.lms.web.utils.Utils;

public class LmsInterceptor extends HandlerInterceptorAdapter{
	
//	@Autowired
//	private MongoDAO mongoDao;
	
	@Autowired
    private HttpServletRequest httpRequest;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Enumeration enumeration = request.getParameterNames();
		Map<String, Object> modelMap = new HashMap<>();
        while(enumeration.hasMoreElements()){
            String parameterName = enumeration.nextElement().toString();
            modelMap.put(parameterName, request.getParameter(parameterName));
        }
		System.out.println("pre Parameter--->"+modelMap);
		// TODO Auto-generated method stub
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("response status--->"+response.getStatus());
			System.out.println("URI--->"+request.getRequestURI());
			System.out.println("URL--->"+request.getRequestURL());
			Enumeration enumeration = request.getParameterNames();
			Map<String, Object> modelMap = new HashMap<>();
	        while(enumeration.hasMoreElements()){
	            String parameterName = enumeration.nextElement().toString();
	            modelMap.put(parameterName, request.getParameter(parameterName));
	        }
			System.out.println("Parameter--->"+modelMap);
			UserLog userLog = new UserLog();
			String currDate = Utils.formatDate("dd-MM-yyyy HH:mm:ss", Utils.getInIST());
//			try {
//				String username = request.getUserPrincipal().getName();
//				String ipAddr = getClientIP();
//				userLog.setUsername(username);
//				userLog.setIpAddr(ipAddr);
//				userLog.setAction(request.getRequestURL().toString());
//				userLog.setStatus(String.valueOf(response.getStatus()));
//				userLog.setDateTime(currDate);
//				mongoDao.save(userLog);
//			}catch(Exception e) {
//				String ipAddr = getClientIP();
//				userLog.setIpAddr(ipAddr);
//				userLog.setAction(request.getRequestURL().toString());
//				userLog.setStatus(String.valueOf(response.getStatus()));
//				userLog.setDateTime(currDate);
//				mongoDao.save(userLog);
//			}
			
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}
	
	private String getClientIP() {
	    String xfHeader = httpRequest.getHeader("X-Forwarded-For");
	    if (xfHeader == null){
	        return httpRequest.getRemoteAddr();
	    }
	    return xfHeader.split(",")[0];
	}
	

}
