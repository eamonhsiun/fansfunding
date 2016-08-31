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
import com.fansfunding.project.service.ProjectService;
import com.fansfunding.user.entity.User;
import com.fansfunding.user.service.UserFollowService;
import com.fansfunding.user.service.UserMomentService;
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
	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserFollowService userFollowService;
	@Autowired
	private UserMomentService userMomentService;

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
//		if (password.length() < 6 || password.length() > 16) {
//			return new Status(false, StatusCode.PASSWORD_LENGTH_ERROR, "密码长度错误", null);
//		}
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
	public Status forgetPwd(@RequestParam int checker, @RequestParam String password, @RequestParam String token)
			throws Exception {
		int cid;
		if (password.length() < 6 || password.length() > 16) {
			return new Status(false, StatusCode.PASSWORD_LENGTH_ERROR, "密码长度错误", null);
		}
		if ((checker + "").length() != 6) {
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
		if (user == null)
			return new Status(false, StatusCode.USER_NULL, null, null);
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
		if (user == null) {
			return new Status(false, StatusCode.USER_NULL, null, null);
		}
		if (!userService.CheckPwd(user.getPassword(), password)) {
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
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping(path = "{userId}/head", method = RequestMethod.POST)
	@ResponseBody
	public Status postHead(@PathVariable Integer userId, @RequestParam CommonsMultipartFile file) {
		if (!userService.isExist(userId)) {
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if (!file.isEmpty()) {
			try {
				if (FileFormat.isImage(file.getInputStream())) {
					if (file.getSize() > FileUpload.FILE_MAX_SIZE) {
						return new Status(false, StatusCode.FILE_TOO_LARGE, "图片大小超过了上传限制", null);
					}
					if (settings.uploadHead(userId, file)) {
						return new Status(true, StatusCode.SUCCESS, "图片上传成功", null);
					}
					return new Status(false, StatusCode.FILEUPLOAD_ERROR, "图片上传失败", null);
				} else {
					return new Status(false, StatusCode.UNSUPPORT_IMAGE_FORMAT, "不支持的图片格式", null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Status(false, StatusCode.ERROR_DATA, "文件不可为空", null);
	}

	/**
	 * 上传用户动态图片
	 * 
	 * @param userName
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(path = "{userId}/moment/images", method = RequestMethod.POST)
	@ResponseBody
	public Status postMomentImages(@PathVariable Integer userId, @RequestParam CommonsMultipartFile[] files) throws IOException {
		if (!userService.isExist(userId)) {
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if (files.length == 0) {
			return new Status(false, StatusCode.FAILED, "文件不可为空", null);
		}
		for (CommonsMultipartFile file : files) {
			if (file.isEmpty()) {
				return new Status(false,StatusCode.FAILED,"文件不可为空",null);
			}
			if(file.getSize()>FileUpload.FILE_MAX_SIZE){
				return new Status(false,StatusCode.FILE_TOO_LARGE,"图片大小超过了上传限制",null);
			}
			if(!FileFormat.isImage(file.getInputStream())){
				return new Status(false,StatusCode.UNSUPPORT_IMAGE_FORMAT,"不支持的图片格式",null);
			}
		}
		String[] paths=new String[files.length];
		if((paths=settings.uploadMomentImages(userId, files))!=null){
			return new Status(true,StatusCode.SUCCESS,paths,null);
		}
		return new Status(false,StatusCode.FILEUPLOAD_ERROR,"文件上传失败",null);
	}
	
	
	
	/**
	 * 获取关注的用户
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="following",method=RequestMethod.GET)
	@ResponseBody
	public Status getFollowing(
			@RequestParam int viewId,
			@RequestParam(required=false,defaultValue="1") int page,
			@RequestParam(required=false,defaultValue="10") int rows){
		if(!userService.isExist(viewId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true,StatusCode.SUCCESS,userFollowService.getFollowing(viewId, page, rows),null);
	}
	/**
	 * 获取粉丝
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="followers",method=RequestMethod.GET)
	@ResponseBody
	public Status getFollowers(
			@RequestParam int viewId,
			@RequestParam(required=false,defaultValue="1") int page,
			@RequestParam(required=false,defaultValue="10") int rows){

		if(!userService.isExist(viewId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true,StatusCode.SUCCESS,userFollowService.getFollowers(viewId, page, rows),null);
	}
	
	
	/**
	 * 获取用户动态
	 * @param userId
	 * @param page 
	 * @param rows 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment",method=RequestMethod.GET)
	@ResponseBody
	public Status getMoment(
			@PathVariable int userId, 
			@RequestParam int viewId,
			@RequestParam(required=false,defaultValue="1") Integer page,
			@RequestParam(required=false,defaultValue="10") Integer rows
			) throws Exception{
		if(!(userId==10000000||userService.isExist(userId))){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(!userService.isExist(viewId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true, StatusCode.SUCCESS, userMomentService.getMomentsById(userId, page, rows,viewId), 0);
	}
	
	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	@RequestMapping(path = "info", method = RequestMethod.GET)
	@ResponseBody
	public Status info(
			@RequestParam int viewId
			) {
		if(!userService.isExist(viewId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true, StatusCode.SUCCESS, userService.getUserMap(userService.getUserById(viewId)), null);
	}
	
	

	/**
	 * 获取用户相关项目
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="projects",method=RequestMethod.GET)
	@ResponseBody
	public Status userProject(@RequestParam String type,
			@RequestParam int viewId,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){

		if(!userService.isExist(viewId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if("sponsor".equals(type)){
			return new Status(true,StatusCode.SUCCESS,projectService.getSponsor(viewId,page,rows),null);
		}
		if("follow".equals(type)){
			return new Status(true,StatusCode.SUCCESS,projectService.getFollow(viewId,page,rows),null);
		}
		if("support".equals(type)){
			return new Status(true,StatusCode.SUCCESS,projectService.getSupport(viewId,page,rows),null);
		}
		return new Status(false,StatusCode.FAILED,"不存在的分类",null);
	}
	/**
	 * 获取用户相关项目的数量
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="projects/num",method=RequestMethod.GET)
	@ResponseBody
	public Status userProjectNum(@RequestParam String type,@RequestParam int viewId){

		if(!userService.isExist(viewId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if("sponsor".equals(type)){
			return new Status(true,StatusCode.SUCCESS,projectService.getSponsorNum(viewId),null);
		}
		if("follow".equals(type)){
			return new Status(true,StatusCode.SUCCESS,projectService.getFollowNum(viewId),null);
		}
		if("support".equals(type)){
			return new Status(true,StatusCode.SUCCESS,projectService.getSupportNum(viewId),null);
		}
		return new Status(false,StatusCode.FAILED,"不存在的分类",null);
	}
	
	/**
	 * 获得用户动态
	 * @param momentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment/{momentId}",method=RequestMethod.GET)
	@ResponseBody
	public Status getMomentById(
			@PathVariable int userId,
			@RequestParam int viewId,
			@PathVariable int momentId
			) throws Exception{
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		
		return new Status(true, StatusCode.SUCCESS, userMomentService.getMomentById(userId,momentId), 0);
	}
	
	/**
	 * 获得用户动态评论
	 * @param momentId
	 * @param page 
	 * @param rows 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="moment/{momentId}/comment",method=RequestMethod.GET)
	@ResponseBody
	public Status getMomentComment(
			@PathVariable int momentId,
			@RequestParam(required=false,defaultValue="1")int page,
			@RequestParam(required=false,defaultValue="10")int rows
			) throws Exception{
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		
		return new Status(true, StatusCode.SUCCESS, userMomentService.getCommentUsePage(momentId, page, rows), 0);
	}
	
	/**
	 * 获取动态点赞
	 * @param userId
	 * @param momentId
	 * @param page 
	 * @param rows 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="moment/{momentId}/like",method=RequestMethod.GET)
	@ResponseBody
	public Status getMomentLike(
			@PathVariable int momentId,
			@RequestParam(required=false,defaultValue="1")int page,
			@RequestParam(required=false,defaultValue="10")int rows
			) throws Exception{
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		return new Status(true,StatusCode.SUCCESS,userMomentService.getMomentLikeUsePage(momentId, page, rows),null);
	}
	
	
	
	
}
