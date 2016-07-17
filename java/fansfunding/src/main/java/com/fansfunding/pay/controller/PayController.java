package com.fansfunding.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fansfunding.pay.service.OrderService;

@Controller
@RequestMapping("pay")
public class PayController {
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(path="web",method=RequestMethod.POST)
	public ModelAndView webPay(@RequestParam int feedbackId,@RequestParam int userId){
		ModelAndView mv=new ModelAndView("webPay");
		mv.addObject("params", orderService.buildOrder(feedbackId,userId));
		return mv;
	}

}
