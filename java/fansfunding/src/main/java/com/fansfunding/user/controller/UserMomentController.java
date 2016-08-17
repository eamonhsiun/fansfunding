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
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment",method=RequestMethod.GET)
	@ResponseBody
	public Status getMoment(@PathVariable int userId) throws Exception{
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true, StatusCode.SUCCESS, userMomentService.getMomentsById(userId), 0);
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
			@RequestParam(required=false,defaultValue="") String images
			) throws Exception{
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		if(!CheckUtils.isNullOrEmpty(content)){
			if(content.length()>140){
				return new Status(false,StatusCode.ERROR_DATA,"数据过长过长",null);
			}
			if(userMomentService.postMoment(userId,content,images)){
				return new Status(true, StatusCode.SUCCESS, "发布成功", 0);
			}
			return new Status(false,StatusCode.PERMISSION_LOW,"权限过低",null);			
		}
		return new Status(false,StatusCode.FAILED,"参数错误",null);
	}
	
	/**
	 * 获得用户动态评论
	 * @param momentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="moment/{momentId}/comment",method=RequestMethod.GET)
	@ResponseBody
	public Status getMomentComment(
			@PathVariable int momentId
			) throws Exception{
		if(!userMomentService.isExist(momentId)){
			return new Status(false, StatusCode.MOMENT_NULL, "动态不存在", null);
		}
		
		return new Status(true, StatusCode.SUCCESS, userMomentService.getCommentByMomentId(momentId), 0);
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
			@RequestParam String content
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
			if(userMomentService.postComment(userId,momentId,content)){
				return new Status(true, StatusCode.SUCCESS, "发布成功", 0);
			}
			return new Status(false,StatusCode.PERMISSION_LOW,"权限过低",null);			
		}
		return new Status(false,StatusCode.FAILED,"参数错误",null);
	}
	
	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="{userId}/moment/follow ",method=RequestMethod.GET)
	@ResponseBody
	public Status getFriendMoment(@PathVariable int userId) throws Exception{
		if(!userService.isExist(userId)){
			return new Status(false, StatusCode.USER_NULL, "用户不存在", null);
		}
		return new Status(true, StatusCode.SUCCESS, userMomentService.getFriendsMomentsById(userId), 0);
	}
}
