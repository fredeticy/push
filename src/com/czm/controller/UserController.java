package com.czm.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.czm.po.StudentInfo;
import com.czm.po.StudentVO;
import com.czm.po.User;
import com.czm.service.UserService;
import com.czm.util.JPushUtil;
import com.czm.vo.Report;
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
	
	@RequestMapping("/getstuinfo.do")
	public StudentInfo getStudentInfo(String sno){
		return userService.getStudentInfoBySno(sno);
	}
	
	@RequestMapping(value="/user/jpush/notify.do",produces="text/html;charset=UTF-8")
	public String sendNotification(String title,String content,String audience,HttpSession session){
		return userService.sendNotification(title,content,audience,session);
	}
	
	@RequestMapping("/user/getstuvo.do")
	public List<Object> getAllStuVO()
	{
		return userService.getAllStuVO();
	}
	
	@RequestMapping("/user/getscvo.do")
	public List<Object> getAllSCVO()
	{
		return userService.getAllSCVO();
	}
	
	@RequestMapping("/user/getpushhistory.do")
	public List<Report> getPushHistory(){
		return userService.getPushHistory();
	}
	
	@RequestMapping("/user/get_push_history_by_date.do")
	public List<Report> getPushHistory(String startDate,String endDate){
		return userService.getPushHistoryByDate(startDate, endDate);
	}
	

	@RequestMapping("/user/get_stu_by_credit.do")
	public List<Object> getStuByCredit(String credit){
		return userService.getStuByCredit(credit);
	}
}
