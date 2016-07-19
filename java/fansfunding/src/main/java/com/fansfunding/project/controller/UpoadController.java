package com.fansfunding.project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fansfunding.project.service.ProjectService;
import com.fansfunding.project.service.UploadService;
import com.fansfunding.utils.fileupload.FileFormat;
import com.fansfunding.utils.fileupload.FileUpload;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("project")
public class UpoadController {
	@Autowired
	private UploadService uploadService;
	@Autowired
	private ProjectService projectService;

	/**
	 * 上传项目图片
	 * @param files 上传的文件
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(path="{categoryId}/{projectId}/images",method=RequestMethod.POST)
	@ResponseBody
	public Status uploadProjectImages(@PathVariable Integer categoryId,@PathVariable Integer projectId,
						@RequestParam CommonsMultipartFile[] files) throws IOException{
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		if(files.length==0){
			return new Status(false,StatusCode.FAILED,"文件不可为空",null);
		}
		for(CommonsMultipartFile file:files){
			if(file.isEmpty()){
				return new Status(false,StatusCode.FAILED,"文件不可为空",null);
			}
			if(file.getSize()>FileUpload.FILE_MAX_SIZE){
				return new Status(false,StatusCode.FILE_TOO_LARGE,"图片大小超过了上传限制",null);
			}
			if(!FileFormat.isImage(file.getInputStream())){
				return new Status(false,StatusCode.UNSUPPORT_IMAGE_FORMAT,"不支持的图片格式",null);
			}
		}
		String[] paths=new String[files.length];
		if((paths=uploadService.uploadProjectImages(categoryId, projectId, files))!=null){
			return new Status(true,StatusCode.SUCCESS,paths,null);
		}
		return new Status(false,StatusCode.FILEUPLOAD_ERROR,"文件上传失败",null);
	}
	/**
	 * 上传项目回馈相关的附件
	 * @param files 上传的文件
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(path="{categoryId}/{projectId}/feedback/images",method=RequestMethod.POST)
	@ResponseBody
	public Status uploadFeedbackImage(@PathVariable Integer categoryId,@PathVariable Integer projectId,
						@PathVariable int feedbackId,@RequestParam CommonsMultipartFile[] files) throws IOException{
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		if(files.length==0){
			return new Status(false,StatusCode.FAILED,"文件不可为空",null);
		}
		for(CommonsMultipartFile file:files){
			if(file.isEmpty()){
				return new Status(false,StatusCode.FAILED,"文件不可为空",null);
			}
			if(file.getSize()>FileUpload.FILE_MAX_SIZE){
				return new Status(false,StatusCode.FILE_TOO_LARGE,"图片大小超过了上传限制",null);
			}
			if(!FileFormat.isImage(file.getInputStream())){
				return new Status(false,StatusCode.UNSUPPORT_IMAGE_FORMAT,"不支持的图片格式",null);
			}
		}
		String[] paths=new String[files.length];
		if((paths=uploadService.uploadFeedbackImages(categoryId, projectId, files))!=null){
			return new Status(true,StatusCode.SUCCESS,paths,null);
		}
		return new Status(false,StatusCode.FILEUPLOAD_ERROR,"文件上传失败",null);
	}
	/**
	 * 上传项目动态相关的附件
	 * @param files 上传的文件
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(path="{categoryId}/{projectId}/moment/images",method=RequestMethod.POST)
	@ResponseBody
	public Status uploadMomentImage(@PathVariable Integer categoryId,@PathVariable Integer projectId,
						@RequestParam CommonsMultipartFile[] files) throws IOException{
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		if(files.length==0){
			return new Status(false,StatusCode.FAILED,"文件不可为空",null);
		}
		for(CommonsMultipartFile file:files){
			if(file.isEmpty()){
				return new Status(false,StatusCode.FAILED,"文件不可为空",null);
			}
			if(file.getSize()>FileUpload.FILE_MAX_SIZE){
				return new Status(false,StatusCode.FILE_TOO_LARGE,"图片大小超过了上传限制",null);
			}
			if(!FileFormat.isImage(file.getInputStream())){
				return new Status(false,StatusCode.UNSUPPORT_IMAGE_FORMAT,"不支持的图片格式",null);
			}
		}
		String[] paths=new String[files.length];
		if((paths=uploadService.uploadMomentImages(categoryId, projectId, files))!=null){
			return new Status(true,StatusCode.SUCCESS,paths,null);
		}
		return new Status(false,StatusCode.FILEUPLOAD_ERROR,"文件上传失败",null);
	}
}
