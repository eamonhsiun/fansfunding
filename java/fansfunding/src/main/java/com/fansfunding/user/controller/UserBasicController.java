package com.fansfunding.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.common.entity.Checker;

import com.fansfunding.common.entity.Token;
import com.fansfunding.common.service.CheckerService;
import com.fansfunding.common.service.TokenService;
import com.fansfunding.user.entity.User;
import com.fansfunding.user.entity.UserBasic;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.encrypt.AESUtils;

import com.fansfunding.utils.response.PermissionCode;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping(path = "userbasic")
public class UserBasicController {
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
	 * @throws Exception
	 */
	@RequestMapping(path = "newUser")
	@ResponseBody
	public Status newUser(@RequestParam int checker, @RequestParam String password, @RequestParam String token)
			throws Exception {
		
		int cid;
		Checker c;
		User user;
		try {
			// TODO:Checker过期检测
			cid = Integer.valueOf(AESUtils.Decrypt(token, AESUtils.ENCRYPT_KEY));
			c = checkerService.getCheckerByID(cid);

			if (!(c.getChecknum() == checker)) {
				return new Status(false, StatusCode.CHECKER_ERROR, null, null);
			}
		} catch (Exception e) {
			return new Status(false, StatusCode.ERROR_DATA, null, null);
		}
		if (password.length() != 32) {
			return new Status(false, StatusCode.PASSWORD_ERROR, null, null);
		}

		Token newToken = tokenService.requestToken(PermissionCode.PERMISSION_NORMAL, c.getPhone());

		try {
			user = userService.createUser(c.getPhone(), password, newToken.getId());
			user.setPassword("");
		} catch (Exception e) {
			return new Status(false, StatusCode.USER_EXIST, null, null);

		}
		checkerService.deleteById(cid);
		return new Status(true, StatusCode.SUCCESS, new UserBasic(user),
				AESUtils.Encrypt(newToken.getId() + "", AESUtils.ENCRYPT_KEY));
	}

	/**
	 * @param resp
	 * @param cid
	 * @param phone
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "forgetPwd")
	@ResponseBody
	public Status forgetPwd(@RequestParam int checker, @RequestParam String password, @RequestParam String token) throws Exception {
		int cid;
		Checker c;
		User user;
		try {
			// TODO:Checker过期检测
			cid = Integer.valueOf(AESUtils.Decrypt(token, AESUtils.ENCRYPT_KEY));
			c = checkerService.getCheckerByID(cid);

			if (!(c.getChecknum() == checker)) {
				return new Status(false, StatusCode.CHECKER_ERROR, null, null);
			}
		} catch (Exception e) {
			return new Status(false, StatusCode.ERROR_DATA, null, null);
		}

		if (password.length() != 32) {
			return new Status(false, StatusCode.PASSWORD_ERROR, null, null);
		}

		Token newToken = tokenService.requestToken(PermissionCode.PERMISSION_NORMAL, c.getPhone());


		user = userService.getUserByPhone(c.getPhone());
		if(user==null)return new Status(false, StatusCode.USER_NULL, null, null);
		user.setPassword(password);
		userService.updatePwd(user);
		
		checkerService.deleteById(cid);
		return new Status(true, StatusCode.SUCCESS, new UserBasic(user),
				AESUtils.Encrypt(newToken.getId() + "", AESUtils.ENCRYPT_KEY).replace("+", "%2B"));
		


	}

	@RequestMapping(path = "{userId}/newPwd")
	@ResponseBody
	public Status newPwd(@PathVariable String userId, @RequestParam String token,@RequestParam String password) throws Exception {
		User user = userService.getUserById(Integer.parseInt(userId));
		// TODO:存在性验证
		int tid;
		try {
			tid = Integer.parseInt(AESUtils.Decrypt(token, AESUtils.ENCRYPT_KEY));
		} catch (Exception e) {
			return new Status(false, StatusCode.ERROR_DATA, null, null);
		}
		Token rToken = tokenService.lookUpTokenById(tid);
		if(rToken.getPermission()==PermissionCode.PERMISSION_NORMAL){
			user.setPassword(password);
			userService.updatePwd(user);
		}
		return new Status(true, StatusCode.SUCCESS, new UserBasic(user),
				AESUtils.Encrypt(rToken.getId() + "", AESUtils.ENCRYPT_KEY).replace("+", "%2B"));
	}

	/**
	 * 用户登录
	 * 
	 * @param resp
	 * @param tid
	 * @param id
	 * @param name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "login")
	@ResponseBody
	public Status login(@RequestParam String name, @RequestParam String password) throws Exception {
		User user = userService.getUserByName(name);
		if (user == null)
			return new Status(false, StatusCode.USER_NULL, null, null);

		if (!userService.CheckPwd(user.getPassword(), password))
			return new Status(false, StatusCode.PASSWORD_ERROR, null, null);
		
		// 权限控制
		Token newToken = tokenService.requestToken(PermissionCode.PERMISSION_NORMAL, user.getName());

		user.setPassword("");
		return new Status(true, StatusCode.SUCCESS, new UserBasic(user),
				AESUtils.Encrypt(newToken.getId() + "", AESUtils.ENCRYPT_KEY).replace("+", "%2B"));
	}
	
}
