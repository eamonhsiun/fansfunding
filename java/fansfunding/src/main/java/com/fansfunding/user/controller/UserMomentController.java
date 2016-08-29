package com.fansfunding.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.user.service.UserMomentService;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.CheckUtils;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping(path = "user")
public class UserMomentController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserMomentService userMomentService;



	/**
	 * 获取用户动态
	 * @param userId
	 * @param page 
	 * @param rows 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment",method=RequestMethod.GET)
	@ResponseBody
	public Status getMoment(
			@PathVariable int userId, 
			@RequestParam(required=false,defaultValue="1") Integer page,
			@RequestParam(required=false,defaultValue="10") Integer rows
			) throws Exception{
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true, StatusCode.SUCCESS, userMomentService.getMomentsById(userId, page, rows), 0);
	}
	
	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment/follow ",method=RequestMethod.GET)
	@ResponseBody
	public Status getFriendMoment(
			@PathVariable int userId,
			@RequestParam(required=false,defaultValue="1") Integer page,
			@RequestParam(required=false,defaultValue="10") Integer rows
			) throws Exception{
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true, StatusCode.SUCCESS, userMomentService.getFriendsMomentsById(userId, page, rows), 0);
	}
	
	/**
	 * 上传用户动态
	 * @param userId
	 * @param content
	 * @param images
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment",method=RequestMethod.POST)
	@ResponseBody
	public Status postMoment(@PathVariable int userId,
			@RequestParam String content,
			@RequestParam(required=false,defaultValue="") String images,
			@RequestParam(required=false,defaultValue="0")int origin,
			@RequestParam(required=false,defaultValue="0")int linkCategory,
			@RequestParam(required=false,defaultValue="0")int linkProject
			) throws Exception{
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(!CheckUtils.isNullOrEmpty(content)){
			if(content.length()>140){
				return new Status(false,StatusCode.ERROR_DATA,"数据过长过长",null);
			}
			
			if(userMomentService.postMoment(userId,content,images,origin,linkCategory,linkProject)){
				return new Status(true, StatusCode.SUCCESS, "发布成功", 0);
			}
			return new Status(false,StatusCode.PERMISSION_LOW,"权限过低",null);			
		}
		return new Status(false,StatusCode.FAILED,"参数错误",null);
	}
	
	/**
	 * 获得用户动态
	 * @param momentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment/{momentId}",method=RequestMethod.GET)
	@ResponseBody
	public Status getMomentById(
			@PathVariable int userId,
			@PathVariable int momentId,
			@RequestParam int viewId
			) throws Exception{
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		
		return new Status(true, StatusCode.SUCCESS, userMomentService.getMomentById(userId,momentId,viewId), 0);
	}
	
	/**
	 * 获得用户动态评论
	 * @param momentId
	 * @param page 
	 * @param rows 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="moment/{momentId}/comment",method=RequestMethod.GET)
	@ResponseBody
	public Status getMomentComment(
			@PathVariable int momentId,
			@RequestParam(required=false,defaultValue="1")int page,
			@RequestParam(required=false,defaultValue="10")int rows
			) throws Exception{
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		
		return new Status(true, StatusCode.SUCCESS, userMomentService.getCommentUsePage(momentId, page, rows), 0);
	}
	
	
	
	
	
	/**
	 * 上传用户动态评论
	 * @param userId
	 * @param momentId
	 * @param content
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment/{momentId}/comment",method=RequestMethod.POST)
	@ResponseBody
	public Status postMomentComment(
			@PathVariable int userId,
			@PathVariable int momentId,
			@RequestParam String content,
			@RequestParam int replyTo
			) throws Exception{
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		
		if(!CheckUtils.isNullOrEmpty(content)){
			if(content.length()>140){
				return new Status(false,StatusCode.ERROR_DATA,"数据过长过长",null);
			}
			if(userMomentService.postComment(userId,momentId,content,replyTo)){
				return new Status(true, StatusCode.SUCCESS, "发布成功", 0);
			}
			return new Status(false,StatusCode.PERMISSION_LOW,"权限过低",null);			
		}
		return new Status(false,StatusCode.FAILED,"参数错误",null);
	}
	
	/**
	 * 获取动态点赞
	 * @param userId
	 * @param momentId
	 * @param page 
	 * @param rows 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="moment/{momentId}/like",method=RequestMethod.GET)
	@ResponseBody
	public Status getMomentLike(
			@PathVariable int momentId,
			@RequestParam(required=false,defaultValue="1")int page,
			@RequestParam(required=false,defaultValue="10")int rows
			) throws Exception{
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		return new Status(true,StatusCode.SUCCESS,userMomentService.getMomentLikeUsePage(momentId, page, rows),null);
	}
	
	
	
	
	/**
	 * 上传动态点赞
	 * @param userId
	 * @param momentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment/{momentId}/like",method=RequestMethod.POST)
	@ResponseBody
	public Status postMomentLike(
			@PathVariable int userId,
			@PathVariable int momentId
			) throws Exception{
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		if(userMomentService.postLike(userId,momentId)){
			return new Status(true, StatusCode.SUCCESS, "发布成功", 0);
		}		
		return new Status(false,StatusCode.FAILED,"参数错误",null);
	}
	
	/**
	 * 取消动态点赞
	 * @param userId
	 * @param momentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment/{momentId}/unlike",method=RequestMethod.POST)
	@ResponseBody
	public Status postMomentDisLike(
			@PathVariable int userId,
			@PathVariable int momentId
			) throws Exception{
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		if(userMomentService.postDisLike(userId,momentId)){
			return new Status(true, StatusCode.SUCCESS, "发布成功", 0);
		}		
		return new Status(false,StatusCode.FAILED,"发布失败",null);
	}
	

}
