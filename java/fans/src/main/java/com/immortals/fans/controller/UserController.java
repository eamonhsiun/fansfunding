package com.immortals.fans.controller;



import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.immortals.fans.entity.Checker;
import com.immortals.fans.entity.Response;
import com.immortals.fans.entity.User;
import com.immortals.fans.entity.UserBasic;
import com.immortals.fans.service.UserService;
import com.immortals.fans.service.CheckerService;
import com.immortals.fans.utils.AESUtils;
import com.immortals.fans.utils.MD5Utils;
import com.immortals.fans.utils.MobileChecker;


@Controller
@RequestMapping(path="user")
public class UserController {
	@Autowired
	private UserService userService;	
	@Autowired	
	private CheckerService checkerService;
	
	@RequestMapping(path="update_pwd")
	@ResponseBody
	public Response<UserBasic> updatePwd(HttpServletRequest req, HttpServletResponse resp){
		//初始化返回数据
		Response<UserBasic> jsonData = new Response<>();

		try{
			int uid =Integer.parseInt(req.getParameter("uid"));
			User user = userService.getUserById(uid);
			//用户不存在
			if(user == null){
				//TODO：暂未实现
				//用户不存在时无法访问此请求
				//以后应屏蔽该ip请求
				jsonData.result=false;
				jsonData.errCode=201;
				jsonData.data=null;
				return jsonData;
			}
			//查找加密token
			String token = user.getToken();
			
			//解码cid
			int cid = Integer.parseInt(AESUtils.DecryptByMD5(req.getParameter("cid"),token));

			//查找checker
			Checker checker = checkerService.getCheckerByID(cid);
			
			
			//checker不存在及过期检测
			if(checker ==null){
				jsonData.result=false;
				jsonData.errCode=202;
				jsonData.data=null;
				return jsonData;
			}
			
			//验证码过期校验,token发生变化
			if(!checker.getToken().equals(token)){
				checkerService.deleteById(cid);
				jsonData.result=false;
				jsonData.errCode=201;
				jsonData.data=null;
				return jsonData;
			}
			
			//验证码过期校验，约定有效时长5分钟，设置为6分钟
			Date end = new Date();
			if(end.getTime()-checker.getRequest_time().getTime()>360000){
				checkerService.deleteById(cid);
				jsonData.result=false;
				jsonData.errCode=201;
				jsonData.data=null;
				return jsonData;
			}
			
			String pwd =AESUtils.DecryptByMD5(req.getParameter("pwd"),token);
			int check = Integer.parseInt(pwd.substring(0, 6));
			
			pwd = pwd.substring(6);
						
			//检查验证码
			if(!(checker.getChecknum()==check)){
				jsonData.result=false;
				//验证码错误
				jsonData.errCode=203;
				jsonData.data=null;
				return jsonData;
			}
			//MD5加密密码
			user.setPassword(MD5Utils.MD5(pwd));
			user.setToken(UUID.randomUUID().toString().replace("-", ""));
			//完成所有安全检测，同意更新密码,生成新token,删除认证单
			userService.updatePwd(user);
			userService.updateToken(user);
			checkerService.deleteById(cid);
			UserBasic userBasic = new UserBasic(user);
			//保护私密数据
			userBasic.setPassword("");
			userBasic.setIMEI("");
			//返回json
			jsonData.result=false;
			jsonData.errCode=200;
			jsonData.data=userBasic;
			
		}catch(Exception e){
			//数据解析错误账户与验证码不匹配
			jsonData.result=false;
			jsonData.errCode=204;
			jsonData.data=null;
			return jsonData;
		}
		
		
		return jsonData;
	}
	
	
	@RequestMapping(path="register_check")
	@ResponseBody
	public Response<UserBasic> registerCheck(HttpServletRequest req, HttpServletResponse resp){
		//初始化返回数据
		Response<UserBasic> jsonData = new Response<>();
		try{
			int id =Integer.parseInt(req.getParameter("id"));
			Checker checker = checkerService.getCheckerByID(id);
			//验码被注销或过期
			if(checker==null){
				jsonData.result=false;
				jsonData.errCode=201;
				jsonData.data=null;
				return jsonData;
				
			}
			//验证码过期校验，约定有效时长5分钟，设置为6分钟
			Date end = new Date();
			if(end.getTime()-checker.getRequest_time().getTime()>360000){
				checkerService.deleteById(id);
				jsonData.result=false;
				jsonData.errCode=201;
				jsonData.data=null;
				return jsonData;
			}
			
			//判断手机用户是否存在，如果存在返回204
			if(userService.getUserByPhone(checker.getPhone())!=null){
				jsonData.result=false;
				jsonData.errCode=204;
				jsonData.data=null;
				//及时释放checker，防止请求被拿去修改密码
				checkerService.deleteByPhone(checker.getPhone());
				return jsonData;
			}
			
			
			//验证码解码
			String check = AESUtils.DecryptByMD5(req.getParameter("check"),checker.getIMEI());
			
			//验证码比对
			if(check.equals(checker.getChecknum()+"")) {
				User user = new User();
				
				user.setName(checker.getPhone());
				user.setNickname(checker.getPhone());
				user.setPassword(UUID.randomUUID().toString().replace("-", ""));
				user.setPhone(checker.getPhone());
				user.setIs_red(0);
				user.setHead(UUID.randomUUID().toString().replace("-", ""));
				user.setRemark("");
				user.setDel_flag('0');
				user.setIMEI(checker.getIMEI());
				user.setToken(UUID.randomUUID().toString().replace("-", ""));
				user.setCreate_by("me");
				user.setUpdate_by("me");
				user.setCreate_time(new Date());
				user.setUpdate_time(new Date());
				
				
				//更新token
				checker.setToken(user.getToken());
				checkerService.updateToken(checker);
				
				//创建用户
				userService.createUser(user);
				
				//返回数据
				UserBasic userbasic = new UserBasic(user);
				userbasic.setPassword("");
				userbasic.setIMEI("");
				
				jsonData.result=true;
				jsonData.errCode=200;
				jsonData.data=userbasic;
			}else{
				//验证码错误
				jsonData.result=false;
				jsonData.errCode=202;
				jsonData.data=null;
			}
			
		}catch(Exception e){
			//解析数据出错，返回203
			jsonData.result=false;
			jsonData.errCode=203;
			jsonData.data=null;
			return jsonData;
		}
		return jsonData;
	}
	
