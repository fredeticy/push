package com.czm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.czm.po.Message;
import com.czm.po.Role;
import com.czm.po.User;

public interface AdminMapper {
//	public abstract List<Object> getAllUsers(Integer start,Integer limit);
	public abstract List<Object> getAllUsers();
	public abstract List<Object> getAllRoles();
	public abstract List<Object> getAllMsg();
	
	public abstract List<Object> getUserById(Integer id);
	public abstract List<Object> getRoleById(Integer id);
	public abstract List<Object> getMsgById(Integer id);
	
	public abstract Object getUserByUserid(String userid);
	public abstract Object getRoleByType(String type);	
	public abstract Object getMsgByTitle(String title);
	
	
	public abstract void editUser(@Param("user")User user);
	public abstract void editRole(@Param("role")Role role);
	public abstract void editMsg(@Param("msg")Message msg);
	
	public abstract void addUser(@Param("user")User user);
	public abstract void addRole(@Param("role")Role role);
	public abstract void addMsg(@Param("msg")Message msg);
	
	public abstract void deleteUser(Integer id);
	public abstract void deleteRole(Integer id);
	public abstract void deleteMsg(Integer id);
	
	public abstract List<Object> getUserBySearch(@Param("userid")String userid);
	public abstract List<Object> getRoleBySearch(@Param("type")String type);
	public abstract List<Object> getMsgBySearch(@Param("title")String title);
}
