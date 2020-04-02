package com.czm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.czm.mapper.AdminMapper;
import com.czm.po.Message;
import com.czm.po.Role;
import com.czm.po.User;
import com.czm.service.AdminService;
import com.czm.vo.PageInfo;
import com.czm.vo.UserVO;

@Service
public class AdminServiceImpl implements AdminService{

	@Resource
	private AdminMapper adminMapper;
	
	@Override
	/*public List<Object> getAllUsers(PageInfo pageInfo) {
		// TODO Auto-generated method stub
		Integer start = pageInfo.getPgaeNo();
		Integer limit = pageInfo.getPageSize();
		return adminMapper.getAllUsers(start,limit);
	}
	*/
	public List<Object> getAllUsers() {
		// TODO Auto-generated method stub
		List<Object> list = adminMapper.getAllUsers();
		
		return adminMapper.getAllUsers();
	}

	


	@Override
	public List<Object> getAllRoles() {
		// TODO Auto-generated method stub
		return adminMapper.getAllRoles();
	}
	
	@Override
	public List<Object> getAllMsg() {
		// TODO Auto-generated method stub
		return adminMapper.getAllMsg();
	}

	@Override
	public List<Object> getUserById(Integer id) {
		// TODO Auto-generated method stub
		return adminMapper.getUserById(id);
	}

	@Override
	public List<Object> getRoleById(Integer id) {
		// TODO Auto-generated method stub
		return adminMapper.getRoleById(id);
	}

	@Override
	public List<Object> getMsgById(Integer id) {
		// TODO Auto-generated method stub
		return adminMapper.getMsgById(id);
	}
	
	@Override
	public void editUser(User user){
		adminMapper.editUser(user);
	}

	@Override
	public void editRole(Role role) {
		// TODO Auto-generated method stub
		adminMapper.editRole(role);
	}

	@Override
	public void editMsg(Message msg) {
		// TODO Auto-generated method stub
		adminMapper.editMsg(msg);
	}

	@Override
	public boolean addUser(User user) {
		// TODO Auto-generated method stub
		User u = (User)adminMapper.getUserByUserid(user.getUserid());
		if(u == null){
			adminMapper.addUser(user);
			return true;
		}
		return false;
	}

	@Override
	public void deleteUser(Integer id) {
		// TODO Auto-generated method stub
		adminMapper.deleteUser(id);
	}
	
	@Override
	public boolean addRole(Role role) {
		// TODO Auto-generated method stub
		Role r = (Role)adminMapper.getRoleByType(role.getType());
		if(r == null){
			adminMapper.addRole(role);
			return true;
		}
		return false;
	}

	@Override
	public void deleteRole(Integer id) {
		// TODO Auto-generated method stub
		adminMapper.deleteRole(id);
	}

	@Override
	public boolean addMsg(Message msg) {
		// TODO Auto-generated method stub
		Message m = (Message)adminMapper.getMsgByTitle(msg.getTitle());
		if(m == null){
			adminMapper.addMsg(msg);
			return true;
		}
		return false;
	}

	@Override
	public void deleteMsg(Integer id) {
		// TODO Auto-generated method stub
		adminMapper.deleteMsg(id);
	}

	@Override
	public List<Object> getUserBySearch(String userid) {
		// TODO Auto-generated method stub
		return adminMapper.getUserBySearch(userid);
	}

	@Override
	public List<Object> getRoleBySearch(String type) {
		// TODO Auto-generated method stub
		return adminMapper.getRoleBySearch(type);
	}

	@Override
	public List<Object> getMsgBySearch(String title) {
		// TODO Auto-generated method stub
		return adminMapper.getMsgBySearch(title);
	}


}
