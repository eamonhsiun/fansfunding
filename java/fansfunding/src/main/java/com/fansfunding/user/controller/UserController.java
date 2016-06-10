package com.fansfunding.user.controller;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.user.entity.Checker;
import com.fansfunding.user.entity.User;
import com.fansfunding.user.service.CheckerService;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.encrypt.AESUtils;
import com.fansfunding.utils.encrypt.MD5Utils;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;



@Controller
@RequestMapping(path="user")
public class UserController {
	@Autowired
	private UserService userService;	
	@Autowired	
	private CheckerService checkerService;
	
	@RequestMapping(path="update_pwd")
	@ResponseBody
	public Status updatePwd(@RequestParam int uid,@RequestParam String cid,@RequestParam String pwd){
		try{
			User user = userService.getUserById(uid);
			if(user == null){
				return new Status(false, StatusCode.USER_NULL, null);
			}
			String token = user.getToken();
			int icid = Integer.parseInt(AESUtils.DecryptByMD5(cid,token));
			Checker checker = checkerService.getCheckerByID(icid);
			
			if((!checkerService.isValid(icid))||(!checker.getToken().equals(token))){
				return new Status(false, StatusCode.CHECKER_N_VAILD, null);
			}
			pwd =AESUtils.DecryptByMD5(pwd,token);
			int check = Integer.parseInt(pwd.substring(0, 6));
			pwd = pwd.substring(6);
						
			if(!(checker.getChecknum()==check)){
				return new Status(false,StatusCode.CHECKER_ERROR,null);
			}
			user.setPassword(MD5Utils.MD5(pwd));
			return new Status(true,StatusCode.SUCCESS,userService.updatePwd(user,checker));
		}catch(Exception e){
			return new Status(false,StatusCode.ERROR_DATA,null);
		}
		
	}
	
	
	@RequestMapping(path="register_check")
	@ResponseBody
	public Status registerCheck(@RequestParam int id, @RequestParam String check){
		try{
			if(!checkerService.isValid(id)){
				return new Status(false, StatusCode.CHECKER_N_VAILD, null);
			}
			Checker checker = checkerService.getCheckerByID(id);
			User user =userService.getUserByPhone(checker.getPhone());
			
			if(user!=null){
				return new Status(false, StatusCode.USER_EXIST, null);
			}
			if(AESUtils.DecryptByMD5(check,checker.getIMEI()).equals(checker.getChecknum()+"")) {		
				user = userService.createUser(checker.getPhone(),checker.getIMEI());		
				return new Status(true, StatusCode.SUCCESS, userService.RefreshToken(user, checker));
			}else{
				return new Status(false,StatusCode.CHECKER_ERROR,null);
			}
			
		}catch(Exception e){
			return new Status(false,StatusCode.ERROR_DATA,null);
		}
	}
	
	@RequestMapping(path="forget_check")
	@ResponseBody
	public Status forgetCheck(@RequestParam int id,@RequestParam String check){
		try{
			if(!checkerService.isValid(id)){
				return new Status(false, StatusCode.CHECKER_N_VAILD, null);
			}
			Checker checker = checkerService.getCheckerByID(id);
			User user =userService.getUserByPhone(checker.getPhone());
			
			if(user==null){
				checkerService.deleteById(id);
				return new Status(false, StatusCode.USER_NULL, null);
			}

			if(AESUtils.DecryptByMD5(check,checker.getIMEI()).equals(checker.getChecknum()+"")) {
				return new Status(true, StatusCode.SUCCESS, userService.RefreshToken(user, checker));
			}else{
				return new Status(false,StatusCode.CHECKER_ERROR,null);
			}
			
		}catch(Exception e){
			return new Status(false,StatusCode.ERROR_DATA,null);
		}
	}
	
	
	@RequestMapping(path="gen_checker")
	@ResponseBody
	public Status genChecker(@RequestParam String IMEI, @RequestParam String phone){
		try {
			phone = AESUtils.DecryptByMD5(phone,IMEI);
			if(checkerService.isTimeTooShort(IMEI,phone)){
				return new Status(false, StatusCode.TOO_FREQUENT, null);
			}
			int id =checkerService.genChecker(IMEI, phone);
			
			if(id > 0){
				return new Status(true, StatusCode.SUCCESS, id);
			}else{
				return new Status(false,StatusCode.FAILD,null);
			}
		} catch (Exception e) {
			return new Status(false,StatusCode.ERROR_DATA,null);
		}
	}
	
	
	
	@RequestMapping(path="login")
	@ResponseBody
	public Status login(@RequestParam String IMEI,@RequestParam String uid,@RequestParam String name,@RequestParam String pwd){
		try{
			String duid = AESUtils.DecryptByMD5(uid,IMEI);
			String dname = AESUtils.DecryptByMD5(name,IMEI);
			String dpwd = MD5Utils.MD5(AESUtils.DecryptByMD5(pwd,IMEI));
			
			User user;
			if(duid.equals("")){
				user = userService.getUserByName(dname);
			}else{
				user = userService.getUserById(Integer.parseInt(duid));
			}
			if(user==null){
				return new Status(false,StatusCode.USER_NULL,null);
			}
			if(user.getPassword().equals(dpwd)){
				user.setToken(UUID.randomUUID().toString().replace("-", ""));
				return new Status(true,StatusCode.SUCCESS,userService.updateToken(user));
			}else{
				return new Status(false,StatusCode.PASSWORD_ERROR,null);
			}
		}catch(Exception e){
			return new Status(false,StatusCode.ERROR_DATA,null);
		}
	}
	
	@RequestMapping(path="logout")
	@ResponseBody
	public Status logout(@RequestParam String uid, @RequestParam String name){

		try{
			int duid =Integer.parseInt(uid);
			User user = userService.getUserById(duid);
			if(user == null){
				return new Status(false,StatusCode.USER_NULL,null);
			}
			String dname = AESUtils.DecryptByMD5(name, user.getToken());
			if(!dname.equals(user.getName())){
				return new Status(false,StatusCode.FAILD,null);
			}
			user.setToken("0");
			userService.updateToken(user);
			return new Status(true,StatusCode.SUCCESS,1);
		}catch(Exception e){
			return new Status(false,StatusCode.ERROR_DATA,null);
		}
	}
	
	/**
	 * 获取用户信息
	 * @param userId 用户ID
	 * @return
	 */
	@RequestMapping(path="{userId}/info",method=RequestMethod.GET)
	@ResponseBody
	public Status info(@PathVariable Integer userId){
		return null;
	}

	
}
