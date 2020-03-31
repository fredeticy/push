package com.czm.service;

import java.util.List;

import com.czm.po.Message;
import com.czm.po.Role;
import com.czm.po.User;
import com.czm.vo.PageInfo;

/*管理员接口
 *负责管理用户,提供查看用户,增加用户,删除用户,修改用户密码功能
 *2020-3-26
 */
public interface AdminService {
//	public List<Object> getAllUsers(PageInfo pageInfo);
	public List<Object> getAllUsers();
	public Boolean addUser();
	public Boolean deleteUser();
	public Boolean setUser();
	public List<Object> getAllRoles();
	public List<Object> getAllMsg();
	public List<Object> getUserById(Integer id);
	public List<Object> getRoleById(Integer id);
	public List<Object> getMsgById(Integer id);
	public void editUser(User user);
	public void editRole(Role role);
	public void editMsg(Message msg);
}
