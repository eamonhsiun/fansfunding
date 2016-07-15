package com.fansfunding.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fansfunding.project.entity.Comment;
import com.fansfunding.project.service.CommentService;
import com.fansfunding.project.service.ProjectService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("project")
public class CommentController {
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CommentService commentService;
	
	/**
	 * 
	 * 获取所有的项目评论
	 * @param catagoryId 分类ID
	 * @param projectId 项目ID
	 * @param page 页数
	 * @param rows 每页的记录数
	 * @return
	 */
	@RequestMapping(path="{catagoryId}/{projectId}/comments",method=RequestMethod.GET)
	@ResponseBody
	public Status comments(@PathVariable Integer catagoryId,@PathVariable Integer projectId,
							@RequestParam(required = false, defaultValue = "1") Integer page,
							@RequestParam(required = false, defaultValue = "10") Integer rows){
		if(!projectService.inCatagory(catagoryId, projectId)){
			return new Status(false,StatusCode.FAILD,"该项目不在该分类下",null);
		}
		return new Status(true,StatusCode.SUCCESS,commentService.getComments(projectId,page,rows),null);
	}
	/**
	 * 添加评论
	 * @param catagoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{catagoryId}/{projectId}/comments",method=RequestMethod.POST)
	@ResponseBody
	public Status comment(@PathVariable Integer catagoryId,@PathVariable Integer projectId,Comment comment){
		if(comment==null){
			return new Status(false,StatusCode.ERROR_DATA,null,null);
		}
		if(!projectService.inCatagory(catagoryId, projectId)){
			return new Status(false,StatusCode.FAILD,"该项目不在该分类下",null);
		}
		commentService.add(comment);
		return new Status(true,StatusCode.SUCCESS,"评论成功",null);
	}

}
