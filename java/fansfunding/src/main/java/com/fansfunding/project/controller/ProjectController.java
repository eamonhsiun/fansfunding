package com.fansfunding.project.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
			@RequestParam(required=false,defaultValue="10") Integer rows){
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
	 * @param catagoryId 分类ID
	 * @param name 项目名
	 * @param targetDeadline 截止日期
	 * @param targetMoney 目标金额
	 * @param description 项目描述
	 * @param sponsor 发起人
	 * @param cover 封面
	 * @param content 详细内容
	 * @param images 详细图片
	 * @param others 其他
	 * @param video 详情视频
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("{catagoryId}/add")
	@ResponseBody
	public Status add(
			@PathVariable Integer catagoryId, 
			@RequestParam String name, 
			@RequestParam String targetDeadline, 
			@RequestParam Long targetMoney, 
			@RequestParam String description, 
			@RequestParam Integer sponsor, 
			@RequestParam(required=false,defaultValue="")String cover, 
			@RequestParam(required=false,defaultValue="")String content, 
			@RequestParam(required=false,defaultValue="")String images, 
			@RequestParam(required=false,defaultValue="")String others, 
			@RequestParam(required=false,defaultValue="")String video) throws ParseException{
		
		//TODO:检测该分类是否存在
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		projectService.addProject(name, catagoryId, cover, sponsor, sdf.parse(targetDeadline), targetMoney, description, content, images, others, video);
		return new Status(true,StatusCode.SUCCESS,"添加成功",null);
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
