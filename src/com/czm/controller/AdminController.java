package com.czm.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.czm.po.Message;
import com.czm.po.Role;
import com.czm.po.User;
import com.czm.service.AdminService;
import com.czm.vo.UserVO;
@RestController
public class AdminController {
	
	@Resource
	private AdminService adminService;
	
	@RequestMapping("/admin/user/list.do")
	public List<Object> getUserList(HttpSession session){
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
	
	@RequestMapping("/admin/user/editrole.do")
	public void editRole(Role role){
		adminService.editRole(role);
	}

	@RequestMapping("/admin/user/editmsg.do")
	public void editMsg(Message msg){
		adminService.editMsg(msg);
	}
	
	@RequestMapping(value = "/admin/user/add.do",produces="text/html;charset=UTF-8")
	public String addUser(User user){
		if(adminService.addUser(user))
			return "添加成功";
		else 
			return "用户名已存在";
	}
	
	@RequestMapping(value = "/admin/role/add.do",produces="text/html;charset=UTF-8")
	public String addRole(Role role){
		if(adminService.addRole(role))
			return "添加成功";
		else 
			return "角色类型已存在";
	}

	@RequestMapping(value = "/admin/msg/add.do",produces="text/html;charset=UTF-8")
	public String addMsg(Message msg){
		if(adminService.addMsg(msg))
			return "添加成功";
		else 
			return "标题已存在";
	}
	
	@RequestMapping("/admin/user/delete.do")
	public void deleteUser(Integer id){
		adminService.deleteUser(id);
	}

	@RequestMapping("/admin/role/delete.do")
	public void deleteRole(Integer id){
		adminService.deleteRole(id);
	}
	
	@RequestMapping("/admin/msg/delete.do")
	public void deleteMsg(Integer id){
		adminService.deleteMsg(id);
	}
	
	@RequestMapping("/admin/user/search.do")
	public List<Object> searchUser(String userid){
		return adminService.getUserBySearch(userid);
	}
	
	@RequestMapping("/admin/role/search.do")
	public List<Object> searchRole(String type){
		return adminService.getRoleBySearch(type);
	}
	
	@RequestMapping("/admin/msg/search.do")
	public List<Object> searchMsg(String title){
		return adminService.getMsgBySearch(title);
	}
}
