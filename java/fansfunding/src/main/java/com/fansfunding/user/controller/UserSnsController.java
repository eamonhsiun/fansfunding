package com.fansfunding.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.utils.response.Status;

@Controller
@RequestMapping("user")
public class UserSnsController {

	/**
	 * 获取用户的SNS账号
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/sns",method=RequestMethod.GET)
	@ResponseBody
	public Status getSns(@PathVariable Integer userId){
		return null;
	}
	/**
	 * 添加用户SNS账号
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/sns",method=RequestMethod.POST)
	@ResponseBody
	public Status addSns(@PathVariable Integer userId){
		return null;
	}
	/**
	 * 更新用户SNS账号
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/sns",method=RequestMethod.PATCH)
	@ResponseBody
	public Status updateSns(@PathVariable Integer userId){
		return null;
	}
	/**
	 * 删除用户SNS账号
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/sns",method=RequestMethod.DELETE)
	@ResponseBody
	public Status deleteSns(@PathVariable Integer userId){
		return null;
	}
}
