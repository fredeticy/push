package com.czm.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
@RestController
public class UserController {
	
	@Resource
	private UserService userService;
	
	
	@RequestMapping(value = "/login.do")
	@ResponseBody
	public Object login(String userid,String pwd,HttpSession session){
		UserVO vo = (UserVO)userService.login(userid, pwd);
		System.out.println(vo.getMsg() + "#####################");
		if(vo.getUser() == null)
			vo.setUser(new User());
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
		String censorContent = title + "#" + content ;
		UserVO vo = (UserVO)session.getAttribute("uservo");
		String createrid = vo.getUserid();
		if(userService.censorPushContent(censorContent, createrid))
			return "{\"wrong\":\"内容包含敏感字符,发送失败,请检查内容,或咨询管理员\"}";
		return userService.sendNotification(title,content,audience,createrid);
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
	
	@RequestMapping("/user/getparvo.do")
	public List<Object> getAllParVO()
	{
		return userService.getAllParVO();
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
	
	@RequestMapping("/user/get_sc_by_sno.do")
	public List<Object> getSCBySno(String sno){
		return userService.getSCBySno(sno);
	}
	
	@RequestMapping("/user/get_par_by_sno.do")
	public List<Object> getParBySno(String sno){
		return userService.getParBySno(sno);
	}
	
	@RequestMapping("/user/export_stu.do")
	public ResponseEntity<byte[]> exportStu(HttpServletRequest request) throws IOException{
		return userService.exportStu(request);
	}
	
	@RequestMapping("/user/export_sc.do")
	public ResponseEntity<byte[]> exportSC(HttpServletRequest request) throws IOException{
		return userService.exportSC(request);
	}
	
	@RequestMapping("/user/export_par.do")
	public ResponseEntity<byte[]> exportParVO(HttpServletRequest request) throws IOException{
		return userService.exportParVO(request);
	}
	
	@RequestMapping("/user/download_tmp.do")
	public ResponseEntity<byte[]> downloadTmp(HttpServletRequest request) throws IOException{
		return userService.downloadTmp(request);
	}
	
}
