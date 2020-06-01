package com.czm.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.naming.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
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
import com.czm.util.ProgressUtil;
import com.czm.util.WordFilterUtil;
import com.czm.vo.CreditPushVO;
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
	   	 	if(rep.getMsg_id()==null)
	   	 		continue;
	   		JSONObject obj = arr.getJSONObject(j);
	   		j++;
	   		if(obj.containsKey("error"))
	   			return reps;
	   		if(rep.getMsg_id().equals(obj.getString("msg_id"))){
	   			rep.setPush_received(obj.getString("jpush_received"));
	   		}	
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
	public void saveImportStu(List successList) throws NullPointerException{
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

	private JSONArray generateCreditPush(){
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		List<Object> scVO = userMapper.getAllSCVO();
		List<Object> creditPush = userMapper.getAllCreditPush();
		for(int i=0;i<scVO.size();i++){
			CreditPushVO cwvo = (CreditPushVO) creditPush.get(i);
			SCVO scvo = (SCVO) scVO.get(i); 
			json.put("sno", cwvo.getSno());
			json.put("name",cwvo.getName());
			json.put("aver", cwvo.getAver());
			json.put("failnum", cwvo.getFailnum());
			json.put("credit_failed", cwvo.getCredit_failed());
			json.put("c1", scvo.getC1());
			json.put("c2", scvo.getC2());
			json.put("c3", scvo.getC3());
			json.put("c4", scvo.getC4());
			json.put("c5", scvo.getC5());
			json.put("c6", scvo.getC6());
			json.put("c7", scvo.getC7());
			jsonArr.add(json);
		}
		return jsonArr;
	}
	
	private JSONArray generateCreditWarning(){
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		List<Object> scVO = userMapper.getSCVOLess60();
		List<Object> creditPush = userMapper.getAllCreditWarning();
		for(int i=0;i<scVO.size();i++){
			CreditPushVO cwvo = (CreditPushVO) creditPush.get(i);
			SCVO scvo = (SCVO) scVO.get(i); 
			json.put("sno", cwvo.getSno());
			json.put("name",cwvo.getName());
			json.put("aver", cwvo.getAver());
			json.put("failnum", cwvo.getFailnum());
			json.put("credit_failed", cwvo.getCredit_failed());
			json.put("c1", scvo.getC1());
			json.put("c2", scvo.getC2());
			json.put("c3", scvo.getC3());
			json.put("c4", scvo.getC4());
			json.put("c5", scvo.getC5());
			json.put("c6", scvo.getC6());
			json.put("c7", scvo.getC7());
			jsonArr.add(json);
			
		}
		return jsonArr;
	}
	
	@Override
	public String pushAllCredit(Integer prog_key,HttpSession session) {
		// TODO Auto-generated method stub
		String creditMsg = "";
		String title = "成绩通知";
		String content = "";
		String audience = "";
		JSONArray jsonArr = generateCreditPush();
		int len = jsonArr.size();
		String userid = ((UserVO)session.getAttribute("uservo")).getUserid();
		JSONArray result= new JSONArray();
		double starttime = System.currentTimeMillis();
		JPushUtil jpush = new JPushUtil();
		List<Object> msgList = new ArrayList<>();
		
		for(int i=0;i<len;i++){		
			JSONObject json = jsonArr.getJSONObject(i);
			creditMsg = "{name}家长你好,我是njupt的老师{userid},以下是您孩子的学习成绩,平均学分成绩:{aver},不及格课程数:{failnum}门,所欠学分:{credit_failed}分,"
					+ "各科成绩分别为: 语文:{c1},数学:{c2},英语:{c3},物理:{c4},地理:{c5},生物:{c6},化学:{c7}。"
					+ "希望您能多关心孩子学习,理解鼓励支持和敦促,谢谢!";
			creditMsg = creditMsg.replace("{name}", json.getString("name"));
			creditMsg = creditMsg.replace("{userid}", userid);
			creditMsg = creditMsg.replace("{aver}", json.getString("aver"));
			creditMsg = creditMsg.replace("{failnum}", json.getString("failnum"));
			creditMsg = creditMsg.replace("{credit_failed}", json.getString("credit_failed"));
			creditMsg = creditMsg.replace("{c1}", json.getString("c1"));
			creditMsg = creditMsg.replace("{c2}", json.getString("c2"));
			creditMsg = creditMsg.replace("{c3}", json.getString("c3"));
			creditMsg = creditMsg.replace("{c4}", json.getString("c4"));
			creditMsg = creditMsg.replace("{c5}", json.getString("c5"));
			creditMsg = creditMsg.replace("{c6}", json.getString("c6"));
			creditMsg = creditMsg.replace("{c7}", json.getString("c7"));
			content = creditMsg;
			audience = "Alias" + ":" +json.getString("sno");
			String res = jpush.sendNotification(title, content, audience);
			result.add(res);
			Message msg = new Message();
			msg.setTitle(title);
			msg.setContent(content);
			msg.setDate(DateFormat.getDateInstance().format(new Date()));
			msg.setCreaterid(userid);
			msgList.add(msg);
			Double d = i*1.0/len*100;
			ProgressUtil.progMap.put(prog_key, d.intValue());//计算进度
			System.out.println(creditMsg);
		}
		userMapper.addBatchMessage(msgList);
		double endtime = System.currentTimeMillis();
		System.out.println("执行用时: " + (endtime - starttime));
		return result.toString();
	}
	
	@Override
	public String pushAllCreditWarning(Integer prog_key,HttpSession session) {
		// TODO Auto-generated method stub
		String warningMSG = "";
		String title = "成绩通知";
		String content = "";
		String audience = "";
		JSONArray jsonArr = generateCreditWarning();
		JSONArray result = new JSONArray();
		String userid = ((UserVO)session.getAttribute("uservo")).getUserid();
		int len = jsonArr.size();
		JPushUtil jpush = new JPushUtil();
		List<Object> msgList = new ArrayList<>();		
		for(int i=0;i<len;i++){
			JSONObject json = jsonArr.getJSONObject(i);
			warningMSG = "{name}家长你好,我是njupt的老师{userid},以下是您孩子的学习成绩,平均学分成绩:{aver},不及格课程数:{failnum}门,所欠学分:{credit_failed}分,将有编级的危险" +
			"各科成绩分别为: 语文:{c1},数学:{c2},英语:{c3},物理:{c4},化学:{c5},生物:{c6},地理:{c7}。" +
			"希望您能多关心孩子学习,理解鼓励支持和敦促,谢谢!";
			warningMSG = warningMSG.replace("{name}", json.getString("name"));
			warningMSG = warningMSG.replace("{userid}", userid);
			warningMSG = warningMSG.replace("{aver}", json.getString("aver"));
			warningMSG = warningMSG.replace("{failnum}", json.getString("failnum"));
			warningMSG = warningMSG.replace("{credit_failed}", json.getString("credit_failed"));
			warningMSG = warningMSG.replace("{c1}", json.getString("c1"));
			warningMSG = warningMSG.replace("{c2}", json.getString("c2"));
			warningMSG = warningMSG.replace("{c3}", json.getString("c3"));
			warningMSG = warningMSG.replace("{c4}", json.getString("c4"));
			warningMSG = warningMSG.replace("{c5}", json.getString("c5"));
			warningMSG = warningMSG.replace("{c6}", json.getString("c6"));
			warningMSG = warningMSG.replace("{c7}", json.getString("c7"));
			content = warningMSG;
			audience = "Alias" + ":" +json.getString("sno");
			String res = jpush.sendNotification(title, content, audience);
			result.add(res);
			Message msg = new Message();
			msg.setTitle(title);
			msg.setContent(content);
			msg.setDate(DateFormat.getDateInstance().format(new Date()));
			msg.setCreaterid(userid);
			msgList.add(msg);
			Double d = i*1.0/len*100;
			ProgressUtil.progMap.put(prog_key, d.intValue());//计算进度
			System.out.println(warningMSG);
		}
		userMapper.addBatchMessage(msgList);
			
		return result.toString();
	}

}
