package com.fansfunding.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.common.entity.Token;
import com.fansfunding.common.service.CheckerService;
import com.fansfunding.common.service.TokenService;
import com.fansfunding.user.entity.User;
import com.fansfunding.user.entity.UserBasic;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.encrypt.AESUtils;
import com.fansfunding.utils.encrypt.MD5Utils;
import com.fansfunding.utils.response.PermissionCode;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;



@Controller
@RequestMapping(path="user")
public class UserController {
	@Autowired
	private UserService userService;	
	@Autowired	
	private CheckerService checkerService;
	
	@Autowired
	private TokenService tokenService;
	
	/**
	 * @param resp
	 * @param cid
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping(path="{cid}/newUser")
	@ResponseBody
	public Status newUser(HttpServletResponse resp,@PathVariable int cid,@RequestParam String phone,@RequestParam String password){
		try{
			//TODO:Checker过期检测
			//TODO:用户存在检测
			password = AESUtils.DecryptByMD5(password, MD5Utils.MD5(checkerService.getCheckerByID(cid).getChecknum()+""));

			Token token = tokenService.requestToken(
					PermissionCode.PERMISSION_NORMAL, phone);
			
			//新建用户
			User user = userService.createUser(
					phone,
					password,
					token.getId()
					);
			user.setPassword("");

			//ADD COOKIES
			resp.addCookie(new Cookie("userId", user.getId()+""));
			resp.addCookie(new Cookie("username", user.getName()));
			resp.addCookie(new Cookie("nickname", user.getNickname()));
			
			//TODO:分配Token
			resp.addCookie(new Cookie("token", token.getValue()));
			//TODO:删除Checker
			return new Status(true,StatusCode.SUCCESS,new UserBasic(user));
		}catch(Exception e){
			return new Status(false,StatusCode.ERROR_DATA,null);
		}
	}
	/**
	 * @param resp
	 * @param cid
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping(path="{cid}/forgetPwd")
	@ResponseBody
	public Status forgetPwd(HttpServletResponse resp,@PathVariable int cid,@RequestParam String phone,@RequestParam String password){
		try{
			//TODO:Checker过期检测
			//TODO:用户存在检测
			password = AESUtils.DecryptByMD5(password, MD5Utils.MD5(checkerService.getCheckerByID(cid).getChecknum()+""));

			Token token = tokenService.requestToken(
					PermissionCode.PERMISSION_NORMAL, phone);
			
			//更新密码
			User user = userService.getUserByPhone(phone);
			user.setPassword(password);
			userService.updatePwd(user);

			//ADD COOKIES
			resp.addCookie(new Cookie("userId", user.getId()+""));
			resp.addCookie(new Cookie("username", user.getName()));
			resp.addCookie(new Cookie("nickname", user.getNickname()));
			
			//TODO:分配Token
			resp.addCookie(new Cookie("token", token.getValue()));
			//TODO:删除Checker
			return new Status(true,StatusCode.SUCCESS,new UserBasic(user));
		}catch(Exception e){
			return new Status(false,StatusCode.ERROR_DATA,null);
		}
	}
	
	
	
	@RequestMapping(path="{userId}/{token}/newPwd")
	@ResponseBody
	public Status newPwd(HttpServletResponse resp,@PathVariable String userId,@PathVariable String token,@RequestParam String password){
		User user = userService.getUserById(Integer.parseInt(userId));
		//TODO:存在性验证
		
		Token rToken = tokenService.lookUpTokenById(user.getToken());
		//TODO:此处审核token权限
		
		if(rToken.getValue().equals(token)){
			user.setPassword(password);
			userService.updatePwd(user);
			return new Status(true, StatusCode.SUCCESS, null);
		}else{
			return new Status(true, StatusCode.FAILD, null);
		}	
	
	}
	
	
	
	

	/**
	 * 用户登录
	 * @param resp
	 * @param tid
	 * @param id
	 * @param name
	 * @param password
	 * @return
	 */
	@RequestMapping(path="{tid}/login")
	@ResponseBody
	public Status login(HttpServletResponse resp,@PathVariable int tid,@RequestParam String id,@RequestParam String name,@RequestParam String password){
		Token token = tokenService.lookUpTokenById(tid);
		if(token ==null)
			return new Status(false, StatusCode.ERROR_DATA, null);
		
		User user =userService.getUser(id, name);
		if(user == null)
			return new Status(false, StatusCode.USER_NULL, null);

		if(!userService.CheckPwd(user.getPassword(), token.getValue(), password))
			return new Status(false, StatusCode.PASSWORD_ERROR, null);
		
		tokenService.setPermission(token.getId(),PermissionCode.PERMISSION_NORMAL);
		user.setPassword("");
		
		//ADD COOKIES
		resp.addCookie(new Cookie("userId", user.getId()+""));
		resp.addCookie(new Cookie("username", user.getName()));
		resp.addCookie(new Cookie("nickname", user.getNickname()));
		
		return new Status(true, StatusCode.SUCCESS, new UserBasic(user));
	}
	
	/**
	 * 登出
	 * @param token
	 * @param id
	 * @return
	 */
	@RequestMapping(path="{userId}/{token}/logout")
	@ResponseBody
	public Status login(@PathVariable String userId,@PathVariable String token){

		User user = userService.getUserById(Integer.parseInt(userId));
		//TODO:存在性验证
		
		Token rToken = tokenService.lookUpTokenById(user.getToken());
		//TODO:此处审核token权限
		if(rToken.getValue().equals(token)){
			userService.updateToken(0);
			return new Status(true, StatusCode.SUCCESS, null);
		}else{
			return new Status(true, StatusCode.FAILD, null);
		}	
		
	}

	/**
	 * 获取用户信息
	 * @param userId 用户ID
	 * @return
	 */
	@RequestMapping(path="{userId}/{token}/info",method=RequestMethod.GET)
	@ResponseBody
	public Status info(@PathVariable int userId,@PathVariable String token){
		User user = userService.getUserById(userId);
		//TODO:此处审核token权限
		//Token rToken = tokenService.lookUpTokenById(user.getToken());
		user.setPassword("");
		return new Status(true, StatusCode.SUCCESS, user);
		
	}

	
}
