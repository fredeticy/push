package com.czm.handler;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerInterceptor;

import com.czm.vo.UserVO;

public class GlobalInterceptorHandler implements HandlerInterceptor{
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("#################before####################");
		System.out.println("#####################################");
		System.out.println(request.getSession().getAttribute("uservo"));
		UserVO vo = (UserVO) request.getSession().getAttribute("uservo");

		System.out.println("#################after####################");
		System.out.println("#####################################");
		System.out.println(request.getSession().getAttribute("uservo"));

		
		
		String spath = request.getServletPath();
		 
		System.out.println("spapth = "+ spath);
		//抛出异常，交给全局异常处理（这样会输出错误信息）
		//ApiAssert.notNull(user, "页面受到了保护，登录后才能访问~点击去<a href='/login'>登录</a>");
		
		//抛出异常，交给全局异常处理（这样会跳转到错误页面）
		/*if(user == null) {
			throw new RuntimeException("页面受到了保护，登录后才能访问~点击去<a href='/login'>登录</a>");
		}*/
		
		//自定义逻辑（跳转到登录页面）
		if(vo == null) {
			request.setAttribute("message", "页面受到了保护，登录后才能访问");
			response.sendRedirect("/index.html");
			System.out.println("vo == null");
			return true;
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
