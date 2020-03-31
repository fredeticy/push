package com.czm.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.czm.po.User;
import com.czm.service.UserService;
import com.czm.vo.UserVO;
@Controller
public class UserController {
	
	@Resource
	private UserService userService;
	
	
	@RequestMapping("/login.do")
	@ResponseBody
	public UserVO login(String userid,String pwd,HttpSession session){
		
		UserVO vo = userService.login(userid, pwd);
		System.out.println(vo.getMsg() + "#####################");
		session.setAttribute("uservo", vo);
		return vo;
	}
	
}
