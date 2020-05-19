package com.czm.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.alibaba.excel.EasyExcel;
import com.czm.mapper.UserMapper;
import com.czm.po.Censor;
import com.czm.po.Message;
import com.czm.po.SCVO;
import com.czm.po.StudentGrade;
import com.czm.po.StudentInfo;
import com.czm.po.StudentVO;
import com.czm.po.User;
import com.czm.service.UserService;
import com.czm.util.FileUtil;
import com.czm.util.JPushUtil;
import com.czm.util.WordFilterUtil;
import com.czm.vo.ParentVO;
import com.czm.vo.Report;
import com.czm.vo.StuVO;
import com.czm.vo.StuVOExcel;
import com.czm.vo.UserVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class UserServiceImpl implements UserService{

	@Resource
	private UserMapper userMapper;

	@Override
	public Object login(String userid,String pwd) {
		// TODO Auto-generated method stub
		User user = (User) this.findUserById(userid);
		UserVO vo = new UserVO(user);
		if(user == null){
			vo = new UserVO();
			vo.setMsg("用户不存在");
		}
		else if(!user.getPwd().equals(pwd)){
			vo.setMsg("密码不正确");
		}
		else{
			vo.setMsg("success");
		}
		return vo;
	}
	
	
	private Object findUserById(String userid){
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
	public List<Object> getAllParVO() {
		// TODO Auto-generated method stub
		return userMapper.getAllParVO();
	}

	@Override
	public String sendNotification(String title, String content, String audience,String createrid) {
		// TODO Auto-generated method stub
		JPushUtil jpush = new JPushUtil();
		String res = jpush.sendNotification(title, content, audience);
		JSONObject json = JSONObject.fromObject(res);
		
		String date = DateFormat.getDateInstance().format(new Date());
		
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
	
	private List<Report> generateReportObj(List<Object> msgs){
		List<Report> reps = new ArrayList<>();
		
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
	   	 	System.out.println(rep.toString());
	   	 	if(rep.getMsg_id()==null)
	   	 		continue;
	   		JSONObject obj = arr.getJSONObject(j);
	   		j++;
	   		if(obj.containsKey("error"))
	   			return reps;
	   		System.out.println("##################rep"+rep.getMsg_id());
	   		System.out.println("###################obj"+obj.getString("msg_id"));
	   		if(rep.getMsg_id().equals(obj.getString("msg_id"))){
	   			rep.setPush_received(obj.getString("jpush_received"));
	   		}	
	   		System.out.println(rep.toString());
   	 	}
		return reps;
	}

	@Override
	public List<Report> getPushHistory() {
		// TODO Auto-generated method stub
		List<Object> msgs = getAllMessage();
		return generateReportObj(msgs);
	}

	@Override
	public List<Report> getPushHistoryByDate(String startDate,String endDate) {
		// TODO Auto-generated method stub
		List<Object> msgs = userMapper.getMessageByDate(startDate, endDate);
		return generateReportObj(msgs);
	}

	@Override
	public List<Object> getStuByCredit(String credit) {
		// TODO Auto-generated method stub
		return userMapper.getStuByCredit(credit);
	}
	
	@Override
	public List<Object> getSCBySno(String sno) {
		// TODO Auto-generated method stub
		return userMapper.getSCBySno(sno);
	}
	
	@Override
	public List<Object> getParBySno(String sno) {
		// TODO Auto-generated method stub
		return userMapper.getParBySno(sno);
	}
	
	private List<Object> export_Stu_Data(){
		return getAllStuVO();
	}
	
	private List<Object> export_SC_Data(){
		return getAllSCVO();
	}
	@Override
	public ResponseEntity<byte[]> exportStu(HttpServletRequest request) throws IOException{
		// TODO Auto-generated method stub
		String filename = FileUtil.getPath() + "学生信息" + ".xlsx";
		EasyExcel.write(filename,StudentVO.class).sheet("学生信息").doWrite(export_Stu_Data());
		String download = new String("学生信息.xlsx".getBytes("utf-8"),"iso-8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);	
		headers.setContentDispositionFormData("attachment", download);
		File file = new File(filename);
		return new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),headers, HttpStatus.CREATED);
	}
	
	@Override
	public ResponseEntity<byte[]> exportSC(HttpServletRequest request) throws IOException{
		// TODO Auto-generated method stub
		String filename = FileUtil.getPath() + "学生成绩" + ".xlsx";
		EasyExcel.write(filename,SCVO.class).sheet("学生成绩").doWrite(export_SC_Data());
		String download = new String("学生成绩.xlsx".getBytes("utf-8"),"iso-8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);	
		headers.setContentDispositionFormData("attachment", download);
		File file = new File(filename);
		return new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),headers, HttpStatus.CREATED);
	}
	
	@Override
	public ResponseEntity<byte[]> exportParVO(HttpServletRequest request) throws IOException{
		// TODO Auto-generated method stub
		String filename = FileUtil.getPath() + "家长信息" + ".xlsx";
		List<Object> parVOData = getAllParVO();
		EasyExcel.write(filename,ParentVO.class).sheet("家长信息").doWrite(parVOData);
		String download = new String("家长信息.xlsx".getBytes("utf-8"),"iso-8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);	
		headers.setContentDispositionFormData("attachment", download);
		File file = new File(filename);
		return new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),headers, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<byte[]> downloadTmp(HttpServletRequest request) throws IOException{
		// TODO Auto-generated method stub
		String filename = FileUtil.getPath() + "示例" + ".xlsx";
		EasyExcel.write(filename,StuVO.class).sheet("示例").doWrite(new ArrayList<StuVO>());
		String download = new String("示例.xlsx".getBytes("utf-8"),"iso-8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);	
		headers.setContentDispositionFormData("attachment", download);
		File file = new File(filename);
		return new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),headers, HttpStatus.CREATED);
	}

	@Override
	public void saveImportStu(List successList) {
		// TODO Auto-generated method stub
		if(successList.isEmpty()||successList==null){
			System.out.println("servicesave is null");
			return ;
		}
		int len = successList.size();
		List<StudentInfo> siList = new ArrayList<StudentInfo>();
		List<SCVO> scList = new ArrayList<SCVO>();
		StudentInfo si;
		SCVO sc;
		for(int i =0;i<len;i++){
			Object obj = successList.get(i);
			if(obj instanceof StuVOExcel){
				si = new StudentInfo();
				sc = new SCVO();
				si.setSno(((StuVOExcel) obj).getSno());
				si.setName(((StuVOExcel) obj).getName());
				si.setGender(((StuVOExcel) obj).getGender());
				si.setPhonenumber(((StuVOExcel) obj).getPhone_number());
				si.setClassno(((StuVOExcel) obj).getClass_no());
				si.setGuadianphonenumber(((StuVOExcel) obj).getGuadian_phone_number());
				
				siList.add(si);
				
				sc.setSno(((StuVOExcel) obj).getSno());
				sc.setC1(Double.parseDouble(((StuVOExcel) obj).getScore_c1()));
				sc.setC2(Double.parseDouble(((StuVOExcel) obj).getScore_c2()));
				sc.setC3(Double.parseDouble(((StuVOExcel) obj).getScore_c3()));
				sc.setC4(Double.parseDouble(((StuVOExcel) obj).getScore_c4()));
				sc.setC5(Double.parseDouble(((StuVOExcel) obj).getScore_c5()));
				sc.setC6(Double.parseDouble(((StuVOExcel) obj).getScore_c6()));
				sc.setC7(Double.parseDouble(((StuVOExcel) obj).getScore_c7()));
				scList.add(sc);
			}
		}
		userMapper.addBatchStuInfo(siList);
		userMapper.addBatchSC(scList);
	}


	@Override
	public boolean censorPushContent(String pushContent,String createrid) {
		// TODO Auto-generated method stub
		if(!WordFilterUtil.isContaintBadWord(pushContent, 2))
			return false;
		Set<String> set = WordFilterUtil.getBadWord(pushContent, 2);
		Censor censor = new Censor();
		censor.setCensor_word(JSONArray.fromObject(set).toString());
		censor.setCensor_content_len(pushContent.length());
		censor.setCreaterid(createrid);
		censor.setUpload_date(DateFormat.getDateInstance().format(new Date()));
		censor.setCensor_word_num(set.size());
		saveCensorInfo(censor);
		return true;
	}


	@Override
	public void saveCensorInfo(Censor censor) {
		// TODO Auto-generated method stub
		if(censor==null)
			return;
		userMapper.saveCensorInfo(censor);
					
	}

	

	
}
