package com.fansfunding.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.user.service.UserSettingsService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping(path="user")
public class UserSettingsController {
	@Autowired
	private UserSettingsService settings;
	
	/**
	 * 获取头像
	 * @param userName
	 * @return
	 */
	@RequestMapping(path="{userId}/head",method=RequestMethod.GET)
	@ResponseBody
	public Status getHead(@PathVariable Integer userId){
		return new Status(true,StatusCode.SUCCESS,"GET");
	}
	/**
	 * 上传头像
	 * @param userName
	 * @return
	 */
	@RequestMapping(path="{userId}/head",method=RequestMethod.POST)
	@ResponseBody
	public Status postHead(@PathVariable Integer userId){
		return new Status(true,StatusCode.SUCCESS,"POST");
	}
	/**
	 * 修改用户信息
	 * @param userId 用户ID
	 * @return
	 */
	@RequestMapping(path="{userId}/info",method=RequestMethod.POST)
	@ResponseBody
	public Status info_modify(@PathVariable Integer userId){
		return null;
	}
}
