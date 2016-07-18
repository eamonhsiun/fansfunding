package com.fansfunding.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.fansfunding.common.entity.Token;
import com.fansfunding.common.service.TokenService;
import com.fansfunding.user.entity.User;
import com.fansfunding.user.entity.UserBasic;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.encrypt.AESUtils;

import com.fansfunding.utils.response.PermissionCode;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping(path = "user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;


	/**
	 * 登出
	 * 
	 * @param token
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "{userId}/logout")
	@ResponseBody
	public Status logout(@PathVariable String userId, @RequestParam String token) {
		User user = userService.getUserById(Integer.parseInt(userId));
		//TODO:存在性验证
		int tid;
		try {
			tid = Integer.parseInt(AESUtils.Decrypt(token, AESUtils.ENCRYPT_KEY));
		} catch (Exception e) {
			return new Status(false, StatusCode.ERROR_DATA, null, null);
		}
		Token rToken = tokenService.lookUpTokenById(tid);
		if (rToken.getPhone().equals(user.getName())) {
			tokenService.setPermission(tid, PermissionCode.NO_PERMISSION);
		} else {
			return new Status(false, StatusCode.PERMISSION_LOW, null, null);
		}
		return new Status(true, StatusCode.SUCCESS, null, null);
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
				AESUtils.Encrypt(rToken.getId() + "", AESUtils.ENCRYPT_KEY));
	}

}
