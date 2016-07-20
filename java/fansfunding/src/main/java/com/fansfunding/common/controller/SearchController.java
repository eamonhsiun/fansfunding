package com.fansfunding.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.project.service.ProjectService;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("search")
public class SearchController {
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	/**
	 * 用户搜索
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="user",method=RequestMethod.GET)
	@ResponseBody
	public Status searchUser(@RequestParam String keyword,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){
		return new Status(true,StatusCode.SUCCESS,userService.search(keyword,page,rows),null);
	}
	/**
	 * 项目搜索
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="project",method=RequestMethod.GET)
	@ResponseBody
	public Status searchProject(@RequestParam String keyword,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){
		return new Status(true,StatusCode.SUCCESS,projectService.search(keyword,page,rows),null);
	}
}
