package com.fansfunding.pay.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fansfunding.pay.service.OrderService;
import com.fansfunding.project.service.FeedbackService;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("pay")
public class PayController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private UserService userService;
	
	/**
	 * 网页支付
	 * @param feedbackId 回报id
	 * @param userId 用户id
	 * @return
	 */
	@RequestMapping(path="web",method=RequestMethod.POST)
	public ModelAndView webPay(@RequestParam int feedbackId,@RequestParam int userId){
		if(!userService.isExist(userId)){
			return null;
		}
		if(!feedbackService.isExist(feedbackId)){
			return null;
		}
		ModelAndView mv=new ModelAndView("webPay");
		mv.addObject("params", orderService.buildWebOrder(feedbackId,userId));
		return mv;
	}

	/**
	 * 移动支付
	 * @param feedbackId 回报id
	 * @param userId 用户id
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(path="mobile",method=RequestMethod.POST)
	@ResponseBody
	public Status mobilePay(@RequestParam int feedbackId,@RequestParam int userId) throws UnsupportedEncodingException{
		if(!userService.isExist(userId)){
			return new Status(false,StatusCode.USER_NULL,"用户不存在",null);
		}
		if(!feedbackService.isExist(feedbackId)){
			return new Status(false,StatusCode.FAILED,"回报不存在",null);
		}
		return new Status(true,StatusCode.SUCCESS,orderService.sign(feedbackId, userId),null);
	}

}
