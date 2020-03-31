package com.czm.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.czm.po.User;
import com.czm.service.AdminService;
import com.czm.vo.PageInfo;
@RestController
public class AdminController {
	
	@Resource
	private AdminService adminService;
	
	@RequestMapping("/admin/user/list.do")
	public List<Object> getUserList(){
		return adminService.getAllUsers();
	}
	
	@RequestMapping("/admin/role/list.do")
	public List<Object> getRoleList(){
		return adminService.getAllRoles();
	}
	
	@RequestMapping("/admin/msg/list.do")
	public List<Object> getMsgList(){
		return adminService.getAllMsg();
	}
	
	@RequestMapping("/admin/user/edit.do")
	public List<Object> getUserById(Integer id){
		System.out.println("############################");
		System.out.println("############################");
		System.out.println(id);
		return adminService.getUserById(id);
	}
	
	@RequestMapping("/admin/role/edit.do")
	public List<Object> getRoleById(Integer id){
		return adminService.getRoleById(id);
	}
		
	@RequestMapping("/admin/msg/edit.do")
	public List<Object> getMsgById(Integer id){
		return adminService.getMsgById(id);
	}
	
	@RequestMapping("/admin/user/edituser.do")
	public void editUser(User user){
		adminService.editUser(user);
	}
}
