package com.czm.service.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.czm.mapper.UserMapper;
import com.czm.po.Message;
import com.czm.po.StudentInfo;
import com.czm.po.StudentVO;
import com.czm.po.User;
import com.czm.service.UserService;
import com.czm.util.JPushUtil;
import com.czm.vo.Report;
import com.czm.vo.UserVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
	
	public  StudentInfo getStudentInfoBySno(String sno){
		return userMapper.getStudentInfoBySno(sno);
	}

	@Override
	public List<Object> getAllStuVO() {
		// TODO Auto-generated method stub
		return userMapper.getAllStuVO();
	}

	@Override
	public List<Object> getAllSCVO() {
		// TODO Auto-generated method stub
		return userMapper.getAllSCVO();
	}

	@Override
	public String sendNotification(String title, String content, String audience,HttpSession session) {
		// TODO Auto-generated method stub
		JPushUtil jpush = new JPushUtil();
		String res = jpush.sendNotification(title, content, audience);
		UserVO vo = (UserVO)session.getAttribute("uservo");
		JSONObject json = JSONObject.fromObject(res);
		
		String date = DateFormat.getDateInstance().format(new Date());
		String createrid = vo.getUserid();
		
		String msg_id = json.get("msg_id").toString();
		Message msgpo = new Message();
		msgpo.setTitle(title);
		msgpo.setContent(content);
		msgpo.setDate(date);
		msgpo.setCreaterid(createrid);
		msgpo.setMsg_id(msg_id);
		
		userMapper.setMessage(msgpo);
		return res;
	}

	@Override
	public List<Object> getAllMessage() {
		// TODO Auto-generated method stub
		return userMapper.getAllMessage();
	}
	
	private List<Report> generateReportObj(){
		List<Report> reps = new ArrayList<>();
		List<Object> msgs = getAllMessage();
		JPushUtil jpush = new JPushUtil();
		int len = msgs.size();
		String msg_ids = "";
		for(int i=0;i<len;i++)
		{
			Message msg = (Message)msgs.get(i);
			Report r = new Report(msg);
			reps.add(r);
			if(msg.getMsg_id()!=null)
				msg_ids+=msg.getMsg_id()+",";
		}
		if(msg_ids=="")
			return reps;
		msg_ids = msg_ids.substring(0, msg_ids.length()-1);
		String res = jpush.getReport(msg_ids);
		JSONArray arr = JSONArray.fromObject(res);
   	 	for(int i=0,j=0;i<len;i++){
	   	 	Report rep = (Report)reps.get(i);
	   	 	if(rep.getMsg_id()==null)
	   	 		continue;
	   		JSONObject obj = arr.getJSONObject(j);
	   		j++;
	   		if(obj.containsKey("error"))
	   			return reps;
	   		if(rep.getMsg_id()==obj.getString("msg_id")){
	   			rep.setPush_received(obj.getString("jpush_received"));
	   		}
	   		
   	 }
		return reps;
	}

	@Override
	public List<Report> getPushHistory() {
		// TODO Auto-generated method stub
		
		return generateReportObj();
	}
}
