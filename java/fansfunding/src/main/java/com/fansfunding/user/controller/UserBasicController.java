package com.fansfunding.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fansfunding.common.entity.Checker;
import com.fansfunding.common.entity.Token;
import com.fansfunding.common.service.CheckerService;
import com.fansfunding.common.service.TokenService;
import com.fansfunding.user.entity.User;
import com.fansfunding.user.service.UserService;
import com.fansfunding.user.service.UserSettingsService;
import com.fansfunding.utils.encrypt.AESUtils;
import com.fansfunding.utils.fileupload.FileFormat;
import com.fansfunding.utils.fileupload.FileUpload;
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
	@Autowired
	private UserSettingsService settings;

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
		if(password.length()<6||password.length()>16){
			return new Status(false,StatusCode.PASSWORD_LENGTH_ERROR,"密码长度错误",null);
		}
		int cid;
		Checker c;
		User user;
		try {
			// TODO:Checker过期检测
			cid = Integer.valueOf(AESUtils.Decrypt(token.replace("ADD_ADD", "+"), AESUtils.ENCRYPT_KEY));
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
		try {
			user = userService.createUser(c.getPhone(), password);
			user.setPassword("");
		} catch (Exception e) {
			return new Status(false, StatusCode.USER_EXIST, null, null);

		}
		checkerService.deleteById(cid);
		Token newToken = tokenService.requestToken(PermissionCode.PERMISSION_NORMAL, user);

		return new Status(true, StatusCode.SUCCESS, userService.getUserBasicMap(user),
				AESUtils.Encrypt(newToken.getId() + "", AESUtils.ENCRYPT_KEY).replace("+", "%ADD_ADD"));
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
		if(password.length()<6||password.length()>16){
			return new Status(false,StatusCode.PASSWORD_LENGTH_ERROR,"密码长度错误",null);
		}
		if((checker+"").length()!=6){
			return new Status(false, StatusCode.CHECKER_ERROR, null, null);
		}
		Checker c;
		User user;
		try {
			// TODO:Checker过期检测
			cid = Integer.valueOf(AESUtils.Decrypt(token.replace("ADD_ADD", "+"), AESUtils.ENCRYPT_KEY));
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

		user = userService.getUserByPhone(c.getPhone());
		
		Token newToken = tokenService.requestToken(PermissionCode.PERMISSION_NORMAL, user);
		if(user==null)return new Status(false, StatusCode.USER_NULL, null, null);
		user.setPassword(password);
		userService.updatePwd(user);
		
		checkerService.deleteById(cid);
		return new Status(true, StatusCode.SUCCESS, userService.getUserBasicMap(user),
				AESUtils.Encrypt(newToken.getId() + "", AESUtils.ENCRYPT_KEY).replace("+", "ADD_ADD"));
		


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
		if (user == null){
			return new Status(false, StatusCode.USER_NULL, null, null);
		}
		if (!userService.CheckPwd(user.getPassword(), password)){
			return new Status(false, StatusCode.PASSWORD_ERROR, null, null);
		}
		
		// 权限控制
		Token newToken = tokenService.requestToken(PermissionCode.PERMISSION_NORMAL, user);

		user.setPassword("");
		return new Status(true, StatusCode.SUCCESS, userService.getUserBasicMap(user),
				AESUtils.Encrypt(newToken.getId() + "", AESUtils.ENCRYPT_KEY).replace("+", "ADD_ADD"));
	}
	
	/**
	 * 上传头像
	 * @param userName
	 * @return
	 */
	@RequestMapping(path="{userId}/head",method=RequestMethod.POST)
	@ResponseBody
	public Status postHead(@PathVariable Integer userId,@RequestParam CommonsMultipartFile file){
		if(!userService.isExist(userId)){
			return new Status(false,StatusCode.USER_NULL,"用户不存在",null);
		}
		if(!file.isEmpty()){
			try {
				if(FileFormat.isImage(file.getInputStream())){
					if(file.getSize()>FileUpload.FILE_MAX_SIZE){
						return new Status(false,StatusCode.FILE_TOO_LARGE,"图片大小超过了上传限制",null);
					}
					if(settings.uploadHead(userId, file)){
						return new Status(true,StatusCode.SUCCESS,"图片上传成功",null);
					}
					return new Status(false,StatusCode.FILEUPLOAD_ERROR,"图片上传失败",null);
				}
				else{
					return new Status(false,StatusCode.UNSUPPORT_IMAGE_FORMAT,"不支持的图片格式",null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Status(false,StatusCode.ERROR_DATA,"文件不可为空",null);
	}

}
