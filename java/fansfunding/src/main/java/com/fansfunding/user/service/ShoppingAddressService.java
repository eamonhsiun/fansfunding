package com.fansfunding.user.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.user.dao.ShoppingAddressDao;
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.entity.ShoppingAddress;


@Service
public class ShoppingAddressService {
	@Autowired
	private ShoppingAddressDao shoppingAddressDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 判断是否存在
	 * @param addressId
	 * @return
	 */
	public boolean exist(int addressId){
		return shoppingAddressDao.selectByPrimaryKey(addressId)!=null;
	}
	
	/**
	 * 查询用户的所有地址
	 * @param id
	 * @return
	 */
	public List<ShoppingAddress> findByUserId(int id){
		return shoppingAddressDao.selectByUserId(id);
	}

	/**
	 * 删除地址
	 * @param userId
	 * @param addressId
	 * @return
	 */
	public boolean deleteById(int userId,int addressId){
		if(shoppingAddressDao.selectByPrimaryKey(addressId).getUserId().intValue()!=userId){
			return false;
		}
		if(shoppingAddressDao.selectByPrimaryKey(addressId).getIsDefault()==1){
			shoppingAddressDao.deleteByPrimaryKey(addressId);
			if(shoppingAddressDao.selectByUserId(userId).size()!=0){
				ShoppingAddress address=shoppingAddressDao.selectByUserId(userId).get(0);
				address.setIsDefault(1);
				this.setDefault(userId, address.getId());
			}
		}
		else{
			shoppingAddressDao.deleteByPrimaryKey(addressId);
		}
		return true;
	}
	/**
	 * 更新地址
	 * @param id
	 * @param address
	 * @param city
	 * @param district
	 * @param province
	 * @param phone
	 * @param post_code
	 * @param name
	 * @param userId
	 * @return
	 */
	public boolean updateById(int id, String address, String city, String district, String province, String phone, int post_code, String name, Integer userId){
		if(shoppingAddressDao.selectByPrimaryKey(id).getUserId().intValue()!=userId.intValue()){
			return false;
		}
		ShoppingAddress shoppingAddress = shoppingAddressDao.selectByPrimaryKey(id);
		shoppingAddress.setAddress(address);
		shoppingAddress.setCity(city);
		shoppingAddress.setDistrict(district);
		shoppingAddress.setProvince(province);
		shoppingAddress.setPhone(phone);
		shoppingAddress.setPostCode(post_code);
		shoppingAddress.setName(name);
		shoppingAddress.setUserId(userId);
		shoppingAddress.setUpdateBy(userDao.selectById(userId).getName());
		shoppingAddressDao.updateByPrimaryKey(shoppingAddress);
		return true;
	}
	/**
	 * 
	 * 添加地址
	 * @param userId
	 * @param name
	 * @param phone
	 * @param province
	 * @param city
	 * @param district
	 * @param address
	 * @param post_code
	 * @return
	 */
	public int AddNewAddress(Integer userId, String name, String phone, String province, 
			String city, String district, String address, int post_code){
		ShoppingAddress shoppingAddress = new ShoppingAddress();
		shoppingAddress.setAddress(address);
		shoppingAddress.setCity(city);
		shoppingAddress.setDistrict(district);
		shoppingAddress.setProvince(province);
		shoppingAddress.setPhone(phone);
		shoppingAddress.setPostCode(post_code);
		shoppingAddress.setName(name);
		shoppingAddress.setUserId(userId);
		shoppingAddress.setCreateBy(userDao.selectById(userId).getName());
		shoppingAddress.setUpdateBy(userDao.selectById(userId).getName());
		if(shoppingAddressDao.selectByUserId(userId).size()==0){
			shoppingAddress.setIsDefault(1);
		}
		shoppingAddressDao.insert(shoppingAddress);
		return shoppingAddress.getId();
	}

	/**
	 * 获取该用户所有地址
	 * @return
	 */
	public List<Map<String,Object>> getByUserId(int id){
		List<Map<String,Object>> shoppingAddresses=new ArrayList<>();
		shoppingAddressDao.selectByUserId(id).stream().forEach((e)->{
			Map<String,Object> shoppingAddress=new HashMap<>();
			shoppingAddress.put("addressId", e.getId());
			shoppingAddress.put("province",e.getProvince());
			shoppingAddress.put("city",e.getCity());
			shoppingAddress.put("district",e.getDistrict());
			shoppingAddress.put("address",e.getAddress());
			shoppingAddress.put("phone",e.getPhone());
			shoppingAddress.put("name", e.getName());
			shoppingAddress.put("postCode",e.getPostCode());
			shoppingAddress.put("isDefault",e.getIsDefault());
			shoppingAddresses.add(shoppingAddress);
		});
		return shoppingAddresses;
	}
	/**
	 * 设置默认地址
	 * @param addressId
	 */
	public boolean setDefault(int userId,int addressId){
		if(shoppingAddressDao.selectByPrimaryKey(addressId).getUserId().intValue()!=userId){
			return false;
		}
		this.findByUserId(userId).forEach((e)->{
			if(e.getIsDefault()==1){
				e.setIsDefault(0);
				shoppingAddressDao.updateByPrimaryKey(e);
			}
		});
		ShoppingAddress address=shoppingAddressDao.selectByPrimaryKey(addressId);
		address.setIsDefault(1);
		shoppingAddressDao.updateByPrimaryKey(address);
		return true;
	}
	/**
	 * 获取默认地址
	 * @param userId
	 * @param addressId
	 * @return
	 */
	public Map<String, Object> getDefault(int userId){
		Map<String,Object> shoppingAddress=new HashMap<>();
		this.findByUserId(userId).forEach((e)->{
			if(e.getIsDefault()==1){
				shoppingAddress.put("addressId", e.getId());
				shoppingAddress.put("province",e.getProvince());
				shoppingAddress.put("city",e.getCity());
				shoppingAddress.put("district",e.getDistrict());
				shoppingAddress.put("address",e.getAddress());
				shoppingAddress.put("phone",e.getPhone());
				shoppingAddress.put("name", e.getName());
				shoppingAddress.put("postCode",e.getPostCode());
				shoppingAddress.put("isDefault",e.getIsDefault());
			}
		});
		return shoppingAddress;
	}



}
