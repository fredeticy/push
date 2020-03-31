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
	
	public abstract void editUser(@Param("user")User user);
	public abstract void editRole(@Param("role")Role role);
	public abstract void editMsg(@Param("msg")Message msg);
}
