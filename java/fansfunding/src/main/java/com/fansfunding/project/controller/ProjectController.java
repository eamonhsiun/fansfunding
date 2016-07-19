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

import com.fansfunding.project.entity.ProjectMoment;
import com.fansfunding.project.service.CategoryService;
import com.fansfunding.project.service.ProjectService;
import com.fansfunding.utils.CheckUtils;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("project")
public class ProjectController {
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * 获取分类下的所有项目
	 * @param catagroyId 分类ID
	 * @return
	 */
	@RequestMapping("{categoryId}")
	@ResponseBody
	public Status projects(@PathVariable Integer categoryId,
			@RequestParam(required=false,defaultValue="1") Integer page,
			@RequestParam(required=false,defaultValue="10") Integer rows){
		return new Status(true,StatusCode.SUCCESS,projectService.getByCategoryId(categoryId,page,rows),null);
	}
	
	/**
	 * 根据项目ID获取项目
	 * @param categoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping("{categoryId}/{projectId}")
	@ResponseBody
	public Status project(@PathVariable Integer categoryId,@PathVariable Integer projectId){
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		return new Status(true,StatusCode.SUCCESS,projectService.getByProjectId(projectId),null);
	}
	/**
	 * 获取项目详情
	 * @param categoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	@RequestMapping("{categoryId}/{projectId}/detail")
	@ResponseBody
	public Status prjectDetail(@PathVariable Integer categoryId,@PathVariable Integer projectId){
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		return new Status(true,StatusCode.SUCCESS,projectService.getDetails(projectId),null);
	}
	
	/**
	 * 添加项目
	 * @param categoryId 分类ID
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
	@RequestMapping(path="{categoryId}",method=RequestMethod.POST)
	@ResponseBody
	public Status add(
			@PathVariable Integer categoryId, 
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
		if(!categoryService.isExist(categoryId)){
			return new Status(false,StatusCode.FAILED,"分类不存在",null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		projectService.addProject(name, categoryId, cover, sponsor, sdf.parse(targetDeadline),
				targetMoney, description, content, images, others, video);
		return new Status(true,StatusCode.SUCCESS,"添加成功",null);
	}
	/**
	 * 获取动态
	 * @param categoryId
	 * @param projectId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="{categoryId}/{projectId}/moment",method=RequestMethod.GET)
	@ResponseBody
	public Status moment(@PathVariable int categoryId,@PathVariable int projectId,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		return new Status(true,StatusCode.SUCCESS,projectService.moment(projectId, page, rows),null);
	}
	/**
	 * 添加动态
	 * @param categoryId
	 * @param projectId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="{categoryId}/{projectId}/moment",method=RequestMethod.POST)
	@ResponseBody
	public Status addMoment(@PathVariable int categoryId,@PathVariable int projectId,
			ProjectMoment moment,@RequestParam(required=false,defaultValue="") String images
			,@RequestParam int sponsorId){
		if(!projectService.inCategory(categoryId, projectId)){
			return new Status(false,StatusCode.FAILED,"该项目不在该分类下",null);
		}
		if(CheckUtils.isNullOrEmpty(moment)){
			if(projectService.addMoment(categoryId,projectId,moment,images,sponsorId)){
				return new Status(true,StatusCode.SUCCESS,"动态添加成功",null);
			}
			return new Status(true,StatusCode.PERMISSION_LOW,"你不是项目发起者，没有权限添加动态",null);
		}
		return new Status(false,StatusCode.FAILED,"参数错误",null);
	}
	/**
	 * 项目搜索
	 * @param keyword
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="search",method=RequestMethod.GET)
	@ResponseBody
	public Status search(@RequestParam String keyword,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer rows){
		return new Status(true,StatusCode.SUCCESS,projectService.search(keyword,page,rows),null);
	}
}
