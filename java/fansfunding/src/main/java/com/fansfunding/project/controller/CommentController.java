package com.fansfunding.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.utils.response.Status;

@Controller
@RequestMapping("project")
public class CommentController {
	
	/**
	 * 获取所有的项目评论
	 * @param catagroyId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{catagroyId}/{projectId}/comments",method=RequestMethod.GET)
	@ResponseBody
	public Status comments(@PathVariable Integer catagroyId,@PathVariable Integer projectId){
		return null;
	}
	/**
	 * 添加评论
	 * @param catagroyId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping(path="{catagroyId}/{projectId}/comments",method=RequestMethod.POST)
	@ResponseBody
	public Status comment(@PathVariable Integer catagroyId,@PathVariable Integer projectId){
		return null;
	}
}
