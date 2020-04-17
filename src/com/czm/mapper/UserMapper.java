package com.czm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.messaging.handler.annotation.Payload;

import com.czm.po.Message;
import com.czm.po.StudentInfo;
import com.czm.po.User;

public interface UserMapper {
	
	public User findUserById(@Param("userid")String userid);
	public StudentInfo getStudentInfoBySno(String sno);
	public List<Object> getAllStuVO();
	public List<Object> getAllSCVO();
	public void setMessage(@Param("msgpo")Message msgpo);
	
	public List<Object> getAllMessage();
	public List<Object> getPushHistory();
	
}