	@RequestMapping(path="forget_check")
	@ResponseBody
	public Response<UserBasic> forgetCheck(HttpServletRequest req, HttpServletResponse resp){
		//初始化返回数据
		Response<UserBasic> jsonData = new Response<>();
		try{
			int id =Integer.parseInt(req.getParameter("id"));
			Checker checker = checkerService.getCheckerByID(id);
			//验证码被注销或过期
			if(checker==null){
				jsonData.result=false;
				jsonData.errCode=201;
				jsonData.data=null;
				return jsonData;
				
			}
			//验证码过期校验，约定有效时长5分钟，设置为6分钟
			Date end = new Date();
			if(end.getTime()-checker.getRequest_time().getTime()>360000){
				checkerService.deleteById(id);
				jsonData.result=false;
				jsonData.errCode=201;
				jsonData.data=null;
				return jsonData;
			}
			
			//判断手机用户是否存在，如果不存在返回204
			User user= userService.getUserByPhone(checker.getPhone());
			if(user==null){
				jsonData.result=false;
				jsonData.errCode=204;
				jsonData.data=null;
				//及时释放checker
				checkerService.deleteByPhone(checker.getPhone());
				return jsonData;
			}
			
			
			//验证码解码
			String check = AESUtils.DecryptByMD5(req.getParameter("check"),checker.getIMEI());
			
			//验证码比对
			if(check.equals(checker.getChecknum()+"")) {
				
				//更新token
				user.setToken(UUID.randomUUID().toString().replace("-", ""));
				checker.setToken(user.getToken());
				checkerService.updateToken(checker);
				userService.updateToken(user);
				
				//返回数据
				UserBasic userbasic = new UserBasic(user);
				userbasic.setPassword("");
				userbasic.setIMEI("");
				
				jsonData.result=true;
				jsonData.errCode=200;
				jsonData.data=userbasic;
			}else{
				//验证码错误
				jsonData.result=false;
				jsonData.errCode=202;
				jsonData.data=null;
			}
			
		}catch(Exception e){
			//解析数据出错，返回203
			jsonData.result=false;
			jsonData.errCode=203;
			jsonData.data=null;
			return jsonData;
		}
		return jsonData;
	}
	
	
	@RequestMapping(path="gen_checker")
	@ResponseBody
	public Response<Integer> genChecker(HttpServletRequest req, HttpServletResponse resp){
		//初始化返回数据
		Response<Integer> jsonData = new Response<>();
		try {
			//解析数据
			String S1 = req.getParameter("IMEI");
			String phone = AESUtils.DecryptByMD5(req.getParameter("phone"),S1);
						
			Date end = new Date();
			//手机号判断是否连续请求发送给同一号码(间隔1s)
			Checker start=checkerService.getCheckerByPhone(phone);
			if(start!=null){
				//时间小于1分钟，拒绝请求，返回202
				if(end.getTime()-start.getRequest_time().getTime()<60000){
					jsonData.result=false;
					jsonData.errCode=202;
					jsonData.data=null;
					return jsonData;
				//时间大于1分钟，删除原记录，同意请求
				}else{
					checkerService.deleteByPhone(phone);
				}
			}
			
			//IMEI判断是否同一设备连续请求(间隔1s)
			start=checkerService.getCheckerByIMEI(S1);
			if(start!=null){
				//时间小于1分钟，拒绝请求，返回202
				if(end.getTime()-start.getRequest_time().getTime()<60000){
					jsonData.result=false;
					jsonData.errCode=202;
					jsonData.data=null;
					return jsonData;
				//时间大于1分钟，删除原记录，同意请求
				}else{
					checkerService.deleteByIMEI(S1);
				}
			}
			
			//验证均通过，开始生产Checker
			int checker = (int)(Math.random()*899999) +100000;
			int id = checkerService.createChecker(S1, phone, checker, end);
			if(id > 0){
				MobileChecker.sendMsg(phone, checker);
				jsonData.result=true;
				jsonData.errCode=200;
				jsonData.data=id;
			}else{
				jsonData.result=false;
				jsonData.errCode=204;
				jsonData.data=null;
				return jsonData;
			}
			
		} catch (Exception e) {
			//解析数据出错，返回203
			jsonData.result=false;
			jsonData.errCode=203;
			jsonData.data=null;
			return jsonData;
		}
		//userService.createUser(null);
		return jsonData;
	}
	
	
	
