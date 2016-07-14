package com.fansfunding.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
	public Status projects(@PathVariable Integer catagoryId,
			@RequestParam(required=false,defaultValue="1") Integer page,
			@RequestParam(required=false,defaultValue="1") Integer rows){
		return new Status(true,StatusCode.SUCCESS,projectService.getByCatagoryId(catagoryId,page,rows),null);
	}
	/**
	 * 根据项目ID获取项目
	 * @param catagoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping("{catagoryId}/{projectId}")
	@ResponseBody
	public Status project(@PathVariable Integer catagoryId,@PathVariable Integer projectId){
		if(!projectService.inCatagory(catagoryId, projectId)){
			return new Status(false,StatusCode.FAILD,"该项目不在该分类下",null);
		}
		return new Status(true,StatusCode.SUCCESS,projectService.getByProjectId(projectId),null);
	}
	/**
	 * 获取项目详情
	 * @param catagoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping("{catagoryId}/{projectId}/detail")
	@ResponseBody
	public Status prjectDetail(@PathVariable Integer catagoryId,@PathVariable Integer projectId){
		if(!projectService.inCatagory(catagoryId, projectId)){
			return new Status(false,StatusCode.FAILD,"该项目不在该分类下",null);
		}
		return new Status(true,StatusCode.SUCCESS,projectService.getDetails(projectId),null);
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
	 * @param catagoryId 分类ID
	 * @param projectId 项目ID
	 * @return 
	 */
	@RequestMapping("{catagoryId}/{projectId}/support")
	@ResponseBody
	public Status support(@PathVariable Integer catagoryId,@PathVariable Integer projectId){
		return null;
	}
	/**
	 * 上传项目相关的附件
	 * @param files 上传的文件
	 * @return
	 */
	@RequestMapping(path="{catagoryId}/{projectId}/attachments",method=RequestMethod.POST)
	@ResponseBody
	public Status uploadAttachment(@PathVariable Integer catagoryId,@PathVariable Integer projectId,@RequestParam CommonsMultipartFile[] files){
		if(!projectService.inCatagory(catagoryId, projectId)){
			return new Status(false,StatusCode.FAILD,"该项目不在该分类下",null);
		}
		if(files.length==0){
			return new Status(false,StatusCode.FAILD,"文件不可为空",null);
		}
		for(CommonsMultipartFile file:files){
			if(file.isEmpty()){
				return new Status(false,StatusCode.FAILD,"文件不可为空",null);
			}
		}
		if(projectService.uploadAttachments(catagoryId, projectId, files)){
			return new Status(true,StatusCode.SUCCESS,"文件上传成功",null);
		}
		return new Status(false,StatusCode.FILEUPLOAD_ERROR,"文件上传失败",null);
	}
}
