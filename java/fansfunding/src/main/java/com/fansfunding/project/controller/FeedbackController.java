package com.fansfunding.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.project.service.FeedbackService;
import com.fansfunding.project.service.ProjectService;
import com.fansfunding.utils.CheckUtils;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("project")
public class FeedbackController {
	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private ProjectService projectService;
	
	/**
	 * 获取相关项目的回馈方式
	 * @param categoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{categoryId}/{projectId}/feedbacks",method=RequestMethod.GET)
	@ResponseBody
	public Status feedbacks(@PathVariable Integer categoryId,@PathVariable Integer projectId,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下或者项目不存在",null);
		}
		return new Status(true,StatusCode.SUCCESS,feedbackService.getFeedbacks(projectId,page,rows),null);
	}
	/**
	 * 添加回馈方式
	 * @param categoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{categoryId}/{projectId}/feedbacks",method=RequestMethod.POST)
	@ResponseBody
	public Status addFeedbacks(@PathVariable Integer categoryId,@PathVariable Integer projectId,
			@RequestParam String title,@RequestParam String description,@RequestParam double limitation, 
			@RequestParam(required=false,defaultValue="") String images){
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		if(title.length()>20||description.length()>255){
			return new Status(false,StatusCode.ERROR_DATA,"参数过长",null);
		}
		if(!CheckUtils.isNullOrEmpty(title,description,images)){
			feedbackService.add(projectId,title,description,limitation,images);
			return new Status(true,StatusCode.SUCCESS,"回馈方式添加成功",null);
		}
		return new Status(false,StatusCode.FAILED,"参数错误",null);
	}
}
