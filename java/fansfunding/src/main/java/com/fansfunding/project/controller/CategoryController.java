package com.fansfunding.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.project.service.CategoryService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("project")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * 获取项目分类
	 * @return
	 */
	@RequestMapping("catagorys")
	@ResponseBody
	public Status catagroys(){
		return new Status(true,StatusCode.SUCCESS,categoryService.getAll(),null);
	}
}