	@RequestMapping(path="login")
	@ResponseBody
	public Response<UserBasic> login(HttpServletRequest req, HttpServletResponse resp){
		//初始化返回数据
		Response<UserBasic> jsonData = new Response<>();
		try{
			//解析数据
			String S1 = req.getParameter("IMEI");
			String uid = AESUtils.DecryptByMD5(req.getParameter("uid"),S1);
			String name = AESUtils.DecryptByMD5(req.getParameter("name"),S1);
			String pwd = MD5Utils.MD5(AESUtils.DecryptByMD5(req.getParameter("pwd"),S1));
			
			System.err.println(S1+" "+uid+" "+name+" "+pwd);
			
			if(uid.equals("")){
				User user = userService.getUserByName(name);
				if(user==null){
					//用户不存在
					jsonData.result=false;
					jsonData.errCode=202;
					jsonData.data=null;
					return jsonData;
				}
				if(user.getPassword().equals(pwd)){
					user.setToken(UUID.randomUUID().toString().replace("-", ""));
					userService.updateToken(user);
					UserBasic userBasic = new UserBasic(user);
					userBasic.setPassword("");
					
					jsonData.result=true;
					jsonData.errCode=200;
					jsonData.data=userBasic;
					return jsonData;
				}else{
					jsonData.result=false;
					jsonData.errCode=201;
					jsonData.data=null;
					return jsonData;
				}
				
			
			}else{
				User user = userService.getUserById(Integer.parseInt(uid));
				if(user==null){
					//用户不存在
					jsonData.result=false;
					jsonData.errCode=202;
					jsonData.data=null;
					return jsonData;
				}
				
				if(user.getPassword().trim().equals(pwd.trim())){
					user.setToken(UUID.randomUUID().toString().replace("-", ""));
					userService.updateToken(user);
					UserBasic userBasic = new UserBasic(user);
					userBasic.setPassword("");
					jsonData.result=true;
					jsonData.errCode=200;
					jsonData.data=userBasic;
					return jsonData;
				}else{
					jsonData.result=false;
					jsonData.errCode=201;
					jsonData.data=null;
					return jsonData;
				}
			}
		}catch(Exception e){
			//解析数据出错，返回203
			jsonData.result=false;
			jsonData.errCode=203;
			jsonData.data=null;
			return jsonData;
		}
	}
	
	@RequestMapping(path="logout")
	@ResponseBody
	public Response<Integer> logout(HttpServletRequest req, HttpServletResponse resp){
		//初始化返回数据
		Response<Integer> jsonData = new Response<>();
		try{
			int uid =Integer.parseInt(req.getParameter("uid"));
			User user = userService.getUserById(uid);
			if(user == null){
				//用户不存在
				jsonData.result=false;
				jsonData.errCode=202;
				jsonData.data=null;
				return jsonData;
			}
			System.err.println(user.getToken());
			System.err.println(req.getParameter("name"));
			System.err.println(uid);
			System.err.println(user.getName());
			String name = AESUtils.DecryptByMD5(req.getParameter("name"), user.getToken());
			if(!name.equals(user.getName())){
				//验证不通过
				jsonData.result=false;
				jsonData.errCode=202;
				jsonData.data=null;
				return jsonData;
			}
			user.setToken("0");
			userService.updateToken(user);
			jsonData.result=true;
			jsonData.errCode=200;
			jsonData.data=1;
			return jsonData;
		}catch(Exception e){
			//解析错误
			jsonData.result=false;
			jsonData.errCode=203;
			jsonData.data=null;
			return jsonData;
		}
	}
	
	
	
	
	
	
	
}
