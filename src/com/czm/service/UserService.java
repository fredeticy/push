package com.czm.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.czm.po.StudentInfo;
import com.czm.po.StudentVO;
import com.czm.vo.Report;
import com.czm.vo.UserVO;

/*辅导员接口
 *主要功能包括查看所有学生信息,搜索学分预警学生,编辑消息并发送,
 *导入学生信息,导入消息目标,删除消息目标,查看推送结果,查看推送历史,
 *设置消息发送需要的第三方配置 
 */
public interface UserService {
	public abstract ArrayList<Object> showAllStudent();
	public abstract ArrayList<Object> selectWarningStudent();
	public abstract void importStudentInfo();
	public abstract void importMessageTarget();
	public abstract void deleteMessageTarget();
	public abstract void showPushResult();
	public abstract void showPushHistory();
	public abstract void setAliyunKey();
	public abstract UserVO login(String userid,String pwd);
	public abstract StudentInfo getStudentInfoBySno(String sno);
	
	public abstract List<Object> getAllMessage();
	
	public abstract List<Object> getAllStuVO();
	public abstract List<Object> getAllSCVO();
	public abstract String sendNotification(String title, String content, String audience,HttpSession session);
	public abstract List<Report> getPushHistory();
}
