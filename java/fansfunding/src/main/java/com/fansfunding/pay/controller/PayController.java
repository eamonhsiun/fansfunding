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
import com.fansfunding.user.service.ShoppingAddressService;
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
	@Autowired
	private ShoppingAddressService addressService;
	/**
	 * 网页支付
	 * @param feedbackId 回报id
	 * @param userId 用户id
	 * @return
	 */
	@RequestMapping(path="web",method=RequestMethod.POST)
	public ModelAndView webPay(@RequestParam int feedbackId,@RequestParam int userId,@RequestParam int addressId){
		if(!userService.isExist(userId)){
			ModelAndView error=new ModelAndView("payError");
			error.addObject("message", "用户不存在");
			return error;
		}
		if(!feedbackService.isExist(feedbackId)){
			ModelAndView error=new ModelAndView("payError");
			error.addObject("message", "回报不存在");
			return error;
		}
		if(!addressService.exist(addressId)){
			ModelAndView error=new ModelAndView("payError");
			error.addObject("message", "地址不存在");
			return error;
		}
		if(!orderService.isPayable(feedbackId)){
			ModelAndView error=new ModelAndView("payError");
			error.addObject("message", "回报支持人数超过限制");
			return error;
		}
		ModelAndView mv=new ModelAndView("webPay");
		mv.addObject("params", orderService.buildWebOrder(feedbackId,userId,addressId));
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
	public Status mobilePay(@RequestParam int feedbackId,@RequestParam int userId,@RequestParam int addressId) throws UnsupportedEncodingException{
		if(!userService.isExist(userId)){
			return new Status(false,StatusCode.USER_NULL,"用户不存在",null);
		}
		if(!feedbackService.isExist(feedbackId)){
			return new Status(false,StatusCode.FAILED,"回报不存在",null);
		}
		if(!orderService.isPayable(feedbackId)){
			return new Status(false,StatusCode.FAILED,"回报支持人数超过限制",null);
		}
		if(!addressService.exist(addressId)){
			return new Status(false,StatusCode.FAILED,"地址不存在",null);
		}
		return new Status(true,StatusCode.SUCCESS,orderService.sign(feedbackId, userId,addressId),null);
	}

}
