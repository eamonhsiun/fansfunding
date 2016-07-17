package com.fansfunding.user.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.user.dao.ShoppingAddressDao;
import com.fansfunding.user.entity.ShoppingAddress;


@Service
public class ShoppingAddressService {
	@Autowired
	private ShoppingAddressDao shoppingAddressDao;
	
	public List<ShoppingAddress> findByUserId(int id){
		return shoppingAddressDao.selectByUserId(id);
	}

	public void updateById(int id, String address, String city, String district, String province, String phone, int post_code, String name, Integer userId){
		ShoppingAddress shoppingAddress = new ShoppingAddress();
		shoppingAddress.setAddress(address);
		shoppingAddress.setCity(city);
		shoppingAddress.setDistrict(district);
		shoppingAddress.setProvince(province);
		shoppingAddress.setPhone(phone);
		shoppingAddress.setPost_code(post_code);
		shoppingAddress.setName(name);
		shoppingAddress.setUserId(userId);
		shoppingAddressDao.updateByPrimaryKey(shoppingAddress);
	}
	
	
	
	public ShoppingAddress AddNewAddress(Integer userId, String name, String phone, String province, String city, String district, String address, int post_code){
		ShoppingAddress shoppingAddress = new ShoppingAddress();
		shoppingAddress.setAddress(address);
		shoppingAddress.setCity(city);
		shoppingAddress.setDistrict(district);
		shoppingAddress.setProvince(province);
		shoppingAddress.setPhone(phone);
		shoppingAddress.setPost_code(post_code);
		shoppingAddress.setName(name);
		shoppingAddress.setUserId(userId);
		shoppingAddressDao.insert(shoppingAddress);
		return shoppingAddress;
	}
	
	/**
	 * 获取该用户所有地址
	 * @return
	 */
	public List<Map<String,Object>> getByUserId(int id){
		List<Map<String,Object>> shoppingAddresses=new ArrayList<>();
		shoppingAddressDao.selectByUserId(id).stream().forEach((e)->{
			Map<String,Object> shoppingAddress=new HashMap<>();
			shoppingAddress.put("id", e.getId());
			shoppingAddress.put("province",e.getProvince());
			shoppingAddress.put("city",e.getCity());
			shoppingAddress.put("district",e.getDistrict());
			shoppingAddress.put("address",e.getAddress());
			shoppingAddress.put("phone",e.getPhone());
			shoppingAddress.put("name", e.getName());
			shoppingAddress.put("post_code",e.getPost_code());
			shoppingAddresses.add(shoppingAddress);
		});
		return shoppingAddresses;
	}
	
	
	
	
}
