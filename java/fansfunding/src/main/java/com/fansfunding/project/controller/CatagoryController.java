package com.fansfunding.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.project.service.CatagoryService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("project")
public class CatagoryController {
	@Autowired
	private CatagoryService catagoryService;
	
	/**
	 * 获取项目分类
	 * @return
	 */
	@RequestMapping("catagorys")
	@ResponseBody
	public Status catagroys(){
		return new Status(true,StatusCode.SUCCESS,catagoryService.getAll(),null);
	}
}
