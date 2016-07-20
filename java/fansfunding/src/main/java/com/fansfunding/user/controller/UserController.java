package com.fansfunding.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;






import com.fansfunding.common.entity.Token;
import com.fansfunding.common.service.TokenService;
import com.fansfunding.project.service.ProjectService;
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
	@Autowired
	private ProjectService projectService;

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

	/**
	 * 用户搜索
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="search",method=RequestMethod.GET)
	@ResponseBody
	public Status search(@RequestParam String keyword,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){
		return new Status(true,StatusCode.SUCCESS,userService.search(keyword,page,rows),null);
	}
	/**
	 * 用户的订单
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="{userId}/orders",method=RequestMethod.GET)
	@ResponseBody
	public Status userOrder(@RequestParam int userId,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){
		return new Status(true,StatusCode.SUCCESS,userService.paidOrder(userId,page,rows),null);
	}
	/**
	 * 获取用户相关项目
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="{userId}/projects",method=RequestMethod.GET)
	@ResponseBody
	public Status userProject(@RequestParam String type,@PathVariable int userId,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){
		if("sponsor".equals(type)){
			return new Status(true,StatusCode.SUCCESS,projectService.getSponsor(userId,page,rows),null);
		}
		if("follow".equals(type)){
			return new Status(true,StatusCode.SUCCESS,projectService.getFollow(userId,page,rows),null);
		}
		return new Status(false,StatusCode.FAILED,"不存在的分类",null);
	}
	/**
	 * 关注项目
	 * @param categoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{userId}/follow/{categoryId}/{projectId}")
	@ResponseBody
	public Status follow(@PathVariable int userId,@PathVariable Integer categoryId,@PathVariable Integer projectId){
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		if(projectService.follow(userId,categoryId,projectId)){
			return new Status(true,StatusCode.SUCCESS,"项目关注成功",null);
		}
		return new Status(false,StatusCode.USER_NULL,"用户不存在",null);
	}
	/**
	 * 取消关注项目
	 * @param categoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{userId}/unfollow/{categoryId}/{projectId}")
	@ResponseBody
	public Status unfollow(@PathVariable int userId,@PathVariable Integer categoryId,@PathVariable Integer projectId){
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		projectService.unfollow(userId,categoryId,projectId);
		return new Status(true,StatusCode.SUCCESS,"项目关注取消成功",null);
	}
}
