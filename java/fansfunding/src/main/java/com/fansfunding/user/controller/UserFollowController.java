package com.fansfunding.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.user.service.UserFollowService;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("user")
public class UserFollowController {
	@Autowired
	private UserFollowService userFollowService;
	@Autowired
	private UserService userService;
	/**
	 * 获取关注的用户
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="{userId}/following",method=RequestMethod.GET)
	@ResponseBody
	public Status getFollowing(@PathVariable int userId,
			@RequestParam(required=false,defaultValue="1") int page,
			@RequestParam(required=false,defaultValue="10") int rows){
		if(!userService.isExist(userId)){
			return new Status(false,StatusCode.USER_NULL,"用户不存在",null);
		}
		return new Status(true,StatusCode.SUCCESS,userFollowService.getFollowing(userId, page, rows),null);
	}
	/**
	 * 获取粉丝
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="{userId}/followers",method=RequestMethod.GET)
	@ResponseBody
	public Status getFollowers(@PathVariable int userId,
			@RequestParam(required=false,defaultValue="1") int page,
			@RequestParam(required=false,defaultValue="10") int rows){
		if(!userService.isExist(userId)){
			return new Status(false,StatusCode.USER_NULL,"用户不存在",null);
		}
		return new Status(true,StatusCode.SUCCESS,userFollowService.getFollowers(userId, page, rows),null);
	}
	/**
	 * 关注
	 * @param userId
	 * @param followId
	 * @return
	 */
	@RequestMapping(path="{userId}/follow/{followId}",method=RequestMethod.POST)
	@ResponseBody
	public Status followUser(@PathVariable int userId,@PathVariable int followId){
		if(!userFollowService.follow(userId, followId)){
			return new Status(false,StatusCode.USER_NULL,"用户不存在",null);
		}
		return new Status(true,StatusCode.SUCCESS,"用户关注成功",null);
	}
	
	/**
	 * 取消关注
	 * @param userId
	 * @param followId
	 * @return
	 */
	@RequestMapping(path="{userId}/unfollow/{followId}",method=RequestMethod.POST)
	@ResponseBody
	public Status unfollowUser(@PathVariable int userId,@PathVariable int followId){
		if(!userFollowService.unfollow(userId, followId)){
			return new Status(false,StatusCode.USER_NULL,"用户不存在",null);
		}
		return new Status(true,StatusCode.SUCCESS,"用户取消关注成功",null);
	}
}
