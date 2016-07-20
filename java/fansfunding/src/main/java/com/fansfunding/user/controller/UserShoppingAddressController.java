package com.fansfunding.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.user.service.ShoppingAddressService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;


@Controller
@RequestMapping("user")
public class UserShoppingAddressController {

	@Autowired
	ShoppingAddressService shoppingAddressService;
	
	/**
	 * 获取用户收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address",method=RequestMethod.GET)
	@ResponseBody
	public Status getShoppingAddress(@PathVariable Integer userId){
		return new Status(true, StatusCode.SUCCESS, shoppingAddressService.getByUserId(userId), null);
	}
	/**
	 * 添加用户收货地址
	 * @param userId
	 * @param name 
	 * @param phone 
	 * @param district 
	 * @param postCode 
	 * @param province 
	 * @param city 
	 * @param address 
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address",method=RequestMethod.POST)
	@ResponseBody
	public Status addShoppingAddress(@PathVariable Integer userId,
			@RequestParam String name,
			@RequestParam String phone,
			@RequestParam String district,
			@RequestParam int post_code,
			@RequestParam String province,
			@RequestParam String city,
			@RequestParam String address){
		shoppingAddressService.AddNewAddress(userId, name, phone, province, city, district, address, post_code);
		return new Status(true, StatusCode.SUCCESS,shoppingAddressService.getByUserId(userId) , null);
	}
	/**
	 * 更新用户收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address/{addressId}",method=RequestMethod.POST)
	@ResponseBody
	public Status deleteShoppingAddress(
			@PathVariable Integer userId,
			@PathVariable Integer addressId,
			@RequestParam String name,
			@RequestParam String phone,
			@RequestParam String district,
			@RequestParam int postCode,
			@RequestParam String province,
			@RequestParam String city,
			@RequestParam String address
			){
		shoppingAddressService.updateById(addressId, address, city, district, province, phone, postCode, name, userId);
		return new Status(true, StatusCode.SUCCESS, shoppingAddressService.getByUserId(userId), null);
	}
	/**
	 * 删除用户收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address/{addressId}",method=RequestMethod.DELETE)
	@ResponseBody
	public Status updateShoppingAddress(
			@PathVariable Integer userId,
			@PathVariable Integer addressId
			){
		shoppingAddressService.deleteById(addressId);
		return new Status(true, StatusCode.SUCCESS, "删除成功", null);
	}
	/**
	 * 获得默认地址
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address/default",method=RequestMethod.GET)
	@ResponseBody
	public Status getDefault(@PathVariable Integer userId){
		return new Status(true, StatusCode.SUCCESS, shoppingAddressService.getDefault(userId),null);
	}
	/**
	 * 设置默认地址
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address/default",method=RequestMethod.POST)
	@ResponseBody
	public Status setDefault(@PathVariable Integer userId,@RequestParam int addressId){
		shoppingAddressService.setDefault(userId, addressId);
		return new Status(true, StatusCode.SUCCESS, "设置成功",null);
	}
	
}
