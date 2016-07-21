package com.fansfunding.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fansfunding.pay.service.OrderService;
import com.fansfunding.project.service.FeedbackService;
import com.fansfunding.user.service.UserService;

@Controller
@RequestMapping("pay")
public class PayController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(path="web",method=RequestMethod.POST)
	public ModelAndView webPay(@RequestParam int feedbackId,@RequestParam int userId){
		if(!userService.isExist(userId)){
			return null;
		}
		if(!feedbackService.isExist(feedbackId)){
			return null;
		}
		ModelAndView mv=new ModelAndView("webPay");
		mv.addObject("params", orderService.buildOrder(feedbackId,userId));
		return mv;
	}

}
