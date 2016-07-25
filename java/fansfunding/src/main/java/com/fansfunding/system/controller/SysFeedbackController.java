package com.fansfunding.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.system.service.SysFeedbackService;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.CheckUtils;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("sys")
public class SysFeedbackController {
	@Autowired
	private SysFeedbackService feedbackService;
	@Autowired
	private UserService userService;
	/**
	 * 添加反馈
	 * @param feedback
	 * @return
	 */
	@RequestMapping(path="feedback",method=RequestMethod.POST)
	@ResponseBody
	public Status addFeedback(@RequestParam String email,@RequestParam String content){
		if(email==null||!CheckUtils.isEmail(email)){
			return new Status(false,StatusCode.ERROR_DATA,"必须提供正确的邮箱",null);
		}
		feedbackService.add(email,content);
		return new Status(true,StatusCode.SUCCESS,"反馈成功",null);
	}
}
