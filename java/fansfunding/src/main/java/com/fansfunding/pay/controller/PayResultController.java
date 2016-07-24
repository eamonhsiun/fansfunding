package com.fansfunding.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.pay.service.PayResultService;
import com.fansfunding.pay.util.AlipayNotify;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("payResult")
public class PayResultController {
	@Autowired
	private PayResultService payResultService;
	/**
	 * 支付宝同步支付结果通知（网页支付）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(path="web/return",method=RequestMethod.GET)
	@ResponseBody
	public Status webReturn(HttpServletRequest request,HttpServletResponse response){
		Map<String,String> params=payResultService.getParams(request);
		//合法性验证
		if(AlipayNotify.verify(params)){
			//判断是否是本商户订单
			if(payResultService.isIllegelOrder(params)){
				//判断该笔订单是否在商户网站中已经做过处理
				if(!payResultService.isFinished(params)){
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					//如果有做过处理，不执行商户的业务程序
					//注意：付款完成后，支付宝系统发送该交易状态通知
					if(payResultService.verify(params)){
						payResultService.finishOrder(params);
						payResultService.confirmReturn(params);
						return new Status(true,StatusCode.SUCCESS,"订单完成支付成功",null);
					}
					return new Status(false,StatusCode.ORDER_INFO_DISAGREE,"订单信息不一致",null);
				}
				payResultService.confirmReturn(params);
				return new Status(true,StatusCode.SUCCESS,"订单已完成",null);
			}
			return new Status(false,StatusCode.NOT_ILLEGEL_ORDER,"非本商户订单",null);
		}
		return new Status(false,StatusCode.PAY_VERIFY_FAILED,"验证失败",null);
	}
	/**
	 * 支付宝异步通知接口（网页支付）
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(path="web/notify",method=RequestMethod.POST)
	public void webNotify(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out=response.getWriter();
		response.setCharacterEncoding("UTF-8");
		Map<String,String> params=payResultService.getParams(request);
		if(AlipayNotify.verify(params)){
			if(payResultService.isIllegelOrder(params)){
				if(!payResultService.isFinished(params)){
					if(payResultService.verify(params)){
						payResultService.finishOrder(params);
						payResultService.confirmNotify(params);
						out.print("success");
					}
					else{
						out.print("订单信息不一致");
					}
				}
				else{
					payResultService.confirmNotify(params);
					out.print("success");
				}
			}
			else{
				out.print("非本商户订单");
			}
		}
		else{
			out.print("验证失败");
		}
	}

	/**
	 * 支付宝异步通知接口(移动支付)
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(path="mobile/notify",method=RequestMethod.POST)
	public void mobileNotify(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out=response.getWriter();
		response.setCharacterEncoding("UTF-8");
		Map<String,String> params=payResultService.getParams(request);
		String status =request.getParameter("trade_status");
		if(AlipayNotify.verify(params)){
			if(payResultService.isIllegelOrder(params)){
				if(!payResultService.isFinished(params)){
					if(payResultService.verify(params)){
						if(status.equals("TRADE_SUCCESS")||status.equals("TRADE_FINISHED")){
							payResultService.finishOrder(params);
							payResultService.confirmNotify(params);
							out.print("success");
						}
						else{
							out.print("failed");
						}
					}
					else{
						out.print("订单信息不一致");
					}
				}
				else{
					payResultService.confirmNotify(params);
					out.print("success");
				}
			}
			else{
				out.print("非本商户订单");
			}
		}
		else{
			out.print("验证失败");
		}
	}

	/**
	 * 支付宝同步通知接口(移动支付)
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@RequestMapping(path="mobile/return",method=RequestMethod.POST)
	@ResponseBody
	public Status mobileReturn(@RequestParam String orderNo){
		if(!payResultService.isIllegelOrder(orderNo)){
			return new Status(false,StatusCode.NOT_ILLEGEL_ORDER,"非本商户订单",null);
		}
		return new Status(true,StatusCode.SUCCESS,payResultService.getStatus(orderNo),null);
	}
}
