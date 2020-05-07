package com.czm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.messaging.handler.annotation.Payload;

import com.czm.po.Message;
import com.czm.po.SCVO;
import com.czm.po.StudentInfo;
import com.czm.po.User;
import com.czm.vo.Report;

public interface UserMapper {
	
	public User findUserById(@Param("userid")String userid);
	public StudentInfo getStudentInfoBySno(String sno);
	public List<Object> getAllStuVO();
	public List<Object> getAllSCVO();
	public void setMessage(@Param("msgpo")Message msgpo);
	
	public List<Object> getAllMessage();
	public List<Object> getPushHistory();
	public List<Object> getMessageByDate(@Param("from_date")String startDate,@Param("to_date")String endDate);
	public List<Object> getStuByCredit(String credit);
	public void addBatchStuInfo(List<StudentInfo> siList);
	public void addBatchSC(List<SCVO> scList);
	
}
