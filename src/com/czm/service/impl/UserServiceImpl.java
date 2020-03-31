package com.czm.service.impl;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.czm.mapper.UserMapper;
import com.czm.po.User;
import com.czm.service.UserService;
import com.czm.vo.UserVO;

@Service
public class UserServiceImpl implements UserService{

	@Resource
	private UserMapper userMapper;

	@Override
	public ArrayList<Object> showAllStudent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> selectWarningStudent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void importStudentInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importMessageTarget() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMessageTarget() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPushResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPushHistory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAliyunKey() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserVO login(String userid,String pwd) {
		// TODO Auto-generated method stub
		User user = this.findUserById(userid);
		UserVO vo = new UserVO(user);
		if(user == null){
			vo.setMsg("用户不存在");
		}
		else if(!user.getPwd().equals(pwd)){
			vo.setMsg("密码不正确");
		}
		else{
			vo.setUserid(userid);
			vo.setRoleid(user.getRoleid());
			vo.setMsg("success");
		}
		return vo;
	}
	
	
	private User findUserById(String userid){
		return userMapper.findUserById(userid);
	}
}
