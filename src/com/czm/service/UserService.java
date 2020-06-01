package com.czm.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.czm.po.Censor;
import com.czm.po.StudentInfo;
import com.czm.vo.Report;
import com.czm.vo.StuVO;
import com.czm.vo.UserVO;

/*辅导员接口
 *主要功能包括查看所有学生信息,搜索学分预警学生,编辑消息并发送,
 *导入学生信息,导入消息目标,删除消息目标,查看推送结果,查看推送历史,
 *设置消息发送需要的第三方配置 
 */
public interface UserService{
	public abstract Object login(String userid,String pwd);
	public abstract StudentInfo getStudentInfoBySno(String sno);
	
	public abstract List<Object> getAllMessage();
	
	public abstract List<Object> getAllStuVO();
	public abstract List<Object> getAllSCVO();
	public abstract String sendNotification(String title, String content, String audience,String createrid);
	public abstract List<Report> getPushHistory();
	public abstract List<Report> getPushHistoryByDate(String startDate,String endDate);
	public abstract List<Object> getStuByCredit(String credit);
	public abstract ResponseEntity<byte[]> exportStu(HttpServletRequest request) throws IOException;
	public abstract ResponseEntity<byte[]> exportSC(HttpServletRequest request)throws IOException;
	public abstract ResponseEntity<byte[]> downloadTmp(HttpServletRequest request) throws IOException;
	public abstract void saveImportStu(List successList);
	public abstract List<Object> getSCBySno(String sno);
	public abstract List<Object> getAllParVO();
	public abstract ResponseEntity<byte[]> exportParVO(HttpServletRequest request) throws IOException;
	public abstract List<Object> getParBySno(String sno);
	public abstract boolean censorPushContent(String pushContent,String createrid); 
	public abstract void saveCensorInfo(Censor censor);
	
	public abstract String pushAllCredit(Integer prog_k,HttpSession session);
	public abstract String pushAllCreditWarning(Integer prog_k,HttpSession session);
}
