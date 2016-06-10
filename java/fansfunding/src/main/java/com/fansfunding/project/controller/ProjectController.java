package com.fansfunding.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.project.entity.Project;
import com.fansfunding.project.service.ProjectService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("project")
public class ProjectController {
	@Autowired
	private ProjectService projectService;
	
	/**
	 * 获取分类下的所有项目
	 * @param catagroyId 分类ID
	 * @return
	 */
	@RequestMapping("{catagoryId}")
	@ResponseBody
	public Status projects(@PathVariable Integer catagoryId){
		return new Status(true,StatusCode.SUCCESS,projectService.getByCatagoryId(catagoryId));
	}
	/**
	 * 根据项目ID获取项目
	 * @param catagroyId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping("{catagoryId}/{projectId}")
	@ResponseBody
	public Status project(@PathVariable Integer catagroyId,@PathVariable Integer projectId){
		return new Status(true,StatusCode.SUCCESS,projectService.getByProjectId(projectId));
	}
	/**
	 * 获取项目详情
	 * @param catagroyId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping("{catagoryId}/{projectId}/detail")
	@ResponseBody
	public Status prjectDetail(@PathVariable Integer catagroyId,@PathVariable Integer projectId){
		return null;
	}
	
	/**
	 * 添加项目
	 * @param project 项目
	 * @return
	 */
	@RequestMapping("add")
	@ResponseBody
	public Status add(Project project){
		return null;
	}
	/**
	 * 
	 * 支持项目
	 * @param catagroyId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping("{catagoryId}/{projectId}/support")
	@ResponseBody
	public Status support(@PathVariable Integer catagroyId,@PathVariable Integer projectId){
		return null;
	}
}
