package com.fansfunding.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.user.service.ShoppingAddressService;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;


@Controller
@RequestMapping("user")
public class UserShoppingAddressController {

	@Autowired
	private ShoppingAddressService shoppingAddressService;
	@Autowired
	private UserService userService;
	/**
	 * 获取用户收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address",method=RequestMethod.GET)
	@ResponseBody
	public Status getShoppingAddress(@PathVariable Integer userId){
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
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
			@RequestParam int postCode,
			@RequestParam String province,
			@RequestParam String city,
			@RequestParam String address){
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(name.length()>20||province.length()>20||city.length()>20||
				district.length()>20||phone.length()>15||address.length()>50){
			return new Status(false,StatusCode.ERROR_DATA,"数据过长",null);
		}
		int addressId=shoppingAddressService.AddNewAddress(userId, name, phone, province,
				city, district, address, postCode);
		return new Status(true, StatusCode.SUCCESS,addressId, null);
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
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(name.length()>20||province.length()>20||city.length()>20||
				district.length()>20||phone.length()>15||address.length()>50){
			return new Status(false,StatusCode.ERROR_DATA,"数据过长",null);
		}
		if(shoppingAddressService.updateById(addressId, address, city,
				district, province, phone, postCode, name, userId)){
			return new Status(true, StatusCode.SUCCESS, shoppingAddressService.getByUserId(userId), null);
		}
		return new Status(false, StatusCode.PERMISSION_LOW,"没有权限修改", null);
	}
	/**
	 * 删除用户收货地址
	 * @param userId
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address/{addressId}/delete",method=RequestMethod.POST)
	@ResponseBody
	public Status updateShoppingAddress(
			@PathVariable Integer userId,
			@PathVariable Integer addressId
			){
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(shoppingAddressService.deleteById(userId,addressId)){
			return new Status(true, StatusCode.SUCCESS, "删除成功", null);
		}
		return new Status(false, StatusCode.PERMISSION_LOW,"没有权限删除", null);
	}
	/**
	 * 获得默认地址
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address/default",method=RequestMethod.GET)
	@ResponseBody
	public Status getDefault(@PathVariable Integer userId){
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true, StatusCode.SUCCESS, shoppingAddressService.getDefault(userId),null);
	}
	/**
	 * 设置默认地址
	 * @return
	 */
	@RequestMapping(path="{userId}/shopping_address/default",method=RequestMethod.POST)
	@ResponseBody
	public Status setDefault(@PathVariable Integer userId,@RequestParam int addressId){
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(shoppingAddressService.setDefault(userId, addressId)){
			return new Status(true, StatusCode.SUCCESS, "设置成功",null);
		}
		return new Status(false, StatusCode.PERMISSION_LOW,"没有权限设置", null);
	}
	
}
