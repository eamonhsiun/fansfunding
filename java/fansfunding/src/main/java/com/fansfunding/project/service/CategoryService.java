package com.fansfunding.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.project.dao.CategoryDao;

@Service
public class CategoryService {
	@Autowired
	private CategoryDao categoryDao;
	
	/**
	 * 获取所有的分类
	 * @return
	 */
	public List<Map<String,Object>> getAll(){
		List<Map<String,Object>> catagorys=new ArrayList<>();
		categoryDao.selectAll().stream().forEach((e)->{
			Map<String,Object> catagory=new HashMap<>();
			catagory.put("id", e.getId());
			catagory.put("name", e.getName());
			catagory.put("description",e.getDescription());
			catagorys.add(catagory);
		});
		return catagorys;
	}
	/**
	 * 判断是否存在
	 * @param categoryId
	 * @return
	 */
	public boolean isExist(int categoryId){
		return categoryDao.selectByPrimaryKey(categoryId)!=null;
	}
}
