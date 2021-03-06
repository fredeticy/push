package com.czm.vo;
import com.czm.po.User;

public class UserVO {
	
	
	@Override
	public String toString() {
		if(user == null)
			return "UserVO [ " + "msg=" + msg + "]";
		return "UserVO [user=" + user.toString() + ", msg=" + msg + "]";
	}
	private User user;
	public UserVO(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}
	public UserVO() {
		// TODO Auto-generated constructor stub
	}
	public String getPhonenumber() {
		return user.getPhonenumber();
	}
	public void setPhonenumber(String phonenumber) {
		user.setPhonenumber(phonenumber);
	}
	public String getEmail() {
		return user.getEmail();
	}
	public void setEmail(String email) {
		user.setEmail(email);
	}
	public String getAddress() {
		return user.getAddress();
	}
	public void setAddress(String address) {
		user.setAddress(address);
	}
	private String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUserid() {
		return user.getUserid();
	}
	public void setUserid(String userid) {
		user.setUserid(userid);
	}
	public Integer getRoleid() {
		return user.getRoleid();
	}
	public void setRoleid(Integer roleid) {
		user.setRoleid(roleid);
	}
	public Integer getId(){
		return user.getId();
	}
	public void setId(Integer id){
		user.setId(id);
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
