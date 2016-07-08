package com.fansfunding.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.project.entity.Feedback;
import com.fansfunding.project.service.FeedbackService;
import com.fansfunding.project.service.ProjectService;
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
	 * @param catagoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{catagoryId}/{projectId}/feedbacks",method=RequestMethod.GET)
	@ResponseBody
	public Status feedbacks(@PathVariable Integer catagoryId,@PathVariable Integer projectId){
		if(projectService.inCatagory(catagoryId, projectId)){
			return new Status(false,StatusCode.FAILD,"该项目不在该分类下",null);
		}
		return new Status(true,StatusCode.SUCCESS,feedbackService.getAll(projectId),null);
	}
	/**
	 * 添加回馈方式
	 * @param catagoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{catagoryId}/{projectId}/feedbacks",method=RequestMethod.POST)
	@ResponseBody
	public Status addFeedbacks(@PathVariable Integer catagoryId,@PathVariable Integer projectId,Feedback feedback){
		if(projectService.inCatagory(catagoryId, projectId)){
			return new Status(false,StatusCode.FAILD,"该项目不在该分类下",null);
		}
		feedbackService.add(feedback);
		return new Status(true,StatusCode.SUCCESS,"回馈方式添加成功",null);
	}
}
