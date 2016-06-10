package com.fansfunding.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.utils.response.Status;


@Controller
@RequestMapping("user")
public class UserShoppingAddressController {

	/**
	 * 获取用户收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address",method=RequestMethod.GET)
	@ResponseBody
	public Status getShoppingAddress(@PathVariable Integer userId){
		return null;
	}
	/**
	 * 添加用户收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address",method=RequestMethod.POST)
	@ResponseBody
	public Status addShoppingAddress(@PathVariable Integer userId){
		return null;
	}
	/**
	 * 更新用户收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address",method=RequestMethod.PATCH)
	@ResponseBody
	public Status updateShoppingAddress(@PathVariable Integer userId){
		return null;
	}
	/**
	 * 删除用户收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address",method=RequestMethod.DELETE)
	@ResponseBody
	public Status deleteShoppingAddress(@PathVariable Integer userId){
		return null;
	}
}
