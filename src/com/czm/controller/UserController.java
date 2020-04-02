package com.czm.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.czm.po.User;
import com.czm.service.UserService;
import com.czm.vo.UserVO;
@RestController
public class UserController {
	
	@Resource
	private UserService userService;
	
	
	@RequestMapping("/login.do")
	public UserVO login(String userid,String pwd,HttpSession session){
		UserVO vo = userService.login(userid, pwd);
		System.out.println(vo.getMsg() + "#####################");
		session.setAttribute("uservo", vo);
		return vo;
	}
	
	@RequestMapping("/logout.do")
	public void logout(HttpServletRequest req, HttpServletResponse rep, HttpSession session) throws IOException{
		session.setAttribute("uservo", null);
		rep.sendRedirect("/index.html");
		return ;
	}
}
