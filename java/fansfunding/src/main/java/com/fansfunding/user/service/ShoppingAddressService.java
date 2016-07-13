package com.fansfunding.user.service;


import java.util.List;

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

	public void updateById(int id){
		return ;
	}
	
	public ShoppingAddress AddNewAddress(int id){
		ShoppingAddress shoppingAddress = new ShoppingAddress();
		return shoppingAddress;
	}
	
	
	
	
	
	
}
