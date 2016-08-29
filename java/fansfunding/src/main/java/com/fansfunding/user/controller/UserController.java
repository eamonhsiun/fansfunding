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
import com.fansfunding.project.service.CategoryService;
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
	@Autowired
	private CategoryService categoryService;
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
		if(!userService.isExist(Integer.parseInt(userId))){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		User user = userService.getUserById(Integer.parseInt(userId));
		//TODO:存在性验证
		int tid;
		try {
			tid = Integer.parseInt(AESUtils.Decrypt(token.replace("ADD_ADD", "+"), AESUtils.ENCRYPT_KEY));
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
	/**
	 * 更换密码
	 * @param userId
	 * @param token
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "{userId}/newPwd")
	@ResponseBody
	public Status newPwd(@PathVariable String userId, @RequestParam String token,@RequestParam String password) throws Exception {
		if(!userService.isExist(Integer.parseInt(userId))){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		User user = userService.getUserById(Integer.parseInt(userId));
		// TODO:存在性验证
		int tid;
		try {
			tid = Integer.parseInt(AESUtils.Decrypt(token.replace("ADD_ADD", "+"), AESUtils.ENCRYPT_KEY));
		} catch (Exception e) {
			return new Status(false, StatusCode.ERROR_DATA, null, null);
		}
		Token rToken = tokenService.lookUpTokenById(tid);
		if(rToken.getPermission()==PermissionCode.PERMISSION_NORMAL){
			user.setPassword(password);
			userService.updatePwd(user);
		}
		
		return new Status(true, StatusCode.SUCCESS, new UserBasic(user),
				AESUtils.Encrypt(rToken.getId() + "", AESUtils.ENCRYPT_KEY).replace("+", "ADD_ADD"));
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
	public Status userOrder(@PathVariable int userId,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true,StatusCode.SUCCESS,userService.paidOrder(userId,page,rows),null);
	}
	/**
	 * 根据订单号获取用户的订单
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="{userId}/orders/{orderNo}",method=RequestMethod.GET)
	@ResponseBody
	public Status userOrderByOrderNo(@PathVariable int userId,@PathVariable String orderNo){
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true,StatusCode.SUCCESS,userService.paidOrder(orderNo),null);
	}
	/**
	 * 关注项目
	 * @param categoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{userId}/follow/{categoryId}/{projectId}",method=RequestMethod.POST)
	@ResponseBody
	public Status follow(@PathVariable int userId,@PathVariable Integer categoryId,@PathVariable Integer projectId){
		if(!categoryService.isExist(categoryId)){
			return new Status(false,StatusCode.FAILED,"分类不存在",null);
		}
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
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
	@RequestMapping(path="{userId}/unfollow/{categoryId}/{projectId}",method=RequestMethod.POST)
	@ResponseBody
	public Status unfollow(@PathVariable int userId,@PathVariable Integer categoryId,@PathVariable Integer projectId){
		if(!categoryService.isExist(categoryId)){
			return new Status(false,StatusCode.FAILED,"分类不存在",null);
		}
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		projectService.unfollow(userId,categoryId,projectId);
		return new Status(true,StatusCode.SUCCESS,"项目关注取消成功",null);
	}
}
