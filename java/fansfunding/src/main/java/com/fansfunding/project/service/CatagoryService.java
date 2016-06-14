package com.fansfunding.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.project.dao.CatagoryDao;

@Service
public class CatagoryService {
	@Autowired
	private CatagoryDao catagoryDao;
	
	/**
	 * 获取所有的分类
	 * @return
	 */
	public List<Map<String,Object>> getAll(){
		List<Map<String,Object>> catagorys=new ArrayList<>();
		catagoryDao.selectAll().stream().forEach((e)->{
			Map<String,Object> catagory=new HashMap<>();
			catagory.put("id", e.getId());
			catagory.put("name", e.getName());
			catagory.put("description",e.getDescription());
			catagorys.add(catagory);
		});
		return catagorys;
	}
}
