package com.fansfunding.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.system.entity.SysFeedback;
import com.fansfunding.system.service.SysFeedbackService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("sys")
public class SysFeedbackController {
	@Autowired
	private SysFeedbackService feedbackService;
	/**
	 * 添加反馈
	 * @param feedback
	 * @return
	 */
	@RequestMapping(path="feedback",method=RequestMethod.POST)
	@ResponseBody
	public Status addFeedback(SysFeedback feedback){
		feedbackService.add(feedback);
		return new Status(true,StatusCode.SUCCESS,"反馈成功");
	}
}
