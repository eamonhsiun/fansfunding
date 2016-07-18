package com.fansfunding.user.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.user.entity.User;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping(path="user")
public class UserSettingsController {
	
	
	
	@Autowired
	private UserService userService;
	/**
	 * 获取头像
	 * @param userName
	 * @return
	 */
	@RequestMapping(path="{userId}/head",method=RequestMethod.GET)
	@ResponseBody
	public Status getHead(@PathVariable Integer userId){
		User user = userService.getUserById(userId);
		return new Status(true,StatusCode.SUCCESS,user.getHead(),null);
	}
	
	
	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	@RequestMapping(path = "{userId}/info", method = RequestMethod.GET)
	@ResponseBody
	public Status info(@PathVariable int userId) {
		return new Status(true, StatusCode.SUCCESS, userService.getUserMap(userService.getUserById(userId)), null);
	}
	

	/**
	 * POST Info
	 * @param userId
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(path = "{userId}/info", method = RequestMethod.POST)
	@ResponseBody
	public Status postInfo(@PathVariable int userId,
			@RequestParam(required = false, defaultValue = "") String nickname,
			@RequestParam(required = false, defaultValue = "") String email,
			@RequestParam(required = false, defaultValue = "0") String sex,
			@RequestParam(required = false, defaultValue = "") String idNumber,
			@RequestParam(required = false, defaultValue = "1999-12-31") String birthday) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		
		return new Status(true,StatusCode.SUCCESS,userService.getUserMap(userService.updateUserInfo(userId,nickname, email, Byte.parseByte(sex), idNumber, sdf.parse(birthday))),null);
	}
	
	
	/**
	 * GET NICKNAME
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = "{userId}/nickname", method = RequestMethod.GET)
	@ResponseBody
	public Status getNickName(@PathVariable int userId) {
		User user = userService.getUserById(userId);
		return new Status(true,StatusCode.SUCCESS,user.getNickname(),null);
	}
	
	
	/**
	 * POST NICKNAME
	 * @param userId
	 * @param nickname
	 * @return
	 */
	@RequestMapping(path = "{userId}/nickname", method = RequestMethod.POST)
	@ResponseBody
	public Status postNickName(@PathVariable int userId,@RequestParam String nickname) {
		userService.updateNickName(userId, nickname);
		return new Status(true,StatusCode.SUCCESS,nickname,null);
	}
	
}
